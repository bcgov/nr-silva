import React from 'react';

import {
  ProgressIndicator,
  ProgressStep
} from '@carbon/react';

import History from '../../types/History';

import { formatDate } from '../../utils/DateUtils';
import statusClass from '../../utils/HistoryStatus';

import './styles.scss';

interface OpeningHistoryProps {
  histories: History[];
}

const OpeningHistory = ({ histories }: OpeningHistoryProps) => (
  <>
    <div className="row">
      {histories.map((history, index) => (
        <div key={index} className="col-md-4">
          <div>
            <p className="activity-history-header">{`Opening Id ${history.id}`}</p>
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
  </>
);

export default OpeningHistory;
