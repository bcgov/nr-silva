// selectedClientRolesReducer.js
import { SET_SELECTED_CLIENT_ROLES } from "../constants/selectedClientRolesConstants";
import { UserClientRolesType } from "../types/UserRoleType";

const selectedClientRolesFromStorage = JSON.parse(localStorage.getItem('selectedClientRoles') as string) as
  | UserClientRolesType
  | undefined
  | null;

const initialState = selectedClientRolesFromStorage ?? null;

export const selectedClientRolesReducer = (state = initialState, action: any) => {
  switch (action.type) {
    case SET_SELECTED_CLIENT_ROLES:
      return action.payload;
    default:
      return state;
  }
};
