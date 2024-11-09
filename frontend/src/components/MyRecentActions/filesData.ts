import { RecentAction } from "../../types/RecentAction";
import { ITableHeader } from "../../types/TableHeader";

export const rows: RecentAction[] = [];

export const headers: ITableHeader[] = [
  {
    key: 'activityType',
    header: 'Activity Type',
    selected: true
  },
  {
    key: 'fileFormat',
    header: 'File Format',
    selected: true
  },
  {
    key: 'statusCode',
    header: 'Status',
    selected: true
  },
  {
    key: 'lastUpdatedLabel',
    header: 'Last Updated',
    selected: true
  }
];

