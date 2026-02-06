
import { useMutation, useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { Button, type ButtonTooltipPosition, InlineLoading } from '@carbon/react';
import { BookmarkAdd, BookmarkFilled } from '@carbon/icons-react';
import { isAuthRefreshInProgress } from '@/constants/tanstackConfig';

import './styles.scss';

type OpeningBookmarkBtnProps = {
  openingId?: number;
  tooltipPosition?: ButtonTooltipPosition;
}

const BookmarkFilledIcon = () => <BookmarkFilled className="bookmark-filled-icon" data-testid="bookmark-filled-icon" />;

const OpeningBookmarkBtn = ({ openingId, tooltipPosition = 'top' }: OpeningBookmarkBtnProps) => {
  const openingFavouriteQuery = useQuery({
    queryKey: ["openings", "favourites"],
    queryFn: () => API.OpeningEndpointService.getFavorites()
  });

  const invalidateFavouriteQuery = () => {
    openingFavouriteQuery.refetch();
  }

  const deleteFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.removeFromFavorites(openingId!),
    onSettled: () => {
      invalidateFavouriteQuery();
    }
  });

  const putFavOpenMutation = useMutation({
    mutationFn: () => API.OpeningEndpointService.addToFavorites(openingId!),
    onSettled: () => {
      invalidateFavouriteQuery();
    }
  });

  const handleFavouriteChange = () => {
    if (openingFavouriteQuery.data === undefined || openingId === undefined) {
      return;
    }

    if (openingFavouriteQuery.data.includes(openingId)) {
      deleteFavOpenMutation.mutate();
    } else {
      putFavOpenMutation.mutate();
    }
  };

  if (!openingId) {
    return null;
  }

  if (openingFavouriteQuery.isLoading || deleteFavOpenMutation.isPending || putFavOpenMutation.isPending || isAuthRefreshInProgress()) {
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
      iconDescription={`${openingFavouriteQuery.data?.includes(openingId) ? 'Unb' : 'B'}ookmark ${openingId}`}
      kind="ghost"
      onClick={handleFavouriteChange}
      renderIcon={openingFavouriteQuery.data?.includes(openingId) ? BookmarkFilledIcon : BookmarkAdd}
      size="md"
      tooltipPosition={tooltipPosition}
    />
  );
}

export default OpeningBookmarkBtn;
