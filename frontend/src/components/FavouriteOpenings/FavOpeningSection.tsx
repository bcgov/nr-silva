import { useEffect, useState } from "react";
import { Button, Column, Grid } from "@carbon/react";
import FavOpeningItem from "./FavOpeningItem";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import useBreakpoint from "@/hooks/UseBreakpoint";
import { BreakpointType } from "@/types/BreakpointType";
import { Bookmark, ChevronUp } from "@carbon/icons-react";

const FavOpeningSection = () => {

  const [showSection, setShowSection] = useState(false);

  const breakPoint = useBreakpoint();

  const favouriteOpeningsQuery = useQuery({
    queryKey: ['openings', 'favourites'],
    queryFn: () => API.OpeningEndpointService.getFavorites(),
    refetchOnMount: 'always'
  });

  useEffect(
    () => {
      const favCount = favouriteOpeningsQuery.data?.length ?? 0;
      const thresholds: { [k in BreakpointType]: number } = {
        sm: 5,
        md: 7,
        lg: 10,
        xlg: 13,
        max: 16,
      };

      const threshold = thresholds[breakPoint] ?? 0;
      setShowSection(favCount < threshold);
    },
    [favouriteOpeningsQuery.data]
  );



  if (showSection) {
    return (
      favouriteOpeningsQuery.data && favouriteOpeningsQuery.data.length > 0
        ? (
          <Column sm={4} md={8} lg={16}>
            <Grid narrow>
              {
                favouriteOpeningsQuery.data
                  .map((openingId) => (
                    <Column key={openingId} sm={4} md={4} lg={5} xlg={4} max={3}>
                      <FavOpeningItem openingId={openingId} />
                    </Column>
                  ))
              }
              <Column sm={4} md={8} lg={16}>
                <span className="hide-bookmarks-btn-container">
                  <Button
                    type="button"
                    kind="ghost"
                    onClick={() => setShowSection(false)}
                    renderIcon={ChevronUp}
                  >
                    Hide bookmarked openings
                  </Button>
                </span>
              </Column>
            </Grid>
          </Column>

        )
        : null
    );
  }

  return (
    <Column sm={4} md={8} lg={16}>
      <Button
        type="button"
        kind="ghost"
        onClick={() => setShowSection(true)}
        renderIcon={Bookmark}
      >
        Show bookmarks
      </Button>
    </Column>
  )

};

export default FavOpeningSection;
