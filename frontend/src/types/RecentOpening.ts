export interface RecentOpening {
  openingId: string;
  forestFileId: string;
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
