import React from 'react';
import { TabList } from "@carbon/react";
import { Tabs } from "@carbon/react";
import { Tab } from "@carbon/react";
import { rows, headers } from "./testData";
import { rows as fileRows, headers as fileHeaders } from "./filesData";
import { TabPanels } from "@carbon/react";
import { TabPanel } from "@carbon/react";
import ActionsTable from "../ActionsTable";
const MyRecentActions = () => {
  return (
    <Tabs>
        <TabList activation="manual" aria-label="List of tabs">
          <Tab><div className="tab-header">Recent</div></Tab>
          <Tab><div className="tab-header">Files and Docs</div></Tab>
        </TabList>
        <TabPanels>
          <TabPanel className="tab-content">
            <ActionsTable rows={rows} headers={headers}/>
          </TabPanel>
          <TabPanel className="tab-content">
            <ActionsTable rows={fileRows} headers={fileHeaders} />
          </TabPanel>
        </TabPanels>
      </Tabs>
  );
};

export default MyRecentActions;
