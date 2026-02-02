import React, { useCallback, useEffect, useRef, useState } from "react";
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
import UserButton from "./UserButton";
import { LeftMenuItem, mainActivitiesItems } from "./constants";

import "./styles.scss";

/**
 * Renders an BC Headerw Side component.
 *
 * @returns {React.JSX.Element} The rendered BCHeaderwSide component.
 */
function BCHeader(): React.JSX.Element {
  const [myProfile, setMyProfile] = useState<boolean>(false);
  const panelWrapperRef = useRef<HTMLDivElement | null>(null);

  // Closes the MyProfile panel when user clicks outside of it.
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        panelWrapperRef.current &&
        !panelWrapperRef.current.contains(event.target as Node)
      ) {
        setMyProfile(false);
      }
    };

    if (myProfile) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [myProfile]);

  const handleMyProfilePanel = useCallback((): void => {
    setMyProfile(!myProfile);
  }, [myProfile]);

  const closeMyProfilePanel = useCallback((): void => {
    setMyProfile(false);
  }, []);

  const navigate = useNavigate();
  const location = useLocation();

  // Returns true when the provided `target` path should be considered active
  const isPathActive = (target: string | undefined): boolean => {
    if (!target) return false;
    const path = location.pathname;
    if (path === target) return true;
    return path.startsWith(target + "/");
  };

  const renderSideNavMenu = (
    subItem: LeftMenuItem,
    IconComponent: React.ElementType
  ) => {
    const isActive = subItem.subItems?.some((subSubItem) =>
      isPathActive(subSubItem.link)
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
      isActive={isPathActive(subSubItem.link)}
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
      isActive={isPathActive(subItem.link)}
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
          <div ref={panelWrapperRef}>
            <HeaderPanel
              aria-label="User Profile Tab"
              expanded={myProfile}
              className="notifications-panel"
            >
              <RightPanelTitle title="My Profile" closeFn={closeMyProfilePanel} />
              <MyProfile />
            </HeaderPanel>
          </div>
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

export default BCHeader;
