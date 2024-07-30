import { createStore, combineReducers, applyMiddleware } from 'redux'
import { thunk } from 'redux-thunk'
import { composeWithDevTools } from '@redux-devtools/extension'
import type { CognitoUserSession } from 'amazon-cognito-identity-js'
import { userDetailsReducer } from './reducers/userReducer'
import { UserClientRolesType } from './types/UserRoleType'
import { selectedClientRolesReducer } from './reducers/selectedClientRolesReducer'
import { FamLoginUser } from './services/AuthService'

const reducer = combineReducers({
  userDetails: userDetailsReducer,
  selectedClientRoles: selectedClientRolesReducer
});

const FAM_LOGIN_USER = 'famLoginUser';

const userInfoFromStorage = JSON.parse(localStorage.getItem(FAM_LOGIN_USER) as string) as
  | FamLoginUser
  | undefined
  | null;

const selectedClientRolesFromStorage = JSON.parse(localStorage.getItem('selectedClientRoles') as string) as
  | UserClientRolesType
  | undefined
  | null;

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

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
