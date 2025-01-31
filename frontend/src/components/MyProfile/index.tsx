import React, { useState, useEffect } from 'react';
import { SideNavLink } from '@carbon/react';
import { Asleep, Light, UserFollow } from '@carbon/icons-react';
import AvatarImage from '../AvatarImage';
import { useThemePreference } from '../../utils/ThemePreference';
import PanelSectionName from '../PanelSectionName';
import './MyProfile.scss';
import { useAuth } from '../../contexts/AuthProvider';

const MyProfile = () => {
  const { theme, setTheme } = useThemePreference();
  const [goTo, setGoTo] = useState<boolean>(false);
  const { logout, user: authUser } = useAuth();

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
          <AvatarImage userName={`${authUser?.firstName} ${authUser?.lastName}`} size="large" />
        </div>
        <div className="user-data">
          <p className="user-name">{`${authUser?.firstName} ${authUser?.lastName}`}</p>
          <p>{`IDIR: ${authUser?.userName}`}</p>
          <p>{`Email: ${authUser?.email}`}</p>

        </div>
      </div>
      <hr className="divisory" />
      <nav className="account-nav">
        <ul>
          <li>
            <hr className="divisory" />
            <PanelSectionName title="Options" light />
          </li>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={theme === 'g10' ? Asleep : Light}
            onClick={() => { changeTheme(); }}
          >
            Change theme
          </SideNavLink>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={UserFollow}
            onClick={() => { logout() }}
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
