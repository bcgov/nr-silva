import { OpeningForestCoverLayerDto } from "@/services/OpenApi";
import { DefaultMultiLayerCodes, MultiLayerDisplayType, MultiLayerMainKey } from "./definitions";

/**
 * Groups forest cover layers into structured display format, ensuring keys are ordered.
 *
 * @param layers - List of OpeningForestCoverLayerDto items to group by primary layer number.
 * @returns A MultiLayerDisplayType object organized in order: "1", "2", "3", "4", "other".
 */
export function groupMultiLayerDisplay(
  layers: OpeningForestCoverLayerDto[]
): MultiLayerDisplayType {
  const tempMap: Partial<MultiLayerDisplayType> = {};
  const otherLayers: OpeningForestCoverLayerDto[] = [];

  for (const layer of layers) {
    const code = layer.layer.code ?? "";
    const isSilviculture = code.endsWith("S");
    const base = isSilviculture ? code.slice(0, -1) : code;

    if (DefaultMultiLayerCodes.includes(base as MultiLayerMainKey)) {
      const key = base as MultiLayerMainKey;

      let group = tempMap[key];
      if (!group) {
        group = { inventoryLayer: layer };
      } else {
        group = { ...group };
        if (isSilviculture) {
          group.silvicultureLayer = layer;
        } else {
          group.inventoryLayer = layer;
        }
      }

      tempMap[key] = group;
    } else {
      otherLayers.push(layer);
    }
  }

  // Construct the final sorted object
  const result: MultiLayerDisplayType = {};

  (DefaultMultiLayerCodes).forEach((key) => {
    if (tempMap[key]) {
      result[key] = tempMap[key]!;
    }
  });

  if (otherLayers.length > 0) {
    result.other = { layers: otherLayers };
  }

  return result;
}
