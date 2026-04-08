import { ForestCoverHeaderType } from "@/types/TableHeader";

export const DAMAGE_AGENT_DISPLAY_LIMIT = 3 as const;

export const defaultForestCoverSearchTableHeaders: ForestCoverHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'fileId', header: 'File ID', selected: false },
  { key: 'forestCoverId', header: 'Forest cover', selected: true },
  { key: 'damageAgents', header: 'Damaging agent type', selected: true },
  { key: 'stockingType', header: 'Stocking type', selected: true },
  { key: 'stockingStatus', header: 'Stocking status', selected: true },
  { key: 'openingCategory', header: 'Opening category', selected: false },
  { key: 'orgUnit', header: 'Org unit', selected: true },
  { key: 'regenDueDate', header: 'Regeneration due date', selected: false },
  { key: 'freeGrowingDueDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false },
];
