import React, { useEffect, useState } from "react";
import FavouriteCard from "../../components/FavouriteCard";
import PageTitle from "../../components/PageTitle";
import {
  Grid,
  Column
} from "@carbon/react";
import RecentOpenings from "../../components/RecentOpenings";
import OpeningMetricsTab from "../../components/OpeningMetricsTab";
import OpeningSubmissionTrend from "../../components/OpeningSubmissionTrend";

import { FavouriteCardsConfig } from "./constants";
import './styles.scss'
import FavouriteOpenings from "../../components/FavouriteOpenings";

const Dashboard: React.FC = () => {

  const [showSpatial, setShowSpatial] = useState<boolean>(false);
  const [activeTab, setActiveTab] = useState<number>(0); // Track active tab index

  // const tabChange = (tabSelection: { selectedIndex: number }) => {
  //   setActiveTab(tabSelection.selectedIndex);
  // };

  // useEffect(() => {
  //   const urlParams = new URLSearchParams(window.location.search);

  //   if (urlParams.has('tab') && urlParams.get('tab')?.includes('metrics')) {
  //     setActiveTab(1);
  //   } else {
  //     setActiveTab(0);
  //   }
  // }, []);

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title="Dashboard"
          subtitle="Manage and track silvicultural information about openings"
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        {/* Fav cards sub-grid */}
        <Grid>
          {
            FavouriteCardsConfig.map((card) => (
              <Column key={card.index} sm={4} md={4} lg={4}>
                <FavouriteCard
                  index={card.index}
                  title={card.title}
                  link={card.link}
                  icon={card.icon}
                />
              </Column>
            ))
          }
        </Grid>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <RecentOpenings />
      </Column>

      <Column sm={4} md={8} lg={16} max={8}>
        <OpeningSubmissionTrend />
      </Column>

      <Column sm={4} md={8} lg={16} max={8}>
        <FavouriteOpenings />
      </Column>
    </Grid>
  );

  // return (
  //   <>
  //     <div className="container-fluid">
  //       <div className="row px-0 py-4 p-sm-4" data-testid="opening-pagetitle">
  //         <PageTitle
  //           title="Openings"
  //           subtitle="Create, manage or check opening information"
  //         />
  //       </div>
  //     </div>

  //     {!showSpatial && (
  //       <div className="favourite-activities">
  //         <div className="row gy-0 ">
  //           <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
  //             <FavouriteCard
  //               index={1}
  //               title={'Silviculture search'}
  //               link="/silviculture-search"
  //               icon={'SearchLocate'}
  //               description={'Search for opening types, activities or standards'}
  //             />
  //           </div>
  //           <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
  //             <FavouriteCard
  //               index={2}
  //               title={'Create an opening'}
  //               link="#"
  //               icon={'MapBoundary'}
  //               description={'Create different opening types to track silviculture activities, treatments and generate their identifier (ID)'}
  //               disabled
  //             />
  //           </div>
  //           <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
  //             <FavouriteCard
  //               index={3}
  //               title={'Reports'}
  //               link="#"
  //               icon={'ReportData'}
  //               description={'Consult and manage opening reports'}
  //               disabled
  //             />
  //           </div>
  //           <div className="col-sm-6 col-md-6 col-lg-6 col-xl-6 col-xxl-3">
  //             <FavouriteCard
  //               index={4}
  //               title={'Upcoming activities'}
  //               link="#"
  //               icon={'Activity'}
  //               description={'Track opening activities and milestones'}
  //               disabled
  //             />
  //           </div>
  //         </div>
  //       </div>
  //     )}

  //     <Tabs onChange={tabChange} selectedIndex={activeTab}>
  //       <TabList className="tab-list" aria-label="List of Tab" contained>
  //         <Tab><div className="tab-header">Recent Openings</div></Tab>
  //         <Tab><div className="tab-header">Dashboard</div></Tab>
  //       </TabList>
  //       <TabPanels>
  //         <TabPanel className="tab-content tab-openings">
  //           {activeTab === 0 &&
  //             <OpeningsTab
  //               showSpatial={showSpatial}
  //               setShowSpatial={setShowSpatial}
  //             />
  //           }
  //         </TabPanel>
  //         <TabPanel className="tab-content tab-metrics">
  //           {activeTab === 1 && <OpeningMetricsTab />}
  //         </TabPanel>
  //       </TabPanels>
  //     </Tabs>
  //   </>
  // );
};

export default Dashboard;
