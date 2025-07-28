import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import FavouriteCard from "@/components/FavouriteCard";
import PageTitle from "@/components/PageTitle";
import {
  Grid,
  Column,
} from "@carbon/react";
import RecentOpenings from "@/components/RecentOpenings";
import OpeningSubmissionTrend from "@/components/OpeningSubmissionTrend";
import FavouriteOpenings from "@/components/FavouriteOpenings";
import { REDIRECT_KEY } from "@/constants";
import { FavouriteCardsConfig } from "./constants";
import { isValidRedirect } from "./utils";
import './styles.scss'

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [willRedirect, setWillRedirect] = useState<boolean>(true);

  useEffect(() => {
    const path = localStorage.getItem(REDIRECT_KEY);
    localStorage.removeItem(REDIRECT_KEY);

    if (path && isValidRedirect(path)) {
      // Redirect if a valid path was stored
      navigate(path, { replace: true });
    }

    // Mark redirect process complete (whether redirect happened or not)
    setWillRedirect(false);
  }, []);

  if (willRedirect) {
    return null;
  }

  return (
    <Grid className="default-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle
          title="Dashboard"
          subtitle="Manage and track silvicultural information about openings"
        />
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
};

export default Dashboard;
