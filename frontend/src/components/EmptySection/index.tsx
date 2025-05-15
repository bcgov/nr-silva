import React from 'react';
import * as Icons from '@carbon/icons-react';
import * as Pictograms from '@carbon/pictograms-react';
import Subtitle from '../Subtitle';
import './styles.scss';

interface EmptySectionProps {
  icon?: keyof typeof Icons;
  title: string;
  description: string | React.ReactNode;
  pictogram?: keyof typeof Pictograms;
  className?: string;
  whiteLayer?: boolean;
}

/**
 * Renders an Empty Section component.
 *
 * @param {object} props - Component props
 * @param {string} [props.icon] - Optional. The name of the icon to display.
 * @param {string} props.title - The title of the empty section.
 * @param {string | React.ReactNode} props.description - The description of the empty section.
 * @param {string} [props.pictogram] - Optional. The name of the pictogram to display.
 * @param {string} [props.whiteLayer] - Optional. Whether the background is white.
 * @returns {React.JSX.Element} A div element containing the empty section.
 */
function EmptySection({
  icon, title, description, pictogram, whiteLayer, className
}: EmptySectionProps): React.JSX.Element {
  let Img: React.ElementType | undefined;

  if (icon && Icons[icon]) {
    Img = Icons[icon] as React.ElementType;
  }

  if (pictogram && Pictograms[pictogram]) {
    Img = Pictograms[pictogram] as React.ElementType;
  }

  return (
    <div className={`${className ?? ''} empty-section-container ${whiteLayer ? 'empty-section-white-layer' : undefined}`}>
      {
        Img
          ? <Img className="empty-section-icon" data-testid="empty-section-icon" />
          : null
      }
      <div className="empty-section-title">
        {title}
      </div>
      <Subtitle className="empty-section-subtitle" text={description} />
    </div>
  );
};

export default EmptySection;
