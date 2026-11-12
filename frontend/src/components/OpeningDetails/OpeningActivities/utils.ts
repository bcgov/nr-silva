import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import { CodeDescriptionDto, OpeningDetailsActivitiesActivitiesDto } from "@/services/OpenApi";
import { codeDescriptionToDisplayText, extractCodesFromCodeDescriptionArr } from "@/utils/multiSelectUtils";

/**
 * Generates tooltip definitions and display text for objectives.
 *
 * This function processes all objectives (`objectives`, falling back to `objective1..3`)
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

  return formatCodeDescriptionList(
    data.objectives?.length ? data.objectives : [data.objective1, data.objective2, data.objective3]
  );
};

/**
 * Generates tooltip definitions and display text for funding sources, using `fundingSources`
 * and falling back to the single `funding` value.
 */
export const formatActivityFundingSources = (
  data: OpeningDetailsActivitiesActivitiesDto | null,
): { tooltipDefinition: string[] | null; displayText: string } => {
  if (!data) {
    return {
      tooltipDefinition: null,
      displayText: PLACE_HOLDER
    }
  }

  return formatCodeDescriptionList(
    data.fundingSources?.length ? data.fundingSources : [data.funding]
  );
};

const formatCodeDescriptionList = (
  items: (CodeDescriptionDto | null | undefined)[],
): { tooltipDefinition: string[]; displayText: string } => {
  const validItems = items.filter(
    (item): item is CodeDescriptionDto => item != null && codeDescriptionToDisplayText(item) !== PLACE_HOLDER
  );

  const tooltipDefinition: string[] = validItems.map((item) => codeDescriptionToDisplayText(item));

  const displayText = extractCodesFromCodeDescriptionArr(validItems)
    .filter((code) => code != null && code !== '')
    .join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `);

  return {
    tooltipDefinition,
    displayText: displayText.length ? displayText : PLACE_HOLDER,
  };
};
