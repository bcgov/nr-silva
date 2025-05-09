import { BreadCrumbType } from "@/types/BreadCrumbTypes";

export const OpeningDetailBreadCrumbs: BreadCrumbType[] = [
  {
    name: 'Openings',
    path: '/openings'
  }
] as const;

export const OpeningDetailsTabs = [
  'overview',
  'standard-units',
  'activities'
] as const;
