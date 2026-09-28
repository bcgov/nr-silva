import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import CommentLocationTag from '@/components/Tags/CommentLocationTag';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import '@testing-library/jest-dom';

describe('CommentLocationTag', () => {
  it('renders known location with default styling', () => {
    render(
      <CommentLocationTag
        location={CommentSearchResponseDto.commentLocation.OPENING}
      />
    );
    expect(screen.getByText('Opening')).toBeInTheDocument();
  });

  it('renders Activities location with Activity activityKind', () => {
    render(
      <CommentLocationTag
        location={CommentSearchResponseDto.commentLocation.ACTIVITIES}
        activityKind={CommentSearchResponseDto.activityKind.ACTIVITY}
      />
    );
    expect(screen.getByText('Activity')).toBeInTheDocument();
  });

  it('renders Activities location with Disturbance activityKind', () => {
    render(
      <CommentLocationTag
        location={CommentSearchResponseDto.commentLocation.ACTIVITIES}
        activityKind={CommentSearchResponseDto.activityKind.DISTURBANCE}
      />
    );
    expect(screen.getByText('Disturbance')).toBeInTheDocument();
  });

  it('renders suffix text when provided', () => {
    render(
      <CommentLocationTag
        location={CommentSearchResponseDto.commentLocation.STANDARDS_UNIT}
        suffixText=" (Unit 1)"
      />
    );
    expect(screen.getByText('Standards unit (Unit 1)')).toBeInTheDocument();
  });

  it('renders Unknown when location is null or invalid', () => {
    render(<CommentLocationTag location={null} />);
    expect(screen.getByText('Unknown')).toBeInTheDocument();

    render(<CommentLocationTag location={'INVALID_LOC' as any} />);
    expect(screen.getAllByText('Unknown').length).toBeGreaterThan(0);
  });
});
