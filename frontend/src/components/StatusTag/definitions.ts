import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export const StatusColourMap: {[status: string]: keyof typeof TYPES} = {
  Completed: 'green',
  Cancelled: 'magenta',
  Active: 'blue',
  Expired: 'high-contrast',
  'Free growing': 'purple',
  Pending: 'gray',
  Submitted: 'cyan',
  Unknown: 'outline',
  Approved:'green',
  'In progress':'purple'
};
