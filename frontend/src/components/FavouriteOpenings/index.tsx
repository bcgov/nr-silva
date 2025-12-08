import React from "react";
import { Link } from "react-router-dom";
import { Column, Loading, Grid } from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import { OpeningDetailsRoute } from "@/routes/config";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";
import OpeningBookmarkBtn from "../OpeningBookmarkBtn";

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
          !favouriteOpeningsQuery.isLoading && favouriteOpeningsQuery.data?.length
            ? (
              <Grid className="twelve-col-grid">
                {
                  favouriteOpeningsQuery.data.map((openingId) => (
                    <div
                      key={openingId}
                      className="fav-open-tile-container"
                      id={`favourite-opening-tile-${openingId}`}
                      data-testid={`favourite-opening-tile-${openingId}`}
                    >
                      <OpeningBookmarkBtn openingId={openingId} />
                      <Link
                        className="fav-open-label"
                        to={OpeningDetailsRoute.path!.replace(":openingId", openingId.toString())}
                      >
                        Opening ID{' '}
                        <span className="fav-open-id">{openingId}</span>
                      </Link>

                    </div>
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
