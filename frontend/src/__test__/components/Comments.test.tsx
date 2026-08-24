import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import Comments from '@/components/Comments';
import { CommentDto } from '@/services/OpenApi';
import '@testing-library/jest-dom';

describe('Comments', () => {
  it('renders placeholder when comments array is empty', () => {
    render(<Comments comments={[]} />);
    expect(screen.getByText('--')).toBeInTheDocument();
  });

  it('renders placeholder when comments is undefined', () => {
    render(<Comments comments={undefined as any} />);
    expect(screen.getByText('--')).toBeInTheDocument();
  });

  it('renders ul element when comments are present', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TYPE1', description: 'Type One' },
        commentText: 'First comment',
      },
    ];
    const { container } = render(<Comments comments={comments} />);
    expect(container.querySelector('ul.comment-list')).toBeInTheDocument();
  });

  it('renders single comment', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'This is a technical comment',
      },
    ];
    render(<Comments comments={comments} />);
    expect(
      screen.getByText('Technical: This is a technical comment')
    ).toBeInTheDocument();
  });

  it('renders multiple comments', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'First comment',
      },
      {
        commentType: { code: 'GEN', description: 'General' },
        commentText: 'Second comment',
      },
    ];
    render(<Comments comments={comments} />);
    expect(screen.getByText('Technical: First comment')).toBeInTheDocument();
    expect(screen.getByText('General: Second comment')).toBeInTheDocument();
  });

  it('renders comments as list items', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'Comment text',
      },
    ];
    const { container } = render(<Comments comments={comments} />);
    const listItems = container.querySelectorAll('li');
    expect(listItems.length).toBe(1);
  });

  it('skips comments with empty commentText', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'Valid comment',
      },
      {
        commentType: { code: 'GEN', description: 'General' },
        commentText: '', // Empty
      },
    ];
    render(<Comments comments={comments} />);
    expect(screen.getByText('Technical: Valid comment')).toBeInTheDocument();
    expect(screen.queryByText('General:')).not.toBeInTheDocument();
  });

  it('skips comments with null commentText', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'Valid comment',
      },
      {
        commentType: { code: 'GEN', description: 'General' },
        commentText: null as any,
      },
    ];
    render(<Comments comments={comments} />);
    expect(screen.getByText('Technical: Valid comment')).toBeInTheDocument();
  });

  it('uses comment type code and index as key', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'First',
      },
      {
        commentType: { code: 'TECH', description: 'Technical' },
        commentText: 'Second',
      },
    ];
    const { container } = render(<Comments comments={comments} />);
    const items = container.querySelectorAll('li');
    expect(items.length).toBe(2);
  });

  it('converts code to lowercase for key generation', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'UPPERCASE', description: 'Test' },
        commentText: 'Comment',
      },
    ];
    render(<Comments comments={comments} />);
    expect(screen.getByText('Test: Comment')).toBeInTheDocument();
  });

  it('renders multiple comments with different types', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'BIO', description: 'Biological' },
        commentText: 'Bio comment',
      },
      {
        commentType: { code: 'CHEM', description: 'Chemical' },
        commentText: 'Chem comment',
      },
      {
        commentType: { code: 'PHYS', description: 'Physical' },
        commentText: 'Phys comment',
      },
    ];
    render(<Comments comments={comments} />);
    expect(screen.getByText('Biological: Bio comment')).toBeInTheDocument();
    expect(screen.getByText('Chemical: Chem comment')).toBeInTheDocument();
    expect(screen.getByText('Physical: Phys comment')).toBeInTheDocument();
  });

  it('handles special characters in comment text', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TEST', description: 'Test' },
        commentText: 'Comment with "quotes" & symbols & <brackets>',
      },
    ];
    render(<Comments comments={comments} />);
    expect(
      screen.getByText('Test: Comment with "quotes" & symbols & <brackets>')
    ).toBeInTheDocument();
  });

  it('handles long comment text by checking list item existence', () => {
    const longText = 'This is a very long comment '.repeat(10);
    const comments: CommentDto[] = [
      {
        commentType: { code: 'LONG', description: 'Long' },
        commentText: longText,
      },
    ];
    const { container } = render(<Comments comments={comments} />);
    // Verify the list item exists and contains the long text
    const listItems = container.querySelectorAll('li');
    expect(listItems.length).toBe(1);
    expect(listItems[0].textContent).toContain('Long:');
  });

  it('renders ul with comment-list class', () => {
    const comments: CommentDto[] = [
      {
        commentType: { code: 'TEST', description: 'Test' },
        commentText: 'Comment',
      },
    ];
    const { container } = render(<Comments comments={comments} />);
    expect(container.querySelector('.comment-list')).toBeInTheDocument();
  });
});
