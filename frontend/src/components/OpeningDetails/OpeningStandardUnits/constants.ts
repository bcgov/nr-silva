import CodeDescriptionDto from "@/types/CodeDescriptionType";

export type DummyStandardUnitType = {
  standardUnitId: number;
  name: string;
  ssid: number | null;
  fspId: number | null;
  species: (CodeDescriptionDto & { minHeight: number })[];
}

export const DummyStandardUnits: DummyStandardUnitType[] = [
  {
    standardUnitId: 0,
    name: 'A',
    ssid: -12345,
    fspId: -789,
    species: [
      { code: "TE", description: "Treebeard Elder", minHeight: 3.5 },
      { code: "WS", description: "Willowshade", minHeight: 1.1 },
      { code: "AM", description: "Amaranth Sprig", minHeight: 0.7 },
      { code: "BL", description: "Barkling", minHeight: 0.6 },
      { code: "FL", description: "Fangorn Leaf", minHeight: 2.2 },
      { code: "SH", description: "Shambling Husk", minHeight: 2.8 },
      { code: "VD", description: "Verdant Drift", minHeight: 1.4 },
    ]
  },
  {
    standardUnitId: 1,
    name: 'B',
    ssid: -12345,
    fspId: null,
    species: [
      { code: "YL", description: "Yggdrasil Leaflet", minHeight: 0.8 },
      { code: "WR", description: "Whomping Root", minHeight: 1.0 },
      { code: "DR", description: "Dryad's Whisper", minHeight: 0.9 },
      { code: "AN", description: "Ancient Nimbroot", minHeight: 2.5 },
    ]
  },
  {
    standardUnitId: 2,
    name: 'C',
    ssid: null,
    fspId: null,
    species: [
      { code: "GR", description: "Groot Sapling", minHeight: 0.5 },
      { code: "EN", description: "Entling Sprout", minHeight: 1.2 },
    ]
  }
]
