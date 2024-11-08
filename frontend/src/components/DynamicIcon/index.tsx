import * as CarbonIcons from '@carbon/icons-react';
import { FunctionComponent } from 'react';

// Define a type for the props your icon component will accept
interface IconProps {
  iconName: string;
  size?: number;
}

const DynamicIcon: FunctionComponent<IconProps> = ({ iconName, size = 24 }) => {
  // Dynamically access the icon using bracket notation
  const IconComponent = (CarbonIcons as Record<string, FunctionComponent<{ size?: number }>>)[iconName];

  // Check if the icon exists, if not render a fallback (optional)
  if (!IconComponent) {
    console.warn(`Icon "${iconName}" not found in Carbon icons`);
    return <span>Icon Not Found</span>;
  }

  // Render the icon component
  return <IconComponent size={size} />;
};

export default DynamicIcon;