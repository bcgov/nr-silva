import React, { useEffect, useState } from "react";
import PageTitle from "../../components/PageTitle";
import {
  TabList,
  Tabs,
  Tab,
  TabPanels,
  TabPanel,
  Grid,
  Column
} from "@carbon/react";
import { MapBoundaryVegetation } from '@carbon/icons-react';

import SearchTab from "@/components/SilvicultureSearch/Openings/SearchTab";

import './styles.scss'

const SilvicultureSearch: React.FC = () => {
  const [activeTab, setActiveTab] = useState<number>(0); // Track active tab index

  const onTabChange = (tabSelection: { selectedIndex: number }) => {
    setActiveTab(tabSelection.selectedIndex);
  };

  return (
    <Grid className="silviculture-search-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title="Silviculture Search"
          subtitle="Search for opening types, activities, stocking standards or standards units"
        />
      </Column>

      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <Tabs onChange={onTabChange} selectedIndex={activeTab}>
          <TabList className="search-tablist" aria-label="List of Tab" contained>
            <Tab renderIcon={MapBoundaryVegetation}>Openings</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="tab-content">
              <SearchTab />
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column>
    </Grid>
  );
};

export default SilvicultureSearch;
