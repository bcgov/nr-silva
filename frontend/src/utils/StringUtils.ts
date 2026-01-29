import { PLACE_HOLDER } from "@/constants";

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


/**
 * Formats a label and value pair with a unit, returning a display string.
 *
 * @param label - The label to display (e.g., "Target", "Min").
 * @param value - The numeric or string value to display. If undefined or falsy, a placeholder is used.
 * @param unit - The unit to append after the value (e.g., "(st/ha)", "(m²/ha)").
 * @returns A formatted string like "Target: 100 (st/ha)" or "Min: —" if the value is missing.
 */
export const renderLabelValueWithUnit = (
    label: string,
    value: string | number | null | undefined,
    unit: string
): string => {
    const display = value ? `${value} ${unit}` : PLACE_HOLDER;
    return `${label}: ${display}`;
};
