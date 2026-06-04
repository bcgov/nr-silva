import React from 'react';

/**
 * Wraps all occurrences of `searchTerm` in `text` with a `<strong>` element.
 * Matching is case-insensitive; original casing is preserved in the output.
 *
 * @param text - The source text to search within.
 * @param searchTerm - The keyword to highlight.
 * @returns An array of strings and React nodes with matches bolded.
 */
export const highlightKeyword = (
  text: string | null | undefined,
  searchTerm: string
): (string | React.ReactNode)[] => {
  if (!text || !searchTerm.trim()) return [text ?? ''];

  const result: (string | React.ReactNode)[] = [];
  const lowerText = text.toLowerCase();
  const lowerKeyword = searchTerm.toLowerCase();
  let lastIndex = 0;
  let currentIndex = 0;

  while ((currentIndex = lowerText.indexOf(lowerKeyword, lastIndex)) !== -1) {
    if (currentIndex > lastIndex) {
      result.push(text.substring(lastIndex, currentIndex));
    }
    result.push(
      <strong key={`${currentIndex}-${searchTerm}`}>
        {text.substring(currentIndex, currentIndex + searchTerm.length)}
      </strong>
    );
    lastIndex = currentIndex + searchTerm.length;
  }

  if (lastIndex < text.length) {
    result.push(text.substring(lastIndex));
  }

  return result.length > 0 ? result : [text];
};
