
import { TableHeaderType } from "@/types/TableHeader";
import { OpeningDetailsStockingDto, OpeningDetailsStockingLayerDto, OpeningStockingHistoryOverviewDto } from "@/services/OpenApi";

export const LayerHeaderConfig: TableHeaderType<keyof OpeningDetailsStockingLayerDto | keyof OpeningDetailsStockingDto>[] = [
  {
    key: 'layers',
    header: 'Layers'
  },
  {
    key: 'preferredSpecies',
    header: 'Preferred species • Minimum height'
  },
  {
    key: 'acceptableSpecies',
    header: 'Acceptable species • Minimum height'
  },
  {
    key: 'targetWellspacedTrees',
    header: 'Well spaced tress'
  },
  {
    key: 'minResidualBasalArea',
    header: 'Residual basal area'
  },
  {
    key: 'minPostspacingDensity',
    header: 'Post spacing'
  },
  {
    key: 'maxConiferous',
    header: 'Max coniferous'
  },
  {
    key: 'heightRelativeToComp',
    header: 'Height relative to competition'
  },
];

export const HistoryOverviewTableHeaders: TableHeaderType<keyof OpeningStockingHistoryOverviewDto>[] = [
  {
    key: 'eventTimestamp',
    header: 'Date',
  },
  {
    key: 'auditAction',
    header: 'Action',
  },
  {
    key: 'amendmentNumber',
    header: 'Amendment #',
  },
  {
    key: 'suCount',
    header: 'Total SU',
  },
  {
    key: 'totalNar',
    header: 'Total NAR (ha)',
  },
  {
    key: 'esfSubmissionId',
    header: 'ESF Submission ID',
  },
  {
    key: 'submittedByUserId',
    header: 'Submitted By',
  },
  {
    key: 'approvedByUserId',
    header: 'Approved By',
  },
];
