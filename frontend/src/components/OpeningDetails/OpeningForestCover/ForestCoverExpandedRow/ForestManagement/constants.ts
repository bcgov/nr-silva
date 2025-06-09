import { TableHeaderType } from "@/types/TableHeader";
import { LayerDto, UnmappedAreaDto } from "../../definitions";

export const UnmappedAreaHeaders: TableHeaderType<keyof UnmappedAreaDto>[] = [
  {
    key: 'unmappedAreaId',
    header: 'Unmapped area ID'
  },
  {
    key: 'area',
    header: 'Area'
  },
  {
    key: 'stockingStatus',
    header: 'Stocking status'
  },
  {
    key: 'stockingType',
    header: 'Stocking type'
  }
];

export const LayerTableHeaders: TableHeaderType<keyof LayerDto | string>[] = [
  {
    key: 'speciesDistribution',
    header: 'Species • Distribution'
  },
  {
    key: 'averageAgeHeight',
    header: 'Average age  • Average height'
  },
  {
    key: 'crownClosure',
    header: 'Crown closure'
  },
  {
    key: 'basalAreaPerTotalStems',
    header: 'Basal area per total stems'
  },
  {
    key: 'totalStems',
    header: 'Stems'
  },
]

export const TEXT_CONFIG = {
  singleLayerDesc: 'A forest stand with trees predominantly in the same age or height class, forming a single canopy layer',
  multiLayerDesc: 'A forest stand with multiple vertical layers of vegetation, including different tree ages, sizes, and often species, creating a complex canopy.',
  inventoryLayer: {
    title: 'Inventory layer',
    subtitle: 'Data layer information related to all tree species'
  },
  silvicultureLayer: {
    title: 'Silviculture layer',
    subtitle: 'Data layer information related to preferred and acceptable tree species'
  }
}
