import { OpeningsPerYearDto } from "../../types/OpeningTypes";

/**
 * The type that is used by the chart.
 */
export type SubmissionTrendChartObj = OpeningsPerYearDto & {
  group: string;
  key: string;
  value: number;
}
