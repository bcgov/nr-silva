import { RefObject, useEffect } from "react";
import { scrollToTarget } from "@/utils/ScrollUtils";

const useScrollToSearchResults = (
  resultsRef: RefObject<HTMLDivElement | null>,
  emptyRef: RefObject<HTMLDivElement | null>,
  shouldScrollRef: RefObject<boolean>,
  isLoading: boolean,
  data: unknown,
  totalElements: number | undefined,
) => {
  useEffect(() => {
    if (!isLoading && data && shouldScrollRef.current) {
      shouldScrollRef.current = false;
      const target = (totalElements ?? 0) > 0 ? resultsRef.current : emptyRef.current;
      scrollToTarget(target);
    }
  }, [isLoading, data]);
};

export default useScrollToSearchResults;
