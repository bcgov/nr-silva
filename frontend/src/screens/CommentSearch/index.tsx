import { useEffect } from "react";
import CommentSearchSection from "@/components/CommentSearchSection";
import { Column, Grid } from "@carbon/react";
import PageTitle from "@/components/PageTitle";


const CommentSearch = () => {
  useEffect(() => {
    document.title = `Comments Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Comments" />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        <CommentSearchSection />
      </Column>
    </Grid>
  );
}

export default CommentSearch;
