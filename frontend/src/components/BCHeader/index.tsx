import React from "react";
import { Link } from "react-router-dom";
import { useThemePreference } from "../../utils/ThemePreference";
import { toggleTheme } from "../../utils/ThemeFunction";
import {
  HeaderContainer,
  Header,
  SkipToContent,
  HeaderNavigation,
  HeaderGlobalBar,
  HeaderGlobalAction,
  HeaderMenu,
  HeaderMenuButton,
  HeaderMenuItem,
  SideNav,
  SideNavItems,
  HeaderSideNavItems,

} from '@carbon/react';
import { NavLink } from "react-router-dom";
import * as Icons from '@carbon/icons-react';

import './BCHeader.scss'



const BCHeader: React.FC = () => {
  //can only be impored at component level
  const { theme, setTheme } = useThemePreference();

    return (
      <>
        <HeaderContainer
          render={({ isSideNavExpanded, onClickSideNavExpand }: any) => (
            <Header aria-label="React TS Carbon Quickstart"
            className="spar-header"
            data-testid="header">
              <SkipToContent />
              <HeaderMenuButton
                aria-label={isSideNavExpanded ? 'Close menu' : 'Open menu'}
                onClick={onClickSideNavExpand}
                isActive={isSideNavExpanded}
              />
              <Link to="/" className="header-link" data-testid="header-name">
                BCGOV
                <span className="header-full-name"> RESULTS EXAM</span>
              </Link>
              <HeaderNavigation aria-label="BC-Gov Starter">
                <HeaderMenuItem href="#">Link 1</HeaderMenuItem>
                <HeaderMenuItem href="#">Link 2</HeaderMenuItem>
                <HeaderMenuItem href="#">Link 3</HeaderMenuItem>
                <HeaderMenu isCurrentPage aria-label="Link 4" menuLinkName="Link 4">
                  <HeaderMenuItem href="#">Sub-link 1</HeaderMenuItem>
                  <HeaderMenuItem href="#">Sub-link 2</HeaderMenuItem>
                  <HeaderMenuItem href="#">Sub-link 3</HeaderMenuItem>
                </HeaderMenu>
              </HeaderNavigation>
              <HeaderGlobalBar>

              <HeaderGlobalAction
                  aria-label={theme==='g10'?'Switch to Dark Mode':'Switch to Light Mode'}
                  tooltipAlignment="end"
                  onClick = {()=>{toggleTheme(theme,setTheme)}}
                  >
                  {/* Must have a child component */}
                  <>{theme === 'g10'?<Icons.Asleep size={20} />:<Icons.Light size={20} />}</>
              </HeaderGlobalAction>

                <NavLink to='/help'>
                  <HeaderGlobalAction
                    aria-label="Help"
                  >
                    <Icons.Help size={20} />
                  </HeaderGlobalAction>
                </NavLink>
                
                

                <HeaderGlobalAction
                  aria-label="App Switch"
                  tooltipAlignment="end">
                  <Icons.Switcher size={20} />
                </HeaderGlobalAction>

              </HeaderGlobalBar>
                <SideNav
                aria-label="Side navigation"
                expanded={isSideNavExpanded}
                isPersistent={false}>
                  <SideNavItems>
                    <HeaderSideNavItems>
                      <HeaderMenuItem href="#">Link 1</HeaderMenuItem>
                      <HeaderMenuItem href="#">Link 2</HeaderMenuItem>
                      <HeaderMenuItem href="#">Link 3</HeaderMenuItem>
                      <HeaderMenu aria-label="Link 4" menuLinkName="Link 4">
                        <HeaderMenuItem href="#">Sub-link 1</HeaderMenuItem>
                        <HeaderMenuItem href="#">Sub-link 2</HeaderMenuItem>
                        <HeaderMenuItem href="#">Sub-link 3</HeaderMenuItem>
                      </HeaderMenu>
                    </HeaderSideNavItems>
                  </SideNavItems>
              </SideNav>
            </Header>
          )}
        />
      </>
    );
  };

export default BCHeader;