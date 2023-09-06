import { USER_DETAILS_REQUEST, USER_DETAILS_SUCCESS, USER_DETAILS_FAIL } from '../constants/userConstants'
import { isCurrentAuthUser } from '../services/AuthService'

export const getUserDetails = () => async (dispatch: any) => {
  try {
    dispatch({
      type: USER_DETAILS_REQUEST
    })
    // readt the localStorage Here
    const data = await isCurrentAuthUser()
    dispatch({
      type: USER_DETAILS_SUCCESS,
      payload: { TestUser: 'Jazz as always', isLoggedIn: data }
    })
  } catch (error) {
    dispatch({
      type: USER_DETAILS_FAIL,
      payload: { error }
    })
  }
}
