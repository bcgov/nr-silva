import React, { useState, useEffect } from 'react';
import { TabList, Tabs, Tab, TabPanels, TabPanel } from "@carbon/react";
import ActionsTable from "../ActionsTable";
import { fetchRecentActions } from '../../services/OpeningService';
import { rows as fileRows, headers as fileHeaders } from "./filesData";
import { RecentAction } from '../../types/RecentAction';
import { ITableHeader } from '../../types/TableHeader';
import './styles.scss'

const MyRecentActions: React.FC = () => {
  const [recentActions, setRecentActions] = useState<RecentAction[]>([]);

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

  useEffect(() => {
    function fetchData() {
      try {
        const actions: RecentAction[] = fetchRecentActions();
        setRecentActions(actions);
      } catch (error) {
        console.error('Error fetching recent actions:', error);
      }
    }
    fetchData();
  }, []);

  return (
    <div className="recent-actions">
      <Tabs>
        <TabList activation="manual" aria-label="List of tabs">
          <Tab>
            <div
              className="tab-header-recent"
              data-testid={"my-recent-actions__recent-tab-header"}
            >
              Recent
            </div>
          </Tab>
          <Tab>
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
            <ActionsTable rows={recentActions} headers={headers}/>
          </TabPanel>
          <TabPanel className="tab-content">
            {/* fileRows and fileHeaders are still static */}
            <ActionsTable rows={fileRows} headers={fileHeaders} /> {/* Empty rows for the "Files and Docs" tab */}
          </TabPanel>
        </TabPanels>
      </Tabs>
    </div>
  );
};

export default MyRecentActions;
