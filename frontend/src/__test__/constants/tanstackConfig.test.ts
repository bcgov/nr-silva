import type { User } from 'oidc-client-ts';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ACCESS_TOKEN_KEY } from '@/constants';
import {
  isAuthRefreshInProgress,
  queryClientConfig,
  subscribeAuthRefresh,
} from '@/constants/tanstackConfig';
import { ensureFreshUser } from '@/services/keycloak';
import { deleteCookie, setCookie } from '@/utils/CookieUtils';

vi.mock('@/services/keycloak', () => ({
  ensureFreshUser: vi.fn(),
}));

vi.mock('@/utils/CookieUtils', () => ({
  setCookie: vi.fn(),
  deleteCookie: vi.fn(),
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

      expect(ensureFreshUser).not.toHaveBeenCalled();
      expect(mockQuery.fetch).not.toHaveBeenCalled();
    });

    it('handles 401 error on queryCache: refreshes token via Keycloak and refetches query', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(ensureFreshUser).mockResolvedValueOnce({
        access_token: 'new-access-token-123',
      } as User);

      queryErrorHandler({ status: 401 }, mockQuery);

      expect(mockQuery.setState).toHaveBeenCalledWith({
        error: null,
        status: 'pending',
      });

      await vi.waitFor(() => {
        expect(setCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY, 'new-access-token-123');
        expect(mockQuery.fetch).toHaveBeenCalledWith(undefined, { cancelRefetch: true });
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles 401 error on mutationCache: refreshes token and retries mutation', async () => {
      const mutationErrorHandler = (
        queryClientConfig.mutationCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockMutation = {
        execute: vi.fn(),
      };
      const variables = { id: 101, name: 'Test' };

      vi.mocked(ensureFreshUser).mockResolvedValueOnce({
        access_token: 'mutation-token-456',
      } as User);

      mutationErrorHandler({ response: { status: 401 } }, variables, {}, mockMutation);

      await vi.waitFor(() => {
        expect(setCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY, 'mutation-token-456');
        expect(mockMutation.execute).toHaveBeenCalledWith(variables);
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when ensureFreshUser throws', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(ensureFreshUser).mockRejectedValueOnce(new Error('Keycloak session expired'));

      queryErrorHandler({ response: { statusCode: 401 } }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(mockQuery.fetch).not.toHaveBeenCalled();
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when ensureFreshUser returns null', async () => {
      const queryErrorHandler = (
        queryClientConfig.queryCache as unknown as { config: { onError: CacheErrorHandler } }
      ).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);

      queryErrorHandler({ status: 401 }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(isAuthRefreshInProgress()).toBe(false);
      });
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

      vi.mocked(ensureFreshUser).mockResolvedValueOnce({
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

      vi.mocked(ensureFreshUser).mockImplementationOnce(() => sessionPromise);

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
