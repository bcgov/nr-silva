import { DisturbanceHeaderType } from "@/types/TableHeader";

export const defaultDisturbanceSearchTableHeaders: DisturbanceHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'fileId', header: 'File ID', selected: true },
  { key: 'cutBlock', header: 'Cut block', selected: true },
  { key: 'activityId', header: 'Activity ID', selected: true },
  { key: 'disturbance', header: 'Disturbance code', selected: true },
  { key: 'silvSystem', header: 'Silviculture system', selected: true },
  { key: 'variant', header: 'Variant', selected: true },
  { key: 'cutPhase', header: 'Cut phase', selected: true },
  { key: 'openingCategory', header: 'Opening category', selected: false },
  { key: 'orgUnit', header: 'Org unit', selected: false },
  { key: 'openingClient', header: 'Client', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];
