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

export type LayerDto = {
  speciesDistribution: {
    species: CodeDescriptionDto;
    distribution: number;
    averageAge: number;
    averageHeight: number;
  }[];
  crownClosure: number;
  basalAreaPerTotalStems: number;
  totalStems: number;
  totalWellSpaced: number;
  wellSpaced: number;
  freeGrowing: number;

  damageAgent?: {
    species: CodeDescriptionDto;
    forestHealthIncidence: number;
    incidenceArea: number;
  }
}

export type ForestManagementLayerDto = {
  layer: CodeDescriptionDto;
  inventoryLayer: LayerDto;
  silvicultureLayer: LayerDto;
}

export type ForestManagementDto = {
  unmappedArea: UnmappedAreaDto[];
  layers: ForestManagementLayerDto[];
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
    "polygonDetail": {
      "forestCoverId": "3416434",
      "reserveType": null,
      "reserveObjective": "Riparian",
      "siteClass": null,
      "siteIndex": 36,
      "siteIndexSource": "H - SI from stand before harvest",
      "treeCoverPattern": null,
      "reEntryYear": null,
      "comment": "A Free Growing heli survey was done on this block by Mike Netzel and Paul Larsen on 2001/10/04."
    },
    "forestManagement": {
      "unmappedArea": [
        {
          "unmappedAreaId": "A1",
          "area": 23,
          "stockingStatus": {
            "code": "A",
            "description": "Alpine"
          },
          "stockingType": {
            "code": "RT",
            "description": "Road"
          }
        },
        {
          "unmappedAreaId": "A2",
          "area": 2.2,
          "stockingStatus": {
            "code": "S",
            "description": "Swamp"
          },
          "stockingType": {
            "code": "RT",
            "description": "Road"
          }
        }
      ],
      "layers": [
        {
          "layer": {
            "code": "01",
            "description": "Layer 1"
          },
          "inventoryLayer": {
            "speciesDistribution": [
              {
                "species": { "code": "PL", "description": "Lodgepole Pine" },
                "distribution": 60
              },
              {
                "species": { "code": "SX", "description": "Spruce" },
                "distribution": 40
              }
            ],
            "averageAge": 45,
            "averageHeight": 14.3,
            "crownClosure": 55,
            "basalAreaPerTotalStems": 18.2,
            "totalStems": 980,
            "totalWellSpaced": 720,
            "wellSpaced": 620,
            "freeGrowing": 500,
            "damageAgent": {
              "species": { "code": "IB", "description": "Insect - Bark Beetle" },
              "forestHealthIncidence": 2,
              "incidenceArea": 1.2
            }
          },
          "silvicultureLayer": {
            "speciesDistribution": [
              {
                "species": { "code": "PL", "description": "Lodgepole Pine" },
                "distribution": 100
              }
            ],
            "averageAge": 10,
            "averageHeight": 5.2,
            "crownClosure": 35,
            "basalAreaPerTotalStems": 6.4,
            "totalStems": 300,
            "totalWellSpaced": 280,
            "wellSpaced": 250,
            "freeGrowing": 200
          }
        }
      ]
    }
  },
  {
    "polygonDetail": {
      "forestCoverId": "3416435",
      "reserveType": null,
      "reserveObjective": "Wildlife",
      "siteClass": null,
      "siteIndex": 28,
      "siteIndexSource": "S - SI from sample plot",
      "treeCoverPattern": null,
      "reEntryYear": 2010,
      "comment": "Wildlife reserve established in 2010. Surveyed by Jane Doe."
    },
    "forestManagement": {
      "unmappedArea": [
        {
          "unmappedAreaId": "A1",
          "area": 23,
          "stockingStatus": { "code": "AF", "description": "Alpine Forest" },
          "stockingType": { "code": "RT", "description": "Road" }
        }
      ],
      "layers": [
        { "layer": { "code": "01", "description": "Layer 1" }, "inventoryLayer": { "speciesDistribution": [{ "species": { "code": "PL", "description": "Lodgepole Pine" }, "distribution": 60 }, { "species": { "code": "SX", "description": "Spruce" }, "distribution": 40 }], "averageAge": 36, "averageHeight": 13.5, "crownClosure": 70, "basalAreaPerTotalStems": 15.4, "totalStems": 1000, "totalWellSpaced": 850, "wellSpaced": 650, "freeGrowing": 500 }, "silvicultureLayer": { "speciesDistribution": [{ "species": { "code": "FD", "description": "Douglas Fir" }, "distribution": 100 }], "averageAge": 15, "averageHeight": 6.0, "crownClosure": 40, "basalAreaPerTotalStems": 7.5, "totalStems": 350, "totalWellSpaced": 300, "wellSpaced": 280, "freeGrowing": 250 } }
      ]
    }
  },
  {
    "polygonDetail": {
      "forestCoverId": "3416436",
      "reserveType": "Riparian",
      "reserveObjective": null,
      "siteClass": "A",
      "siteIndex": null,
      "siteIndexSource": null,
      "treeCoverPattern": "Patchy",
      "reEntryYear": null,
      "comment": "Riparian buffer zone. No recent activity. Maintained for water quality."
    },
    "forestManagement": {
      "unmappedArea": [
        {
          "unmappedAreaId": "A1",
          "area": 23,
          "stockingStatus": { "code": "C", "description": "Cultivated" },
          "stockingType": { "code": "RT", "description": "Road" }
        }
      ],
      "layers": [
        { "layer": { "code": "01", "description": "Layer 1" }, "inventoryLayer": { "speciesDistribution": [{ "species": { "code": "AT", "description": "Trembling Aspen" }, "distribution": 50 }, { "species": { "code": "EP", "description": "Poplar" }, "distribution": 50 }], "averageAge": 28, "averageHeight": 10, "crownClosure": 65, "basalAreaPerTotalStems": 12.5, "totalStems": 850, "totalWellSpaced": 600, "wellSpaced": 500, "freeGrowing": 400, "damageAgent": { "species": { "code": "FR", "description": "Frost" }, "forestHealthIncidence": 3, "incidenceArea": 0.8 } }, "silvicultureLayer": { "speciesDistribution": [{ "species": { "code": "PL", "description": "Lodgepole Pine" }, "distribution": 100 }], "averageAge": 10, "averageHeight": 2, "crownClosure": 30, "basalAreaPerTotalStems": 5, "totalStems": 200, "totalWellSpaced": 180, "wellSpaced": 150, "freeGrowing": 120 } },
        { "layer": { "code": "02", "description": "Layer 2" }, "inventoryLayer": { "speciesDistribution": [{ "species": { "code": "FD", "description": "Douglas Fir" }, "distribution": 80 }, { "species": { "code": "CW", "description": "Western Red Cedar" }, "distribution": 20 }], "averageAge": 32, "averageHeight": 14, "crownClosure": 70, "basalAreaPerTotalStems": 18, "totalStems": 950, "totalWellSpaced": 700, "wellSpaced": 650, "freeGrowing": 600 }, "silvicultureLayer": { "speciesDistribution": [{ "species": { "code": "FD", "description": "Douglas Fir" }, "distribution": 100 }], "averageAge": 12, "averageHeight": 4, "crownClosure": 35, "basalAreaPerTotalStems": 6, "totalStems": 300, "totalWellSpaced": 280, "wellSpaced": 250, "freeGrowing": 200 } },
        { "layer": { "code": "03", "description": "Layer 3" }, "inventoryLayer": { "speciesDistribution": [{ "species": { "code": "SX", "description": "Spruce" }, "distribution": 60 }, { "species": { "code": "BL", "description": "Balsam" }, "distribution": 40 }], "averageAge": 40, "averageHeight": 16, "crownClosure": 80, "basalAreaPerTotalStems": 22, "totalStems": 1050, "totalWellSpaced": 900, "wellSpaced": 750, "freeGrowing": 700 }, "silvicultureLayer": { "speciesDistribution": [{ "species": { "code": "SX", "description": "Spruce" }, "distribution": 100 }], "averageAge": 20, "averageHeight": 8, "crownClosure": 50, "basalAreaPerTotalStems": 10, "totalStems": 400, "totalWellSpaced": 380, "wellSpaced": 350, "freeGrowing": 300 } },
        { "layer": { "code": "04", "description": "Layer 4" }, "inventoryLayer": { "speciesDistribution": [{ "species": { "code": "HW", "description": "Hardwood Mix" }, "distribution": 100 }], "averageAge": 22, "averageHeight": 9, "crownClosure": 45, "basalAreaPerTotalStems": 14, "totalStems": 700, "totalWellSpaced": 650, "wellSpaced": 500, "freeGrowing": 450 }, "silvicultureLayer": { "speciesDistribution": [{ "species": { "code": "HW", "description": "Hardwood Mix" }, "distribution": 100 }], "averageAge": 7, "averageHeight": 3, "crownClosure": 20, "basalAreaPerTotalStems": 4, "totalStems": 180, "totalWellSpaced": 160, "wellSpaced": 140, "freeGrowing": 100 } }
      ]
    }
  }
]
