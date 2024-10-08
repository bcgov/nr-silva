import React from "react";
import PageTitle from "../../components/PageTitle";
import './SilvicultureSearch.scss'
import { TabList } from "@carbon/react";
import { Tabs } from "@carbon/react";
import { Tab } from "@carbon/react";
import { TabPanels } from "@carbon/react";
import { TabPanel } from "@carbon/react";
import * as Icons from '@carbon/icons-react';
import OpeningsSearchTab from "../../components/SilvicultureSearch/Openings/OpeningsSearchTab";
import { OpeningsSearchProvider } from "../../contexts/search/OpeningsSearch";

const SilvicultureSearch: React.FC = () => {

  return (
    <>
      <div className="container-fluid">
        <div className="row px-0 py-4 p-sm-4" data-testid="opening-pagetitle">
          <PageTitle
            title="Silviculture Search"
            subtitle="Search for opening types, activities, stocking standards or standards units"
          />
        </div>
      </div>

      <Tabs>
        <TabList className="search-tablist tab-list" aria-label="List of Tab" contained>
          <Tab renderIcon={Icons.MapBoundaryVegetation}><div className="tab-header">Openings</div></Tab>
          <Tab renderIcon={Icons.Activity}><div className="tab-header">Activities</div></Tab>
          <Tab renderIcon={Icons.Tree}><div className="tab-header">Stocking standards</div></Tab>
          <Tab renderIcon={Icons.VegetationAsset}><div className="tab-header">Standard units</div></Tab>
        </TabList>
        <TabPanels>
          <TabPanel className="tab-content">
            <OpeningsSearchTab />
          </TabPanel>
          <TabPanel className="tab-content">
            <h4>Hi there this is from the second tab</h4>
          </TabPanel>
          <TabPanel className="tab-content">
            <h4>Hi there this is from the THIRD tab</h4>
          </TabPanel>
          <TabPanel className="tab-content">
            <h4>Hi  this is from the FOURTH tab</h4>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </>
  );
};

export default SilvicultureSearch;
