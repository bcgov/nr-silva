import React, { useCallback, useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useThemePreference } from '../../utils/ThemePreference';
import {
  HeaderContainer,
  Header,
  SkipToContent,
  HeaderMenuButton,
  HeaderGlobalBar,
  HeaderGlobalAction,
  HeaderPanel,
  SideNav,
  SideNavItems,
  SideNavLink
} from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './BCHeaderwSide.scss';

import RightPanelTitle from '../RightPanelTitle';
import ThemeToggle from '../ThemeToggle';
import MyProfile from '../MyProfile';

interface ListItem {
  name: string;
  icon: string;
  link: string;
  disabled: boolean;
}
interface ListItems {
  name: string;
  items: ListItem[]
}

const listItems = [
  {
    name: 'Main activities',
    items: [
      {
        name: 'Dashboard',
        icon: 'Dashboard',
        link: '/dashboard',
        disabled: false
      },
      {
        name: 'Opening',
        icon: 'MapBoundaryVegetation',
        link: '/opening',
        disabled: false
      },
    ]
  },
  {
    name: 'Management',
    items: [
      {
        name: 'Settings',
        icon: 'Settings',
        link: '#',
        disabled: true
      },
      {
        name: 'Notifications',
        icon: 'Notification',
        link: '#',
        disabled: true
      }
    ]
  }
];

const BCHeaderwSide = () => {
  //can only be impored at component level
  const { theme, setTheme } = useThemePreference();

  const [myProfile, setMyProfile] = useState<boolean>(false);
  const [notifications, setNotifications] = useState<boolean>(false);

  const handleNotificationsPanel = useCallback((): void => {
    if (notifications) {
      setNotifications(false);
    } else {
      setNotifications(true);
    }
    setMyProfile(false);
  }, [notifications]);

  const handleMyProfilePanel = useCallback((): void => {
    if (myProfile) {
      setMyProfile(false);
    } else {
      setMyProfile(true);
    }
    setNotifications(false);
  }, [myProfile]);

  const closeNotificationsPanel = useCallback((): void => {
    setNotifications(false);
  }, []);

  const closeMyProfilePanel = useCallback((): void => {
    setMyProfile(false);
  }, []);

  const navigate = useNavigate();

  return (
    <HeaderContainer
      render={({ isSideNavExpanded, onClickSideNavExpand }: any) => (
        <Header
          aria-label="React TS Carbon Quickstart"
          className="quickstart-header"
          data-testid="header"
        >
          <SkipToContent />
          <HeaderMenuButton
            aria-label="Open menu"
            onClick={onClickSideNavExpand}
            isActive={isSideNavExpanded}
          />
          <Link to="/" className="header-link" data-testid="header-name">
            SILVA
          </Link>
          <HeaderGlobalBar className="align-items-center">
            <div className="mx-2">
              <ThemeToggle/>
            </div>
            
            <HeaderGlobalAction
              aria-label="User Settings"
              tooltipAlignment="end"
              data-testid="header-button__user"
              onClick={handleMyProfilePanel}
              isActive={myProfile}
            >
              <Icons.UserAvatar size={20} />
            </HeaderGlobalAction>
          </HeaderGlobalBar>
          <HeaderPanel aria-label="User Profile Tab" expanded={myProfile} className="notifications-panel">
            <RightPanelTitle
              title="My Profile"
              closeFn={closeMyProfilePanel}
            />
            <MyProfile/>
          </HeaderPanel>
          <SideNav isChildOfHeader expanded={isSideNavExpanded} aria-label="Side menu" className="bcheaderwside-sidenav">
            <SideNavItems>
              {listItems.map((item: ListItems) => (
                <div key={item.name}>
                  <SideNavLink className="side-nav-category-name">
                    {item.name}
                  </SideNavLink>
                  {item.items.map((subItem: ListItem) => {
                    const IconComponent = Icons[subItem.icon];
                    return (
                      <SideNavLink
                        key={subItem.name}
                        renderIcon={IconComponent || ''}
                        onClick={() => navigate(subItem.link)}
                        isActive={window.location.pathname === subItem.link}
                      >
                        {subItem.name}
                      </SideNavLink>
                    );
                  })}
                </div>
              ))}
            </SideNavItems>
          </SideNav>
        </Header>
      )}
    />
  );
};

export default BCHeaderwSide;
