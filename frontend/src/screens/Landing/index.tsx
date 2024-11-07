import React from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button } from "@carbon/react";
import { Login } from '@carbon/icons-react';
import './Landing.scss';
import '../../custom.scss';
import { useLottie } from "lottie-react";
import silvaLottie from "../../assets/lotties/silva-logo-lottie-1.json"
import { useGetAuth } from "../../contexts/AuthProvider";

const Landing: React.FC = () => {

  const { login, isLoggedIn } = useGetAuth();

    // Adding the Lottie Loader and loading the View for lottie with initial options
    const options = {
      animationData: silvaLottie,
      loop: true
    };
    const { View } = useLottie(options);
    
    // If the user is already logged in, redirect to the dashboard
    if (isLoggedIn) {
      window.location.href = '/dashboard';
    }

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
          <div className="col-lg-5">
            <div className="lottie-container">
              {View}
            </div>
            <div className="text-end fixed-bottom py-2 pe-3">
              <a href="https://lottiefiles.com/animations/grow-your-forest-DywVyvml06">Animation by Sara Figueroa</a>
            </div>
          </div>
        </div>
      </div>
    );
  };

export default Landing;