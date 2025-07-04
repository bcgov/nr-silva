import { describe, it, expect, beforeEach, vi } from 'vitest';
import { parseToken, getAuthIdToken } from '../../services/AuthService';
import { JWT } from '../../types/amplify';

// Partially mock famUtils, only override parsePrivileges
vi.mock('../../utils/famUtils', async () => {
  const actual = await vi.importActual<typeof import('../../utils/famUtils')>(
    '../../utils/famUtils'
  );

  return {
    ...actual,
    parsePrivileges: vi.fn(),
  };
});

import { parsePrivileges } from '../../utils/famUtils';

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
            "Planner_00012797",
            "Planner_00001012",
            "Planner_00002176",
            "Viewer",
            "Submitter_00001012"
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

      (parsePrivileges as vi.Mock).mockReturnValue({
        Planner: ["00012797", "00001012"],
        Viewer: null,
        Submitter: ["00001012"]
      });

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'JRYAN',
        displayName: "Ryan, Jack CIA:IN",
        email: 'jack.ryan@gov.bc.ca',
        idpProvider: 'IDIR',
        privileges: {
          Planner: ["00012797", "00001012"],
          Viewer: null,
          Submitter: ["00001012"]
        },
        firstName: 'Jack',
        lastName: 'Ryan',
        providerUsername: 'IDIR\\JRYAN',
      });
    });

    it('should handle displayName without comma correctly', () => {
      const mockToken: JWT = {
        payload: {
          "cognito:groups": [
            "Planner_00012797",
            "Planner_00001012",
            "Planner_00002176",
            "Viewer",
            "Submitter_00001012"
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

      (parsePrivileges as vi.Mock).mockReturnValue({
        Planner: ["00012797", "00001012"],
        Viewer: null,
        Submitter: ["00001012"]
      });

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'JRYAN',
        displayName: 'Jack Ryan',
        email: 'jack.ryan@gov.bc.ca',
        idpProvider: 'IDIR',
        privileges: {
          Planner: ["00012797", "00001012"],
          Viewer: null,
          Submitter: ["00001012"]
        },
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

      (parsePrivileges as vi.Mock).mockReturnValue({});

      const result = parseToken(mockToken);

      expect(getAuthIdToken()).toBe('mockTokenString');
      expect(result).toEqual({
        userName: 'johndoe',
        displayName: 'Doe, John',
        email: 'john.doe@example.com',
        idpProvider: undefined,
        privileges: {},
        firstName: 'John',
        lastName: 'Doe',
        providerUsername: "undefined\\johndoe",
      });
    });
  });
});
