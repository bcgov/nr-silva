
import { Button, Column, Grid } from "@carbon/react";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import PageTitle from "@/components/PageTitle";
import StockingStandardsSearchSection from "@/components/StockingStandardsSearchSection";
import { ArrowRight } from "@carbon/icons-react";
import { STOCKING_STANDARDS_COMMENT_SEARCH_PATH } from "@/routes/paths";

import "./styles.scss";

const StockingStandardsSearch = () => {
  const navigate = useNavigate();

  useEffect(() => {
    document.title = `Stocking Standards Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid stocking-standards-search-grid">
      <Column sm={4} md={4} lg={12}>
        <PageTitle title="Stocking standards" />
      </Column>

      <Column sm={4} md={4} lg={4}>
        <div className="search-btn-container">
          <Button
            type="button"
            kind="primary"
            renderIcon={ArrowRight}
            onClick={() => navigate(STOCKING_STANDARDS_COMMENT_SEARCH_PATH)}
          >
            Keywords search
          </Button>
        </div>
      </Column>

      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <StockingStandardsSearchSection />
      </Column>
    </Grid>
  )
};

export default StockingStandardsSearch;
