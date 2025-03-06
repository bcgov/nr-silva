import React from 'react';
import * as Icons from '@carbon/icons-react';
import * as Pictograms from '@carbon/pictograms-react';
import Subtitle from '../Subtitle';
import './styles.scss';

interface EmptySectionProps {
  icon?: string;
  title: string;
  description: string | React.ReactNode;
  pictogram?: string;
  fill?: string;
  className?: string;
}

/**
 * Renders an Empty Section component.
 *
 * @param {object} props - Component props
 * @param {string} [props.icon] - Optional. The name of the icon to display.
 * @param {string} props.title - The title of the empty section.
 * @param {string | React.ReactNode} props.description - The description of the empty section.
 * @param {string} [props.pictogram] - Optional. The name of the pictogram to display.
 * @param {string} [props.fill] - Optional. The fill color of the icon or pictogram.
 * @returns {React.JSX.Element} A div element containing the empty section.
 */
function EmptySection({
  icon, title, description, pictogram, fill = "#0073E6", className
}: EmptySectionProps): React.JSX.Element {
  let Img;

  if (icon) {
    Img = Icons[icon];
  }
  // If both icon and pictogram are passed in then pictogram will be used
  if (pictogram) {
    Img = Pictograms[pictogram];
  }

  return (
    <div className={`${className ?? ''} empty-section-container`}>
      <Img className="empty-section-icon" data-testid="empty-section-icon" style={{ fill: fill }} />
      <p className="empty-section-title">
        {title}
      </p>
      <Subtitle className="empty-section-subtitle" text={description} />
    </div>
  );
};

export default EmptySection;
