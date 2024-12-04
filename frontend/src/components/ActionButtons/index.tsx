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
}

const ActionButtons: React.FC<ActionButtonsProps> = ({ favorited, rowId }) => {
  const { displayNotification } = useNotification();
  const navigate = useNavigate();

  const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
    try {
      if(!newStatus){      
        await deleteOpeningFavorite(openingId);        
      }else{
        await setOpeningFavorite(openingId);        
      }
      displayNotification({
        title: `Opening Id ${openingId} ${!newStatus ? 'un' : ''}favourited`, 
        subTitle: newStatus ? "You can follow this opening ID on your dashboard" : undefined,
        type: 'success',
        dismissIn: 8000,
        buttonLabel: newStatus ? "Go to track openings" : undefined,
          onClose: () => {
            navigate("/opening?tab=metrics&scrollTo=trackOpenings");
          }
      });
    } catch (error) {
      displayNotification({
        title: 'Error',
        subTitle: `Failed to update favorite status for ${openingId}`,
        type: 'error',
        dismissIn: 8000,
        onClose: () => {}
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
    <Button
      className="align-self-stretch"
      hasIconOnly
      iconDescription="Document Download"
      tooltipPosition="bottom-left"
      kind="ghost"
      renderIcon={Icons.Download}
      onClick={() => null}
      size="md"
      disabled
    />
  </>
)};

export default ActionButtons;
