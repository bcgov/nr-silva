import React from "react";
import { useSelector } from "react-redux";
import StandardCard from "../../components/StandardCard";
import FavouriteCard from "../../components/FavouriteCard";
import PageTitle from "../../components/PageTitle";
import RecentOpeningsTable from "../../components/RecentOpeningsTable";
import { recentOpeningItems, recentOpeningItems1, recentOpeningsHeader } from "../../mock-data/constants";


const Opening: React.FC = () => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const goToActivity = () => {
    console.log("clicked a row")
  }
  const { user } = userDetails
    return (
      <>
      <div className="container-fluid">
        <div className="row p-4">
          <PageTitle title="Openings" subtitle="Create, manage or check opening information " />
        </div>
      </div>

      <div className="bg-light p-4">
        <div className="row">
          <div className="col-md-6 col-lg-4 col-xl-3">
            <FavouriteCard index={1} title={'Search opening'} link={'/'} icon={'Dashboard'} description={'Search for a opening to update or consult using advance filters such as activities, standards, codes or links'}/>
          </div>
          <div className="col-md-6 col-lg-4 col-xl-3">
            <FavouriteCard index={1} title={'Create a new opening'} link={'/'} icon={'MapBoundary'} description={'Register a opening for tracking silviculture treatments'}/>
          </div>
          <div className="col-md-6 col-lg-4 col-xl-3">
            <FavouriteCard index={1} title={'Reports'} link={'/'} icon={'ReportData'} description={'Consult and manage opening reports'}/>
          </div>
          <div className="col-md-6 col-lg-4 col-xl-3">
            <FavouriteCard index={1} title={'Upcoming activities'} link={'/'} icon={'Activity'} description={'Track opening activities and milestones'}/>
          </div>
        </div>
      </div>

      <div className="container-fluid">
        <div className="row p-4">
            <PageTitle title="Recent Openings" subtitle="Track your recent openings and select to check spatial activity" />
        </div>
      </div>

      <div className="container-fluid">
        <RecentOpeningsTable
          headers={recentOpeningsHeader}
          elements={recentOpeningItems1}
          clickFn={goToActivity}
        />
      </div>
      </>
    );
  };

export default Opening;