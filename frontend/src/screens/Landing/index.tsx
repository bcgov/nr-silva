import React from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button } from "@carbon/react";
import { Login } from '@carbon/icons-react';
import { signIn } from "../../services/AuthService";
import './Landing.scss';
import LottieLoader from "../../components/LottieLoader";
import silvaLottie from "../../assets/lotties/silva-logo-lottie-1.json"

const Landing: React.FC = () => {

    return (
      <>
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-7 px-4">
            
              <BCGovLogo />
              {/* Welcome - Title and Subtitle */}
              <h1 data-testid="landing-title" className="landing-title">Welcome to SILVA</h1>
              <h2 data-testid="landing-subtitle" className="landing-subtitle">
                Plan, report and analyze your silviculture activities
              </h2>
              {/* Button Group */}
              <div className="row gy-3">
                <div className="col-xl-5 col-lg-6">
                  <Button
                    onClick={signIn}
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
                    onClick={() => { console.log("Hi BCEid was clicked") }}
                    renderIcon={Login}
                    data-testid="landing-button__bceid"
                    className="btn-landing"
                  >
                    Login with Business BCeID
                  </Button>
                </div>
              </div>

            </div>
            <div className="col-lg-5">
              <div className="lottie-container">
                <LottieLoader animationData={silvaLottie}/>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  };

export default Landing;