import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, describe, expect, it, vi } from 'vitest';
import '@testing-library/jest-dom';

import CommentSearchCard from '../../../components/CommentSearchSection/CommentSearchCard';
import { CommentSearchResponseDto } from '../../../services/OpenApi';

vi.mock('../../../components/OpeningBookmarkBtn', () => ({
  default: ({
    openingId,
    onClick
  }: {
    openingId: number;
    onClick: React.MouseEventHandler<HTMLButtonElement>;
  }) => (
    <button data-testid={`bookmark-${openingId}`} onClick={onClick} type="button">
      Bookmark
    </button>
  )
}));

vi.mock('@/components/Tags', () => ({
  CommentLocationTag: ({
    location,
    activityKind,
    suffixText
  }: {
    location: string;
    activityKind?: string;
    suffixText?: string;
  }) => (
    <span data-activity-kind={activityKind ?? ''} data-suffix={suffixText ?? ''} data-testid="comment-location-tag">
      {location}
      {suffixText}
    </span>
  )
}));

vi.mock('@/utils/DateUtils', () => ({
  formatLocalDate: (date?: string | null) => `Formatted ${date ?? ''}`
}));

const location = CommentSearchResponseDto.commentLocation;
const activityKind = CommentSearchResponseDto.activityKind;

const createCommentDto = (
  overrides: Partial<CommentSearchResponseDto> = {}
): CommentSearchResponseDto => ({
  openingId: 12345,
  commentLocation: location.OPENING,
  activityKind: undefined,
  activityTreatmentUnitId: null,
  standardsUnitId: null,
  standardsUnitName: null,
  commentText: 'The keyword appears twice: KEYWORD.',
  updateTimestamp: '2026-05-01T10:20:30',
  ...overrides
});

const renderCard = (
  overrides: Partial<CommentSearchResponseDto> = {},
  props: Partial<Pick<React.ComponentProps<typeof CommentSearchCard>, 'keyword' | 'index'>> = {}
) => render(
  <CommentSearchCard
    commentDto={createCommentDto(overrides)}
    index={props.index ?? 0}
    keyword={props.keyword ?? 'keyword'}
  />
);

describe('CommentSearchCard', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('renders the opening title, location, formatted date, and highlighted keyword matches', () => {
    const { container } = renderCard();

    expect(screen.getByText('Opening ID 12345')).toBeInTheDocument();
    expect(screen.getByTestId('comment-location-tag')).toHaveTextContent('OPENING');
    expect(screen.getByText('Date: Formatted 2026-05-01T10:20:30')).toBeInTheDocument();
    expect(container.querySelector('#comment-search-card-12345-opening')).toBeInTheDocument();

    const highlightedMatches = Array.from(container.querySelectorAll('strong')).map(
      (element) => element.textContent
    );
    expect(highlightedMatches).toEqual(['keyword', 'KEYWORD']);
  });

  it('keeps unhighlighted comment text when the keyword is blank and handles null comment text', () => {
    const { container, rerender } = renderCard({}, { keyword: '   ' });

    expect(container.querySelectorAll('strong')).toHaveLength(0);
    expect(screen.getByText('The keyword appears twice: KEYWORD.')).toBeInTheDocument();

    rerender(
      <CommentSearchCard
        commentDto={createCommentDto({ commentText: null })}
        index={0}
        keyword="keyword"
      />
    );

    expect(container.querySelector('.comment-search-comment-row')).toHaveTextContent('');
  });

  it('applies shaded and focused classes without changing the rendered result identity', () => {
    const { container } = renderCard({}, { index: 1 });
    const card = container.querySelector('.comment-search-card-container');
    const link = screen.getByRole('link', {
      name: 'Open comment search result for Opening ID 12345, OPENING'
    });

    expect(card).toHaveClass('comment-search-card-container--shaded');
    expect(card).not.toHaveClass('comment-search-card-container--focused');

    fireEvent.focus(link);
    expect(card).toHaveClass('comment-search-card-container--focused');

    fireEvent.blur(link);
    expect(card).not.toHaveClass('comment-search-card-container--focused');
  });

  it('opens the opening details page on click and keyboard activation', async () => {
    const user = userEvent.setup();
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null);
    renderCard();

    const link = screen.getByRole('link', {
      name: 'Open comment search result for Opening ID 12345, OPENING'
    });

    await user.click(link);
    expect(openSpy).toHaveBeenLastCalledWith('/openings/12345', '_blank', 'noopener,noreferrer');

    link.focus();
    fireEvent.keyDown(link, { key: 'Enter' });
    expect(openSpy).toHaveBeenLastCalledWith('/openings/12345', '_blank', 'noopener,noreferrer');

    fireEvent.keyDown(link, { key: ' ' });
    expect(openSpy).toHaveBeenLastCalledWith('/openings/12345', '_blank', 'noopener,noreferrer');
    expect(openSpy).toHaveBeenCalledTimes(3);
  });

  it('does not open the details page when the bookmark button is clicked', async () => {
    const user = userEvent.setup();
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null);
    renderCard();

    await user.click(screen.getByTestId('bookmark-12345'));

    expect(openSpy).not.toHaveBeenCalled();
  });

  it.each([
    [location.OPENING, '/openings/12345'],
    [location.MILESTONE, '/openings/12345?tab=standards-units'],
    [location.STANDARDS_UNIT, '/openings/12345?tab=standards-units'],
    [location.ACTIVITIES, '/openings/12345?tab=activities'],
    [location.FOREST_COVER, '/openings/12345?tab=forest-cover'],
    ['UNKNOWN_LOCATION' as CommentSearchResponseDto.commentLocation, '/openings/12345']
  ])('opens the expected route for %s comments', async (commentLocation, expectedPath) => {
    const user = userEvent.setup();
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null);
    renderCard({ commentLocation });

    await user.click(screen.getByRole('link'));

    expect(openSpy).toHaveBeenCalledWith(expectedPath, '_blank', 'noopener,noreferrer');
  });

  it.each([
    [
      'standards unit name',
      {
        commentLocation: location.STANDARDS_UNIT,
        standardsUnitName: 'SU-1'
      },
      ': SU-1'
    ],
    [
      'milestone standards unit name',
      {
        commentLocation: location.MILESTONE,
        standardsUnitName: 'SU-2'
      },
      ' - Standard unit: SU-2'
    ],
    [
      'activity treatment unit id',
      {
        activityKind: activityKind.ACTIVITY,
        activityTreatmentUnitId: 987,
        commentLocation: location.ACTIVITIES
      },
      ': 987'
    ],
    [
      'no matching suffix data',
      {
        commentLocation: location.FOREST_COVER
      },
      ''
    ]
  ])('passes suffix text for %s', (_name, overrides, expectedSuffix) => {
    renderCard(overrides);

    expect(screen.getByTestId('comment-location-tag')).toHaveAttribute('data-suffix', expectedSuffix);
  });
});
