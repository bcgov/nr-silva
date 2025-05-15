
import { TableHeaderType } from "@/types/TableHeader";
import { OpeningDetailsStockingLayerDto, OpeningDetailsStockingDto } from "@/types/OpeningTypes";

export const LayerHeaderConfig: TableHeaderType<keyof OpeningDetailsStockingLayerDto | keyof OpeningDetailsStockingDto>[] = [
  {
    key: 'layers',
    header: 'Layers'
  },
  {
    key: 'preferredSpecies',
    header: 'Preferred species • Minimum height'
  },
  {
    key: 'acceptableSpecies',
    header: 'Acceptable species • Minimum height'
  },
  {
    key: 'targetWellspacedTrees',
    header: 'Well spaced tress'
  },
  {
    key: 'minResidualBasalArea',
    header: 'Residual basal area'
  },
  {
    key: 'minPostspacingDensity',
    header: 'Post spacing'
  },
  {
    key: 'maxConiferous',
    header: 'Max coniferous'
  },
  {
    key: 'heightRelativeToComp',
    header: 'Height relative to competition'
  },
];
