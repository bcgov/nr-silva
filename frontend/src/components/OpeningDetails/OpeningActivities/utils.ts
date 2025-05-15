import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { OpeningDetailsActivitiesActivitiesDto } from "@/types/OpeningTypes";
import { codeDescriptionToDisplayText, extractCodesFromCodeDescriptionArr } from "@/utils/multiSelectUtils";

/**
 * Generates tooltip definitions and display text for objectives.
 *
 * This function processes up to three objectives (`objective1`, `objective2`, `objective3`)
 * from the provided data object and returns a formatted tooltip definition and display text.
 *
 * @param data - The data object containing objectives. Expected to conform to the `OpeningDetailsActivitiesActivitiesDto` type.
 * @returns An object containing:
 *  - `tooltipDefinition`: A string with formatted tooltip definitions for all objectives, separated by new lines.
 *  - `displayText`: A string with formatted display text for all objectives, separated by bullet points.
 */
export const formatActivityObjective = (
  data: OpeningDetailsActivitiesActivitiesDto | null,
): { tooltipDefinition: string[] | null; displayText: string } => {
  if (!data) {
    return {
      tooltipDefinition: null,
      displayText: PLACE_HOLDER
    }
  }

  const objectives = [data.objective1, data.objective2, data.objective3];

  const tooltipDefinition: string[] = objectives
    .filter((obj) => codeDescriptionToDisplayText(obj) !== PLACE_HOLDER)
    .map((obj) => codeDescriptionToDisplayText(obj));

  let displayText = extractCodesFromCodeDescriptionArr(objectives)
    .filter((code) => code != null && code !== '')
    .join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `);

  displayText = displayText.length ? displayText : PLACE_HOLDER

  return {
    tooltipDefinition,
    displayText,
  };
};
