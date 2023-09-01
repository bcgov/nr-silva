import React from "react";
import { useThemePreference } from "../../utils/ThemePreference";


const Dashboard: React.FC = () => {
  
  
  const { theme, setTheme } = useThemePreference();
    return (
      <>
        <div className="container">
          <div className="row">
            <div className="col-md-6">
              <div className="display-6">Hi There, <br/>Welcome to Main Screen</div>
            </div>
          </div>
        </div>
      </>
    );
  };

export default Dashboard;
