// ActionButtons.tsx

import React from "react";
import { Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import { useNavigate } from "react-router-dom";
import FavoriteButton from "../FavoriteButton";
import { useNotification } from "../../contexts/NotificationProvider";
import { setOpeningFavorite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";

interface ActionButtonsProps {
  favorited: boolean;
  rowId: string;
  showDownload?: boolean;
  showToast?: boolean;
}

const ActionButtons: React.FC<ActionButtonsProps> = ({
  favorited,
  rowId,
  showDownload,
  showToast
}) => {
  const { displayNotification } = useNotification();
  const navigate = useNavigate();

  const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
    try {
      if (!newStatus) {
        await deleteOpeningFavorite(openingId);
      } else {
        await setOpeningFavorite(openingId);
      }
      if (showToast) {
        displayNotification({
          title: `Opening Id ${openingId} ${!newStatus ? 'un' : ''}favourited`,
          subTitle: newStatus ? "You can follow this opening ID on your dashboard" : undefined,
          type: 'success',
          dismissIn: 8000,
          buttonLabel: newStatus ? "Go to track openings" : undefined,
          onClose: () => {
            navigate("/opening?scrollTo=trackOpenings");
          }
        });
      }
    } catch (error) {
      displayNotification({
        title: 'Error',
        subTitle: `Failed to update favorite status for ${openingId}`,
        type: 'error',
        dismissIn: 8000,
        onClose: () => { }
      });
    }
  };

  return (
    <>
      <FavoriteButton
        id={rowId}
        tooltipPosition="right"
        kind="ghost"
        size="sm"
        fill="#0073E6"
        favorited={favorited}
        onFavoriteChange={(newStatus: boolean) => handleFavoriteChange(newStatus, parseFloat(rowId))}
      />
      {
        showDownload ? (
          <Button
            className="align-self-stretch"
            hasIconOnly
            iconDescription="Document Download"
            tooltipPosition="right"
            kind="ghost"
            renderIcon={Icons.Download}
            onClick={() => null}
            size="md"
            disabled
          />
        )
          : null
      }
    </>
  )
};

export default ActionButtons;
