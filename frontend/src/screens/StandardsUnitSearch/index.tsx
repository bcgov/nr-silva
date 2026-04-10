import { Column, Grid } from "@carbon/react";
import { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import StandardsUnitSearchSection from "@/components/StandardsUnitSearchSection";

const StandardsUnitSearch = () => {
  useEffect(() => {
    document.title = `Standards Unit Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Standards units" />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <StandardsUnitSearchSection />
      </Column>
    </Grid>
  )
};

export default StandardsUnitSearch;
