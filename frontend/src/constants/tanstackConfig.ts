import { QueryClientConfig } from "@tanstack/react-query";
import { THREE_HOURS } from "./TimeUnits";
import { isAxiosError } from "axios";

const HTTP_STATUS_TO_NOT_RETRY = [400, 401, 403, 404];
const MAX_RETRIES = 3;

export const queryClientConfig: QueryClientConfig = {
  defaultOptions: {
    queries: {
      refetchOnMount: false, // Default is caching fetched values
      refetchOnWindowFocus: false,
      staleTime: THREE_HOURS,
      gcTime: THREE_HOURS,
      // Do not retry on errors defined above
      retry: (failureCount, error) => {
        if (failureCount > MAX_RETRIES) {
          return false;
        }
        if (
          isAxiosError(error)
          && HTTP_STATUS_TO_NOT_RETRY.includes(error.response?.status ?? 0)
        ) {
          return false;
        }
        return true;
      }
    }
  }
}
