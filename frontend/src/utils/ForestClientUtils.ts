import { ForestClientAutocomplete } from "../services/OpeningClientLocationService";
import CodeDescriptionDto from "../types/CodeDescriptionType";


/**
 * Generates a label for a ForestClientAutocomplete object.
 * The label consists of `name`, `id`, and `acronym`, separated by commas.
 * If any of these values are empty or missing, they are omitted.
 *
 * @param {ForestClientAutocomplete} client - The client object containing name, id, and acronym.
 * @returns {string} A formatted label with non-empty values.
 */
export const getClientLabel = (client?: ForestClientAutocomplete | null): string => {
  if (!client) {
    return '';
  }
  return [client.name, client.id, client.acronym]
    .filter(value => value && value.length > 0)
    .join(', ');
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
  if (!location) {
    return '';
  }
  return [location.code, location.description]
    .filter(value => value && value.length > 0)
    .join(' - ');
};
