import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import StockingStandardsCommentLocationTag from '@/components/Tags/StockingStandardsCommentLocationTag';
import { StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';
import '@testing-library/jest-dom';

describe('StockingStandardsCommentLocationTag', () => {
  it('renders known locations correctly', () => {
    const { rerender } = render(
      <StockingStandardsCommentLocationTag
        location={
          StockingStandardsCommentSearchResponseDto.commentLocation
            .STANDARDS_NAME
        }
      />
    );
    expect(screen.getByText('Standards name')).toBeInTheDocument();

    rerender(
      <StockingStandardsCommentLocationTag
        location={
          StockingStandardsCommentSearchResponseDto.commentLocation
            .ADDITIONAL_STANDARDS
        }
      />
    );
    expect(screen.getByText('Additional standards')).toBeInTheDocument();

    rerender(
      <StockingStandardsCommentLocationTag
        location={
          StockingStandardsCommentSearchResponseDto.commentLocation
            .STANDARDS_OBJECTIVE
        }
      />
    );
    expect(screen.getByText('Standards objective')).toBeInTheDocument();
  });

  it('renders Unknown when location is unknown or null', () => {
    const { rerender } = render(
      <StockingStandardsCommentLocationTag location={null} />
    );
    expect(screen.getByText('Unknown')).toBeInTheDocument();

    rerender(
      <StockingStandardsCommentLocationTag location={'INVALID_LOC' as any} />
    );
    expect(screen.getByText('Unknown')).toBeInTheDocument();
  });
});
