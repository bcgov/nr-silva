import { USER_DETAILS_REQUEST, USER_DETAILS_SUCCESS, USER_DETAILS_RESET, USER_DETAILS_FAIL } from '../constants/userConstants'

export const userDetailsReducer = (state = { user: {} }, action: any): any => {
  switch (action.type) {
    case USER_DETAILS_REQUEST:
      return { ...state, loading: true }
    case USER_DETAILS_SUCCESS:
      return { loading: false, user: action.payload }
    case USER_DETAILS_FAIL:
      return { loading: false, error: action.payload }
    case USER_DETAILS_RESET:
      return { user: {} }
    default:
      return state
  }
}
