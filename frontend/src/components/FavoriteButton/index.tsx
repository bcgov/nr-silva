import React, { useState, useEffect } from 'react';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './style.scss'; // Import the styles

interface FavoriteButtonProps {
  tooltipPosition: string;
  kind: string;
  size: string;
  fill: string;
  favorited: boolean;
  onFavoriteChange: (newStatus: boolean) => void;
}

/**
 * Renders an Favourite Button component.
 *
 * @param {object} props - Component props
 * @param {string} props.tooltipPosition - The tooltip position.
 * @param {string} props.kind - The favourite button kind.
 * @param {string} props.size - The favourite button size.
 * @param {string} props.fill - The favourite button fill.
 * @param {boolean} props.favorited - The favourite button state.
 * @returns {JSX.Element} The FavoriteButton element to be rendered.
 */
function FavoriteButton({
  tooltipPosition,
  kind,
  size,
  fill,
  favorited = false,
  onFavoriteChange
}: FavoriteButtonProps): JSX.Element {
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
  const CustomIcon = () => <Icon data-testid="favourite-button-icon" style={{ fill }} />;

  useEffect(() => {
    setIsFavorite(favorited);
  }, [favorited]);

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
      aria-pressed={isFavorite}
    />
  );
};

export default FavoriteButton;
