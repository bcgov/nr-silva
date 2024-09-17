import {
  USER_DETAILS_REQUEST,
  USER_DETAILS_SUCCESS,
  USER_DETAILS_FAIL,
  SET_CLIENT_ROLES
} from "../constants/userConstants";

const initialState = {
  user: {
    clientRoles: [],
    selectedClientRoles: null
  },
  loading: false,
  error: null
};

export const userDetailsReducer = (state = initialState, action: any) => {
  switch (action.type) {
    case USER_DETAILS_REQUEST:
      return { ...state, loading: true };
    case USER_DETAILS_SUCCESS:
      return { ...state, loading: false, user: action.payload };
    case USER_DETAILS_FAIL:
      return { ...state, loading: false, error: action.payload };
    case SET_CLIENT_ROLES:
      return {
        ...state,
        user: { ...state.user, clientRoles: action.payload }
      };
    default:
      return state;
  }
};