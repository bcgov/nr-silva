import React from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button } from "@carbon/react";
import { Login } from '@carbon/icons-react';
import './Landing.scss';
import '../../custom.scss';
import { useGetAuth } from "../../contexts/AuthProvider";

const Landing: React.FC = () => {
  const { login } = useGetAuth();

  return (
    <div className="container-fluid">
      <div className="row">
        <div className=" col-xl-7 col-lg-7 px-4">
          <div className="mt-4 pb-5">
            <BCGovLogo />
          </div>

          {/* Welcome - Title and Subtitle */}
          <div className="mt-5">
            <h1 data-testid="landing-title" className="landing-title">Welcome to SILVA</h1>
            <h2 data-testid="landing-subtitle" className="landing-subtitle">
              Plan, report, and analyze your silviculture activities
            </h2>
          </div>
          {/* Button Group */}
          <div className="row gy-3">
            <div className="col-xl-5 col-lg-6">
              <Button
                onClick={() => login('idir')}
                renderIcon={Login}
                data-testid="landing-button__idir"
                className="btn-landing"
              >
                Login with IDIR
              </Button>
            </div>
            <div className="col-xl-5 col-lg-6 ">
              <Button
                kind="tertiary"
                onClick={() => login('bceid')}
                renderIcon={Login}
                data-testid="landing-button__bceid"
                className="btn-landing"
              >
                Login with Business BCeID
              </Button>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Landing;
