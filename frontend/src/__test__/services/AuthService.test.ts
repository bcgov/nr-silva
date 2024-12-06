import { describe, it, expect, beforeEach } from 'vitest';
import { parseToken, getAuthIdToken } from '../../services/AuthService';
import { formatRolesArray } from '../../utils/famUtils';
import { JWT } from '../../types/amplify';

vi.mock('../../utils/famUtils', () => ({
  formatRolesArray: vi.fn(),
}));

describe('AuthService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('parseToken', () => {
    it('should return undefined if idToken is undefined', () => {
      const result = parseToken(undefined);
      expect(result).toBeUndefined();
    });

    it('should set authIdToken and return parsed user data', () => {
      const mockToken: JWT = {
        payload: {
          "cognito:groups": [
            "group1",
            "group2"
          ],
          "preferred_username": "b5ecdb094dfb4149a6a8445a01a96bf0@idir",
          "custom:idp_user_id": "B5ECDB094DFB4149A6A8445A01A96BF0",
          "custom:idp_username": "JRYAN",
          "custom:idp_display_name": "Ryan, Jack CIA:IN",
          "email": "jack.ryan@gov.bc.ca",
          "email_verified": false,
          "custom:idp_name": "idir",
          "given_name": "Jack",
          "name": "Jack Ryan",
          "family_name": "Ryan"
        },
        toString: () => 'mockTokenString',
      };

      (formatRolesArray as vi.Mock).mockReturnValue([{ role: 'role1' }, { role: 'role2' }]);

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'JRYAN',
        displayName: "Ryan, Jack CIA:IN",
        email: 'jack.ryan@gov.bc.ca',
        idpProvider: 'IDIR',
        clientRoles: [{ role: 'role1' }, { role: 'role2' }],
        firstName: 'Jack',
        lastName: 'Ryan',
        providerUsername: 'IDIR\\JRYAN',
      });
    });

    it('should handle displayName without comma correctly', () => {
      const mockToken: JWT = {
        payload: {
          "cognito:groups": [
            "group1",
            "group2"
          ],
          "preferred_username": "b5ecdb094dfb4149a6a8445a01a96bf0@idir",
          "custom:idp_user_id": "B5ECDB094DFB4149A6A8445A01A96BF0",
          "custom:idp_username": "JRYAN",
          "custom:idp_display_name": "Jack Ryan",
          "email": "jack.ryan@gov.bc.ca",
          "email_verified": false,
          "custom:idp_name": "idir",
          "given_name": "Jack",
          "name": "Jack Ryan",
          "family_name": "Ryan"
        },
        toString: () => 'mockTokenString',
      };

      (formatRolesArray as vi.Mock).mockReturnValue([{ role: 'role1' }, { role: 'role2' }]);

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'JRYAN',
        displayName: 'Jack Ryan',
        email: 'jack.ryan@gov.bc.ca',
        idpProvider: 'IDIR',
        clientRoles: [{ role: 'role1' }, { role: 'role2' }],
        firstName: 'Jack',
        lastName: 'Ryan',
        providerUsername: 'IDIR\\JRYAN',
      });
    });

    it('should handle missing identities correctly', () => {
      const mockToken: JWT = {
        payload: {
          'custom:idp_display_name': 'Doe, John',
          'custom:idp_username': 'johndoe',
          'email': 'john.doe@example.com'
        },
        toString: () => 'mockTokenString',
      };

      (formatRolesArray as vi.Mock).mockReturnValue([{ role: 'role1' }, { role: 'role2' }]);

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'johndoe',
        displayName: 'Doe, John',
        email: 'john.doe@example.com',
        idpProvider: '',
        clientRoles: [{ role: 'role1' }, { role: 'role2' }],
        firstName: 'John',
        lastName: 'Doe',
        providerUsername: '\\johndoe',
      });
    });
  });
});