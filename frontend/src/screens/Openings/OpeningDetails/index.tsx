import React, { useEffect } from "react";
import { Column, Grid, Tab, TabList, TabPanel, TabPanels, Tabs } from "@carbon/react";
import { MapBoundaryVegetation, Development, Construction } from "@carbon/icons-react";
import { useParams } from "react-router-dom";
import { useMutation, useQuery } from "@tanstack/react-query";

import { searchOpenings } from "@/services/OpeningSearchService";
import { putUserRecentOpening } from "@/services/OpeningService";
import { OpeningStandardUnits, OpeningSummary } from "@/components/OpeningDetails";
import { OpeningOverview } from "@/components/OpeningDetails";
import ActionableFavouriteButton from "@/components/FavoriteButton/ActionableFavouriteButton";
import PageTitle from "@/components/PageTitle";

import { OpeningDetailBreadCrumbs } from "./constants";
import './styles.scss';

const OpeningDetails = () => {

  const param = useParams();
  const openingId = param.openingId;

  /**
   * TODO:
   * Temporarily using opening search to get data, will need to update this once API is done
   */
  const openingQuery = useQuery({
    queryKey: ['openings', 'search', { openingId }],
    queryFn: () => searchOpenings({ mainSearchTerm: openingId, page: 0, size: 100 }),
    enabled: !!openingId,
    select: (data) => {
      const { content } = data;
      if (!content.length) {
        return undefined;
      }
      return content.find((opening) => opening.openingId.toString() === openingId)
    },
    refetchOnMount: true
  })

  const postRecentOpeningMutation = useMutation({
    mutationFn: (openingId: number) => putUserRecentOpening(openingId)
  });

  /**
   * Update most recent openings when this page loads
   */
  useEffect(() => {
    if (openingId && Number.isInteger(Number(openingId))) {
      postRecentOpeningMutation.mutate(Number(openingId));
    }
  }, [openingId]);


  return (
    <Grid className="default-grid opening-detail-grid">
      <PageTitle
        title={`Opening ID ${openingId}`}
        subtitle="Check and manage this opening"
        breadCrumbs={OpeningDetailBreadCrumbs}
        experimental
      >
        <ActionableFavouriteButton openingId={Number(openingId)} />
      </PageTitle>

      <Column sm={4} md={8} lg={16}>
        <OpeningSummary openingObj={openingQuery.data} isLoading={openingQuery.isLoading} />
      </Column>

      <Column className="opening-detail-tabs-col" sm={4} md={8} lg={16}>
        <Tabs>
          <TabList className="default-tab-list" aria-label="List of Tab" contained>
            <Tab renderIcon={() => <MapBoundaryVegetation size={16} />}>Overview</Tab>
            <Tab renderIcon={() => <Development size={16} />}>Standard units</Tab>
          </TabList>
          <TabPanels>
            <TabPanel className="tab-content full-width-col">
              <OpeningOverview isLoading={openingQuery.isLoading} />
            </TabPanel>
            <TabPanel className="tab-content full-width-col">
              <OpeningStandardUnits />
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Column>
    </Grid >
  )
}

export default OpeningDetails;
