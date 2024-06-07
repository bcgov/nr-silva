import React from 'react';

import {
  ProgressIndicator,
  ProgressStep,
  Button
} from '@carbon/react';

import History from '../../types/History';
import { formatDate } from '../../utils/DateUtils';
import statusClass from '../../utils/HistoryStatus';

import './styles.scss';
import FavoriteButton from '../FavoriteButton';

interface OpeningHistoryProps {
  histories: History[];
}

const OpeningHistory = ({ histories }: OpeningHistoryProps) => (
  <div className='px-3 pb-3'>
    <div className="row activity-history-container gx-4">
      {histories.map((history, index) => (
        <div key={index} className="col-12 col-sm-4">
          <div className='activity-history-col'>
            <p className="activity-history-header">
              <div className="d-flex flex-row align-items-center">
                <div className="favorite-icon">
                  <FavoriteButton
                    tooltipPosition="bottom"
                    kind="ghost"
                    size="sm"
                    fill="#0073E6"
                  />
                </div>
                {`Opening Id ${history.id}`}
              </div>
            </p>
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
