import React, { useState } from "react";
import { Button, Column, Grid, Search, SkeletonPlaceholder } from "@carbon/react";
import { useAuth } from "@/contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { SELECTED_CLIENT_KEY } from "@/constants";
import { filterClientByKeyword } from "./utils";
import DistrictItem from "./DistrictItem";
import { ArrowRight } from "@carbon/icons-react";
import { MIN_CLIENTS_SHOW_SEARCH } from "./constants";

import './styles.scss';

type DistrictSelectionProps = {
  simpleView?: boolean; // Determines if back and continue buttons will show
}

const DistrictSelection = ({ simpleView }: DistrictSelectionProps) => {
  const [filterText, setFilterText] = useState<string>('');
  const { user, selectedClient, setSelectedClient, logout } = useAuth();
  const [preSelectedClient, setPreSelectedClient] = useState<string | undefined>(selectedClient);

  const userClientQuery = useQuery({
    queryKey: ['forest-clients', 'search', user?.associatedClients],
    queryFn: () => API.ForestClientEndpointService.searchByClientNumbers(user!.associatedClients, 0, user!.associatedClients.length),
    enabled: !!user?.associatedClients.length
  });

  const storeClient = (clientNumber: string) => {
    localStorage.setItem(SELECTED_CLIENT_KEY, clientNumber)
  }

  const handleListItemSelect = (clientNumber: string) => {
    if (simpleView) {
      storeClient(clientNumber);
      setSelectedClient(clientNumber);
    }
    setPreSelectedClient(clientNumber);
  }

  const handleContinue = () => {
    if (preSelectedClient) {
      storeClient(preSelectedClient);
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
              <ul className="district-list" aria-label="District list">
                {
                  userClientQuery.data?.filter((client) => filterClientByKeyword(client, filterText))
                    .map((client) => (
                      <li key={client.clientNumber} className="district-list-item" aria-label={client.clientName} title={client.clientName}>
                        <button
                          type="button"
                          className={`district-list-item-btn${preSelectedClient === client.clientNumber ? ' selected-district' : ''}`}
                          onClick={() => handleListItemSelect(client.clientNumber)}
                        >
                          <DistrictItem
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
