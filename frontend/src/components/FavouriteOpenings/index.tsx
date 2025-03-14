import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { Column, Loading, Grid } from "@carbon/react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNotification } from '@/contexts/NotificationProvider';
import { fetchOpeningFavourites, deleteOpeningFavorite } from "@/services/OpeningFavouriteService";
import { EIGHT_SECONDS } from "@/constants/TimeUnits";
import { OpeningDetailsRoute } from "@/routes/config";

import FavoriteButton from '../FavoriteButton';
import EmptySection from "../EmptySection";
import ChartContainer from "../ChartContainer";

import './styles.scss';

const FavouriteOpenings: React.FC = () => {
  const { displayNotification } = useNotification();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const favouriteOpeningsQuery = useQuery({
    queryKey: ['openings', 'favourites'],
    queryFn: () => fetchOpeningFavourites(),
    refetchOnMount: true
  });

  const deleteFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => deleteOpeningFavorite(openingId),
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
          favouriteOpeningsQuery.isError
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
                fill="#0073E6"
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
                        Opening ID
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
