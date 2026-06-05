import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { highlightKeyword } from '../../utils/HighlightUtils';

describe('highlightKeyword', () => {
  describe('no-op cases', () => {
    it('returns empty string for null text', () => {
      const result = highlightKeyword(null, 'foo');
      expect(result).toEqual(['']);
    });

    it('returns empty string for undefined text', () => {
      const result = highlightKeyword(undefined, 'foo');
      expect(result).toEqual(['']);
    });

    it('returns original text when searchTerm is empty string', () => {
      const result = highlightKeyword('hello world', '');
      expect(result).toEqual(['hello world']);
    });

    it('returns original text when searchTerm is whitespace only', () => {
      const result = highlightKeyword('hello world', '   ');
      expect(result).toEqual(['hello world']);
    });

    it('returns original text when searchTerm not found', () => {
      const result = highlightKeyword('hello world', 'xyz');
      expect(result).toEqual(['hello world']);
    });
  });

  describe('matching behaviour', () => {
    it('wraps a single match in <strong>', () => {
      const result = highlightKeyword('hello world', 'world');
      expect(result).toHaveLength(2);
      expect(result[0]).toBe('hello ');
      const { container } = render(<>{result}</>);
      expect(container.querySelector('strong')).toHaveTextContent('world');
    });

    it('is case-insensitive', () => {
      const result = highlightKeyword('Hello World', 'hello');
      const { container } = render(<>{result}</>);
      expect(container.querySelector('strong')).toHaveTextContent('Hello');
    });

    it('preserves original casing inside <strong>', () => {
      const result = highlightKeyword('FOO bar FOO', 'foo');
      const { container } = render(<>{result}</>);
      const strongs = container.querySelectorAll('strong');
      expect(strongs[0]).toHaveTextContent('FOO');
      expect(strongs[1]).toHaveTextContent('FOO');
    });

    it('wraps multiple occurrences', () => {
      const result = highlightKeyword('cat and cat and cat', 'cat');
      const { container } = render(<>{result}</>);
      expect(container.querySelectorAll('strong')).toHaveLength(3);
    });

    it('handles match at the very start of text', () => {
      const result = highlightKeyword('keyword at start', 'keyword');
      const { container } = render(<>{result}</>);
      expect(container.querySelector('strong')).toHaveTextContent('keyword');
      expect(screen.queryByText('keyword at start')).not.toBeInTheDocument();
    });

    it('handles match at the very end of text', () => {
      const result = highlightKeyword('ends with match', 'match');
      const { container } = render(<>{result}</>);
      expect(container.querySelector('strong')).toHaveTextContent('match');
    });

    it('handles text that is entirely the search term', () => {
      const result = highlightKeyword('exact', 'exact');
      const { container } = render(<>{result}</>);
      expect(container.querySelector('strong')).toHaveTextContent('exact');
      expect(result).toHaveLength(1);
    });

    it('handles adjacent matches', () => {
      const result = highlightKeyword('abab', 'ab');
      const { container } = render(<>{result}</>);
      expect(container.querySelectorAll('strong')).toHaveLength(2);
    });

    it('includes surrounding text outside matches', () => {
      const result = highlightKeyword('before keyword after', 'keyword');
      expect(result).toContain('before ');
      expect(result).toContain(' after');
    });
  });
});
