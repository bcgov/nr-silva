import * as CarbonIcon from '@carbon/icons-react';

/**
 * Renders a Carbon Icon component.
 *
 * @param {string} iconName - The name of the icon to render.
 * @returns {JSX.Element} A Carbon Icon component.
 */
function Icon ({iconName}: {iconName: string}): JSX.Element {
  const Base = CarbonIcon[iconName]
  return <Base data-testid={`carbon-icon-${iconName}`} />;
} 

export default Icon;
