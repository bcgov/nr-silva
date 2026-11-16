import { User, UserManager, WebStorageStateStore } from 'oidc-client-ts';

import { env } from '@/env';

const TOKEN_RENEWAL_THRESHOLD_SECONDS = 60;

let userManager: UserManager | null = null;
let inFlightRenewal: Promise<User | null> | null = null;

/**
 * Returns the lazy-initialized {@link UserManager} singleton with `localStorage` for user sessions
 * (shared across tabs) and `sessionStorage` for per-tab OIDC transaction state.
 */
export const getUserManager = (): UserManager => {
  if (!userManager) {
    const origin = window.location.origin;

    userManager = new UserManager({
      authority: env.VITE_KEYCLOAK_URL ?? '',
      client_id: env.VITE_KEYCLOAK_CLIENT_ID ?? '',
      redirect_uri: `${origin}/authCallback`,
      post_logout_redirect_uri: `${origin}/`,
      response_type: 'code',
      scope: 'openid profile email',
      automaticSilentRenew: false,
      loadUserInfo: false,
      userStore: new WebStorageStateStore({ store: window.localStorage }),
      stateStore: new WebStorageStateStore({ store: window.sessionStorage }),
    });
  }

  return userManager;
};

/**
 * Resets cached singleton state. Intended for unit tests.
 */
export const resetUserManager = (): void => {
  userManager = null;
  inFlightRenewal = null;
};

/**
 * Determines whether the OIDC user's access token is missing, expired, or within the renewal window.
 */
export const needsRenewal = (
  user: User | null | undefined,
  thresholdSeconds: number = TOKEN_RENEWAL_THRESHOLD_SECONDS
): boolean => {
  if (!user || !user.access_token || user.expired) {
    return true;
  }

  return typeof user.expires_in === 'number' && user.expires_in <= thresholdSeconds;
};

/**
 * Loads a currently valid (non-expired) user from `localStorage` without performing network I/O.
 */
export const loadStoredUser = async (): Promise<User | null> => {
  const user = await getUserManager().getUser();
  if (!user || !user.access_token || user.expired) {
    return null;
  }
  return user;
};

/**
 * Forces a deduplicated silent token renewal using `UserManager.signinSilent()`.
 * Clears stale stored user state and returns `null` if renewal fails.
 */
export const forceRenew = async (): Promise<User | null> => {
  if (inFlightRenewal) {
    return inFlightRenewal;
  }

  const manager = getUserManager();
  inFlightRenewal = (async () => {
    try {
      const renewedUser = await manager.signinSilent();
      if (!renewedUser?.access_token) {
        await manager.removeUser().catch(() => undefined);
        return null;
      }
      return renewedUser;
    } catch {
      await manager.removeUser().catch(() => undefined);
      return null;
    } finally {
      inFlightRenewal = null;
    }
  })();

  return inFlightRenewal;
};

/**
 * Returns the stored user if still fresh, or silently renews the session if within the renewal
 * window. Returns `null` when no user session exists or renewal fails.
 */
export const ensureFreshUser = async (): Promise<User | null> => {
  const manager = getUserManager();
  const user = await manager.getUser();

  if (!user) {
    return null;
  }

  if (needsRenewal(user)) {
    return forceRenew();
  }

  return user;
};
