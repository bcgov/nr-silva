export interface RecentAction {
  [key: string]: string | undefined | Date;
  activityType: string;
  openingId: string;
  statusCode: string;
  statusDescription: string;
  lastUpdated: Date,
  lastUpdatedLabel: string;
  fileFormat?: string;
}
