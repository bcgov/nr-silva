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
        name: 'Opening',
        icon: 'MapBoundaryVegetation',
        link: '/opening',
        disabled: false,
        breadcrumb: false,
        subItems: [
          {
            name: 'Home page',
            link: '/opening',
            disabled: false,
            breadcrumb: false
          },
          {
            name: 'Silviculture search',
            link: '/silviculture-search',
            disabled: false,
            breadcrumb: true
          }
        ]
      }
    ]
  }
];

const managementItems: LeftMenu[] = [
  {
    name: 'Management',
    items: [
      {
        name: 'Settings',
        icon: 'Settings',
        link: '#',
        disabled: true,
        breadcrumb: false
      },
      {
        name: 'Notifications',
        icon: 'Notification',
        link: '#',
        disabled: true,
        breadcrumb: false
      }
    ]
  }
];

export const leftMenu: LeftMenu[] = [
  ...mainActivitiesItems,
  ...managementItems
];
