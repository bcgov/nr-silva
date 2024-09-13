import { UserClientRolesType } from "../types/UserRoleType";

export function formatRolesArray(decodedIdToken: any): UserClientRolesType[] {
    if (!decodedIdToken || !decodedIdToken['cognito:groups']) {
      return [];
    }
  
    const cognitoGroups: string[] = decodedIdToken['cognito:groups'];
    const rolesMap: { [key: string]: string[] } = {};
  
    cognitoGroups.forEach((group: string) => {
      if (group.indexOf('_') > 0) {
        const [role, clientId] = group.split('_');
        if (!rolesMap[clientId]) {
          rolesMap[clientId] = [];
        }
        rolesMap[clientId].push(role);
      }
    });
  
    const rolesArray: UserClientRolesType[] = Object.keys(rolesMap).map(clientId => ({
      clientId,
      roles: rolesMap[clientId],
      clientName: `Client Number ${clientId}` // Placeholder for client name, modify as needed
    }));
  
    return rolesArray;
  }
  