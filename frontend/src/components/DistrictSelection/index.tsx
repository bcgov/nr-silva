import React, { useState } from "react";
import { Button, Column, Grid, Search, SkeletonPlaceholder } from "@carbon/react";
import { useAuth } from "@/contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";

import './styles.scss';
import { filterClientByKeyword } from "./util";
import DistrictItem from "./DistrictItem";
import { ArrowRight } from "@carbon/icons-react";
import { MIN_CLIENTS_SHOW_SEARCH } from "./constants";

type DistrictSelectionProps = {
  simpleView?: boolean; // Determines if back and continue buttons will show
}

const DistrictSelection = ({ simpleView }: DistrictSelectionProps) => {
  const [filterText, setFilterText] = useState<string>('');
  const [preSelectedClient, setPreSelectedClient] = useState<string | undefined>();
  const { user, setSelectedClient, logout } = useAuth();

  const userClientQuery = useQuery({
    queryKey: ['forest-clients', 'search', { ids: user?.associatedClients }],
    queryFn: () => API.ForestClientEndpointService.searchByClientNumbers(user!.associatedClients, 0, user!.associatedClients.length),
    enabled: !!user?.associatedClients.length
  });

  const handleContinue = () => {
    if (preSelectedClient) {
      setSelectedClient(preSelectedClient);
    }
  }

  return (
    <Grid className="district-selection-grid">

      {
        user?.associatedClients && user?.associatedClients.length > MIN_CLIENTS_SHOW_SEARCH
          ? (
            <Column className="full-width-col" sm={4} md={8} lg={16} max={16}>
              <Search
                className="search-bar"
                labelText="Search by district name or ID"
                placeholder="Search by district name or ID"
                onChange={
                  (e: React.ChangeEvent<HTMLInputElement>) => setFilterText(e.target.value)
                }
              />
            </Column>
          )
          : null
      }

      <Column className="full-width-col" sm={4} md={8} lg={16} max={16}>
        {
          userClientQuery.isLoading
            ? (
              <div className="skeleton-container">
                <SkeletonPlaceholder />
              </div>
            )
            : (
              <ul className="district-list">
                {
                  userClientQuery.data?.filter((client) => filterClientByKeyword(client, filterText))
                    .map((client) => (
                      <li className="district-list-item">
                        <button
                          type="button"
                          className={`district-list-item-btn${preSelectedClient === client.clientNumber ? ' selected-district' : ''}`}
                          onClick={() => setPreSelectedClient(client.clientNumber)}
                        >
                          <DistrictItem
                            key={client.clientNumber}
                            client={client}
                            preSelected={preSelectedClient === client.clientNumber}
                          />
                        </button>
                      </li>
                    ))
                }
              </ul>
            )
        }
      </Column>

      {
        simpleView
          ? null
          : (
            <>
              <Column className="full-width-col" sm={4} md={8} lg={8}>
                <Button
                  className="action-btn"
                  kind="ghost"
                  size="lg"
                  onClick={() => logout()}
                >
                  Cancel
                </Button>
              </Column>
              <Column className="full-width-col" sm={4} md={8} lg={8}>
                <Button
                  className="action-btn"
                  kind="primary"
                  size="lg"
                  renderIcon={ArrowRight}
                  onClick={handleContinue}
                >
                  Continue
                </Button>
              </Column>
            </>
          )
      }
    </Grid >
  )
}

export default DistrictSelection;
