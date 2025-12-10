
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import API from '@/services/API';
import { Button, type ButtonTooltipPosition, InlineLoading } from '@carbon/react';
import { BookmarkAdd, BookmarkFilled } from '@carbon/icons-react';

import './styles.scss';

type OpeningBookmarkBtnProps = {
  openingId?: number;
  tooltipPosition?: ButtonTooltipPosition;
}

const BookmarkFilledIcon = () => <BookmarkFilled className="bookmark-filled-icon" data-testid="bookmark-filled-icon" />;

const OpeningBookmarkBtn = ({ openingId, tooltipPosition = 'top' }: OpeningBookmarkBtnProps) => {
  const qc = useQueryClient();

  const openingFavouriteQuery = useQuery({
    queryKey: ["openings", "favourites", openingId],
    queryFn: () => API.OpeningEndpointService.checkFavorite(openingId!),
    refetchOnMount: 'always',
    enabled: !!openingId,
  });

  const invalidateBookmarkQuery = () => {
    qc.invalidateQueries({
      queryKey: ['openings', 'favourites']
    });
    qc.invalidateQueries({
      queryKey: ["opening", "recent"]
    });
  }

  const deleteFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.removeFromFavorites(openingId!),
    onSettled: () => {
      openingFavouriteQuery.refetch();
      invalidateBookmarkQuery();
    }
  });

  const putFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.addToFavorites(openingId!),
    onSettled: () => {
      openingFavouriteQuery.refetch();
      invalidateBookmarkQuery();
    }
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
      <div className="opening-bookmark-btn-loading-container">
        <InlineLoading className="bookmark-button-inline-loading" />
      </div>
    );
  }

  return (
    <Button
      data-testid={`actionable-bookmark-button-${openingId}`}
      id={`actionable-bookmark-button-${openingId}`}
      className="actionable-bookmark-button"
      hasIconOnly
      iconDescription={`${openingFavouriteQuery.data === true ? 'Unb' : 'B'}ookmark ${openingId}`}
      kind="ghost"
      onClick={handleFavouriteChange}
      renderIcon={openingFavouriteQuery.data === true ? BookmarkFilledIcon : BookmarkAdd}
      size="md"
      tooltipPosition={tooltipPosition}
    />
  );
}

export default OpeningBookmarkBtn;
