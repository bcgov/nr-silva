import { describe, it, expect, vi } from 'vitest';
import { getUserDetails, setClientRoles } from '../../actions/userAction';
import {
  USER_DETAILS_REQUEST,
USER_DETAILS_SUCCESS,
USER_DETAILS_FAIL,
SET_CLIENT_ROLES
} from '../../constants/userConstants';
import { useGetAuth } from '../../contexts/AuthProvider';
import { AppDispatch } from '../../store';
import { UserClientRolesType } from '../../types/UserRoleType';



vi.mock('../../contexts/AuthProvider', () => ({
useGetAuth: vi.fn(),
}));

describe('userAction', () => {
describe('getUserDetails', () => {
  it('should dispatch USER_DETAILS_REQUEST and USER_DETAILS_SUCCESS with user data when successful', async () => {
    const mockDispatch = vi.fn();
    const mockIsLoggedIn = true;
    const mockUser = { firstName: 'John', lastName: 'Doe' };
    const mockUserJSON = JSON.stringify(mockUser);

    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: mockIsLoggedIn });
    localStorage.setItem('famLoginUser', mockUserJSON);

    await getUserDetails()(mockDispatch as unknown as AppDispatch);

    expect(mockDispatch).toHaveBeenCalledWith({ type: USER_DETAILS_REQUEST });
    expect(mockDispatch).toHaveBeenCalledWith({
      type: USER_DETAILS_SUCCESS,
      payload: { ...mockUser, isLoggedIn: mockIsLoggedIn },
    });
  });

  it('should dispatch USER_DETAILS_FAIL with error when an error occurs', async () => {
    const mockDispatch = vi.fn();
    const mockError = new Error('Test error');

    (useGetAuth as vi.Mock).mockImplementation(() => {
      throw mockError;
    });

    await getUserDetails()(mockDispatch as unknown as AppDispatch);

    expect(mockDispatch).toHaveBeenCalledWith({ type: USER_DETAILS_REQUEST });
    expect(mockDispatch).toHaveBeenCalledWith({
      type: USER_DETAILS_FAIL,
      payload: { error: mockError },
    });
  });

  it('should handle missing user data in localStorage', async () => {
    const mockDispatch = vi.fn();
    const mockIsLoggedIn = true;

    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: mockIsLoggedIn });
    localStorage.removeItem('famLoginUser');

    await getUserDetails()(mockDispatch as unknown as AppDispatch);

    expect(mockDispatch).toHaveBeenCalledWith({ type: USER_DETAILS_REQUEST });
    expect(mockDispatch).toHaveBeenCalledWith({
      type: USER_DETAILS_SUCCESS,
      payload: { isLoggedIn: mockIsLoggedIn },
    });
  });
});

describe('setClientRoles', () => {
  it('should dispatch SET_CLIENT_ROLES with client roles', () => {
    const mockDispatch = vi.fn();
    const mockClientRoles: UserClientRolesType[] = [{ clientId: '123', roles: ['admin'] }];

    setClientRoles(mockClientRoles)(mockDispatch as unknown as AppDispatch);

    expect(mockDispatch).toHaveBeenCalledWith({
      type: SET_CLIENT_ROLES,
      payload: mockClientRoles,
    });
  });
});
});