import { createStore, combineReducers, applyMiddleware } from 'redux'
import { thunk } from 'redux-thunk'
import { composeWithDevTools } from '@redux-devtools/extension'
import type { CognitoUserSession } from 'amazon-cognito-identity-js'
import { userDetailsReducer } from './reducers/userReducer'
import { UserClientRolesType } from './types/UserRoleType'
import { selectedClientRolesReducer } from './reducers/selectedClientRolesReducer'

const reducer = combineReducers({
  userDetails: userDetailsReducer,
  selectedClientRoles: selectedClientRolesReducer
});

export interface FamLoginUser {
  username?: string;
  idpProvider?: string;
  roles?: string[];
  authToken?: CognitoUserSession;
}
const FAM_LOGIN_USER = 'famLoginUser';

const userInfoFromStorage = JSON.parse(localStorage.getItem(FAM_LOGIN_USER) as string) as
  | FamLoginUser
  | undefined
  | null;

const selectedClientRolesFromStorage = JSON.parse(localStorage.getItem('selectedClientRoles') as string) as
  | UserClientRolesType
  | undefined
  | null;

interface User extends FamLoginUser {
  isLoggedIn: boolean;
}

interface UserState {
  userDetails: {
    user: User;
    loading: boolean;
    error: boolean;
  };
  selectedClientRoles: UserClientRolesType | undefined | null;
}

// set the initial state
const initialState: any = {
  userDetails: {
    user: {
      ...userInfoFromStorage,
      isLoggedIn: !!userInfoFromStorage?.authToken
    },
    loading: true,
    error: false
  },
  selectedClientRoles: selectedClientRolesFromStorage
};

const middleware = [thunk];

const store = createStore(
  reducer,
  initialState,
  composeWithDevTools(applyMiddleware(...middleware))
);

export default store;
