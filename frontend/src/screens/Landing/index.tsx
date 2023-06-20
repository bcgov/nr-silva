import React from "react";
import { Button } from "@carbon/react";

import { signIn } from "../../services/AuthService";

const Landing: React.FC = () => {

    return (
      <>
        <div className="container">
          <div className="row">
            <div className="col-md-12 text-center">
              <div className="display-3">Welcome to the SILVA Landing Page</div>
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
            <Button kind='secondary'  onClick={()=>signIn()} >Login</Button>
          </div>
        </div>
      </>
    );
  };

export default Landing;