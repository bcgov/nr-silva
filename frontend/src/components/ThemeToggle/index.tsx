import React, { useEffect, useState } from 'react';
import './ThemeToggle.scss';
import { AsleepFilled, LightFilled } from '@carbon/icons-react';
import { useThemePreference } from '../../utils/ThemePreference';
import { toggleTheme } from '../../utils/ThemeFunction';

const ThemeToggle = () => {
  const { theme, setTheme } = useThemePreference();
  console.log("theme:"+theme)
  const [isToggled, setIsToggled] = useState(theme === 'g10'?false:true);

  useEffect(()=>{
    setIsToggled(theme === 'g10'?false:true)
  },[])

  const handleToggle = async () => {
    toggleTheme(theme,setTheme)
    // keep the logic opposite at the time of sending the toggle
    setIsToggled(theme === 'g10'?true:false);
  };

  return (
    <div className={`theme-toggle ${isToggled ? 'on' : 'off'}`} onClick={handleToggle}>
      <div className="circle">
        {isToggled ? <AsleepFilled className="icon" /> : <LightFilled className="icon" />}
      </div>
    </div>
  );
};

export default ThemeToggle;
