import logo from '../../assets/img/bc-gov-logo.png';
import './styles.css';

/**
 * Renders an BC Gov Logo component.
 *
 * @returns {JSX.Element} The rendered BCGovLogo component.
 */
function BCGovLogo(): JSX.Element {
  return (
    <img src={logo} alt="BCGov Logo" width={160} className="logo" />
  );
}

export default BCGovLogo;
