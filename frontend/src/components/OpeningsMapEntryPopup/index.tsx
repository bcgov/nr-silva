import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import FavoriteButton from '../FavoriteButton';
import { isOpeningFavourite, putOpeningFavourite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";
import { useNotification } from '../../contexts/NotificationProvider';
import { EIGHT_SECONDS } from '../../constants/TimeUnits';

import './styles.scss';

interface OpeningsMapEntryPopupProps {
  openingId: number;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({ openingId }) => {

  const { displayNotification } = useNotification();

  const navigate = useNavigate()

  const displayFavSuccessToast = (openingId: number, isFavourite: boolean) => (
    displayNotification({
      title: `Opening Id ${openingId} ${!isFavourite ? 'un' : ''}favourited`,
      subTitle: isFavourite ? "You can follow this opening ID on your dashboard" : undefined,
      type: 'success',
      dismissIn: EIGHT_SECONDS,
      buttonLabel: isFavourite ? "Go to track openings" : undefined,
      onClose: () => {
        navigate("/dashboard");
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

  const queryClient = useQueryClient();

  const isFavouriteQuery = useQuery({
    queryKey: ['openings', 'favourites', openingId],
    queryFn: () => isOpeningFavourite(openingId),
    refetchOnMount: true
  })

  const deleteFavOpenMutation = useMutation({
    mutationFn: (openingId: number) => deleteOpeningFavorite(openingId),
    onSuccess: (_, openingId) => {
      displayFavSuccessToast(openingId, false);
      // Invalidate tracked favourite opening data
      queryClient.invalidateQueries({
        queryKey: ['openings', 'favourites'],
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
      displayFavSuccessToast(openingId, true);
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

  const handleFavoriteChange = (isFavouriting: boolean) => {
    if (isFavouriting) {
      putFavOpenMutation.mutate(openingId)
    } else {
      deleteFavOpenMutation.mutate(openingId)
    }
  }

  return (
    <div className="map-popup-container">
      <FavoriteButton
        tooltipPosition="bottom"
        kind="ghost"
        size="sm"
        favorited={isFavouriteQuery.data ?? false}
        onFavoriteChange={handleFavoriteChange}
        disabled={deleteFavOpenMutation.isPending || putFavOpenMutation.isPending}
      />
      <span>{`Opening ID: ${openingId}`}</span>
    </div>
  );
};

export default OpeningsMapEntryPopup;
