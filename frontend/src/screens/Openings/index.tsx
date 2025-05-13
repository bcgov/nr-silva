import React, { useEffect } from "react";

import './styles.scss';
import { Column, Grid } from "@carbon/react";
import PageTitle from "@/components/PageTitle";
import RecentOpenings from "../../components/RecentOpenings";

const Openings = () => {
  useEffect(() => {
    document.title = `Openings - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title="Openings"
          experimental
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <RecentOpenings defaultMapOpen />
      </Column>
    </Grid>
  )
}

export default Openings;
