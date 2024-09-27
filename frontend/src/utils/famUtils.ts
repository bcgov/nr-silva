import { UserClientRolesType } from '../types/UserRoleType';

/**
 * Decode user roles from token and format for the UserClientRolesType type.
 *
 * @param {object | undefined} decodedIdToken Decoded token with payload.
 * @returns {UserClientRolesType} Array of UserClientRolesType containing user roles.
 */
export function formatRolesArray(decodedIdToken: object | undefined): UserClientRolesType[] {
    if (!decodedIdToken) {
      return [];
    }

    if ('cognito:groups' in decodedIdToken) {
      const cognitoGroups: string[] = decodedIdToken['cognito:groups'] as string[];
      const rolesMap: { [key: string]: string[] } = {};
    
      cognitoGroups.forEach((group: string) => {
        if (group.indexOf('_') > 0) {
          const [role, clientId] = group.split('_');
          if (!rolesMap[clientId]) {
            rolesMap[clientId] = [];
          }
          rolesMap[clientId].push(role);
        }
<<<<<<< HEAD
        rolesMap[clientId].push(role);
      }
    });
  
    const rolesArray: UserClientRolesType[] = Object.keys(rolesMap).map(clientId => ({
      clientId,
      roles: rolesMap[clientId],
      clientName: `Client Number ${clientId}` // Placeholder for client name, modify as needed
    }));
  
    return rolesArray;
=======
      });
    
      const rolesArray: UserClientRolesType[] = Object.keys(rolesMap).map(clientId => ({
        clientId,
        roles: rolesMap[clientId],
        clientName: `Client Number ${clientId}` // Placeholder for client name, modify as needed
      }));
    
      return rolesArray;
    }

    return [];
>>>>>>> 03e42be1caa1bb07bea033ab5fe87e2211ed2906
  }
  