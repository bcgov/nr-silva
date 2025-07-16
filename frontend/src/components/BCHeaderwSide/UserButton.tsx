import React, { useContext, useEffect } from 'react';
import { ChevronDown } from '@carbon/icons-react';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '@/contexts/AuthProvider';
import useBreakpoint from '@/hooks/UseBreakpoint';
import API from '@/services/API';
import Avatar from '@/components/Avatar';


const UserButton = () => {
  const { user, selectedClient } = useAuth();
  const breakpoint = useBreakpoint();

  const userClientQuery = useQuery({
    queryKey: ['forest-clients', 'search', user?.associatedClients],
    queryFn: () => API.ForestClientEndpointService.searchByClientNumbers(user!.associatedClients, 0, user!.associatedClients.length),
    enabled: !!user?.associatedClients.length,
    select: (data) => (
      data.find((client) => client.clientNumber === selectedClient)
    )
  });

  const showSimpleView: boolean = breakpoint === 'sm' || !user?.associatedClients.length;

  return (
    <div className="user-header-btn">
      <div className="avatar-and-org-name">
        <Avatar
          size="sm"
          initial={`${user?.firstName?.charAt(0).toUpperCase()}${user?.lastName?.charAt(0).toUpperCase()}`}
        />
        {
          showSimpleView
            ? null
            : (
              <p className="client-name">
                {
                  userClientQuery.data
                    ? (userClientQuery.data.name ?? userClientQuery.data.clientName)
                    : 'Client name not available'
                }
              </p>
            )
        }
      </div>
      {
        showSimpleView
          ? null
          : <ChevronDown />
      }
    </div>
  );
};

export default UserButton;
