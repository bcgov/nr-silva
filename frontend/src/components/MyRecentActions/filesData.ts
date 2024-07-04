import { RecentAction } from "../../types/RecentAction";
import { ITableHeader } from "../../types/TableHeader";

export const rows: RecentAction[] = [
  {
    activityType: 'Opening details',
    openingId: '11',
    statusCode: 'PND',
    statusDescription: 'Pending',
    fileFormat: 'PDF file',
    lastUpdated: '-',
    lastUpdatedLabel: 'Now'
  },
  {
    activityType: 'Opening report',
    openingId: '12',
    statusCode: 'SUB',
    statusDescription: 'Submitted',
    fileFormat: 'Excel file',
    lastUpdated: '-',
    lastUpdatedLabel: '1 minute ago'
  },
  {
    activityType: 'Opening spatial',
    openingId: '13',
    statusCode: 'APP',
    statusDescription: 'Approved',
    fileFormat: 'CSV file',
    lastUpdated: '-',
    lastUpdatedLabel: 'Aug 20, 2023'
  },
  {
    activityType:'Opening spatial', 
    openingId: '14',
    statusCode: 'APP',
    statusDescription: 'Approved',
    fileFormat: 'PDF file',
    lastUpdated: '-',
    lastUpdatedLabel:'Nov 20, 2023'
   },
   {
    activityType:'Opening details', 
    openingId: '15',
    statusCode: 'PRG',
    statusDescription: 'In progress',
    fileFormat: 'Excel file',
    lastUpdated: '-',
    lastUpdatedLabel:'Now'
   }
];

export const headers: ITableHeader[] = [
  {
    key: 'activityType',
    header: 'Activity Type'
  },
  {
    key: 'fileFormat',
    header: 'File Format'
  },
  {
    key: 'statusCode',
    header: 'Status'
  },
  {
    key: 'lastUpdatedLabel',
    header: 'Last Updated'
  }
];
