import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ClickableTile } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import { FavouriteCardProps } from './definitions';
import './styles.scss';

/**
 * A card component that displays a favourite item.
 *
 * @param {FavouriteCardProps} props - The component props.
 * @property {number} index - The index of the card.
 * @property {string} title - The title of the card.
 * @property {string} link - The link to navigate to when the card is clicked.
 * @property {string} icon - The name of the Carbon icon to display on the card.
 * @property {string} description - The description of the card.
 * @returns {React.ReactElement} The rendered card component.
 */
function FavouriteCard({
  index,
  title,
  link,
  icon
}: FavouriteCardProps): JSX.Element {
  const navigate = useNavigate();
  const Icon = Icons[icon];

  return (
    <ClickableTile
      id={`fav-card-${index}`}
      className='fav-card'
      tabIndex={index}
      onClick={() => navigate(link)}
    >
      <Icon className="fav-card-icon" />
      <p className="fav-card-title">{title}</p>
    </ClickableTile>
  );
}

export default FavouriteCard;
