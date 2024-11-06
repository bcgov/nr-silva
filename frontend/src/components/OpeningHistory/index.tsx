import React from 'react';
import {
  ProgressIndicator,
  ProgressStep
} from '@carbon/react';

import History from '../../types/History';
import statusClass from '../../utils/HistoryStatus';
import FavoriteButton from '../FavoriteButton';
import './styles.scss';
import { deleteOpeningFavorite } from '../../services/OpeningFavoriteService';
import { useNotification } from '../../contexts/NotificationProvider';

interface OpeningHistoryProps {
  histories: History[];
}


const OpeningHistory: React.FC<OpeningHistoryProps> = ({ histories }: OpeningHistoryProps) => {
  const { displayNotification } = useNotification();

  const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
    try {
      if(!newStatus){      
        await deleteOpeningFavorite(openingId);
        displayNotification({
          title: 'Favorite Removed',
          subTitle: `Opening Id ${openingId} removed from favorites`,
          type: 'success',
          dismissIn: 8000,
          onClose: () => {}
        });
        
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
      {histories.map((history, index) => (
        <div key={index} className="col-12 col-sm-4">
          <div className='activity-history-col'>
            <div className="activity-history-header">
              <div className="d-flex flex-row align-items-center">
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
                {`Opening Id ${history.id}`}
              </div>
            </div>
            <ProgressIndicator vertical className="activity-history-box">
              {history.steps.map((step) => {
                const status = statusClass(step.status);
                return (
                  <ProgressStep
                    key={step.step.toString()}
                    complete={status.complete}
                    current={status.current}
                    invalid={status.invalid}
                    disabled={status.disabled}
                    label={step.description}
                    secondaryLabel={step.subtitle}
                    className = 'py-2'
                  />
                );
              })}
            </ProgressIndicator>
          </div>
        </div>
      ))}
    </div>
  </div>
);
};

export default OpeningHistory;
