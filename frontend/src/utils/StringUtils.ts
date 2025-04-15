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
