import { useEffect } from "react";
import { Column, Grid } from '@carbon/react'
import { useParams } from "react-router-dom";
import { getAddActivityCrumbs } from "./constants";
import PageTitle from "@/components/PageTitle";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { PLACE_HOLDER } from "@/constants";


const CreateActivity = () => {
  const param = useParams();
  const openingId = param.openingId;

  useEffect(() => {
    document.title = `Add Activity - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const crumbs = openingId ? getAddActivityCrumbs(openingId) : [];

  const openingDetailsTombstoneQuery = useQuery({
    queryKey: ["openings", openingId, "tombstone"],
    queryFn: () => API.OpeningEndpointService.getOpeningTombstone(Number(openingId)),
    enabled: !!openingId,
    refetchOnMount: "always",
  });


  return (
    <Grid className="default-grid default-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title={`Add Activity - Opening ${openingId}`}
          subtitle={
            openingDetailsTombstoneQuery.data ?
              `File ID ${openingDetailsTombstoneQuery.data.tombstone.fileId ?? PLACE_HOLDER}, Cut block ${openingDetailsTombstoneQuery.data.tombstone.cutBlockID ?? PLACE_HOLDER}`
              : undefined
          }
          breadCrumbs={crumbs}
        />
      </Column>
      <Column className="full-width-col" sm={4} md={8} lg={16}>
        haha
      </Column>
    </Grid>
  );
};

export default CreateActivity;
