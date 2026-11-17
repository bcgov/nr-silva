import { BreadCrumbType } from "@/types/BreadCrumbTypes";
import { CREATE_ACTIVITY_PATH, OPENING_DETAILS_PATH, OPENINGS_PATH } from "@/routes/paths";
import { generatePath } from "react-router-dom";
import { ActivityFormType } from "./definitions";

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

export const NON_BASIC_SILV_BASE_CODES = new Set(['PL', 'DS']);

export const MAX_MULTISELECT_ITEMS = 5;

export const DefaultActivityForm: ActivityFormType = {
  status: { id: 'activity-status-input' },
  baseCode: { id: 'silv-base-code-dropdown' },
  techniqueCode: { id: 'silv-technique-dropdown' },
  methodCode: { id: 'silv-method-dropdown' },
  objectiveCodes: { id: 'activity-objective-codes-input', value: [] },
  fundingSourceCodes: { id: 'activity-funding-source-input', value: [] },
  projectNumber: { id: 'activity-project-number-input' },
  licenseeActivityId: { id: 'activity-licensee-id-input' },
  comment: { id: 'activity-comment-input' },
  plannedDate: { id: 'activity-planned-date-input' },
  plannedArea: { id: 'activity-planned-area-input' },
  plannedCost: { id: 'activity-planned-cost-input' },
  completionDate: { id: 'activity-completion-date-input' },
  treatedArea: { id: 'activity-treated-area-input' },
  actualCost: { id: 'activity-actual-cost-input' },
  targetPreparedSpots: { id: 'activity-target-prepared-spots-input' },
  surveyPlannedNumPlots: { id: 'activity-survey-planned-num-plots-input' },
  surveyActualNumPlots: { id: 'activity-survey-actual-num-plots-input' },
  surveyMinPlotsPerStratum: { id: 'activity-survey-min-plots-per-stratum-input' },
  file: { id: 'activity-spatial-file-drop-container' },
};
