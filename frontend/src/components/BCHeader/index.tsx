import React from "react";
import { useThemePreference } from "../../utils/ThemePreference";
import { toggleTheme } from "../../utils/ThemeFunction";
import {
  HeaderContainer,
  Header,
  SkipToContent,
  HeaderGlobalBar,
  HeaderGlobalAction,
  HeaderMenuButton
} from "@carbon/react";
import { BrowserRouter, NavLink, Link } from "react-router-dom";
import * as Icons from "@carbon/icons-react";
import "./BCHeader.scss";
import { HeaderContainerProps } from "./definitions";

const BCHeader: React.FC = () => {
  //can only be imported at component level
  const { theme, setTheme } = useThemePreference();

  return (
    <HeaderContainer
      render={({
        isSideNavExpanded,
        onClickSideNavExpand
      }: HeaderContainerProps) => (
        <BrowserRouter>
          <Header
            aria-label="React TS Carbon QuickStart"
            className="bcgov-header"
            data-testid="bc-header__header"
          >
            <SkipToContent />
            <HeaderMenuButton
              aria-label={
                isSideNavExpanded ? "Close menu" : "Open menu"
              }
              onClick={onClickSideNavExpand}
              isActive={isSideNavExpanded}
            />
            <Link
              to="/"
              className="header-link"
            >
              Silva
            </Link>
            <HeaderGlobalBar>
              <HeaderGlobalAction
                aria-label={
                  theme === "g10"
                    ? "Switch to Dark Mode"
                    : "Switch to Light Mode"
                }
                tooltipAlignment="end"
                onClick={() => {
                  toggleTheme(theme, setTheme);
                }}
              >
                {/* Must have a child component */}
                <>
                  {theme === "g10" ? (
                    <Icons.Asleep size={20} />
                  ) : (
                    <Icons.Light size={20} />
                  )}
                </>
              </HeaderGlobalAction>

              <NavLink to="/help">
                <HeaderGlobalAction aria-label="Help">
                  <Icons.Help size={20} />
                </HeaderGlobalAction>
              </NavLink>

              <HeaderGlobalAction
                aria-label="App Switch"
                tooltipAlignment="end"
              >
                <Icons.Switcher size={20} />
              </HeaderGlobalAction>
            </HeaderGlobalBar>
          </Header>
        </BrowserRouter>
      )}
    />
  );
};

export default BCHeader;
