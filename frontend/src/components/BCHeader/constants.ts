import * as Icons from '@carbon/icons-react';
import {
  DASHBOARD_PATH,
  OPENINGS_PATH,
  OPENINGS_SEARCH_PATH,
  ACTIVITY_SEARCH_PATH,
  FOREST_COVER_SEARCH_PATH,
  STANDARDS_UNIT_SEARCH_PATH,
  COMMENT_SEARCH_PATH,
} from '@/routes/paths';

export type LeftMenuItem = {
  id: string;
  name: string;
  icon?: keyof typeof Icons;
  link: string;
  disabled: boolean;
  breadcrumb: boolean;
  subItems?: LeftMenuItem[];
}

export type LeftMenu = {
  name: string;
  items: LeftMenuItem[];
}

export const getMainActivitiesItems = (): LeftMenu[] => [
  {
    name: 'Main activities',
    items: [
      {
        id: 'dashboard',
        name: 'Dashboard',
        icon: 'Dashboard',
        link: DASHBOARD_PATH,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'openings',
        name: 'Openings',
        icon: 'MapBoundaryVegetation',
        link: OPENINGS_PATH,
        disabled: false,
        breadcrumb: false,
      }
    ]
  },
  {
    name: 'Search',
    items: [
      {
        id: 'openings-search',
        name: 'Openings',
        icon: 'MapBoundaryVegetation',
        link: OPENINGS_SEARCH_PATH,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'activity-search',
        name: 'Activities',
        icon: 'CropHealth',
        link: ACTIVITY_SEARCH_PATH,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'standards-unit-search',
        name: 'Standards units',
        icon: 'Development',
        link: STANDARDS_UNIT_SEARCH_PATH,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'forest-cover-search',
        name: 'Forest covers',
        icon: 'VegetationAsset',
        link: FOREST_COVER_SEARCH_PATH,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'comment-search',
        name: 'Comments',
        icon: 'Chat',
        link: COMMENT_SEARCH_PATH,
        disabled: false,
        breadcrumb: false
      }
    ]
  }
];
