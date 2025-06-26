import { TableHeaderType } from "@/types/TableHeader";
import { OpeningForestCoverDamageDto, OpeningForestCoverDetailedSpeciesDto, OpeningForestCoverLayerDto, OpeningForestCoverUnmappedDto } from "@/services/OpenApi";

export const UnmappedAreaHeaders: TableHeaderType<keyof OpeningForestCoverUnmappedDto>[] = [
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

export const LayerTableHeaders: TableHeaderType<keyof OpeningForestCoverLayerDto | keyof OpeningForestCoverDetailedSpeciesDto>[] = [
  {
    key: 'species',
    header: 'Species • Distribution'
  },
  {
    key: 'averageAge',
    header: 'Average age  • Average height'
  },
  {
    key: 'crownClosure',
    header: 'Crown closure'
  },
  {
    key: 'basalAreaSt',
    header: 'Basal area per total stems'
  },
  {
    key: 'totalStems',
    header: 'Stems'
  },
];

export const DamageAgentTableHeader: TableHeaderType<keyof OpeningForestCoverDamageDto>[] = [
  {
    key: 'damageAgent',
    header: 'Damage agent'
  },
  {
    key: 'healthIncidencePercentage',
    header: 'Forest health incidence'
  },
  {
    key: 'incidenceArea',
    header: 'Incidence area'
  }
];

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
  },
  veteranLayer: {
    title: 'Veteran layer',
    subtitle: 'Mature forest trees characterized by age, size, and features like dead wood, cavities, and decay.'
  }
}
