import React, { useCallback, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import {
  SideNavLink
} from '@carbon/react';
import * as Icons from '@carbon/icons-react';


import AvatarImage from '../AvatarImage';

import { useThemePreference } from '../../utils/ThemePreference';

import './MyProfile.scss';
import { logout } from '../../services/AuthService';

const MyProfile = () => {
  const { theme, setTheme } = useThemePreference();
  const userData = {firstName:'Catherine', lastName:"Meng", idirUsername:"Jasgrewal", email:"jazz@test.com"};

  const [goToURL, setGoToURL] = useState<string>('');
  const [goTo, setGoTo] = useState<boolean>(false);

  const navigate = useNavigate();

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
      navigate(goToURL);
    }
  }, [goTo]);

  return (
    <>
      <div className="user-info-section">
        <div className="user-image">
          <AvatarImage userName={`${userData.firstName} ${userData.lastName}`} size="large" />
        </div>
        <div className="user-data">
          <p className="user-name">{`${userData.firstName} ${userData.lastName}`}</p>
          <p>{`IDIR: ${userData.idirUsername}`}</p>
          <p>{userData.email}</p>
        </div>
      </div>
      <hr className="divisory" />
      <nav className="account-nav">
        <ul>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={theme === 'g10'?Icons.Asleep:Icons.Light}
            onClick={() => { changeTheme(); }}
          >
            Change theme
          </SideNavLink>
          <SideNavLink
            className="cursor-pointer"
            renderIcon={Icons.UserFollow}
            onClick={()=>{logout()}}
          >
            Log out
          </SideNavLink>
        </ul>
      </nav>
    </>
  );
};

export default MyProfile;
