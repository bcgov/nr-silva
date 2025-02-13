// ActionButtons.tsx

import React from "react";
import { Button } from "@carbon/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import * as Icons from "@carbon/icons-react";
import { useNavigate } from "react-router-dom";
import FavoriteButton from "../FavoriteButton";
import { useNotification } from "../../contexts/NotificationProvider";
import { putOpeningFavourite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";
import { EIGHT_SECONDS } from "../../config/TimeUnits";

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

  const displayFavSuccessToast = (openingId: number, isFavourite: boolean) => (
    displayNotification({
      title: `Opening Id ${openingId} ${!isFavourite ? 'un' : ''}favourited`,
      subTitle: isFavourite ? "You can follow this opening ID on your dashboard" : undefined,
      type: 'success',
      dismissIn: EIGHT_SECONDS,
      buttonLabel: isFavourite ? "Go to track openings" : undefined,
      onClose: () => {
        navigate("/opening?scrollTo=trackOpenings");
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
    mutationFn: (openingId: number) => putOpeningFavourite(openingId),
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

  const handleFavoriteChange = (isFavourite: boolean, openingId: number) => {
    if (isFavourite) {
      putFavOpenMutation.mutate(openingId);
    } else {
      deleteFavOpenMutation.mutate(openingId);
    }
  }


  return (
    <>
      <FavoriteButton
        id={rowId}
        tooltipPosition="right"
        kind="ghost"
        size="sm"
        favorited={favorited}
        onFavoriteChange={(isFavourite: boolean) => handleFavoriteChange(isFavourite, Number(rowId))}
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
