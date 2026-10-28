import { BreadCrumbType } from "@/types/BreadCrumbTypes";
import { CREATE_ACTIVITY_PATH, OPENING_DETAILS_PATH, OPENINGS_PATH } from "@/routes/paths";
import { generatePath } from "react-router-dom";

export const getAddActivityCrumbs = (openingId: string): BreadCrumbType[] => [
  {
    name: 'Openings',
    path: OPENINGS_PATH
  },
  {
    name: `Opening ${openingId}`,
    path: `${generatePath(OPENING_DETAILS_PATH, { openingId })}?tab=activities`
  },
  {
    name: 'Add activity',
    path: generatePath(CREATE_ACTIVITY_PATH, { openingId })
  }
] as const;
