import * as Icons from '@carbon/icons-react';

export type LeftMenuItem = {
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

const mainActivitiesItems: LeftMenu[] = [
  {
    name: 'Main activities',
    items: [
      {
        name: 'Dashboard',
        icon: 'Dashboard',
        link: '/dashboard',
        disabled: false,
        breadcrumb: false
      },
      {
        name: 'Silviculture search',
        icon: 'SearchLocate',
        link: '/silviculture-search',
        disabled: false,
        breadcrumb: false
      }
    ]
  }
];

export const leftMenu: LeftMenu[] = [
  ...mainActivitiesItems
];
