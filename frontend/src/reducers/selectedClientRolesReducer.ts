// selectedClientRolesReducer.js
import { SET_SELECTED_CLIENT_ROLES } from "../constants/selectedClientRolesConstants";
import { UserClientRolesType } from "../types/UserRoleType";

const selectedClientRolesFromStorage = JSON.parse(localStorage.getItem('selectedClientRoles') as string) as
  | UserClientRolesType
  | null;

const initialState = selectedClientRolesFromStorage ?? null;

interface ActionType {
  type: string;
  payload: UserClientRolesType
}

export const selectedClientRolesReducer = (state = initialState, action: ActionType): UserClientRolesType | null => {
  switch (action.type) {
    case SET_SELECTED_CLIENT_ROLES:
      return action.payload;
    default:
      return state;
  }
};
