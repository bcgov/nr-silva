import { Column, Grid } from "@carbon/react";
import { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import ForestCoverSearchSection from "@/components/ForestCoverSearchSection";

const ForestCoverSearch = () => {
  useEffect(() => {
    document.title = `Forest Cover Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Forest cover" />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <ForestCoverSearchSection />
      </Column>
    </Grid>
  )
};

export default ForestCoverSearch;
