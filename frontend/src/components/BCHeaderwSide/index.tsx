import React, { useCallback, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
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
  SideNavMenu,
  SideNavMenuItem
} from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import RightPanelTitle from '../RightPanelTitle';
import ThemeToggle from '../ThemeToggle';
import MyProfile from '../MyProfile';
import ExpandingSearch from '../ExpandingSearch';
import { leftMenu } from './constants';
import './BCHeaderwSide.scss';

/**
 * Renders an BC Headerw Side component.
 *
 * @returns {JSX.Element} The rendered BCHeaderwSide component.
 */
function BCHeaderwSide(): JSX.Element {
  const [myProfile, setMyProfile] = useState<boolean>(false);
  const [notifications, setNotifications] = useState<boolean>(false);

  const handleMyProfilePanel = useCallback((): void => {
    setMyProfile(!myProfile);
    setNotifications(false);
  }, [myProfile]);

  const closeMyProfilePanel = useCallback((): void => {
    setMyProfile(false);
  }, []);

  const navigate = useNavigate();
  const location = useLocation();

  return (
    <HeaderContainer
      render={({ isSideNavExpanded, onClickSideNavExpand }: { isSideNavExpanded: boolean; onClickSideNavExpand: () => void }) => (
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
          <HeaderGlobalBar className="align-items-center w-100">
            <div className="mx-2">
              <ExpandingSearch />
            </div>
            <div className="mx-2">
              <ThemeToggle />
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
            <MyProfile />
          </HeaderPanel>
          <SideNav isChildOfHeader expanded={isSideNavExpanded} aria-label="Side menu" className="bcheaderwside-sidenav">
            <SideNavItems>
              {leftMenu.map(item => (
                <div key={item.name}>
                  <SideNavLink className="side-nav-category-name">
                    {item.name}
                  </SideNavLink>
                  {item.items.map(subItem => {
                    const IconComponent = Icons[subItem.icon as keyof typeof Icons];
                    if (subItem.subItems) {
                      const isActive = subItem.subItems.some(subSubItem => location.pathname.includes(subSubItem.link));
                      return (
                        <SideNavMenu
                          key={subItem.name}
                          title={subItem.name}
                          renderIcon={IconComponent}
                          isActive={isActive}
                          isSideNavExpanded={isActive}
                          defaultExpanded={isActive}
                        >
                          {subItem.subItems.map(subSubItem => (
                            <SideNavMenuItem
                              key={subSubItem.name}
                              onClick={() => navigate(subSubItem.link)}
                              isActive={location.pathname === subSubItem.link}
                            >
                              {subSubItem.name}
                            </SideNavMenuItem>
                          ))}
                        </SideNavMenu>
                      );
                    } else {
                      return (
                        <SideNavLink
                          key={subItem.name}
                          renderIcon={IconComponent}
                          onClick={() => navigate(subItem.link)}
                          isActive={location.pathname === subItem.link}
                        >
                          {subItem.name}
                        </SideNavLink>
                      );
                    }
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
