import React from 'react';
import { useNavigate } from 'react-router-dom';

import { Tile, OverflowMenu, OverflowMenuItem } from '@carbon/react';
import * as Icons from '@carbon/icons-react';

import './styles.scss';

interface FavouriteCardProps {
  index: number,
  title: string,
  link: string,
  icon: string,
  description:String
}

const FavouriteCard = ({
  index,
  title,
  link,
  icon,
  description
}: FavouriteCardProps) => {
  const Icon = Icons[String(icon)];
  const navigate = useNavigate();
  const favActQueryKey = ['favourite-activities'];

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
