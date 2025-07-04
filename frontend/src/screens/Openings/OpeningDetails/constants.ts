import { BreadCrumbType } from "@/types/BreadCrumbTypes";

export const OpeningDetailBreadCrumbs: BreadCrumbType[] = [
  {
    name: 'Openings',
    path: '/openings'
  }
] as const;

export const OpeningDetailsTabs = [
  'overview',
  'tenure-identification',
  'standard-units',
  'activities',
  'forest-cover',
  'attachments'
] as const;
