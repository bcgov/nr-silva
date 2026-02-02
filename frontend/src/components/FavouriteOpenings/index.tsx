import React from "react";
import { Column, Loading, Grid } from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";
import FavOpeningItem from "./FavOpeningItem";

import './styles.scss';

/**
 * Terminology: UI uses "bookmark", but this component retains the
 * backend term "favourite" for compatibility.
 */
const FavouriteOpenings: React.FC = () => {
  const favouriteOpeningsQuery = useQuery({
    queryKey: ['openings', 'favourites'],
    queryFn: () => API.OpeningEndpointService.getFavorites(),
    refetchOnMount: 'always'
  });

  return (
    <ChartContainer
      className="favourite-openings-container"
      title="Track Openings"
      description="Follow your bookmarked openings"
    >
      <Column className="favourite-content-col" sm={4} md={8} lg={16}>
        {
          favouriteOpeningsQuery.isLoading || isAuthRefreshInProgress()
            ? (
              <div className="loading-container">
                <Loading withOverlay={false} />
              </div>
            )
            : null
        }
        {
          !favouriteOpeningsQuery.isLoading && !isAuthRefreshInProgress() && !favouriteOpeningsQuery.data?.length
            ? (
              <EmptySection
                pictogram="UserInsights"
                title="You don't have any bookmarks to show yet!"
                description="Click the bookmark icon on an opening's details page or in opening tables to save it."
              />
            )
            : null
        }
        {
          !favouriteOpeningsQuery.isLoading && favouriteOpeningsQuery.data?.length && !isAuthRefreshInProgress()
            ? (
              <Grid className="twelve-col-grid">
                {
                  favouriteOpeningsQuery.data.map((openingId) => (
                    <FavOpeningItem key={openingId} openingId={openingId} />
                  ))
                }
              </Grid>
            )
            : null
        }
      </Column>
    </ChartContainer>
  );
};

export default FavouriteOpenings;
