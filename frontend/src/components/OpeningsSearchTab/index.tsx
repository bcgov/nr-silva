import React from "react";
import './styles.scss'
import EmptySection from "../EmptySection";
import OpeningsSearchBar from "../OpeningsSearchBar";


const OpeningsSearchTab: React.FC = () => {

  return (
    <>
      <div className="container-fluid p-0 pb-5 align-content-center">
        <OpeningsSearchBar />
        <EmptySection
          pictogram="Summit"
          title={'Nothing to show yet'}
          description={
            'At least one criteria must be entered to start the search: opening ID, opening number, timber mark, file ID or apply advanced search criteria. The matching results will be shown here.'
          }
          fill="#0073E6"
        />
      </div>
    </>
  );
};

export default OpeningsSearchTab;