import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export type StatusKeyType =
  | 'Completed'
  | 'Cancelled'
  | 'Active'
  | 'Expired'
  | 'Free growing'
  | 'Pending'
  | 'Submitted'
  | 'Unknown'
  | 'Approved'
  | 'In progress';

export const StatusColourMap: { [status in StatusKeyType]: keyof typeof TYPES } = {
  Completed: 'green',
  Cancelled: 'magenta',
  Active: 'blue',
  Expired: 'high-contrast',
  'Free growing': 'purple',
  Pending: 'gray',
  Submitted: 'cyan',
  Unknown: 'outline',
  Approved: 'green',
  'In progress': 'purple'
};
