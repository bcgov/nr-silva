export interface OpeningsSearch {
  openingId: number;
  fileId: string | null;
  categoryCode: string | null;
  categoryDescription: string | null;
  statusCode: string;
  statusDescription: string;
  cuttingPermitId: string | null;
  cutBlockId: string | null;
  orgUnitName: string;
  updateTimestamp: string;
}
