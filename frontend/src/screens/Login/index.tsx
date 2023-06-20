import React from "react";
import { signIn } from "../../services/AuthService";
import { useState } from "react";
import { Button, TextInput } from "@carbon/react";
import { Asleep, Light } from '@carbon/icons-react';
import { useThemePreference } from "../../utils/ThemePreference";

const Login: React.FC = () => {
  const { theme, setTheme } = useThemePreference();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const changeTheme = ()=>{
    if (theme === 'g10') {
      setTheme('g100');
      localStorage.setItem('mode', 'dark');
    }
    if (theme === 'g100') {
      setTheme('g10');
      localStorage.setItem('mode', 'light');
    }
  }

  return (
      <>
        <div className="container">
          <div className="row">
            <div className="col-md-6 border border-primary">
              <div className="display-2">Hi There</div>
            </div>
            <div className="col-md-6">
              <div className="display-4">Welcome to the Login Page</div>
            </div>
          </div>
          <div className="row">
              <div className="col-md-6">
              <TextInput
                  id="text-input-1"
                  type="text"
                  labelText="Username"
                  value={username}
                  onChange={(e:any)=>setUsername(e.target.value)}
                />
                <p>Value:{username}</p>
              </div>
              <div className="col-md-6">
              <TextInput
                  id="text-input-1"
                  type="password"
                  labelText="Password"
                  value={password}
                  onChange={(e:any)=>setPassword(e.target.value)}
                />
                <p>Value:{password+'kanbkdb'}</p>
              </div>
          </div>
          <div className="row">
          <Button kind='secondary'  onClick={()=>signIn()} >Login</Button>
          </div>

          <div className="mt-5">
            <Button kind='secondary' renderIcon={theme === 'g10'?Asleep:Light}  onClick={()=>changeTheme()} >Toggle Theme</Button>
          </div>
        </div>
      </>
    );
  };

export default Login;