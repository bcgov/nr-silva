import * as Icons from '@carbon/icons-react';
import {
  DashboardRoute, OpeningsRoute, OpeningsSearchRoute, ActivitySearchRoute,
  StandardsUnitSearchRoute, ForestCoverSearchRoute, CommentSearchRoute
} from '@/routes/config';

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
        link: DashboardRoute.path!,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'openings',
        name: 'Openings',
        icon: 'MapBoundaryVegetation',
        link: OpeningsRoute.path!,
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
        link: OpeningsSearchRoute.path!,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'activity-search',
        name: 'Activities',
        icon: 'CropHealth',
        link: ActivitySearchRoute.path!,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'standards-unit-search',
        name: 'Standards units',
        icon: 'Development',
        link: StandardsUnitSearchRoute.path!,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'forest-cover-search',
        name: 'Forest covers',
        icon: 'VegetationAsset',
        link: ForestCoverSearchRoute.path!,
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'comment-search',
        name: 'Comments',
        icon: 'Chat',
        link: CommentSearchRoute.path!,
        disabled: false,
        breadcrumb: false
      }
    ]
  }
];
