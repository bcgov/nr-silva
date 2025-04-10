import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { SpeciesHeaderType } from "./definitions";

export type DummyStandardUnitType = {
  standardUnitId: number;
  name: string;
  ssid: number | null;
  fspId: number | null;
  preferredSpecies: (CodeDescriptionDto & { minHeight: number })[];
  acceptableSpecies: (CodeDescriptionDto & { minHeight: number })[];
}

export const DummyStandardUnits: DummyStandardUnitType[] = [
  {
    standardUnitId: 0,
    name: 'A',
    ssid: -12345,
    fspId: -789,
    preferredSpecies: [
      { code: "TE", description: "Treebeard Elder", minHeight: 3.5 },
      { code: "WS", description: "Willowshade", minHeight: 1.1 },
      { code: "AM", description: "Amaranth Sprig", minHeight: 0.7 },
      { code: "BL", description: "Barkling", minHeight: 0.6 },
      { code: "FL", description: "Fangorn Leaf", minHeight: 2.2 },
      { code: "SH", description: "Shambling Husk", minHeight: 2.8 },
    ],
    acceptableSpecies: [
      { code: "VD", description: "Verdant Drift", minHeight: 1.4 },
    ]
  },
  {
    standardUnitId: 1,
    name: 'B',
    ssid: -12345,
    fspId: null,
    preferredSpecies: [
      { code: "YL", description: "Yggdrasil Leaflet", minHeight: 0.8 },
      { code: "WR", description: "Whomping Root", minHeight: 1.0 },
      { code: "DR", description: "Dryad's Whisper", minHeight: 0.9 },
    ],
    acceptableSpecies: [
      { code: "AN", description: "Ancient Nimbroot", minHeight: 2.5 },
    ]
  },
  {
    standardUnitId: 2,
    name: 'C',
    ssid: null,
    fspId: null,
    preferredSpecies: [
      { code: "GR", description: "Groot Sapling", minHeight: 0.5 },

    ],
    acceptableSpecies: [
      { code: "EN", description: "Entling Sprout", minHeight: 1.2 },
    ]
  }
];

export const PreferredSpeciesHeaders: SpeciesHeaderType[] = [
  {
    key: 'description',
    header: 'Preferred species'
  },
  {
    key: 'minHeight',
    header: 'Minimum height (m)'
  }
];

export const AcceptableSpeciesHeaders: SpeciesHeaderType[] = [
  {
    key: 'description',
    header: 'Acceptable species'
  },
  {
    key: 'minHeight',
    header: 'Minimum height (m)'
  }
];

