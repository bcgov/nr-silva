import React, { useState } from 'react';
import {
  FlexGrid, Row, Column,
  ButtonSkeleton, Search, Button,
  ContainedListItem
} from '@carbon/react';
import { ArrowRight } from '@carbon/icons-react';
import { useQueries, useQueryClient } from '@tanstack/react-query';
import { logout } from '../../services/AuthService';
import { setSelectedClientRoles } from '../../actions/selectedClientRolesActions';
import { getForestClientByNumberOrAcronym } from '../../services/TestService';
import { THREE_HALF_HOURS, THREE_HOURS } from '../../config/TimeUnits';
import { UserClientRolesType } from '../../types/UserRoleType';
import { ForestClientType } from '../../types/ForestClientTypes/ForestClientType';
import EmptySection from '../EmptySection';

import { MIN_CLIENTS_SHOW_SEARCH, TEXT } from './constants';
import { RoleSelectionProps } from './definitions';
import OrganizationItem from './OrganizationItem';

const SELECTED_CLIENT_ROLES = 'selectedClientRoles';

import './styles.scss';
import { AppDispatch, RootState } from '../../store';
import { useDispatch, useSelector } from 'react-redux';

const OrganizationSelection = ({ simpleView }: RoleSelectionProps) => {
  const dispatch = useDispatch<AppDispatch>();
  const userDetails = useSelector((state: RootState) => state.userDetails);

  const user = userDetails.user;
  const selectedClientRoles = useSelector((state: RootState) => state.selectedClientRoles);
  
  const [matchedClients, setMatchedClients] = useState<string[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [clientRolesToSet, setClientRolesToSet] = useState<UserClientRolesType | null>(selectedClientRoles);

  useQueries({
    queries: user?.clientRoles?.map((clientRole:UserClientRolesType) => ({
      queryKey: ['role', 'forest-clients', clientRole.clientId],
      queryFn: () => getForestClientByNumberOrAcronym(clientRole.clientId),
      staleTime: THREE_HOURS,
      cacheTime: THREE_HALF_HOURS,
      refetchOnReconnect: false
    })) ?? []
  });

  const qc = useQueryClient();

  const filterClientsByValue = (value: string) => {
    const forestClientsQueriesData = qc.getQueriesData({ queryKey: ['role', 'forest-clients'] });

    const forestClients = forestClientsQueriesData.map((qData) => (
      qData.at(1) as ForestClientType
    ));

    const loweredSearchTerm = value.toLowerCase();

    const foundByName = forestClients
      .filter((fc) => (fc.clientName.toLowerCase().includes(loweredSearchTerm)));

    const foundById = forestClients
      .filter((fc) => (fc.clientNumber.includes(loweredSearchTerm)));

    const foundCombined = foundByName.concat(foundById);

    const foundIds = foundCombined.map((fc) => fc.clientNumber);

    setSearchTerm(value);
    setMatchedClients(foundIds);
  };

  const setSelectedClientRolesHandler = (clientId: string, clientName?: string) => {
    if (clientId) {
      const found = user?.clientRoles?.find((uClientRole: UserClientRolesType) => (
        uClientRole.clientId === clientId
      ));
      if (found) {
        const toSet: UserClientRolesType = {
          ...found,
          clientName
        };
        setClientRolesToSet(toSet);
        if (simpleView) {
          localStorage.setItem(SELECTED_CLIENT_ROLES, JSON.stringify(toSet))
          dispatch(setSelectedClientRoles(toSet));
        }
      }
    }
  };

  const continueToDashboard = () => {
    if (clientRolesToSet) {
      localStorage.setItem(SELECTED_CLIENT_ROLES, JSON.stringify(clientRolesToSet))
      dispatch(setSelectedClientRoles(clientRolesToSet));
    }
  };

  const renderOrgItem = (clientRole: UserClientRolesType) => {
    const queryKey = ['role', 'forest-clients', clientRole.clientId];
    const queryState = qc.getQueryState(queryKey);
    const queryData: ForestClientType | undefined = qc.getQueryData(queryKey);
    
    //then it would automatically be loading, but the loading is not working still need to determine
    if (queryState?.status !== 'success' && queryState?.status !== 'error') {
      return (
        <Row className="org-item-skeleton-row" key={`${clientRole.clientId}-${clientRole.roles[0]}`}>
          <Column>
            <ButtonSkeleton />
          </Column>
        </Row>
      );
    }

    if (
      (
        matchedClients.length === 0
        && searchTerm === ''
      )
      || matchedClients.includes(clientRole.clientId)
    ) {
      return (
        <ContainedListItem
          key={`${clientRole.clientId}-${clientRole.roles[0]}`}
          disabled={queryState?.status === 'error'}
          onClick={() => setSelectedClientRolesHandler(clientRole.clientId, queryData?.clientName)}
        >
          <OrganizationItem
            forestClient={queryData}
            queryState={queryState}
            selected={clientRolesToSet?.clientId === clientRole.clientId}
          />
        </ContainedListItem>
      );
    }

    return null;
  };

  const renderListSection = () => {
    if (user?.clientRoles.length === 0) {
      return (
        <Column>
          <EmptySection
            title="No organization found under your user"
            description={TEXT.emptyRole}
            icon="SearchLocate"
          />
        </Column>
      );
    }

    if (searchTerm.length > 0 && matchedClients.length === 0) {
      return (
        <Column>
          <EmptySection
            title="Results not found"
            icon="SearchLocate"
            description={(
              <p>
                {TEXT.emptySearch}
              </p>
            )}
          />
        </Column>
      );
    }

    return (
      <Column className="org-items-col">
        <ul aria-label="List of Organization">
          {
            user?.clientRoles
              .map((clientRole:any) => (
                renderOrgItem(clientRole)
              ))
          }
        </ul>
      </Column>
    );
  };

  return (
    <FlexGrid className="org-selection-grid">
      {
        user!.clientRoles.length > MIN_CLIENTS_SHOW_SEARCH
          ? (
            <Row className="search-row">
              <Column>
                <Search
                  className="search-bar"
                  labelText={TEXT.searchLabel}
                  placeholder={TEXT.searchLabel}
                  onChange={
                    (e: React.ChangeEvent<HTMLInputElement>) => filterClientsByValue(e.target.value)
                  }
                />
              </Column>
            </Row>
          ) : null
      }
      <Row className="org-items-row">
        {
          renderListSection()
        }
      </Row>
      {simpleView?null:(
        <Row className="btn-row">
        <Column>
          <Button
            className="action-btn"
            kind="ghost"
            size="lg"
            onClick={logout}
          >
            Cancel
          </Button>
        </Column>
        <Column>
          <Button
            className="action-btn"
            kind="primary"
            size="lg"
            onClick={continueToDashboard}
            renderIcon={ArrowRight}
          >
            Continue
          </Button>
        </Column>
      </Row>
      )}
    </FlexGrid>
  );
};

export default OrganizationSelection;
