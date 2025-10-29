import React from "react";
import { Favorite, FavoriteFilled } from "@carbon/icons-react";
import { Button, InlineLoading } from "@carbon/react";
import { useNavigate } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";
import API from "@/services/API";

import { useNotification } from "@/contexts/NotificationProvider";
import { EIGHT_SECONDS } from "@/constants/TimeUnits";
import { DashboardRoute } from "@/routes/config";
// import { deleteOpeningFavorite, isOpeningFavourite, putOpeningFavourite } from "@/services/OpeningFavouriteService";

import './styles.scss'

type ActionableFavouriteButtonProps = {
  openingId?: number;
}

const BlueFavoriteFilledIcon = () => <FavoriteFilled className="blue-favorite-icon" />;

/**
 * A standalone favourite button that handles API actions itself.
 */
const ActionableFavouriteButton = ({ openingId }: ActionableFavouriteButtonProps) => {
  const { displayNotification } = useNotification();
  const navigate = useNavigate();

  const openingFavouriteQuery = useQuery({
    queryKey: ["openings", "favourites", openingId],
    queryFn: () => API.OpeningEndpointService.checkFavorite(openingId!),
    refetchOnMount: 'always',
    enabled: !!openingId,
  })

  const displayFavSuccessToast = (isFavourite: boolean) => {
    if (!openingId) return;
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
  };

  const displayFavErrorToast = () => {
    if (!openingId) return;
    displayNotification({
      title: 'Error',
      subTitle: `Failed to update favorite status for ${openingId}`,
      type: 'error',
      dismissIn: EIGHT_SECONDS,
      onClose: () => { },
    })
  };

  const deleteFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.removeFromFavorites(openingId!),
    onSuccess: (_, openingId) => {
      displayFavSuccessToast(false);
      // Invalidate favourite data for this component
      openingFavouriteQuery.refetch();
    },
    onError: () => displayFavErrorToast()
  });

  const putFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.addToFavorites(openingId!),
    onSuccess: () => {
      displayFavSuccessToast(true);
      // Invalidate favourite data for this component
      openingFavouriteQuery.refetch();
    },
    onError: () => displayFavErrorToast()
  });

  const handleFavouriteChange = () => {
    if (openingFavouriteQuery.data === undefined) {
      return;
    }

    if (openingFavouriteQuery.data) {
      deleteFavOpenMutation.mutate();
    } else {
      putFavOpenMutation.mutate();
    }
  };

  if (!openingId) {
    return null;
  }

  if (openingFavouriteQuery.isFetching || deleteFavOpenMutation.isPending || putFavOpenMutation.isPending) {
    return (
      <InlineLoading className="favourite-button-inline-loading" />
    )
  }

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
