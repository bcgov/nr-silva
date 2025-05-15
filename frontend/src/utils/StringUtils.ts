/**
 * Converts a string to kebab-case format by replacing spaces with hyphens
 * and converting to lowercase.
 *
 * @param {string} text - The text to convert to kebab-case
 * @returns {string} The kebab-case formatted string
 */
export const toKebabCase = (text: string): string => {
    return text.trim().split(' ').filter(Boolean).join('-').toLowerCase();
};

/**
 * Returns the pluralized form of a word based on the given count.
 *
 * @param word - The word to pluralize.
 * @param count - The number determining whether to pluralize the word. If undefined, plural is used.
 * @returns The word with an 's' appended unless count is exactly 1.
 */
export const pluralize = (word: string, count?: number): string =>
    `${word}${count === 1 ? '' : 's'}`;
