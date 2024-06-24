export interface RecentAction {
  [key: string]: string | undefined;
  activityType: string;
  openingId: string;
  statusCode: string;
  statusDescription: string;
  lastUpdated: string,
  lastUpdatedLabel: string;
  fileFormat?: string;
}
