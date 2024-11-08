import React, { useState, useEffect } from "react";
import History from '../../types/History';
import FavoriteButton from '../FavoriteButton';
import EmptySection from "../EmptySection";
import './styles.scss';
import { useNotification } from '../../contexts/NotificationProvider';
import { fetchOpeningFavourites, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";


const OpeningHistory: React.FC = () => {
  const { displayNotification } = useNotification();
  const [histories, setHistories] = useState<History[]>([]);

  const loadTrends = async () => {    
    const history = await fetchOpeningFavourites();    
    setHistories(history?.map(item => ({ id: item, steps: [] })) || []);
  };

  useEffect(() => { loadTrends(); },[]);

  const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
    try {
      if(!newStatus){      
        await deleteOpeningFavorite(openingId);
        displayNotification({
          title: `Opening Id ${openingId} unfavourited`,          
          type: 'success',
          dismissIn: 8000,
          onClose: () => {}
        });
        loadTrends();
      }
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
    <div className='px-3 pb-3'>
      <div className="row activity-history-container gx-4">
        {histories && histories.length > 0 ?
        histories.map((history, index) => (
          <div key={index} className="col-12 col-sm-4">
            <div className='d-flex'>
              <div className="activity-history-header">              
                <div className="d-flex flex-row align-items-center" data-id={history.id}>
                  <div className="favorite-icon">
                    <FavoriteButton
                      tooltipPosition="bottom"
                      kind="ghost"
                      size="sm"
                      fill="#0073E6"
                      favorited={true}
                      onFavoriteChange={(newStatus: boolean) => handleFavoriteChange(newStatus, history.id)}
                    />
                  </div>
                  <span className="trend-title">Opening ID</span>
                  &nbsp;
                  {history.id}
                </div>
              </div>
            </div>
          </div>
        )):
        <div className="col-12">
          <EmptySection 
          pictogram="UserInsights"
          fill="#0073E6"
          title={"You don't have any favourites to show yet!"}
          description={"You can favourite your openings by clicking on the heart icon inside opening details page"}
          
          />
        </div>
        }
      </div>
    </div>
  );
};

export default OpeningHistory;
