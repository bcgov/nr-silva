import type { User } from 'oidc-client-ts';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ACCESS_TOKEN_KEY } from '@/constants';
import {
  isAuthRefreshInProgress,
  queryClientConfig,
  subscribeAuthRefresh,
} from '@/constants/tanstackConfig';
import { forceRenew } from '@/services/keycloak';
import { deleteCookie, getCookie, setCookie } from '@/utils/CookieUtils';

vi.mock('@/services/keycloak', () => ({
  forceRenew: vi.fn(),
}));

vi.mock('@/utils/CookieUtils', () => ({
  setCookie: vi.fn(),
  deleteCookie: vi.fn(),
  getCookie: vi.fn(() => 'expired-token'),
}));

type CacheErrorHandler = (...args: unknown[]) => void;

describe('tanstackConfig', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    window.history.replaceState({}, '', '/');
  });

  describe('isAuthRefreshInProgress and subscribeAuthRefresh', () => {
    it('returns false initially and allows subscription and unsubscription', () => {
      expect(isAuthRefreshInProgress()).toBe(false);

      const listener = vi.fn();
      const unsubscribe = subscribeAuthRefresh(listener);

      expect(typeof unsubscribe).toBe('function');
      unsubscribe();
    });
  });

  describe('queryClientConfig.defaultOptions.queries.retry', () => {
    const retryFn = queryClientConfig.defaultOptions?.queries?.retry as (
      failureCount: number,
      error: unknown
    ) => boolean;

    it('returns false when failureCount exceeds MAX_RETRIES (3)', () => {
      expect(retryFn(4, new Error('error'))).toBe(false);
    });

    it.each([400, 401, 403, 404, 409])('returns false when statusCode is %i', (status) => {
      expect(retryFn(1, { status })).toBe(false);
      expect(retryFn(1, { response: { status } })).toBe(false);
    });

    it('returns true when failureCount <= 3 and statusCode is retryable', () => {
      expect(retryFn(1, { status: 500 })).toBe(true);
      expect(retryFn(3, { status: 502 })).toBe(true);
      expect(retryFn(2, new Error('Network error'))).toBe(true);
    });
  });

  describe('queryCache and mutationCache error handling with 401', () => {
    it('ignores non-401 errors on queryCache', () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      queryErrorHandler({ status: 500 }, mockQuery);

      expect(forceRenew).not.toHaveBeenCalled();
      expect(mockQuery.fetch).not.toHaveBeenCalled();
    });

    it('handles 401 error on queryCache: forces renewal and refetches query', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(forceRenew).mockResolvedValueOnce({
        access_token: 'new-access-token-123',
      } as User);

      queryErrorHandler({ status: 401 }, mockQuery);

      expect(mockQuery.setState).toHaveBeenCalledWith({
        error: null,
        status: 'pending',
      });

      await vi.waitFor(() => {
        expect(forceRenew).toHaveBeenCalledWith('expired-token');
        expect(setCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY, 'new-access-token-123');
        expect(mockQuery.fetch).toHaveBeenCalledWith(undefined, { cancelRefetch: true });
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles 401 error on mutationCache: forces renewal and retries mutation', async () => {
      const mutationErrorHandler = (
        queryClientConfig.mutationCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockMutation = {
        execute: vi.fn(),
      };
      const variables = { id: 101, name: 'Test' };

      vi.mocked(forceRenew).mockResolvedValueOnce({
        access_token: 'mutation-token-456',
      } as User);

      mutationErrorHandler({ response: { status: 401 } }, variables, {}, mockMutation);

      await vi.waitFor(() => {
        expect(setCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY, 'mutation-token-456');
        expect(mockMutation.execute).toHaveBeenCalledWith(variables);
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when forceRenew throws', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(forceRenew).mockRejectedValueOnce(new Error('Keycloak session expired'));

      queryErrorHandler({ response: { statusCode: 401 } }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(mockQuery.fetch).not.toHaveBeenCalled();
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when forceRenew returns null or the same rejected token', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(forceRenew).mockResolvedValueOnce(null);

      queryErrorHandler({ status: 401 }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(isAuthRefreshInProgress()).toBe(false);
      });

      vi.clearAllMocks();
      vi.mocked(getCookie).mockReturnValue('stale-token');
      vi.mocked(forceRenew).mockResolvedValueOnce({ access_token: 'stale-token' } as User);
      const mockQuery2 = { fetch: vi.fn(), setState: vi.fn() };

      queryErrorHandler({ status: 401 }, mockQuery2);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(mockQuery2.fetch).not.toHaveBeenCalled();
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('stops retrying when a replayed query receives another 401 and resets after success', async () => {
      const cacheConfig = (
        queryClientConfig.queryCache as unknown as {
          config: { onError: CacheErrorHandler; onSuccess: CacheErrorHandler };
        }
      ).config;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(getCookie).mockReturnValue('initial-token');
      vi.mocked(forceRenew).mockResolvedValueOnce({ access_token: 'fresh-token-1' } as User);

      cacheConfig.onError({ status: 401 }, mockQuery);
      await vi.waitFor(() => expect(mockQuery.fetch).toHaveBeenCalledTimes(1));

      // Second consecutive 401 on the already-retried query aborts without renewing again.
      vi.mocked(forceRenew).mockClear();
      vi.mocked(deleteCookie).mockClear();
      cacheConfig.onError({ status: 401 }, mockQuery);

      expect(forceRenew).not.toHaveBeenCalled();
      expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
      expect(mockQuery.fetch).toHaveBeenCalledTimes(1);

      // Once a fetch succeeds, future 401 recovery is re-enabled for that query.
      cacheConfig.onSuccess({}, mockQuery);
      vi.mocked(getCookie).mockReturnValue('fresh-token-1');
      vi.mocked(forceRenew).mockResolvedValueOnce({ access_token: 'fresh-token-2' } as User);

      cacheConfig.onError({ status: 401 }, mockQuery);
      await vi.waitFor(() => expect(mockQuery.fetch).toHaveBeenCalledTimes(2));
    });

    it('notifies subscribers when auth refresh state transitions', async () => {
      const listener = vi.fn();
      const unsubscribe = subscribeAuthRefresh(listener);

      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(forceRenew).mockResolvedValueOnce({
        access_token: 'token',
      } as User);

      queryErrorHandler({ status: 401 }, mockQuery);

      await vi.waitFor(() => {
        expect(listener).toHaveBeenCalled();
      });

      unsubscribe();
    });

    it('queues multiple queries/mutations while a refresh is in progress', async () => {
      let resolveSession: (value: User | null) => void;
      const sessionPromise = new Promise<User | null>((resolve) => {
        resolveSession = resolve;
      });

      vi.mocked(forceRenew).mockImplementationOnce(() => sessionPromise);

      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mutationErrorHandler = (
        queryClientConfig.mutationCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;

      const query1 = { fetch: vi.fn(), setState: vi.fn() };
      const query2 = { fetch: vi.fn(), setState: vi.fn() };
      const mutation1 = { execute: vi.fn() };

      queryErrorHandler({ status: 401 }, query1);
      expect(isAuthRefreshInProgress()).toBe(true);

      queryErrorHandler({ status: 401 }, query2);
      mutationErrorHandler({ status: 401 }, 'vars', {}, mutation1);

      resolveSession!({ access_token: 'multi-token' } as User);

      await vi.waitFor(() => {
        expect(query1.fetch).toHaveBeenCalled();
        expect(query2.fetch).toHaveBeenCalled();
        expect(mutation1.execute).toHaveBeenCalledWith('vars');
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });
  });
});
