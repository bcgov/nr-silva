import React, { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import {
  Grid,
  Column
} from "@carbon/react";

import OpeningSearch from "@/components/SilvicultureSearch/OpeningSearch";

const OpeningsSearch: React.FC = () => {
  useEffect(() => {
    document.title = `Openings Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="silviculture-search-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings Search" />
      </Column>

      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <OpeningSearch />
      </Column>
    </Grid >
  );
};

export default OpeningsSearch;
