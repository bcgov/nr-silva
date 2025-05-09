import React, { useState, useEffect } from 'react';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';

import { FavoriteButtonProps } from './definitions';
import './style.scss';

/**
 * Renders an Favourite Button component.
 *
 * @param {object} props - Component props
 * @param {string} props.tooltipPosition - The tooltip position.
 * @param {string} props.kind - The favourite button kind.
 * @param {string} props.size - The favourite button size.
 * @param {string} props.fill - The favourite button fill.
 * @param {boolean} props.favorited - The favourite button state.
 * @returns {React.JSX.Element} The FavoriteButton element to be rendered.
 */
const CustomIcon = ({ Icon, isFavorite }: { Icon: React.ElementType; isFavorite: boolean }) => (
  <Icon data-testid="favourite-button-icon" className={isFavorite ? 'favourited-icon' : 'unfavourited-icon'} />
);

const FavoriteButton = ({
  id,
  tooltipPosition,
  kind,
  size,
  favorited = false,
  onFavoriteChange,
  disabled
}: FavoriteButtonProps): React.JSX.Element => {
  const [isFavorite, setIsFavorite] = useState(favorited);

  const handleClick = () => {
    setIsFavorite(!isFavorite);
    onFavoriteChange(!isFavorite);
  };

  const iconName = isFavorite ? 'FavoriteFilled' : 'Favorite';
  const iconDescription = isFavorite ? 'Unfavorite Opening' : 'Favorite Opening';
  const Icon = Icons[iconName];

  if (!Icon) {
    return <div>Invalid icon name</div>;
  }

  useEffect(() => {
    setIsFavorite(favorited);
  }, [favorited]);

  return (
    <div className="favourite-button-container">
      <Button
        data-testid={`action-fav-${id}`}
        className="favorite-button"
        hasIconOnly
        iconDescription={iconDescription}
        tooltipPosition={tooltipPosition}
        kind={kind}
        onClick={handleClick}
        renderIcon={() => <CustomIcon Icon={Icon} isFavorite={isFavorite} />}
        size={size}
        aria-pressed={isFavorite}
        disabled={disabled}
      />
    </div>
  );
};

export default FavoriteButton;
