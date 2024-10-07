export interface RecentOpening {
  id: string;
  openingId: string;
  fileId: string;
  cuttingPermit: string | null;
  timberMark: string | null;
  cutBlock: string | null;
  grossAreaHa: number | null;
  statusDesc: string | null;
  categoryDesc: string | null;
  disturbanceStart: string | null;
  entryTimestamp: string | null;
  updateTimestamp: string | null;
}
