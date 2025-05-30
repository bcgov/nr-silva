import { OpeningsPerYearDto } from "@/types/OpenApiTypes";

/**
 * The type that is used by the chart.
 */
export type SubmissionTrendChartObj = OpeningsPerYearDto & {
  group: string;
  key: string;
  value: number;
}
