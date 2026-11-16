import type { User } from 'oidc-client-ts';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const mockInstance = {
  getUser: vi.fn(),
  signinSilent: vi.fn(),
  removeUser: vi.fn(),
  settings: {} as Record<string, unknown>,
};

vi.mock('oidc-client-ts', () => ({
  WebStorageStateStore: vi.fn(function (this: Record<string, unknown>, opts: unknown) {
    this.opts = opts;
  }),
  UserManager: vi.fn(function (this: Record<string, unknown>, settings: Record<string, unknown>) {
    mockInstance.settings = settings;
    Object.assign(this, mockInstance);
  }),
}));

vi.mock('@/env', () => ({
  env: {
    VITE_KEYCLOAK_URL: 'https://dev.loginproxy.gov.bc.ca/auth/realms/standard',
    VITE_KEYCLOAK_CLIENT_ID: 'nr-silva',
  },
}));

import {
  ensureFreshUser,
  forceRenew,
  getUserManager,
  loadStoredUser,
  needsRenewal,
  resetUserManager,
} from '@/services/keycloak';

describe('keycloak service', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    resetUserManager();
    mockInstance.removeUser.mockResolvedValue(undefined);
  });

  it('creates a singleton UserManager configured with localStorage userStore and sessionStorage stateStore', () => {
    const first = getUserManager();
    const second = getUserManager();

    expect(first).toBe(second);
    expect(mockInstance.settings).toMatchObject({
      authority: 'https://dev.loginproxy.gov.bc.ca/auth/realms/standard',
      client_id: 'nr-silva',
      response_type: 'code',
      scope: 'openid profile email',
      automaticSilentRenew: false,
      loadUserInfo: false,
    });
    expect((mockInstance.settings.userStore as { opts: { store: Storage } }).opts.store).toBe(
      window.localStorage
    );
    expect((mockInstance.settings.stateStore as { opts: { store: Storage } }).opts.store).toBe(
      window.sessionStorage
    );
    expect(String(mockInstance.settings.redirect_uri)).toMatch(/\/authCallback$/);
    expect(String(mockInstance.settings.post_logout_redirect_uri)).toBe(
      `${window.location.origin}/`
    );
  });

  it('needsRenewal returns true when user is missing, expired, or near expiry', () => {
    expect(needsRenewal(null)).toBe(true);
    expect(needsRenewal({ access_token: 'tok', expired: true, expires_in: 120 } as User)).toBe(
      true
    );
    expect(needsRenewal({ access_token: 'tok', expired: false, expires_in: 45 } as User)).toBe(
      true
    );
    expect(needsRenewal({ access_token: 'tok', expired: false, expires_in: 180 } as User)).toBe(
      false
    );
  });

  it('loadStoredUser returns valid non-expired users and null otherwise', async () => {
    const validUser = { access_token: 'tok', expired: false, expires_in: 200 } as User;
    mockInstance.getUser.mockResolvedValueOnce(validUser);
    expect(await loadStoredUser()).toBe(validUser);

    mockInstance.getUser.mockResolvedValueOnce({
      access_token: 'tok',
      expired: true,
    } as User);
    expect(await loadStoredUser()).toBeNull();
  });

  it('ensureFreshUser skips renewal for fresh users and deduplicates concurrent renewals', async () => {
    const freshUser = { access_token: 'fresh', expired: false, expires_in: 200 } as User;
    mockInstance.getUser.mockResolvedValueOnce(freshUser);
    expect(await ensureFreshUser()).toBe(freshUser);
    expect(mockInstance.signinSilent).not.toHaveBeenCalled();

    const expiringUser = { access_token: 'old', expired: false, expires_in: 15 } as User;
    const renewedUser = { access_token: 'renewed', expired: false, expires_in: 300 } as User;
    mockInstance.getUser.mockResolvedValue(expiringUser);
    mockInstance.signinSilent.mockResolvedValueOnce(renewedUser);

    const [r1, r2] = await Promise.all([ensureFreshUser(), forceRenew()]);
    expect(r1).toBe(renewedUser);
    expect(r2).toBe(renewedUser);
    expect(mockInstance.signinSilent).toHaveBeenCalledTimes(1);
  });

  it('forceRenew removes stored user and returns null when signinSilent fails', async () => {
    mockInstance.signinSilent.mockRejectedValueOnce(new Error('Refresh expired'));

    const result = await forceRenew();

    expect(result).toBeNull();
    expect(mockInstance.removeUser).toHaveBeenCalledTimes(1);
  });

  it('coordinates renewal via navigator.locks and avoids wiping a newer session saved by another tab', async () => {
    const lockRequest = vi.fn(
      async (_name: string, cb: () => Promise<unknown>) => cb()
    );
    const originalLocks = navigator.locks;
    Object.defineProperty(navigator, 'locks', {
      configurable: true,
      value: { request: lockRequest },
    });

    try {
      const freshFromOtherTab = {
        access_token: 'other-tab-fresh',
        expired: false,
        expires_in: 300,
      } as User;
      mockInstance.getUser.mockResolvedValueOnce(freshFromOtherTab);

      const skippedResult = await forceRenew();
      expect(lockRequest).toHaveBeenCalledWith('silva_oidc_renewal', expect.any(Function));
      expect(mockInstance.signinSilent).not.toHaveBeenCalled();
      expect(skippedResult).toBe(freshFromOtherTab);

      // When renewal runs for a rejected token and fails after another tab already updated storage,
      // preserve the newer session instead of calling removeUser().
      const rejectedUser = {
        access_token: 'rejected-tok',
        expired: false,
        expires_in: 240,
      } as User;
      mockInstance.getUser
        .mockResolvedValueOnce(rejectedUser)
        .mockResolvedValueOnce(freshFromOtherTab);
      mockInstance.signinSilent.mockRejectedValueOnce(new Error('Rotated token already spent'));

      const recovered = await forceRenew('rejected-tok');
      expect(mockInstance.signinSilent).toHaveBeenCalledTimes(1);
      expect(mockInstance.removeUser).not.toHaveBeenCalled();
      expect(recovered).toBe(freshFromOtherTab);
    } finally {
      Object.defineProperty(navigator, 'locks', {
        configurable: true,
        value: originalLocks,
      });
    }
  });
});
