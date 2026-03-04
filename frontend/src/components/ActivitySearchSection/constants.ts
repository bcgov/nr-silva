import { ActivityHeaderType } from "@/types/TableHeader";

export const defaultActivitySearchTableHeaders: ActivityHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'fileId', header: 'File ID', selected: true },
  { key: 'cutBlock', header: 'Cut block', selected: true },
  { key: 'cuttingPermit', header: 'Cutting permit', selected: false },
  { key: 'activityId', header: 'Activity ID', selected: true },
  { key: 'base', header: 'Base', selected: true },
  { key: 'technique', header: 'Technique', selected: true },
  { key: 'method', header: 'Method', selected: true },
  { key: 'isComplete', header: 'Activity status', selected: true },
  { key: 'treatmentAmountArea', header: 'Area (ha)', selected: false },
  { key: 'fundingSource', header: 'Funding source', selected: true },
  { key: 'intraAgencyNumber', header: 'Intra-agency number', selected: false },
  { key: 'openingCategory', header: 'Opening category', selected: false },
  { key: 'orgUnit', header: 'Org unit', selected: false },
  { key: 'openingClient', header: 'Client', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];
