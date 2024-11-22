import React, { useEffect, useState } from 'react';
import FavoriteButton from '../FavoriteButton';
import { isOpeningFavourite, setOpeningFavorite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";
import { useNotification } from '../../contexts/NotificationProvider';

interface OpeningsMapEntryPopupProps {
  openingId: number;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({ openingId }) => {  

  const [isFavorited, setIsFavorited] = useState(false);
  const { displayNotification } = useNotification();

  useEffect(() => {
    const fetchFavouriteStatus = async () => {
      const openingFavourited = await isOpeningFavourite(openingId);
      setIsFavorited(openingFavourited);
    }
    fetchFavouriteStatus();
  },[openingId]);

  const handleFavoriteChange = async (newStatus: boolean) => {
    
    try {
      if(!newStatus){      
        await deleteOpeningFavorite(openingId);        
      }else{
        await setOpeningFavorite(openingId);        
      }
      setIsFavorited(newStatus);
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
  }

  return (
    <div className="d-flex justify-content-center align-items-center ">
      <FavoriteButton
        tooltipPosition="bottom"
        kind="ghost"
        size="sm"
        fill="#0073E6"
        favorited={isFavorited}
        onFavoriteChange={handleFavoriteChange}
      />
        <span className="trend-title">Opening ID</span>
        &nbsp;
        <span>{openingId}</span>
    </div>
  );
};

export default OpeningsMapEntryPopup;