import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { OpeningDetailsActivitiesActivitiesDto } from "@/types/OpeningTypes";

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
  const objective1 = data?.objective1 as CodeDescriptionDto;
  const objective2 = data?.objective2 as CodeDescriptionDto;
  const objective3 = data?.objective3 as CodeDescriptionDto;

  const tooltipDefinition = [
    (objective1?.code && objective1?.description) ? `${objective1.code} - ${objective1.description}` : null,
    (objective2?.code && objective2?.description) ? `${objective2.code} - ${objective2.description}` : null,
    (objective3?.code && objective3?.description) ? `${objective3.code} - ${objective3.description}` : null,
  ].filter((objective) => objective !== null);

  const displayText = [
    objective1?.code ?? false,
    objective2?.code ?? false,
    objective3?.code ?? false
  ].filter(Boolean).join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `);

  if (displayText.length === 0) {
    return {
      tooltipDefinition: null,
      displayText: PLACE_HOLDER,
    };
  }

  return {
    tooltipDefinition,
    displayText,
  };
};
