import React from "react";
import { Link } from "react-router-dom";
import { Column, Loading, Grid } from "@carbon/react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNotification } from '@/contexts/NotificationProvider';
import { EIGHT_SECONDS } from "@/constants/TimeUnits";
import { OpeningDetailsRoute } from "@/routes/config";
import API from "@/services/API";

import FavoriteButton from '../FavoriteButton';
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";

import './styles.scss';

const FavouriteOpenings: React.FC = () => {
  const { displayNotification } = useNotification();
  const queryClient = useQueryClient();

  const favouriteOpeningsQuery = useQuery({
    queryKey: ['openings', 'favourites'],
    queryFn: () => API.OpeningFavoriteEndpointService.getFavorites(),
    refetchOnMount: 'always'
  });

  const deleteFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => API.OpeningFavoriteEndpointService.removeFromFavorites(openingId),
    onSuccess: (_, openingId) => {
      displayNotification({
        title: `Opening Id ${openingId} unfavourited`,
        type: 'success',
        dismissIn: EIGHT_SECONDS,
        onClose: () => { }
      });
      // Refetch data
      favouriteOpeningsQuery.refetch();
      // Update data on recent openings
      queryClient.invalidateQueries({
        queryKey: ["opening", "recent"]
      });
    },
    onError: (_, openingId) => {
      displayNotification({
        title: 'Error',
        subTitle: `Failed to update favorite status for ${openingId}`,
        type: 'error',
        dismissIn: EIGHT_SECONDS,
        onClose: () => { }
      });
    }
  });

  const deleteFavourite = (openingId: number) => (
    deleteFavOpenMutation.mutate(openingId)
  )

  return (
    <ChartContainer
      className="favourite-openings-container"
      title="Track Openings"
      description="Follow your favourite openings"
    >
      <Column className="favourite-content-col" sm={4} md={8} lg={16}>
        {
          favouriteOpeningsQuery.isLoading
            ? (
              <div className="trend-loading-container">
                <Loading withOverlay={false} />
              </div>
            )
            : null
        }
        {
          !favouriteOpeningsQuery.isLoading && !favouriteOpeningsQuery.data?.length
            ? (
              <EmptySection
                pictogram="UserInsights"
                title="You don't have any favourites to show yet!"
                description="You can favourite your openings by clicking on the heart icon inside opening details page"
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
                    >
                      <FavoriteButton
                        tooltipPosition="bottom"
                        kind="ghost"
                        size="sm"
                        favorited={true}
                        onFavoriteChange={() => deleteFavourite(openingId)}
                        disabled={deleteFavOpenMutation.isPending}
                      />
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
