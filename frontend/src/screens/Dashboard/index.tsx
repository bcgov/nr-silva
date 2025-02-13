import React, { useEffect, useState } from "react";
import FavouriteCard from "../../components/FavouriteCard";
import PageTitle from "../../components/PageTitle";
import {
  Grid,
  Column
} from "@carbon/react";
import RecentOpenings from "../../components/RecentOpenings";
import OpeningSubmissionTrend from "../../components/OpeningSubmissionTrend";

import { FavouriteCardsConfig } from "./constants";
import './styles.scss'
import FavouriteOpenings from "../../components/FavouriteOpenings";

const Dashboard: React.FC = () => (
  <Grid className="default-grid">
    <Column sm={4} md={8} lg={16}>
      <PageTitle
        title="Dashboard"
        subtitle="Manage and track silvicultural information about openings"
      />
    </Column>

    <Column sm={4} md={8} lg={16}>
      {/* Fav cards sub-grid */}
      <Grid>
        {
          FavouriteCardsConfig.map((card) => (
            <Column key={card.index} sm={4} md={4} lg={4}>
              <FavouriteCard
                index={card.index}
                title={card.title}
                link={card.link}
                icon={card.icon}
              />
            </Column>
          ))
        }
      </Grid>
    </Column>

    <Column sm={4} md={8} lg={16}>
      <RecentOpenings />
    </Column>

    <Column sm={4} md={8} lg={16} max={8}>
      <OpeningSubmissionTrend />
    </Column>

    <Column sm={4} md={8} lg={16} max={8}>
      <FavouriteOpenings />
    </Column>
  </Grid>
);

export default Dashboard;
