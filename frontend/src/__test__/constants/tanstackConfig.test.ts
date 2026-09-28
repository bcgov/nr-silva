import { describe, it, expect, vi, beforeEach } from 'vitest';
import { fetchAuthSession } from 'aws-amplify/auth';
import { setCookie, deleteCookie } from '@/utils/CookieUtils';
import { ACCESS_TOKEN_KEY } from '@/constants';
import {
  queryClientConfig,
  isAuthRefreshInProgress,
  subscribeAuthRefresh,
  authRedirectBoundary,
  resetAuthRedirectStateForTesting,
} from '@/constants/tanstackConfig';

vi.mock('aws-amplify/auth', () => ({
  fetchAuthSession: vi.fn(),
}));

vi.mock('@/utils/CookieUtils', () => ({
  setCookie: vi.fn(),
  deleteCookie: vi.fn(),
}));

describe('tanstackConfig', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    window.history.replaceState({}, '', '/');
    resetAuthRedirectStateForTesting();
    vi.spyOn(authRedirectBoundary, 'redirect').mockImplementation(() => {});
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
      const errorWithStatus = { status };
      const errorWithResponseStatus = { response: { status } };

      expect(retryFn(1, errorWithStatus)).toBe(false);
      expect(retryFn(1, errorWithResponseStatus)).toBe(false);
    });

    it('returns true when failureCount <= 3 and statusCode is retryable', () => {
      expect(retryFn(1, { status: 500 })).toBe(true);
      expect(retryFn(3, { status: 502 })).toBe(true);
      expect(retryFn(2, new Error('Network error'))).toBe(true);
    });
  });

  describe('queryCache and mutationCache error handling with 401', () => {
    it('ignores non-401 errors on queryCache', () => {
      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      queryErrorHandler({ status: 500 }, mockQuery);

      expect(fetchAuthSession).not.toHaveBeenCalled();
      expect(mockQuery.fetch).not.toHaveBeenCalled();
    });

    it('handles 401 error on queryCache: refreshes token and refetches query', async () => {
      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(fetchAuthSession).mockResolvedValueOnce({
        tokens: {
          accessToken: { toString: () => 'new-access-token-123' },
        },
      } as any);

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
      const mutationErrorHandler = (queryClientConfig.mutationCache as any).config.onError;
      const mockMutation = {
        execute: vi.fn(),
      };
      const variables = { id: 101, name: 'Test' };

      vi.mocked(fetchAuthSession).mockResolvedValueOnce({
        tokens: {
          accessToken: { toString: () => 'mutation-token-456' },
        },
      } as any);

      mutationErrorHandler({ response: { status: 401 } }, variables, {}, mockMutation);

      await vi.waitFor(() => {
        expect(setCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY, 'mutation-token-456');
        expect(mockMutation.execute).toHaveBeenCalledWith(variables);
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when fetchAuthSession returns no token or throws', async () => {
      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(fetchAuthSession).mockRejectedValueOnce(new Error('Amplify session expired'));

      queryErrorHandler({ response: { statusCode: 401 } }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(authRedirectBoundary.redirect).toHaveBeenCalled();
        expect(window.location.pathname).toBe('/');
        expect(mockQuery.fetch).not.toHaveBeenCalled();
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('handles refresh failure when tokens object has no accessToken', async () => {
      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(fetchAuthSession).mockResolvedValueOnce({
        tokens: {},
      } as any);

      queryErrorHandler({ status: 401 }, mockQuery);

      await vi.waitFor(() => {
        expect(deleteCookie).toHaveBeenCalledWith(ACCESS_TOKEN_KEY);
        expect(authRedirectBoundary.redirect).toHaveBeenCalled();
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });

    it('notifies subscribers when auth refresh state transitions', async () => {
      const listener = vi.fn();
      const unsubscribe = subscribeAuthRefresh(listener);

      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mockQuery = {
        fetch: vi.fn(),
        setState: vi.fn(),
      };

      vi.mocked(fetchAuthSession).mockResolvedValueOnce({
        tokens: { accessToken: { toString: () => 'token' } },
      } as any);

      queryErrorHandler({ status: 401 }, mockQuery);

      await vi.waitFor(() => {
        expect(listener).toHaveBeenCalled();
      });

      unsubscribe();
    });

    it('queues multiple queries/mutations while a refresh is in progress', async () => {
      let resolveSession: (value: any) => void;
      const sessionPromise = new Promise((resolve) => {
        resolveSession = resolve;
      });

      vi.mocked(fetchAuthSession).mockImplementationOnce(() => sessionPromise as any);

      const queryErrorHandler = (queryClientConfig.queryCache as any).config.onError;
      const mutationErrorHandler = (queryClientConfig.mutationCache as any).config.onError;

      const query1 = { fetch: vi.fn(), setState: vi.fn() };
      const query2 = { fetch: vi.fn(), setState: vi.fn() };
      const mutation1 = { execute: vi.fn() };

      // Trigger first 401
      queryErrorHandler({ status: 401 }, query1);
      expect(isAuthRefreshInProgress()).toBe(true);

      // Trigger second query 401 and mutation 401 while in progress
      queryErrorHandler({ status: 401 }, query2);
      mutationErrorHandler({ status: 401 }, 'vars', {}, mutation1);

      // Resolve session
      resolveSession!({
        tokens: { accessToken: { toString: () => 'multi-token' } },
      });

      await vi.waitFor(() => {
        expect(query1.fetch).toHaveBeenCalled();
        expect(query2.fetch).toHaveBeenCalled();
        expect(mutation1.execute).toHaveBeenCalledWith('vars');
        expect(isAuthRefreshInProgress()).toBe(false);
      });
    });
  });
});
