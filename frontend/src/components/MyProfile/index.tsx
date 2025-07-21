import React, { useState, useEffect } from 'react';
import { SideNavLink } from '@carbon/react';
import { Asleep, Light, UserFollow } from '@carbon/icons-react';
import { useThemePreference } from '@/utils/ThemePreference';
import PanelSectionName from '@/components/PanelSectionName';
import DistrictSelection from '@/components/DistrictSelection';
import Avatar from '@/components/Avatar';
import { useAuth } from '@/contexts/AuthProvider';

import './styles.scss';

const MyProfile = () => {
  const { theme, setTheme } = useThemePreference();
  const [goTo, setGoTo] = useState<boolean>(false);
  const { logout, user } = useAuth();

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
    <div className="my-profile-container">
      <div className="user-info-section">
        <div className="user-avatar">
          <Avatar
            size="md"
            initial={`${user?.firstName?.charAt(0).toUpperCase()}${user?.lastName?.charAt(0).toUpperCase()}`}
          />
        </div>
        <div className="user-data">
          <p className="user-name">{`${user?.firstName} ${user?.lastName}`}</p>
          <p>{`${user?.idpProvider ? user?.idpProvider + ': ' : null}${user?.userName}`}</p>
          <p>{`Email: ${user?.email}`}</p>

        </div>
      </div>
      <nav className="account-nav">
        <ul>
          {
            user?.associatedClients.length
              ? (
                <li>
                  <PanelSectionName title="Select district office" light />
                  <div className="district-selection-container">
                    <DistrictSelection simpleView />
                  </div>
                </li>
              )
              : null
          }
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
      </nav>
    </div>
  );
};

export default MyProfile;
