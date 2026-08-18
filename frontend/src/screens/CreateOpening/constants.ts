import { OpeningTypes } from '@/types/OpeningTypes';
import { CreateOpeningFormType } from './definitions';

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
