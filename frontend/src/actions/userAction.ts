import {
  USER_DETAILS_REQUEST,
  USER_DETAILS_SUCCESS,
  USER_DETAILS_FAIL,
  SET_CLIENT_ROLES
} from '../constants/userConstants';
import { isCurrentAuthUser } from '../services/AuthService';
import { AppDispatch } from '../store';
import { UserClientRolesType } from '../types/UserRoleType';

const FAM_LOGIN_USER = 'famLoginUser';

export const getUserDetails = () => async (dispatch: AppDispatch) => {
  try {
    dispatch({
      type: USER_DETAILS_REQUEST
    });
    //first call the isCurrent and only after that extract the JSON
    const data = await isCurrentAuthUser();

    const userJSON = localStorage.getItem(FAM_LOGIN_USER); // Retrieve the JSON string from local storage
    const user = userJSON ? JSON.parse(userJSON) : null; // Parse the JSON string to a JavaScript object

    dispatch({
      type: USER_DETAILS_SUCCESS,
      payload: { ...user, isLoggedIn: data }
    });
  } catch (error) {
    dispatch({
      type: USER_DETAILS_FAIL,
      payload: { error: error }
    });
  }
};

export const setClientRoles = (clientRoles:UserClientRolesType[]) => (dispatch: AppDispatch) => {
  dispatch({
    type: SET_CLIENT_ROLES,
    payload: clientRoles
  });
};
