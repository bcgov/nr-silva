import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { SideNavLink } from '@carbon/react';
import { Asleep, Light, UserFollow } from '@carbon/icons-react';
import AvatarImage from '../AvatarImage';
import { useThemePreference } from '../../utils/ThemePreference';
import PanelSectionName from '../PanelSectionName';
import OrganizationSelection from '../OrganizationSelection';
import './MyProfile.scss';
import { logout } from '../../services/AuthService';
import { RootState } from '../../store';

const MyProfile = () => {
  const { theme, setTheme } = useThemePreference();
  const userDetails = useSelector((state: RootState) => state.userDetails)
  const [goTo, setGoTo] = useState<boolean>(false);

  const changeTheme = () => {
    if (theme === 'g10') {
      setTheme('g100');
      localStorage.setItem('mode', 'dark');
    }
    if (theme === 'g100') {
      setTheme('g10');
      localStorage.setItem('mode', 'light');
    }
  };

  useEffect(() => {
    if (goTo) {
      setGoTo(false);
    }
  }, [goTo]);

  return (
    <>
      <div className="user-info-section">
        <div className="user-image">
          <AvatarImage userName={`${userDetails.user.firstName} ${userDetails.user.lastName}`} size="large" />
        </div>
        <div className="user-data">
          <p className="user-name">{`${userDetails.user.firstName} ${userDetails.user.lastName}`}</p>
          <p>{`IDIR: ${userDetails.user.userName}`}</p>
          <p>{`Email:${userDetails.user.email}`}</p>

        </div>
      </div>
      <hr className="divisory" />
      <nav className="account-nav">
        <ul>
          <li>
            <PanelSectionName title="Select organization" light />
            <div className="org-selection-container">
              <OrganizationSelection simpleView />
            </div>
          </li>
          <li>
            <hr className="divisory" />
            <PanelSectionName title="Options" light />
          </li>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={theme === 'g10'? Asleep : Light}
            onClick={() => { changeTheme(); }}
          >
            Change theme
          </SideNavLink>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={UserFollow}
            onClick={()=>{ logout() }}
          >
            Log out
          </SideNavLink>
          
        </ul>
        <hr className="divisory mt-5" />
      </nav>
    </>
  );
};

export default MyProfile;
