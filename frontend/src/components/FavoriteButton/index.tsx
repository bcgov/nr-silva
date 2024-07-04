import React, { useState } from 'react';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './style.scss'; // Import the styles

interface FavoriteButtonProps {
  tooltipPosition: string;
  kind: string;
  size: string;
  fill: string;
}

const FavoriteButton: React.FC<FavoriteButtonProps> = ({
  tooltipPosition,
  kind,
  size,
  fill,
}) => {
  const [isFavorite, setIsFavorite] = useState(false);

  const handleClick = () => {
    setIsFavorite(!isFavorite);
  };

  const iconName = isFavorite ? 'FavoriteFilled' : 'Favorite';
  const iconDescription = isFavorite ? 'Unfavorite Opening' : 'Favorite Opening';
  const Icon = Icons[iconName];

  if (!Icon) {
    return <div>Invalid icon name</div>;
  }

  const CustomIcon = () => <Icon style={{ fill }} />;

  return (
    <Button
      className={isFavorite ? 'favorite-button favorite' : 'favorite-button'}
      hasIconOnly
      iconDescription={iconDescription}
      tooltipPosition={tooltipPosition}
      kind={kind}
      onClick={handleClick}
      renderIcon={CustomIcon}
      size={size}
    />
  );
};

export default FavoriteButton;
