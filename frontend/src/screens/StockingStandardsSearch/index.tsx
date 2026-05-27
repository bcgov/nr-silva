
import { Column, Grid } from "@carbon/react";
import { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import StockingStandardsSearchSection from "@/components/StockingStandardsSearchSection";

const StockingStandardsSearch = () => {
  useEffect(() => {
    document.title = `Stocking Standards Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Stocking standards" />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <StockingStandardsSearchSection />
      </Column>
    </Grid>
  )
};

export default StockingStandardsSearch;
