export interface OpeningPerYearChart {
  group: string;
  key: string;
  value: string;
  year: number;
  month: number;
  statusCount: Record<string, number>;
}
