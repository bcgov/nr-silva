import React from "react";
import { Favorite, FavoriteFilled } from "@carbon/icons-react";
import { Button, ButtonSkeleton, InlineLoading } from "@carbon/react";
import { useNavigate } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { useNotification } from "@/contexts/NotificationProvider";
import { EIGHT_SECONDS } from "@/constants/TimeUnits";
import { DashboardRoute } from "@/routes/config";
import { deleteOpeningFavorite, isOpeningFavourite, putOpeningFavourite } from "@/services/OpeningFavouriteService";

import './styles.scss'

type ActionableFavouriteButtonProps = {
  openingId?: number;
}

/**
 * A standalone favourite button that handles API actions itself.
 */
const ActionableFavouriteButton = ({ openingId }: ActionableFavouriteButtonProps) => {
  if (!openingId) {
    return null;
  }

  const { displayNotification } = useNotification();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const openingFavouriteQuery = useQuery({
    queryKey: ["openings", "favourites", openingId],
    queryFn: () => isOpeningFavourite(openingId),
    refetchOnMount: 'always'
  })

  const displayFavSuccessToast = (openingId: number, isFavourite: boolean) => (
    displayNotification({
      title: `Opening Id ${openingId} ${!isFavourite ? 'un' : ''}favourited`,
      subTitle: isFavourite ? "You can follow this opening ID on your dashboard" : undefined,
      type: 'success',
      dismissIn: EIGHT_SECONDS,
      buttonLabel: isFavourite ? "Go to track openings" : undefined,
      onClose: () => {
        navigate(DashboardRoute.path!);
      }
    })
  );

  const displayFavErrorToast = (openingId: number) => (
    displayNotification({
      title: 'Error',
      subTitle: `Failed to update favorite status for ${openingId}`,
      type: 'error',
      dismissIn: EIGHT_SECONDS,
      onClose: () => { }
    })
  );

  const deleteFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => deleteOpeningFavorite(openingId),
    onSuccess: (_, openingId) => {
      displayFavSuccessToast(openingId, false);
      // Invalidate favourite data for this component
      queryClient.invalidateQueries({
        queryKey: ["openings", "favourites", openingId]
      });
    },
    onError: (_, openingId) => displayFavErrorToast(openingId)
  });

  const putFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => putOpeningFavourite(openingId),
    onSuccess: (_, openingId) => {
      displayFavSuccessToast(openingId, true);
      // Invalidate favourite data for this component
      queryClient.invalidateQueries({
        queryKey: ["openings", "favourites", openingId]
      });
    },
    onError: (_, openingId) => displayFavErrorToast(openingId)
  });

  const handleFavouriteChange = () => {
    if (openingFavouriteQuery.data === undefined) {
      return;
    }

    if (openingFavouriteQuery.data) {
      deleteFavOpenMutation.mutate(openingId);
    } else {
      putFavOpenMutation.mutate(openingId);
    }
  }

  if (openingFavouriteQuery.isFetching || deleteFavOpenMutation.isPending || putFavOpenMutation.isPending) {
    return (
      <InlineLoading className="favourite-button-inline-loading" />
    )
  }

  const BlueFavoriteFilledIcon = () => <FavoriteFilled className="blue-favorite-icon" />;

  return (
    <Button
      id={`actionable-favourite-button-${openingId}`}
      className="actionable-favourite-button"
      hasIconOnly
      iconDescription={`${openingFavouriteQuery.data === true ? 'Unf' : 'F'}avourite ${openingId}`}
      kind="ghost"
      onClick={handleFavouriteChange}
      renderIcon={openingFavouriteQuery.data === true ? BlueFavoriteFilledIcon : Favorite}
      size="md"
    />
  )

}

export default ActionableFavouriteButton;
