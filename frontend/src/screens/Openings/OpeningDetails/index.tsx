import React, { useEffect } from "react";
import { Column, Grid } from "@carbon/react";
import PageTitle from "@/components/PageTitle";

import './styles.scss';
import { useParams } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";
import { searchOpenings } from "@/services/OpeningSearchService";
import FavoriteButton from "../../../components/FavoriteButton";
import { putUserRecentOpening } from "../../../services/OpeningService";



const OpeningDetails = () => {

  const param = useParams();
  const openingId = param.openingId;

  /**
   * TODO:
   * Temporarily using opening search to get data, will need to update this once API is done
   */
  const openingQuery = useQuery({
    queryKey: ['openings', 'search', { openingId }],
    queryFn: () => searchOpenings({ mainSearchTerm: openingId, page: 0, size: 100 }),
    enabled: !!openingId,
    select: (data) => {
      const { content } = data;
      if (!content.length) {
        return undefined;
      }
      return content.find((opening) => opening.openingId.toString() === openingId)
    }
  })

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => putUserRecentOpening(openingId)
  });


  /**
   * Update most recent openings when this page loads
   */
  useEffect(() => {
    if (openingId && Number.isInteger(Number(openingId))) {
      postRecentOpeningMutation.mutate(Number(openingId));
    }
  }, [openingId]);


  return (
    <Grid className="default-grid opening-detail-grid">
      <PageTitle
        title={`Opening ID ${openingId}`}
        experimental
      >
        <FavoriteButton
          kind="ghost"
          size="md"
          onFavoriteChange={() => { }}
          favorited={openingQuery.data?.favourite ?? false}
        />
      </PageTitle>

      <Column sm={4} md={8} lg={16}>

      </Column>
    </Grid >
  )
}

export default OpeningDetails;
