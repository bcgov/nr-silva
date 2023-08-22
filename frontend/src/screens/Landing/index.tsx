import React from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button, InlineNotification } from "@carbon/react";
import { Login } from '@carbon/icons-react';
import { signIn } from "../../services/AuthService";
import './Landing.scss';
import '../../custom.scss';
import { useLottie } from "lottie-react";
import silvaLottie from "../../assets/lotties/silva-logo-lottie-1.json"
import ThemeToggle from "../../components/ThemeToggle";

const Landing: React.FC = () => {
    // Adding the Lottie Loader and loading the View for lottie with initial options
    const options = {
      animationData: silvaLottie,
      loop: true
    };
    const { View } = useLottie(options);
    return (
      <>
        <div className="container-fluid">
          <div className="row">
            <div className=" col-xl-7 col-lg-7 px-4">
              <div className="mt-4">
                <BCGovLogo />
              </div>

              <div className="mt-4 bg-primary p-4">
                <ThemeToggle/>
              </div>

              <div className="mt-3 ">
                <InlineNotification
                  icondescription="describes the close button"
                  subtitle="See the release notes to find out what's new"
                  title="New Release!"
                  kind="info"
                  lowContrast
                  className = "inline-notification"
                />
              </div>
              <div className="mt-3 ">
                <InlineNotification
                  icondescription="describes the close button"
                  subtitle="SILVA will be offline for up to 4 hours time starting on June 30, 2023 at 9:00pm"
                  title="Upcoming maintenance"
                  kind="warning"
                  lowContrast
                  className = "inline-notification"
                />
              </div>
              <div className="mt-3 ">
                <InlineNotification
                  icondescription="describes the close button"
                  subtitle="SILVA is expected to come back online on July 1, 2023 at 11:11am"
                  title="SILVA is down"
                  kind="error"
                  lowContrast
                  className = "inline-notification"
                />
              </div>

              {/* Welcome - Title and Subtitle */}
              <h1 data-testid="landing-title" className="landing-title">Welcome to SILVA</h1>
              <h2 data-testid="landing-subtitle" className="landing-subtitle">
                Plan, report and analyze your silviculture activities
              </h2>
              {/* Button Group */}
              <div className="row gy-3">
                <div className="col-xl-5 col-lg-6">
                  <Button
                    onClick={()=>signIn('idir')}
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
                    onClick={() => {signIn('bceid')}}
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
            </div>
          </div>
        </div>
      </>
    );
  };

export default Landing;