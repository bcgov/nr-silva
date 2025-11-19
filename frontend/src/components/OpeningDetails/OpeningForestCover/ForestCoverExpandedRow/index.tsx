import React, { use } from "react";
import { useQuery } from "@tanstack/react-query";
import EmptySection from "@/components/EmptySection";
import { Column, Grid } from "@carbon/react";
import API from "@/services/API";
import ForestManagement from "./ForestManagement";
import PolygonDetail from "./PolygonDetail";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";

import './styles.scss';

type ForestCoverDetailProps = {
  forestCoverId: number;
  openingId: number;
  isHistory?: boolean;
  archiveDate?: string;
};

const ForestCoverExpandedRow = ({ forestCoverId, openingId, isHistory = false, archiveDate }: ForestCoverDetailProps) => {
  const query = useQuery({
    queryKey: ["openings", openingId, "cover", forestCoverId],
    queryFn: () => API.OpeningEndpointService.getCoverDetails(openingId, forestCoverId),
    enabled: !isHistory,
  });

  const historyQuery = useQuery({
    queryKey: ["openings", openingId, "cover", "history", forestCoverId, { archiveDate: archiveDate }],
    queryFn: () => API.OpeningEndpointService.getForestCoverHistoryDetails(openingId, forestCoverId, archiveDate!),
    enabled: isHistory && !!archiveDate,
  });

  const queryToUse = isHistory ? historyQuery : query;

  if (!queryToUse.data && !queryToUse.isLoading && !isAuthRefreshInProgress()) {
    return (
      <EmptySection
        pictogram="UserSearch"
        title=""
        description={`Forest cover ${forestCoverId} not found.`}
      />
    )
  }

  return (
    <Grid className="opening-forest-cover-details-grid">
      <PolygonDetail polygon={queryToUse.data?.polygon} isLoading={queryToUse.isLoading || isAuthRefreshInProgress()} />

      {
        queryToUse.data?.layers.length
          ? (
            <>
              <Column sm={4} md={8} lg={16}>
                <hr className="expanded-row-hr" />
              </Column>

              <ForestManagement
                isSingleLayer={queryToUse.data?.isSingleLayer}
                layersData={queryToUse.data?.layers}
                unmappedAreaData={queryToUse.data?.unmapped}
                isLoading={queryToUse.isLoading || isAuthRefreshInProgress()}
              />
            </>
          )
          : null
      }
    </Grid >
  );
};

export default ForestCoverExpandedRow;
