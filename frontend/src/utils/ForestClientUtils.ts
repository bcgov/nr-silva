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
 * Sorts location options by numeric codes (ascending) first, then non-numeric strings (alphabetically).
 * Returns an array of objects with `id` and `label` properties suitable for dropdown rendering.
 *
 * @param locations - Array of CodeDescriptionDto objects to sort.
 * @returns Sorted array of location option objects with id and label.
 */
const parseFullNumericCode = (value: string): number | null => {
  const trimmed = value.trim();
  if (trimmed.length === 0) return null;

  let dotCount = 0;
  for (const char of trimmed) {
    if (char === '.') {
      dotCount += 1;
      if (dotCount > 1) return null;
      continue;
    }
    if (char < '0' || char > '9') {
      return null;
    }
  }

  if (trimmed === '.' || trimmed === '-.') return null;

  const numeric = Number(trimmed);
  return Number.isFinite(numeric) ? numeric : null;
};

export const sortLocationOptions = (
  locations?: CodeDescriptionDto[] | null
): Array<{ id: string; label: string }> => {
  return (locations?.map((location) => ({
    id: location.code ?? '',
    label: getClientLocationLabel(location),
  })) ?? []).sort((a, b) => {
    const numA = parseFullNumericCode(a.id);
    const numB = parseFullNumericCode(b.id);

    const isNumA = numA !== null;
    const isNumB = numB !== null;

    // Both numbers - sort numerically ascending
    if (isNumA && isNumB) {
      return (numA as number) - (numB as number);
    }

    // Only A is number - A comes first
    if (isNumA) {
      return -1;
    }

    // Only B is number - B comes first
    if (isNumB) {
      return 1;
    }

    // Both are strings - sort alphabetically
    return a.id.localeCompare(b.id);
  });
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

/**
 * Return a compact label for a client for use in tight UI spaces.
 * Preference order: `acronym`, then acronym generated from `name`, then `clientNumber`.
 * If none are present, returns the application `PLACE_HOLDER`.
 *
 * @param client - The client object (may be undefined or null).
 * @returns A short display string for the client.
 */
export const getClientSimpleLabel = (
  client?: ForestClientAutocompleteResultDto | null
): string => {
  if (client?.acronym) {
    return client.acronym;
  }
  if (client?.name) {
    return getClientNameAcronym(client.name);
  }
  if (client?.id) {
    return client.id;
  }
  return PLACE_HOLDER;
}

/**
 * Generates an acronym from a client name by taking the first letter of each word.
 *
 * @param clientName - The full client name (e.g., "TAAN FOREST LIMITED PARTNERSHIP").
 * @returns The acronym (e.g., "TFLP"), or an empty string if clientName is empty.
 */
export const getClientNameAcronym = (clientName?: string | null): string => {
  if (!clientName) return '';

  return clientName
    .split(' ')
    .filter(word => word.length > 0)
    .map(word => word.charAt(0).toUpperCase())
    .join('');
};
