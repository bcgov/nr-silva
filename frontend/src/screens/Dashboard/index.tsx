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
import { validPaths } from "@/routes";

import { FavouriteCardsConfig } from "./constants";
import './styles.scss'

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [willRedirect, setWillRedirect] = useState<boolean>(true);

  useEffect(() => {
    const path = localStorage.getItem(REDIRECT_KEY);
    localStorage.removeItem(REDIRECT_KEY);

    /**
     * Checks if a given path is a valid in-app redirect target.
     * Allows exact matches or paths that extend from known base routes.
     */
    const isValidRedirect = (pathToCheck: string | null): pathToCheck is string =>
      !!pathToCheck &&
      validPaths.some((valid) =>
        pathToCheck === valid || pathToCheck.startsWith(valid + '/') || pathToCheck.startsWith(valid + '?')
      );

    if (isValidRedirect(path)) {
      navigate(path, { replace: true });
    } else {
      setWillRedirect(false);
    }
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
};

export default Dashboard;
