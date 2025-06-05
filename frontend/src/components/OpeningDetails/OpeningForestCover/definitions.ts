import { SortDirectionType } from "@/types/PaginationTypes";
import CodeDescriptionDto from "@/types/CodeDescriptionType";

// TODO: Replace 'any' with the actual type for forest cover data
export type ForestCoverFilterType = {
  page: number,
  size: number,
  filter?: string,
  sortField?: keyof any,
  sortDirection?: SortDirectionType,
};

export type OpeningForestCoverType = {
  forestCoverId: string; // Optional for new entries

  forestCoverPolygonId: string;
  forestCoverStandardUnit: string | null;
  forestCoverUnmappedArea: string | null;

  polygonAreaGross: number; // in ha
  polygonAreaNet: number;   // in ha

  stockingStatus: CodeDescriptionDto<string>; // e.g., "Immature", "Non-productive"
  stockingType: string;   // e.g., "Art - Artificial", "UNN - Unnatural"

  inventoryLayerSpecies: CodeDescriptionDto[];
  inventoryLayerTotalStems: number; // in ha
  inventoryLayerTotalWellSpaced?: number | null;
  inventoryLayerWellSpaced?: number | null;
  inventoryLayerFreeGrowing?: number | null;

  silvicultureLayerSpecies: CodeDescriptionDto[];
  silvicultureLayerTotalWellSpaced: number; // in st/ha
  silvicultureLayerWellSpaced: number;      // in st/ha
  silvicultureLayerFreeGrowing: number;     // in st/ha

  referenceYear: number;
};

export type PolygonDetailDto = {
  forestCoverId: string;
  reserveType: string | null;
  reserveObjective: string | null;
  siteClass: string | null;
  siteIndex: number | null;
  siteIndexSource: string | null;
  treeCoverPattern: string | null;
  reEntryYear: number | null;
  comment: string | null;
}

export type UnmappedAreaDto = {
  unmappedAreaId: string;
  area: number;
  stockingStatus: CodeDescriptionDto;
  stockingType: CodeDescriptionDto;
}

export type ForestManagementDto = {
  unmappedArea: UnmappedAreaDto[];
  layers?: null
}

export type OpeningForestCoverDetails = {
  polygonDetail: PolygonDetailDto,
  forestManagement: ForestManagementDto
};

export const mockOpeningDetailsForestCover: OpeningForestCoverType[] = [
  {
    forestCoverId: "3416434",
    forestCoverPolygonId: "A",
    forestCoverStandardUnit: null,
    forestCoverUnmappedArea: null,
    polygonAreaGross: 31.1,
    polygonAreaNet: 27.7,
    stockingStatus: {
      code: "NSR",
      description: "Not Satisfactorily Restocked"
    },
    stockingType: "Art - Artificial",
    inventoryLayerSpecies: [
      { code: "FDC", description: "Douglas Fir" },
      { code: "HW", description: "Hardwood" },
      { code: "DR", description: "Red Alder" }
    ],
    inventoryLayerTotalStems: 1383,
    inventoryLayerTotalWellSpaced: null,
    inventoryLayerWellSpaced: null,
    inventoryLayerFreeGrowing: null,
    silvicultureLayerSpecies: [
      { code: "FDC", description: "Douglas Fir" },
      { code: "HW", description: "Hardwood" }
    ],
    silvicultureLayerTotalWellSpaced: 800,
    silvicultureLayerWellSpaced: 748,
    silvicultureLayerFreeGrowing: 665,
    referenceYear: 2015
  },
  {
    forestCoverId: "3416435",
    forestCoverPolygonId: "B",
    forestCoverStandardUnit: null,
    forestCoverUnmappedArea: null,
    polygonAreaGross: 3.4,
    polygonAreaNet: 1.7,
    stockingStatus: {
      code: "NP",
      description: "Non-productive"
    },
    stockingType: "UNN - Unnatural",
    inventoryLayerSpecies: [
      { code: "FDC", description: "Douglas Fir" },
      { code: "HW", description: "Hardwood" },
      { code: "DR", description: "Red Alder" }
    ],
    inventoryLayerTotalStems: 1383,
    inventoryLayerTotalWellSpaced: null,
    inventoryLayerWellSpaced: null,
    inventoryLayerFreeGrowing: null,
    silvicultureLayerSpecies: [
      { code: "FDC", description: "Douglas Fir" },
      { code: "HW", description: "Hardwood" }
    ],
    silvicultureLayerTotalWellSpaced: 800,
    silvicultureLayerWellSpaced: 748,
    silvicultureLayerFreeGrowing: 665,
    referenceYear: 2015
  },
  {
    forestCoverId: "3416436",
    forestCoverPolygonId: "C",
    forestCoverStandardUnit: "B",
    forestCoverUnmappedArea: null,
    polygonAreaGross: 17.6,
    polygonAreaNet: 17.1,
    stockingStatus: {
      code: "IMM",
      description: "Immature"
    },
    stockingType: "Art - Artificial",
    inventoryLayerSpecies: [
      { code: "HW", description: "Hardwood" },
      { code: "CW", description: "Western Red Cedar" },
      { code: "FDC", description: "Douglas Fir" }
    ],
    inventoryLayerTotalStems: 2880,
    inventoryLayerTotalWellSpaced: null,
    inventoryLayerWellSpaced: null,
    inventoryLayerFreeGrowing: null,
    silvicultureLayerSpecies: [
      { code: "CW", description: "Western Red Cedar" },
      { code: "HW", description: "Hardwood" }
    ],
    silvicultureLayerTotalWellSpaced: 920,
    silvicultureLayerWellSpaced: 800,
    silvicultureLayerFreeGrowing: 720,
    referenceYear: 2015
  }
];

export const mockPolygonDetails: OpeningForestCoverDetails[] = [
  {
    polygonDetail: {
      forestCoverId: "3416434",
      reserveType: null,
      reserveObjective: "Riparian",
      siteClass: null,
      siteIndex: 36,
      siteIndexSource: "H - SI from stand before harvest",
      treeCoverPattern: null,
      reEntryYear: null,
      comment:
        "A Free Growing heli survey was done on this block by Mike Netzel and Paul Larsen on 2001/10/04. This block is Free Growing. There is some alder along the road through the block and along the creek. Mike Netzel"
    },
    forestManagement: {
      unmappedArea: [
        {
          unmappedAreaId: 'A1',
          area: 23,
          stockingStatus: {
            code: 'A', description: 'Alpine'
          },
          stockingType: {
            code: 'RT', description: 'Road'
          },
        },
        {
          unmappedAreaId: 'A2',
          area: 2.2,
          stockingStatus: {
            code: 'S', description: 'Swamp'
          },
          stockingType: {
            code: 'RT', description: 'Road'
          },
        }
      ],
    }
  },
  {
    polygonDetail: {
      forestCoverId: "3416435",
      reserveType: null,
      reserveObjective: "Wildlife",
      siteClass: null,
      siteIndex: 28,
      siteIndexSource: "S - SI from sample plot",
      treeCoverPattern: null,
      reEntryYear: 2010,
      comment:
        "Wildlife reserve established in 2010. Some evidence of deer browsing. Surveyed by Jane Doe."
    },
    forestManagement: {
      unmappedArea: [{
        unmappedAreaId: 'A1',
        area: 23,
        stockingStatus: {
          code: 'AF', description: 'Alpine Forest'
        },
        stockingType: {
          code: 'RT', description: 'Road'
        },
      }],
    }
  },
  {
    polygonDetail: {
      forestCoverId: "3416436",
      reserveType: "Riparian",
      reserveObjective: null,
      siteClass: "A",
      siteIndex: null,
      siteIndexSource: null,
      treeCoverPattern: "Patchy",
      reEntryYear: null,
      comment:
        "Riparian buffer zone. No recent activity. Maintained for water quality."
    },
    forestManagement: {
      unmappedArea: [{
        unmappedAreaId: 'A1',
        area: 23,
        stockingStatus: {
          code: 'C', description: 'Cultivated'
        },
        stockingType: {
          code: 'RT', description: 'Road'
        },
      }],
    }
  }
];
