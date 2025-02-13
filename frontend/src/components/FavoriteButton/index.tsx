import React, { useState, useEffect } from 'react';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import './style.scss'; // Import the styles

interface FavoriteButtonProps {
  id?: string
  tooltipPosition: string;
  kind: "ghost" | "tertiary" | "primary" | "secondary" | "danger" | "danger--primary" | "danger--ghost" | "danger--tertiary" | undefined;
  size: "lg" | "md" | "sm" | "xl" | "2xl" | undefined;
  favorited: boolean;
  onFavoriteChange: (newStatus: boolean) => void;
  disabled?: boolean;
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
const FavoriteButton = ({
  id,
  tooltipPosition,
  kind,
  size,
  favorited = false,
  onFavoriteChange,
  disabled
}: FavoriteButtonProps): JSX.Element => {
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
  const CustomIcon = () => (
    <Icon data-testid="favourite-button-icon" className={favorited ? 'favourited-icon' : 'unfavourited-icon'} />
  );

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
        renderIcon={CustomIcon}
        size={size}
        aria-pressed={isFavorite}
        disabled={disabled}
      />
    </div>
  );
};

export default FavoriteButton;
