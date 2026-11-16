/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  MutationCache,
  QueryCache,
  type QueryClientConfig,
  type Query,
  type Mutation
} from "@tanstack/react-query";
import { deleteCookie, getCookie, setCookie } from "@/utils/CookieUtils";
import { THREE_HOURS } from "@/constants/TimeUnits";
import { forceRenew } from "@/services/keycloak";
import { ACCESS_TOKEN_KEY } from ".";

// === Refresh Token Management ===

let isRefreshing = false;
let isRedirecting = false;
const authRefreshListeners = new Set<() => void>();
const retriedTargets = new WeakSet<object>();

const triggerAuthRedirect = () => {
  deleteCookie(ACCESS_TOKEN_KEY);

  if (!isRedirecting) {
    isRedirecting = true;
    window.location.href = "/";
  }
};

const setAuthRefreshInProgress = (refreshing: boolean) => {
  if (isRefreshing === refreshing) {
    return;
  }

  isRefreshing = refreshing;
  authRefreshListeners.forEach((listener) => listener());
};

export const isAuthRefreshInProgress = () => isRefreshing;
export const subscribeAuthRefresh = (listener: () => void) => {
  authRefreshListeners.add(listener);
  return () => authRefreshListeners.delete(listener);
};

const failedQueue: {
  query?: Query<unknown, unknown, unknown, readonly unknown[]>;
  mutation?: Mutation<unknown, unknown, unknown, unknown>;
  variables?: unknown;
}[] = [];

async function refreshAccessToken(): Promise<string | null> {
  const rejectedToken = getCookie(ACCESS_TOKEN_KEY) ?? undefined;
  try {
    const freshUser = await forceRenew(rejectedToken);
    const newAccess = freshUser?.access_token ?? null;

    if (newAccess && newAccess !== rejectedToken) {
      setCookie(ACCESS_TOKEN_KEY, newAccess);
      return newAccess;
    }

    throw new Error("No fresh token returned from Keycloak session");
  } catch (err) {
    console.error("Token refresh failed:", err);
    triggerAuthRedirect();

    return null;
  }
}

function processFailedQueue() {
  failedQueue.forEach(({ query, mutation, variables }) => {
    if (mutation) {
      retriedTargets.add(mutation);
      void mutation.execute(variables); // this safely retries the mutation
    }
    if (query) {
      retriedTargets.add(query);
      void query.fetch(undefined, { cancelRefetch: true }); // force a fresh refetch after token refresh
    }
  });

  failedQueue.length = 0;
  setAuthRefreshInProgress(false);
}

function refreshTokenAndRetry(
  query?: Query<unknown, unknown, unknown, readonly unknown[]>,
  mutation?: Mutation<unknown, unknown, unknown, unknown>,
  variables?: unknown
) {
  const target: object | undefined = query ?? mutation;
  if (target && retriedTargets.has(target)) {
    failedQueue.length = 0;
    setAuthRefreshInProgress(false);
    triggerAuthRedirect();
    return;
  }

  failedQueue.push({ query, mutation, variables });

  if (!isRefreshing) {
    setAuthRefreshInProgress(true);

    void refreshAccessToken()
      .then((token) => {
        if (token) {
          processFailedQueue();
        } else {
          failedQueue.length = 0;
          setAuthRefreshInProgress(false);
        }
      })
      .catch(() => {
        failedQueue.length = 0;
        setAuthRefreshInProgress(false);
      });
  }
}

function errorHandler(
  error: unknown,
  query?: Query<unknown, unknown, unknown, readonly unknown[]>,
  mutation?: Mutation<unknown, unknown, unknown, unknown>,
  variables?: unknown
) {
  const maybeAxiosError = error as any;

  const statusCode =
    maybeAxiosError?.status ??
    maybeAxiosError?.response?.status ??
    maybeAxiosError?.response?.statusCode ??
    0;
  if (statusCode === 401) {
    // Prevent the query from remaining in errored state while we attempt token refresh.
    try {
      if (query) {
        // clear query error and keep the query pending until retry.
        // Do not force fetchStatus here; that can interfere with query.fetch().
        (query as any).setState?.({
          error: null,
          status: 'pending'
        });
      }
    } catch {
      // ignore if we can't touch internal state
    }

    refreshTokenAndRetry(query, mutation, variables);
  }
}

function queryErrorHandler(
  error: unknown,
  query: Query<unknown, unknown, unknown, readonly unknown[]>
) {
  errorHandler(error, query);
}

function mutationErrorHandler(
  error: unknown,
  variables: unknown,
  _context: unknown,
  mutation: Mutation<unknown, unknown, unknown, unknown>
) {
  errorHandler(error, undefined, mutation, variables);
}

// === Query Client Configuration ===
// 401 needs to be included here to prevent multiple retries on auth failure
const HTTP_STATUS_TO_NOT_RETRY = [400, 401, 403, 404, 409];
const MAX_RETRIES = 3;

export const queryClientConfig: QueryClientConfig = {
  defaultOptions: {
    queries: {
      refetchOnMount: false,
      refetchOnWindowFocus: false,
      staleTime: THREE_HOURS,
      gcTime: THREE_HOURS,
      retry: (failureCount, error) => {
        const statusCode =
          (error as any)?.status ??
          (error as any)?.response?.status ??
          0;

        if (failureCount > MAX_RETRIES) return false;
        if (HTTP_STATUS_TO_NOT_RETRY.includes(statusCode)) return false;

        return true;
      }
    }
  },
  queryCache: new QueryCache({
    onError: queryErrorHandler,
    onSuccess: (_data, query) => {
      retriedTargets.delete(query);
    },
  }),
  mutationCache: new MutationCache({
    onError: mutationErrorHandler,
    onSuccess: (_data, _variables, _context, mutation) => {
      retriedTargets.delete(mutation);
    },
  })
};

