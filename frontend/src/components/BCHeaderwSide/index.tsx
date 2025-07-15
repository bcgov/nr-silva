import React, { useCallback, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
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
  SideNavMenuItem,
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import RightPanelTitle from "../RightPanelTitle";
import ThemeToggle from "../ThemeToggle";
import MyProfile from "../MyProfile";
import { LeftMenuItem, mainActivitiesItems } from "./constants";
import "./BCHeaderwSide.scss";
import UserButton from "./UserButton";

/**
 * Renders an BC Headerw Side component.
 *
 * @returns {React.JSX.Element} The rendered BCHeaderwSide component.
 */
function BCHeaderwSide(): React.JSX.Element {
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

  const renderSideNavMenu = (
    subItem: LeftMenuItem,
    IconComponent: React.ElementType
  ) => {
    const isActive = subItem.subItems?.some((subSubItem) =>
      location.pathname.startsWith(subSubItem.link)
    );

    return (
      <SideNavMenu
        key={subItem.id}
        aria-label={subItem.name}
        title={subItem.name}
        renderIcon={IconComponent as any}
        isActive={isActive}
        isSideNavExpanded={isActive}
        defaultExpanded={isActive}
      >
        {subItem.subItems?.map((subSubItem) =>
          renderSideNavMenuItem(subSubItem)
        )}
      </SideNavMenu>
    );
  };

  const renderSideNavMenuItem = (subSubItem: LeftMenuItem) => (
    <SideNavMenuItem
      id={subSubItem.id}
      data-testid={`side-nav-item-${subSubItem.id}`}
      key={subSubItem.id}
      aria-label={subSubItem.name}
      onClick={() => navigate(subSubItem.link)}
      isActive={location.pathname.startsWith(subSubItem.link)}
    >
      {subSubItem.name}
    </SideNavMenuItem>
  );

  const renderSideNavLink = (
    subItem: LeftMenuItem,
    IconComponent: React.ElementType
  ) => (
    <SideNavLink
      id={subItem.id}
      data-testid={`side-nav-link-${subItem.id}`}
      className="side-nav-item"
      key={subItem.id}
      aria-label={subItem.name}
      renderIcon={IconComponent as any}
      onClick={() => navigate(subItem.link)}
      isActive={location.pathname.startsWith(subItem.link)}
    >
      {subItem.name}
    </SideNavLink>
  );

  const renderSideNavItem = (subItem: LeftMenuItem) => {
    const IconComponent = Icons[subItem.icon as keyof typeof Icons];

    if (subItem.subItems) {
      return renderSideNavMenu(subItem, IconComponent);
    } else {
      return renderSideNavLink(subItem, IconComponent);
    }
  };

  return (
    <HeaderContainer
      render={({
        isSideNavExpanded,
        onClickSideNavExpand,
      }: {
        isSideNavExpanded: boolean;
        onClickSideNavExpand: () => void;
      }) => (
        <Header
          aria-label="Silva header"
          className="silva-header"
          data-testid="header"
        >
          <SkipToContent />
          <HeaderMenuButton
            aria-label="Open menu"
            onClick={onClickSideNavExpand}
            isActive={isSideNavExpanded}
          />
          <Link to="/" className="header-link" data-testid="header-name">
            Silva
          </Link>
          <HeaderGlobalBar className="silva-header-bar align-items-center">
            <div >
              <ThemeToggle />
            </div>
            <HeaderGlobalAction
              aria-label="User Settings"
              tooltipAlignment="end"
              data-testid="header-button__user"
              onClick={handleMyProfilePanel}
              isActive={myProfile}
              className="profile-action-button"
            >
              <UserButton />
            </HeaderGlobalAction>
          </HeaderGlobalBar>
          <HeaderPanel
            aria-label="User Profile Tab"
            expanded={myProfile}
            className="notifications-panel"
          >
            <RightPanelTitle title="My Profile" closeFn={closeMyProfilePanel} />
            <MyProfile />
          </HeaderPanel>
          <SideNav
            isChildOfHeader
            expanded={isSideNavExpanded}
            aria-label="Side menu"
            className="bcheaderwside-sidenav"
          >
            <SideNavItems>
              {mainActivitiesItems.map((item) => (
                <div key={item.name}>
                  <label className="side-nav-category-name">{item.name}</label>
                  {item.items.map((subItem) => renderSideNavItem(subItem))}
                </div>
              ))}
            </SideNavItems>
          </SideNav>
        </Header>
      )}
    />
  );
}

export default BCHeaderwSide;
