import type { User } from 'oidc-client-ts';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { decodeJwtPayload, getAuthIdToken, parseToken } from '@/services/AuthService';

vi.mock('@/utils/famUtils', async () => {
  const actual = await vi.importActual<typeof import('@/utils/famUtils')>('@/utils/famUtils');
  return {
    ...actual,
    parsePrivileges: vi.fn(actual.parsePrivileges),
  };
});

import { parsePrivileges } from '@/utils/famUtils';

const toBase64Url = (obj: Record<string, unknown>): string => {
  const bytes = new TextEncoder().encode(JSON.stringify(obj));
  const binary = Array.from(bytes, (b) => String.fromCharCode(b)).join('');
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
};

const createJwt = (payload: Record<string, unknown>): string => {
  const header = toBase64Url({ alg: 'RS256', typ: 'JWT' });
  const body = toBase64Url(payload);
  return `${header}.${body}.sig`;
};

const createOidcUser = (
  payload: Record<string, unknown>,
  profile: Record<string, unknown> = {}
): User => {
  const accessToken = createJwt(payload);
  return {
    access_token: accessToken,
    token_type: 'Bearer',
    profile: profile as User['profile'],
    session_state: null,
    state: undefined,
    expires_at: typeof payload.exp === 'number' ? payload.exp : undefined,
    expires_in: 300,
    expired: false,
    scopes: ['openid', 'profile', 'email'],
    toStorageString: () => '',
  } as User;
};

describe('AuthService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('decodeJwtPayload', () => {
    it('decodes UTF-8 payloads and returns undefined for malformed tokens', () => {
      const token = createJwt({ display_name: 'da Silva, María WLRS:EX' });
      expect(decodeJwtPayload(token)).toEqual({ display_name: 'da Silva, María WLRS:EX' });
      expect(decodeJwtPayload(undefined)).toBeUndefined();
      expect(decodeJwtPayload('not-a-jwt')).toBeUndefined();
      expect(decodeJwtPayload('a.!!!.c')).toBeUndefined();
    });
  });

  describe('parseToken', () => {
    it('returns undefined and clears authIdToken when user is null or missing access_token', () => {
      expect(parseToken(undefined)).toBeUndefined();
      expect(getAuthIdToken()).toBeNull();
      expect(parseToken({ access_token: '' } as User)).toBeUndefined();
    });

    it('parses Keycloak azureidir token claims, normalizes IDP to IDIR, and parses CSS roles', () => {
      const mockUser = createOidcUser({
        sub: 'user-1',
        azp: 'nr-silva',
        exp: 1999999999,
        identity_provider: 'azureidir',
        preferred_username: 'b5ecdb094dfb4149a6a8445a01a96bf0@azureidir',
        idir_user_guid: 'b5ecdb094dfb4149a6a8445a01a96bf0',
        idir_username: 'JRYAN',
        display_name: 'Ryan, Jack CIA:IN',
        email: 'jack.ryan@gov.bc.ca',
        given_name: 'Jack',
        family_name: 'Ryan',
        client_roles: [
          'SILVA_PLANNER_FOREST_CLIENT-00012797',
          'SILVA_PLANNER_FOREST_CLIENT-00001012',
          'SILVA_VIEWER',
          'SUBMITTER_FOREST_CLIENT-00001012',
          'FAM:LABEL:Submitter',
          'FAM:EXPIRES:2026-12-31',
        ],
      });

      vi.mocked(parsePrivileges).mockReturnValueOnce({
        Planner: ['00012797', '00001012'],
        Viewer: null,
        Submitter: ['00001012'],
      });

      const result = parseToken(mockUser);

      expect(getAuthIdToken()).toBe(mockUser.access_token);
      expect(result).toEqual({
        userName: 'JRYAN',
        displayName: 'Ryan, Jack CIA:IN',
        email: 'jack.ryan@gov.bc.ca',
        idpProvider: 'IDIR',
        roles: [
          'SILVA_PLANNER_FOREST_CLIENT-00012797',
          'SILVA_PLANNER_FOREST_CLIENT-00001012',
          'SILVA_VIEWER',
          'SUBMITTER_FOREST_CLIENT-00001012',
        ],
        authToken: mockUser.access_token,
        exp: 1999999999,
        privileges: {
          Planner: ['00012797', '00001012'],
          Viewer: null,
          Submitter: ['00001012'],
        },
        associatedClients: ['00012797', '00001012'],
        firstName: 'Jack',
        lastName: 'Ryan',
        providerUsername: 'IDIR\\JRYAN',
      });
    });

    it('falls back to uppercased GUID and preferred_username suffix for Business BCeID', () => {
      const mockUser = createOidcUser({
        preferred_username: '99aa88bb77cc@bceidbusiness',
        bceid_user_guid: '99aa88bb77cc',
        display_name: 'Alice Smith',
        email: 'alice@example.com',
        azp: 'nr-silva',
        resource_access: {
          'nr-silva': {
            roles: ['SUBMITTER_FOREST_CLIENT-00002176', 'FAM:DESC:Submit'],
          },
        },
      });

      const result = parseToken(mockUser);

      expect(result).toMatchObject({
        userName: '99AA88BB77CC',
        idpProvider: 'BCEIDBUSINESS',
        providerUsername: 'BCEIDBUSINESS\\99AA88BB77CC',
        firstName: 'Alice',
        lastName: 'Smith',
        roles: ['SUBMITTER_FOREST_CLIENT-00002176'],
        privileges: { Submitter: ['00002176'] },
        associatedClients: ['00002176'],
      });
    });

    it('falls back to given_name and family_name when display_name is absent', () => {
      const mockUser = createOidcUser(
        {
          identity_provider: 'idir',
          idir_username: 'johndoe',
        },
        {
          given_name: 'John',
          family_name: 'Doe',
          email: 'john.doe@gov.bc.ca',
        }
      );

      const result = parseToken(mockUser);

      expect(result).toMatchObject({
        userName: 'johndoe',
        idpProvider: 'IDIR',
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@gov.bc.ca',
      });
    });
  });
});
