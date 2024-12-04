// ActionButtons.tsx

import React from "react";
import { Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";
import FavoriteButton from "../FavoriteButton";
import { useNotification } from "../../contexts/NotificationProvider";
import { setOpeningFavorite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";

interface ActionButtonsProps {
  favorited: boolean;
  rowId: string;
}

const ActionButtons: React.FC<ActionButtonsProps> = ({ favorited, rowId }) => {
  
  const { displayNotification } = useNotification();

  const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
    try {
      if(!newStatus){      
        await deleteOpeningFavorite(openingId);        
      }else{
        await setOpeningFavorite(openingId);        
      }
      displayNotification({
        title: `Opening Id ${openingId} ${!newStatus ? 'un' : ''}favourited`,          
        type: 'success',
        dismissIn: 8000,
        onClose: () => {}
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
      tooltipPosition="right"
      kind="ghost"
      size="sm"
      fill="#0073E6"
      favorited={favorited}
      onFavoriteChange={(newStatus: boolean) => handleFavoriteChange(newStatus, parseFloat(rowId))}
    />
    <Button
      hasIconOnly
      iconDescription="Document Download"
      kind="ghost"
      renderIcon={Icons.Download}
      onClick={() => null}
      size="md"
      disabled
    />
  </>
)};

export default ActionButtons;
