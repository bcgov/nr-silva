import React, { useState, useEffect } from 'react';
import { TabList, Tabs, Tab, TabPanels, TabPanel } from "@carbon/react";
import ActionsTable from "../ActionsTable";
import { fetchRecentActions } from '../../services/OpeningService';
import { rows as fileRows, headers as fileHeaders } from "./filesData";
import { RecentAction } from '../../types/RecentAction';
import { ITableHeader } from '../../types/TableHeader';
import './styles.scss'
import EmptySection from '../EmptySection';

const headers: ITableHeader[] = [
  {
    key: 'activityType',
    header: 'Activity Type',
    selected: true
  },
  {
    key: 'openingId',
    header: 'Opening ID',
    selected: true
  },
  {
    key: 'statusCode',
    header: 'Status',
    selected: true
  },
  {
    key: 'lastUpdatedLabel',
    header: 'Last Updated',
    selected: true
  }
];

const MyRecentActions: React.FC = () => {
  const [recentActions, setRecentActions] = useState<RecentAction[]>([]);

  const fetchData = async () => {
    const actions: RecentAction[] = await fetchRecentActions();
    setRecentActions(actions);
  };

  useEffect(() => { fetchData(); }, []);

  return (
    <div className="recent-actions">
      <Tabs>
        <TabList activation="manual" aria-label="List of tabs">
          <Tab>
            <div
              className="tab-header-recent"
              data-testid={"my-recent-actions__recent-tab-header"}
            >
              Actions
            </div>
          </Tab>
          <Tab disabled>
            <div
              className="tab-header-recent"
              data-testid={"my-recent-actions__files-tab-header"}
            >
              Files and Docs
            </div>
          </Tab>
        </TabList>
        <TabPanels>
          <TabPanel className="tab-content">            
            <ActionsTable 
            rows={recentActions} 
            headers={headers}
            emptySection={
              <EmptySection 
              pictogram="Time"
              fill="#0073E6"
              title={"There is no actions to show yet!"}
              description={"Your recent actions and files will appear here once you generate them"}              
              />}
            />
          </TabPanel>
          <TabPanel className="tab-content">            
            <ActionsTable 
            rows={fileRows} 
            headers={fileHeaders}
            emptySection={
              <EmptySection 
              pictogram="Time"
              fill="#0073E6"
              title={"There is no files to show yet!"}
              description={"Your recent actions and files will appear here once you generate them"}              
              />}
            />
          </TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default MyRecentActions;
