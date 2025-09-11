import { OpeningTypes } from '@/types/OpeningTypes';
import { CreateOpeningFormType } from './definitions';

export const TitleText: Record<OpeningTypes, string> = {
  TENURED_OPENING: 'Harvest associated with tenure',
  GOV_FUNDED_OPENING: 'Harvest associated with government funded silviculture regime or investment',
}

export const DefaultOpeningForm: CreateOpeningFormType = {
  client: {
    id: 'opening-map-file-drop-container',
  },
  orgUnit: {
    id: 'opening-org-unit-input',
  },
  category: {
    id: 'opening-category-input',
  },
  openingGrossArea: {
    id: 'opening-gross-area-input',
  },
  maxAllowablePermAccess: {
    id: 'opening-max-allowable-perm-access-input',
  },
  tenureInfo: {}
}
