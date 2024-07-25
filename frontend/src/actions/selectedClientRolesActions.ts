// selectedClientRolesActions.js
import { SET_SELECTED_CLIENT_ROLES } from '../constants/selectedClientRolesConstants';
import { AppDispatch } from '../store';
import { UserClientRolesType } from '../types/UserRoleType';

export interface SetSelectedClientRolesAction {
  type: typeof SET_SELECTED_CLIENT_ROLES;
  payload: UserClientRolesType;
}

export const setSelectedClientRoles = (selectedClientRoles: UserClientRolesType) => (dispatch: AppDispatch) => {
  dispatch({
    type: SET_SELECTED_CLIENT_ROLES,
    payload: selectedClientRoles
  });
};
