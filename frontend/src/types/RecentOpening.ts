export interface RecentOpening {
  openingId: string;
  fileId: string;
  cuttingPermit: string | null;
  timberMark: string | null;
  cutBlock: string | null;
  grossAreaHa: number | null;
  status: {code: string, description: string} | null;
  category: {code: string, description: string} | null;
  disturbanceStart: string | null;
  entryTimestamp: string | null;
  updateTimestamp: string | null;
}
