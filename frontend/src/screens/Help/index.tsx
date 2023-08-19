import React from "react";
import { NavLink } from 'react-router-dom';
import { Button } from "@carbon/react"; 
import { ArrowLeft } from '@carbon/icons-react';

import './Help.scss';

const Help: React.FC = () => {
    return (
      <>
         <div className="contianer h-100">
          <div className="row align-items-center h-100">
            <div className="col text-center">
              <div className="display-1 py-4">Help Page</div>
              <h4 className="help-header">Welcome to the Help Page</h4>
                <p>This is just for the purpose of templating React Routing!</p>
                <NavLink to="/">
                    <Button kind='secondary' renderIcon={ArrowLeft} className="help-back-btn">Back to Home</Button>
                </NavLink>
            </div>
          </div>
         </div>
      </>
    );
  };

export default Help;