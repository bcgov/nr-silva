import React from "react";
import { Button, Column, Grid } from "@carbon/react";
import { Login } from "@carbon/icons-react";

import BCGovLogo from "../../components/BCGovLogo";
import LandingImg from "../../assets/img/landing.jpg";
import { useAuth } from "../../contexts/AuthProvider";
import useBreakpoint from "../../hooks/UseBreakpoint";
import { BreakpointType } from "../../types/BreakpointType";

import "./styles.scss";

const Landing: React.FC = () => {
  const { login } = useAuth();
  const breakpoint = useBreakpoint();

  // Unit is rem
  const elementMarginMap: Record<BreakpointType, number> = {
    max: 6,
    xlg: 6,
    lg: 6,
    md: 3,
    sm: 2.5,
  };

  /**
   * Defines the vertical gap between the title, subtitle, and buttons.
   */
  const elementGap = elementMarginMap[breakpoint] || elementMarginMap.sm;

  /**
   * Defines whether the login buttons should be on the same row.
   */
  const isBtnSingleRow =
    breakpoint === "max" || breakpoint === "xlg" || breakpoint === "md";

  return (
    <div className="landing-grid-container">
      <Grid fullWidth className="landing-grid">
        {/* First - Column */}
        <Column className="landing-content-col" sm={4} md={8} lg={8}>
          <div
            className="landing-content-wrapper"
            style={{ gap: `${elementGap}rem` }}
          >
            {/* Logo */}
            <div>
              <BCGovLogo />
            </div>

            {/* Welcome - Title and Subtitle */}
            <h1 data-testid="landing-title" className="landing-title">
              Welcome to Silva
            </h1>

            <h2 data-testid="landing-subtitle" className="landing-subtitle">
              Manage reforestation and land base investment activities
            </h2>

            {/* Login buttons */}
            <div
              className={`buttons-container ${isBtnSingleRow ? "single-row" : "two-rows"
                }`}
            >
              <Button
                type="button"
                onClick={() => login("IDIR")}
                renderIcon={Login}
                data-testid="landing-button__idir"
                className="login-btn"
              >
                Login with IDIR
              </Button>

              <Button
                type="button"
                kind="tertiary"
                onClick={() => login("BCEIDBUSINESS")}
                renderIcon={Login}
                data-testid="landing-button__bceid"
                className="login-btn"
                id="bceid-login-btn"
              >
                Login with Business BCeID
              </Button>
            </div>
          </div>
        </Column>

        {/* Landing cover */}
        <Column className="landing-img-col" sm={4} md={8} lg={8}>
          <img src={LandingImg} alt="Landing cover" className="landing-img" />
        </Column>
      </Grid>
    </div>
  );
};

export default Landing;
