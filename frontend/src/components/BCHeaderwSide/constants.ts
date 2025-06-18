import * as Icons from '@carbon/icons-react';

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

export const mainActivitiesItems: LeftMenu[] = [
  {
    name: 'Main activities',
    items: [
      {
        id: 'dashboard',
        name: 'Dashboard',
        icon: 'Dashboard',
        link: '/dashboard',
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'silviculture-search',
        name: 'Silviculture search',
        icon: 'SearchLocate',
        link: '/silviculture-search',
        disabled: false,
        breadcrumb: false
      },
      {
        id: 'openings',
        name: 'Openings',
        icon: 'MapBoundaryVegetation',
        link: '/openings',
        disabled: false,
        breadcrumb: false,
      }
    ]
  }
];
