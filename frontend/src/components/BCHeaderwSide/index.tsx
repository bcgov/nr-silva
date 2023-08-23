import React, { useCallback, useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useThemePreference } from '../../utils/ThemePreference';
import { toggleTheme } from '../../utils/ThemeFunction';
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
  SideNavLink,
  Button
} from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './BCHeaderwSide.scss';

import RightPanelTitle from '../RightPanelTitle';
import { env } from '../../env';

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
        name: 'Seedlots',
        icon: 'SoilMoistureField',
        link: '/seedlot',
        disabled: true
      },
      {
        name: 'Seedlings',
        icon: 'CropGrowth',
        link: '#',
        disabled: true
      },
      {
        name: 'Nurseries',
        icon: 'CropHealth',
        link: '#',
        disabled: true
      },
      {
        name: 'Orchards',
        icon: 'MapBoundaryVegetation',
        link: '#',
        disabled: true
      },
      {
        name: 'Reports',
        icon: 'Report',
        link: '/reports',
        disabled: true
      },
      {
        name: 'Tests',
        icon: 'Chemistry',
        link: '#',
        disabled: true
      },
      {
        name: 'Parent tree',
        icon: 'Tree',
        link: '#',
        disabled: true
      },
      {
        name: 'Tree seed center',
        icon: 'Enterprise',
        link: '#',
        disabled: true
      },
      {
        name: 'Financial',
        icon: 'Money',
        link: '#',
        disabled: true
      }
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

  const version: string = `Version: ${env.REACT_APP_MAIN_VERSION}`;

  const [myProfile, setMyProfile] = useState<boolean>(false);
  const [notifications, setNotifications] = useState<boolean>(false);
  const [goToURL, setGoToURL] = useState<string>('');
  const [goTo, setGoTo] = useState<boolean>(false);

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

  useEffect(() => {
    if (goTo) {
      setGoTo(false);
      navigate(goToURL);
    }
  }, [goTo, goToURL, navigate]);

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
            BCGOV
            <span className="header-full-name"> Quickstarter React Template </span>
          </Link>
          <HeaderGlobalBar>
           <HeaderGlobalAction
                  aria-label={theme==='g10'?'Switch to Dark Mode':'Switch to Light Mode'}
                  tooltipAlignment="end"
                  onClick = {()=>{toggleTheme(theme,setTheme)}}
                  >
                  {/* Must have a child component */}
                  <>{theme === 'g10'?<Icons.Asleep size={20} />:<Icons.Light size={20} />}</>
            </HeaderGlobalAction>
            <HeaderGlobalAction
              aria-label="Notifications"
              data-testid="header-button__notifications"
              onClick={handleNotificationsPanel}
              isActive={notifications}
            >
              <Icons.Notification size={20} />
            </HeaderGlobalAction>
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
          <HeaderPanel aria-label="Notifications Tab" expanded={notifications} className="notifications-panel">
            <RightPanelTitle
              title="Notifications"
              closeFn={closeNotificationsPanel}
            />
          </HeaderPanel>
          <HeaderPanel aria-label="User Profile Tab" expanded={myProfile} className="notifications-panel">
            <RightPanelTitle
              title="My Profile"
              closeFn={closeMyProfilePanel}
            />
            <Button kind='secondary' onClick={()=>console.log('Hi Jazz')} >Log Out</Button>
          </HeaderPanel>
          <SideNav isChildOfHeader expanded={isSideNavExpanded} aria-label="Side menu" className="bcheaderwside-sidenav">
            <SideNavItems>
              {listItems.map((item: ListItems) => (
                <div key={item.name}>
                  {item.items.map((subItem: ListItem) => {
                    const IconComponent = Icons[subItem.icon];
                    return (
                      <SideNavLink
                        key={subItem.name}
                        renderIcon={IconComponent || ''}
                        onClick={() => {
                          setGoToURL(subItem.link);
                          setGoTo(true);
                        }}
                        isActive={window.location.pathname === subItem.link}
                      >
                        {subItem.name}
                      </SideNavLink>
                    );
                  })}
                </div>
              ))}
              <div className="support-section">
                <SideNavLink renderIcon={Icons.Help}>Need help?</SideNavLink>
              </div>
            </SideNavItems>
            <p className=''>{version}</p>
          </SideNav>
        </Header>
      )}
    />
  );
};

export default BCHeaderwSide;