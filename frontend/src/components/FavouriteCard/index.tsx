import React from "react";
import { useNavigate } from "react-router-dom";
import { ClickableTile } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import { FavouriteCardProps } from "./definitions";
import { useModal } from "@/contexts/ModalContext";
import "./styles.scss";

/**
 * A card component that displays a favourite item.
 *
 * @param {FavouriteCardProps} props - The component props.
 * @property {number} index - The index of the card.
 * @property {string} title - The title of the card.
 * @property {string} link - The link to navigate to when the card is clicked.
 * @property {string} icon - The name of the Carbon icon to display on the card.
 * @returns {React.ReactElement} The rendered card component.
 */
function FavouriteCard({
  index,
  title,
  link,
  icon,
  opensModal
}: FavouriteCardProps): React.JSX.Element {
  const navigate = useNavigate();
  const { openModal } = useModal();
  const Icon = Icons[icon];

  const handleClick = () => {
    if (opensModal) {
      openModal('CREATE_OPENING');
    } else {
      navigate(link);
    }
  };

  return (
    <ClickableTile
      aria-label={title}
      data-testid={`fav-card-${index}`}
      role="button"
      id={`fav-card-${index}`}
      className="fav-card"
      tabIndex={index}
      onClick={handleClick}
    >
      <div className="fav-card-content">
        <Icon className="fav-card-icon" />
        <p className="fav-card-title">{title}</p>
      </div>
    </ClickableTile>
  );
}

export default FavouriteCard;
