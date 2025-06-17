// ActionButtons.tsx

import React from "react";
import { Button } from "@carbon/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import * as Icons from "@carbon/icons-react";
import { useLocation, useNavigate } from "react-router-dom";
import FavoriteButton from "../FavoriteButton";
import API from "@/services/API";
import { useNotification } from "@/contexts/NotificationProvider";
import { EIGHT_SECONDS } from "@/constants/TimeUnits";
import { DashboardRoute } from "@/routes/config";

interface ActionButtonsProps {
  favorited: boolean;
  rowId: string;
  showDownload?: boolean;
  showToast?: boolean;
}

const ActionButtons: React.FC<ActionButtonsProps> = ({
  favorited,
  rowId,
  showDownload,
  showToast
}) => {
  const { displayNotification } = useNotification();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const location = useLocation();
  const hideDetail = location.pathname === DashboardRoute.path;

  const displayFavSuccessToast = (openingId: number, isFavourite: boolean) => {
    displayNotification({
      title: `Opening Id ${openingId} ${!isFavourite ? 'un' : ''}favourited`,
      subTitle: !hideDetail && isFavourite ? "You can follow this opening ID on your dashboard" : undefined,
      type: 'success',
      dismissIn: EIGHT_SECONDS,
      buttonLabel: !hideDetail && isFavourite ? "Go to track openings" : undefined,
      onClose: () => {
        if (!hideDetail) {
          navigate(DashboardRoute.path!);
        }
      }
    });
  };

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
    mutationFn: (openingId: number) => API.OpeningFavoriteEndpointService.removeFromFavorites(openingId),
    onSuccess: (_, openingId) => {
      if (showToast) {
        displayFavSuccessToast(openingId, false);
      }
      // Invalidate tracked favourite opening data
      queryClient.invalidateQueries({
        queryKey: ['openings', 'favourites']
      });
      // Invalidate data on recent openings data
      queryClient.invalidateQueries({
        queryKey: ["opening", "recent"]
      });
    },
    onError: (_, openingId) => displayFavErrorToast(openingId)
  });

  const putFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => API.OpeningFavoriteEndpointService.addToFavorites(openingId),
    onSuccess: (_, openingId) => {
      if (showToast) {
        displayFavSuccessToast(openingId, true);
      }
      // Invalidate tracked favourite opening data
      queryClient.invalidateQueries({
        queryKey: ['openings', 'favourites']
      });
      // Invalidate data on recent openings data
      queryClient.invalidateQueries({
        queryKey: ["opening", "recent"]
      });
    },
    onError: (_, openingId) => displayFavErrorToast(openingId)
  });

  const addFavorite = (openingId: number) => {
    putFavOpenMutation.mutate(openingId);
  };

  const removeFavorite = (openingId: number) => {
    deleteFavOpenMutation.mutate(openingId);
  };


  return (
    <>
      <FavoriteButton
        id={rowId}
        tooltipPosition="right"
        kind="ghost"
        size="sm"
        favorited={favorited}
        onFavoriteChange={(isFavourite: boolean) =>
          isFavourite ? addFavorite(Number(rowId)) : removeFavorite(Number(rowId))
        }
        disabled={deleteFavOpenMutation.isPending || putFavOpenMutation.isPending}
      />
      {
        showDownload ? (
          <Button
            className="align-self-stretch"
            hasIconOnly
            iconDescription="Document Download"
            tooltipPosition="right"
            kind="ghost"
            renderIcon={Icons.Download}
            onClick={() => null}
            size="md"
            disabled
          />
        )
          : null
      }
    </>
  )
};

export default ActionButtons;
