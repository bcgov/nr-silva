import { PLACE_HOLDER } from "@/constants";
import { ForestClientAutocompleteResultDto, ForestClientDto } from "@/services/OpenApi";
import { CodeDescriptionDto } from "@/services/OpenApi";


/**
 * Generates a formatted label for a ForestClientAutocomplete object.
 *
 * Combines the `name`, `id`, and `acronym` fields, separated by commas.
 * Fields that are missing or empty strings are excluded from the result.
 * If all fields are empty or the client is null/undefined, returns a placeholder
 * or an empty string depending on the `returnPlaceHolder` flag.
 *
 * @param {ForestClientAutocomplete | null | undefined} client - The client object.
 * @param {boolean} [returnPlaceHolder=false] - If true, returns PLACE_HOLDER when the label is empty.
 * @returns {string} A formatted string like "Name, ID, Acronym" or a fallback value.
 */
export const getClientLabel = (
  client?: ForestClientAutocompleteResultDto | null,
  returnPlaceHolder = false
): string => {
  const fallback = returnPlaceHolder ? PLACE_HOLDER : '';

  if (!client) return fallback;

  const label = [client.name, client.id, client.acronym].filter(Boolean).join(', ');
  return label || fallback;
};


/**
 * Generates a label for a CodeDescriptionDto object.
 * The label consists of `id` and `name`, separated by a hyphen.
 * If any of these values are empty or missing, they are omitted.
 *
 * @param {CodeDescriptionDto} location - The location object containing id and name.
 * @returns {string} A formatted label with non-empty values.
 */
export const getClientLocationLabel = (location?: CodeDescriptionDto | null): string => {
  if (!location || (location && !location.code && !location.description)) {
    return '';
  }
  return [location.code, location.description]
    .filter(value => value && value.length > 0)
    .join(' - ');
};

/**
 * Formats a ForestClientDto into a display string.
 *
 * @param {ForestClientDto | null | undefined} client - The client object to format.
 * @returns {string}
 * - `""` if the client is null or undefined.
 * - `"ACRONYM - Client Name"` if both `acronym` and `clientName` exist.
 * - Only the existing value if one is missing.
 */
export function formatForestClient(client?: ForestClientDto | null): string {
  if (!client) return '';

  const { acronym, clientName } = client;
  if (acronym && clientName) {
    return `${acronym} - ${clientName}`;
  }
  return acronym || clientName || '';
}
