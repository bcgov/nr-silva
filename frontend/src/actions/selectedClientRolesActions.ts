// selectedClientRolesActions.js
import { SET_SELECTED_CLIENT_ROLES } from '../constants/selectedClientRolesConstants';
import { UserClientRolesType } from '../types/UserRoleType';

export const setSelectedClientRoles = (selectedClientRoles: UserClientRolesType) => (dispatch: any) => {
  dispatch({
    type: SET_SELECTED_CLIENT_ROLES,
    payload: selectedClientRoles
  });
};
