import { createStore, combineReducers, applyMiddleware } from 'redux'
import { thunk } from 'redux-thunk'
import { composeWithDevTools } from '@redux-devtools/extension'
import type { CognitoUserSession } from 'amazon-cognito-identity-js'
import { userDetailsReducer } from './reducers/userReducer'

const reducer = combineReducers({
  userDetails: userDetailsReducer
})
export interface FamLoginUser {
  username?: string
  idpProvider?: string
  roles?: string[]
  authToken?: CognitoUserSession
}
const FAM_LOGIN_USER = 'famLoginUser'

const userInfoFromStorage = (JSON.parse(localStorage.getItem(FAM_LOGIN_USER) as string) as
| FamLoginUser
| undefined
| null)

interface User extends FamLoginUser {
  isLoggedIn: boolean
}

interface UserState {
  userDetails: {
    user: User,
    loading: boolean,
    error: boolean
  }
}

// set the initial state
const initialState: UserState = {
  userDetails: {
    user: {
      ...userInfoFromStorage,
      isLoggedIn: !!userInfoFromStorage?.authToken
    },
    loading: true,
    error: false
  }
}

const middleware = [thunk]

const store = createStore(
  reducer,
  initialState,
  composeWithDevTools(applyMiddleware(...middleware))
)

export default store
