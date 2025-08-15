import logo from "../../assets/img/bc-gov-logo.png";
import logo_rev from "../../assets/img/bc-gov-logo-rev.png";
import "./styles.css";
import { usePreference } from "@/contexts/PreferenceProvider";

/**
 * Renders an BC Gov Logo component.
 *
 * @returns {React.JSX.Element} The rendered BCGovLogo component.
 */
function BCGovLogo(): React.JSX.Element {
  const { userPreference } = usePreference();
  return (
    <img
      src={userPreference.theme === "g100" ? logo_rev : logo}
      alt="BCGov Logo"
      width={160}
      className="logo"
    />
  );
}

export default BCGovLogo;
