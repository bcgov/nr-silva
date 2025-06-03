import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { codeDescriptionToDisplayText, extractCodesFromCodeDescriptionArr } from "@/utils/multiSelectUtils";
import CodeDescriptionDto from "@/types/CodeDescriptionType";

/**
 * Generates tooltip definitions and display text for a species array.
 *
 * This function processes an array of CodeDescriptionDto objects and returns
 * a formatted tooltip definition and display text.
 *
 * @param species - The array of species (CodeDescriptionDto[]).
 * @returns An object containing:
 *  - `tooltipDefinition`: An array with formatted tooltip definitions for all species.
 *  - `displayText`: A string with formatted display text for all species, separated by bullet points.
 */
export const formatForestCoverSpeciesArray = (
  species: CodeDescriptionDto[] | null | undefined,
): { tooltipDefinition: string[]; displayText: string } => {
  if (!species || !Array.isArray(species)) {
    return {
      tooltipDefinition: [],
      displayText: PLACE_HOLDER
    }
  }

  const tooltipDefinition: string[] = species
    .filter((specie) => codeDescriptionToDisplayText(specie) !== PLACE_HOLDER)
    .map((specie) => codeDescriptionToDisplayText(specie));

  let displayText = extractCodesFromCodeDescriptionArr(species)
    .filter((code) => code != null && code !== '')
    .join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `);

  displayText = displayText.length ? displayText : PLACE_HOLDER

  return {
    tooltipDefinition,
    displayText,
  };
};
