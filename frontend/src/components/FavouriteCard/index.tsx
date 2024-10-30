import { ClickableTile } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './styles.scss';

interface FavouriteCardProps {
  index: number,
  title: string,
  link: string,
  icon: string,
  description:string
  disabled?: boolean
}

/**
 * A card component that displays a favourite item.
 *
 * @param {FavouriteCardProps} props - The component props.
 * @property {number} props.index - The index of the card.
 * @property {string} props.title - The title of the card.
 * @property {string} props.link - The link to navigate to when the card is clicked.
 * @property {string} props.icon - The name of the Carbon icon to display on the card.
 * @property {string} props.description - The description of the card.
 * @returns {JSX.Element} The rendered card component.
 */
function FavouriteCard ({
  index,
  title,
  link,
  icon,
  description,
  disabled = false
}: FavouriteCardProps): JSX.Element {
  const Icon = Icons[String(icon)];

  return (
    <ClickableTile
      id={`fav-card-${index}`}
      className={'fav-card-main' }
      tabIndex={index}
      href={link}
      disabled={disabled}
    >
      <div className="fav-card-header">
        <Icon className="fav-card-icon" />
        <p className="fav-card-title-small">{title}</p>
        <p className="fav-card-content-description fav-card-content-description-small">{description}</p>
      </div>

      <div className="fav-card-content">
        <p className="fav-card-title-large">{title}</p>
        <p className="fav-card-content-description">{description}</p>
      </div>
    </ClickableTile>
  );
};

export default FavouriteCard;
