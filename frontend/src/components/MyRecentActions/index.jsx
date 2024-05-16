import React, { useState, useEffect } from 'react';
import { TabList, Tabs, Tab, TabPanels, TabPanel } from "@carbon/react";
import ActionsTable from "../ActionsTable";
import { fetchRecentActions } from '../../services/OpeningService';
import { rows as fileRows, headers as fileHeaders } from "./filesData";

const MyRecentActions = () => {
  const [recentActions, setRecentActions] = useState([]);

  const headers = [
    {
      key: 'activityType',
      header: 'Activity Type',
    },
    {
      key: 'openingId',
      header: 'Opening ID',
    },
    {
      key: 'status',
      header: 'Status',
    },
    {
      key: 'lastUpdated',
      header: 'Last Updated',
    }
  ];

  useEffect(() => {
    async function fetchData() {
      try {
        const actions = await fetchRecentActions();
        setRecentActions(actions);
      } catch (error) {
        console.error('Error fetching recent actions:', error);
      }
    }
    fetchData();
  }, []);

  return (
    <Tabs>
      <TabList activation="manual" aria-label="List of tabs">
        <Tab><div className="tab-header">Recent</div></Tab>
        <Tab><div className="tab-header">Files and Docs</div></Tab>
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
  );
};

export default MyRecentActions;
