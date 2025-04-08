export type DummyStandardUnitType = {
  standardUnitId: number;
  name: string;
  ssid: number | null;
  fspId: number | null;
}

export const DummyStandardUnits: DummyStandardUnitType[] = [
  {
    standardUnitId: 0,
    name: 'A',
    ssid: -12345,
    fspId: -789
  },
  {
    standardUnitId: 1,
    name: 'B',
    ssid: -12345,
    fspId: null
  },
  {
    standardUnitId: 2,
    name: 'C',
    ssid: null,
    fspId: null
  }
]
