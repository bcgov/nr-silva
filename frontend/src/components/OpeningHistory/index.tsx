import {
  ProgressIndicator,
  ProgressStep
} from '@carbon/react';

import History from '../../types/History';
import statusClass from '../../utils/HistoryStatus';
import FavoriteButton from '../FavoriteButton';
import './styles.scss';

import { deleteOpeningFavorite } from '../../services/OpeningFavoriteService';

interface OpeningHistoryProps {
  histories: History[];
}

const handleFavoriteChange = async (newStatus: boolean, openingId: number) => {
  try {
    if(!newStatus){      
      await deleteOpeningFavorite(openingId);
    }
  } catch (error) {
    console.error(`Failed to update favorite status for ${openingId}`);
  }
};

const OpeningHistory = ({ histories }: OpeningHistoryProps) => (
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

export default OpeningHistory;
