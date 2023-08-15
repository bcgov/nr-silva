import React from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button, Toggle, ToastNotification, InlineNotification } from "@carbon/react";
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
              <div className="d-flex flex-row justify-content-between align-items-center mt-4">
                <BCGovLogo />
                <Toggle
                  size="md"
                  labelA="Off"
                  labelB="On"
                  defaultToggled
                  hideLabel
                  id="toggle-1"
                />
              </div>

              <div className="mt-3 ">
                <InlineNotification
                  iconDescription="describes the close button"
                  subtitle={<span>See the release notes to find out what's new</span>}
                  title={<span className="fw-bold">New Release!</span>}
                  kind="info"
                  lowContrast
                  className = "w-100"
                />
              </div>
              <div className="mt-3 ">
                <InlineNotification
                  iconDescription="describes the close button"
                  subtitle={<span>SILVA will be offline for up to 4 hours time starting on June 30, 2023 at 9:00pm</span>}
                  title={<span className="fw-bold">Upcoming maintenance</span>}
                  kind="warning"
                  lowContrast
                  className = "w-100"
                />
              </div>
              <div className="mt-3 ">
                <InlineNotification
                  iconDescription="describes the close button"
                  subtitle={<span>SILVA is expected to come back online on July 1, 2023 at 11:11am</span>}
                  title={<span className="fw-bold">SILVA is down</span>}
                  kind="error"
                  lowContrast
                  className = "w-100"
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
                <LottieLoader animationData={silvaLottie}/>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  };

export default Landing;