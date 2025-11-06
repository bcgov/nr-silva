/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  MutationCache,
  QueryCache,
  type QueryClientConfig,
  type Query,
  type Mutation
} from "@tanstack/react-query";
import { setCookie, deleteCookie } from "@/utils/CookieUtils";
import { THREE_HOURS } from "@/constants/TimeUnits";
import { env } from "@/env";
import { ACCESS_TOKEN_KEY } from ".";
import { fetchAuthSession } from "aws-amplify/auth";
import { JWT } from "@/types/amplify";
import { SELECTED_CLIENT_KEY } from "@/constants";
import { parseToken } from "@/services/AuthService";

// === Refresh Token Management ===

let isRefreshing = false;
let isRedirecting = false;

const failedQueue: {
  query?: Query<unknown, unknown, unknown, readonly unknown[]>;
  mutation?: Mutation<unknown, unknown, unknown, unknown>;
  variables?: unknown;
}[] = [];

async function refreshAccessToken(): Promise<string | null> {
  try {
    // Use aws-amplify to refresh tokens via the existing session
    const session = await fetchAuthSession();
    const tokens = (session.tokens ?? {}) as { idToken?: string; accessToken?: string; refreshToken?: string };

    const newAccess = tokens.accessToken?.toString() ?? tokens.idToken?.toString() ?? null;

    if (newAccess) {
      setCookie(ACCESS_TOKEN_KEY, newAccess);
      // Attempt to restore selected client using parseToken (same logic as AuthProvider)
      try {
        // Prefer idToken for parsing, else derive JWT-like object from access token
        let idTokenObj: JWT | undefined;
        if (tokens.idToken) {
          idTokenObj = tokens.idToken as unknown as JWT;
        } else if (newAccess) {
          try {
            const payloadBase64 = newAccess.split('.')[1];
            if (payloadBase64) {
              const payloadJson = atob(payloadBase64);
              idTokenObj = { payload: JSON.parse(payloadJson) } as unknown as JWT;
            } else {
              idTokenObj = undefined;
            }
          } catch (parseErr) {
            console.warn("Failed to manually parse JWT payload from access token", parseErr);
            idTokenObj = undefined;
          }
        } else {
          idTokenObj = undefined;
        }
        const parsed = parseToken(idTokenObj as JWT | undefined);
        const associatedClients: string[] = parsed?.associatedClients ?? [];
        const storedClientId = localStorage.getItem(SELECTED_CLIENT_KEY);

        if (storedClientId) {
          if (!associatedClients.includes(storedClientId)) {
            localStorage.removeItem(SELECTED_CLIENT_KEY);
          }
        } else if (associatedClients.length === 1) {
          const singleClientId = associatedClients[0]!;
          localStorage.setItem(SELECTED_CLIENT_KEY, singleClientId);
        }
      } catch (err) {
        // non-fatal if parsing fails
        console.warn("Failed to parse refreshed token for client selection", err);
      }

      return newAccess;
    }

    throw new Error("No token returned from amplify session");
  } catch (err) {
    console.error("Token refresh failed:", err);
    deleteCookie(ACCESS_TOKEN_KEY);

    if (!isRedirecting) {
      isRedirecting = true;
      window.location.href = "/";
    }

    return null;
  }
}

function processFailedQueue() {
  failedQueue.forEach(({ query, mutation, variables }) => {
    if (mutation) {
      mutation.execute(variables);  // this safely retries the mutation
    }
    if (query) {
      query.fetch();       // triggers query refetch
    }
  });

  failedQueue.length = 0;
  isRefreshing = false;
}

function refreshTokenAndRetry(
  query?: Query<unknown, unknown, unknown, readonly unknown[]>,
  mutation?: Mutation<unknown, unknown, unknown, unknown>,
  variables?: unknown
) {
  failedQueue.push({ query, mutation, variables });

  if (!isRefreshing) {
    isRefreshing = true;

    refreshAccessToken().then((token) => {
      if (token) {
        processFailedQueue();
      } else {
        failedQueue.length = 0;
        isRefreshing = false;
      }
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
        // clear query error and mark as loading/fetching so UI shows a retry in-progress
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        (query as any).setState?.((oldState: any) => ({
          ...oldState,
          error: null,
          status: 'loading',
          fetchStatus: 'fetching'
        }));
      }
    } catch (e) {
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

const HTTP_STATUS_TO_NOT_RETRY = [400, 403, 404, 409];
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
    onError: queryErrorHandler
  }),
  mutationCache: new MutationCache({
    onError: mutationErrorHandler
  })
};
