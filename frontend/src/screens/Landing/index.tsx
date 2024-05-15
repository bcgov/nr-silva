import React, { useCallback } from "react";
import BCGovLogo from "../../components/BCGovLogo";
import { Button, InlineNotification } from "@carbon/react";
import { Login } from '@carbon/icons-react';
import { signIn } from "../../services/AuthService";
import './Landing.scss';
import '../../custom.scss';
import { useLottie } from "lottie-react";
import silvaLottie from "../../assets/lotties/silva-logo-lottie-1.json"
import ThemeToggle from "../../components/ThemeToggle";
import { useNavigate } from "react-router-dom";

const Landing: React.FC = () => {
    // Adding the Lottie Loader and loading the View for lottie with initial options
    const options = {
      animationData: silvaLottie,
      loop: true
    };
    const { View } = useLottie(options);
    const navigate = useNavigate();
    const login = useCallback(async (provider: string) => {
      try {
        await signIn(provider)
      } catch(e) {
        if (e && typeof e === "object" && "message" in e) {
          const messageError = e.message as string;
          if (messageError === 'There is already a signed in user.') {
            navigate('/dashboard');
          }
        }
      }
    }, []);
    return (
      <>
        <div className="container-fluid">
          <div className="row">
            <div className=" col-xl-7 col-lg-7 px-4">
              <div className="mt-4 pb-5">
                <BCGovLogo />
              </div>

              <div className="">
                <InlineNotification
                  icondescription="describes the close button"
                  subtitle="See the release notes to find out what's new"
                  title="New Release!"
                  kind="info"
                  lowContrast
                  className = "inline-notification"
                />
              </div>
              <div className="">
                <InlineNotification
                  icondescription="describes the close button"
                  subtitle="SILVA will be offline for up to 4 hours time starting on June 30, 2023 at 9:00pm"
                  title="Upcoming maintenance"
                  kind="warning"
                  lowContrast
                  className = "inline-notification"
                />
              </div>
              <div className="">
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
                    onClick={() => {login('bceid')}}
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
      </>
    );
  };

export default Landing;