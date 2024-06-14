import { RecentAction } from "../../types/RecentAction";

export const rows: RecentAction[] = [
  {
    activityType: 'Opening details',
    openingId: '11',
    statusCode: 'PND',
    statusDescription: 'Pending',
    fileFormat: 'PDF file',
    lastUpdated: new Date(),
    lastUpdatedLabel: 'Now'
  },
  {
    activityType: 'Opening report',
    openingId: '12',
    statusCode: 'SUB',
    statusDescription: 'Submitted',
    fileFormat: 'Excel file',
    lastUpdated: new Date(),
    lastUpdatedLabel: '1 minute ago'
  },
  {
    activityType: 'Opening spatial',
    openingId: '13',
    statusCode: 'APP',
    statusDescription: 'Approved',
    fileFormat: 'CSV file',
    lastUpdated: new Date(),
    lastUpdatedLabel: 'Aug 20, 2023'
  },
  {
    activityType:'Opening spatial', 
    openingId: '14',
    statusCode: 'APP',
    statusDescription: 'Approved',
    fileFormat: 'PDF file',
    lastUpdated: new Date(),
    lastUpdatedLabel:'Nov 20, 2023'
   },
   {
    activityType:'Opening details', 
    openingId: '15',
    statusCode: 'PRG',
    statusDescription: 'In progress',
    fileFormat: 'Excel file',
    lastUpdated: new Date(),
    lastUpdatedLabel:'Now'
   }
];

export const headers = [
  {
    key: 'activityType',
    header: 'Activity Type',
  },
  {
    key: 'fileFormat',
    header: 'File Format',
  },
  {
    key: 'status',
    header: 'Status',
  },
  {
    key: 'lastUpdated',
    header: 'Last Updated',
  }
];

