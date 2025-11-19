import React, { useEffect, useState } from "react";

import { Button, Column, Grid, TextInput } from "@carbon/react";
import PageTitle from "@/components/PageTitle";
import RecentOpenings from "@/components/RecentOpenings";
import FavouriteCard from "@/components/FavouriteCard";
import { FavouriteCardsConfig } from "./constants";
import { useNavigate } from "react-router-dom";

import './styles.scss';

const Openings = () => {
  const navigate = useNavigate();
  const [openingId, setOpeningId] = useState<string>("");

  useEffect(() => {
    document.title = `Openings - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const cards = FavouriteCardsConfig.filter((card) => !card.hidden);

  const handleNavById = () => {
    navigate(`/openings/${openingId}`)
  }

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings" />
      </Column>

      {
        cards.filter((card) => !card.hidden).length > 0
          ? (
            <Column sm={4} md={8} lg={16}>
              {/* Fav cards sub-grid */}
              <Grid className="fav-cards-subgrid">
                {
                  cards.map((card) => (
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
          )
          : null
      }

      <Column sm={4} md={8} lg={16}>
        <div className="opening-id-nav-container">
          <TextInput
            id="opening-id-input"
            name="opening-id"
            labelText=""
            placeholder="View Opening by ID"
            value={openingId}
            onChange={(e) => setOpeningId(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleNavById();
              }
            }}
          />
          <Button onClick={handleNavById} size="md">Go</Button>
        </div>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <RecentOpenings defaultMapOpen />
      </Column>
    </Grid>
  )
}

export default Openings;
