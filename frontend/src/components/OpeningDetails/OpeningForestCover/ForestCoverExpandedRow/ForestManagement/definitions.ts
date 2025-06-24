import { OpeningForestCoverLayerDto } from "@/services/OpenApi"


export const DefaultMultiLayerCodes = ["1", "2", "3", "4"] as const;
export type MultiLayerMainKey = typeof DefaultMultiLayerCodes[number];

export type MultiLayerKey = MultiLayerMainKey | "veteranLayer";

export type MultiLayerDisplayType = {
  [K in MultiLayerMainKey]?: {
    inventoryLayer: OpeningForestCoverLayerDto;
    silvicultureLayer?: OpeningForestCoverLayerDto;
  };
} & {
  veteranLayer?: OpeningForestCoverLayerDto;
};
