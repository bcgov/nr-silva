
import { BreadCrumbType } from "@/types/BreadCrumbTypes";
import { generatePath } from 'react-router-dom';
import { EDIT_TENURE_PATH, OPENING_DETAILS_PATH, OPENINGS_PATH } from "@/routes/paths";

export const EDIT_TENURE_TOAST_MESSAGES = {
  SAVED: 'Tenure information saved.',
  NO_CHANGES: 'No tenure changes to save.',
} as const;

export const getEditTenureCrumbs = (openingId: string): BreadCrumbType[] => [
  {
    name: 'Openings',
    path: OPENINGS_PATH
  },
  {
    name: `Opening ${openingId}`,
    path: `${generatePath(OPENING_DETAILS_PATH, { openingId })}?tab=tenure-identification`
  },
  {
    name: 'Edit tenure information',
    path: generatePath(EDIT_TENURE_PATH, { openingId })
  }
] as const;
