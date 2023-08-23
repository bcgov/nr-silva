import React from "react";
import { logout } from "../../services/AuthService";
import { Button } from "@carbon/react";
import { Asleep, Light } from '@carbon/icons-react';
import { useThemePreference } from "../../utils/ThemePreference";
import { toggleTheme } from "../../utils/ThemeFunction";


const Dashboard: React.FC = () => {
  
  
  const { theme, setTheme } = useThemePreference();
    return (
      <>
        <div className="container">
          <div className="row">
            <div className="col-md-6 border border-primary">
              <div className="display-2">Hi There </div>
            </div>
            <div className="col-md-6">
              <div className="display-4">Welcome to the Dashboard Page</div>
            </div>
          </div>

          <div className="row my-3">
            <div className="col-6 col-lg-4 border border-primary p-5 bg-primary ">
              Column 1
            </div>
            <div className="col-6 col-lg-4 border border-primary p-5 bg-secondary text-white">
              Column 2
            </div>
            <div className="col-6 col-lg-4 border border-primary p-5 bg-success text-white">
              Column 3
            </div>
          </div>
          <div className="row">
            <div className="lead">Lorem, ipsum dolor sit amet consectetur adipisicing elit. Qui cupiditate nulla dolor debitis harum dignissimos velit, molestias cumque magni laborum explicabo eaque minima. Quos incidunt cupiditate id eveniet sed. Assumenda quia eveniet animi maiores harum.</div>
          </div>

          <div className="mt-5">
            <Button kind='secondary' renderIcon={theme === 'g10'?Asleep:Light}  onClick={()=>toggleTheme(theme,setTheme)} >Toggle Theme</Button>
          </div>
          <div className="mt-5">
            <Button kind='secondary'  onClick={()=>logout()} >Logout</Button>
          </div>
        </div>
      </>
    );
  };

export default Dashboard;
