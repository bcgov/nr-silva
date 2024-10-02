import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Tile } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './styles.scss';

interface FavouriteCardProps {
  index: number,
  title: string,
  link: string,
  icon: string,
  description:String
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
  description
}: FavouriteCardProps): JSX.Element {
  const Icon = Icons[String(icon)];
  const navigate = useNavigate();

  return (
    <Tile
      className={'fav-card-main' }
      tabIndex={index}
      onClick={() => navigate(link)}
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
    </Tile>
  );
};

export default FavouriteCard;
