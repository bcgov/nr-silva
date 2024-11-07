import { useEffect, useState } from 'react';
import './ThemeToggle.scss';
import { AsleepFilled, LightFilled } from '@carbon/icons-react';
import { useThemePreference } from '../../utils/ThemePreference';
import { toggleTheme } from '../../utils/ThemeFunction';

const ThemeToggle = () => {
  const { theme, setTheme } = useThemePreference();
  const [isToggled, setIsToggled] = useState(theme !== 'g10');

  useEffect(()=>{
    setIsToggled(theme === 'g10'?false:true);
  },[theme])

  const handleToggle = async () => {
    toggleTheme(theme,setTheme)
    // keep the logic opposite at the time of sending the toggle
  };

  return (
    <div
      className={`theme-toggle ${isToggled ? 'on' : 'off'}`}
      onClick={handleToggle}
      role="button"
      tabIndex={0}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          handleToggle();
        }
      }}
    >
      <div className="circle">
        {isToggled ? <AsleepFilled className="icon" /> : <LightFilled className="icon" />}
      </div>
    </div>
  );
};

export default ThemeToggle;
