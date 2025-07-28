import React, { useEffect } from "react";

import './styles.scss';
import { Column, Grid } from "@carbon/react";
import PageTitle from "@/components/PageTitle";
import RecentOpenings from "@/components/RecentOpenings";
import FavouriteCard from "@/components/FavouriteCard";
import { FavouriteCardsConfig } from "./constants";

const Openings = () => {
  useEffect(() => {
    document.title = `Openings - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings" />
      </Column>

      <Column sm={4} md={8} lg={16}>
        {/* Fav cards sub-grid */}
        <Grid className="fav-cards-subgrid">
          {
            FavouriteCardsConfig
              .filter((card) => !card.hidden)
              .map((card) => (
                <Column className="fav-card-column" key={card.index} sm={4} md={4} lg={4}>
                  <FavouriteCard
                    index={card.index}
                    title={card.title}
                    link={card.link}
                    icon={card.icon}
                    opensModal={card.opensModal}
                  />
                </Column>
              ))
          }
        </Grid>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <RecentOpenings defaultMapOpen />
      </Column>
    </Grid>
  )
}

export default Openings;
