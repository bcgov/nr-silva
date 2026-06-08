import { Column, Grid } from "@carbon/react";
import { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import StockingStandardsCommentSearchSection from "@/components/StockingStandardsCommentSearchSection";
import { SS_COMMENT_SEARCH_BREADCRUMB } from "./constants";

const StockingStandardsCommentSearch = () => {
  useEffect(() => {
    document.title = `Stocking Standards Keywords Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title="Stocking standards keywords search"
          breadCrumbs={SS_COMMENT_SEARCH_BREADCRUMB}
        />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <StockingStandardsCommentSearchSection />
      </Column>
    </Grid>
  );
};

export default StockingStandardsCommentSearch;
