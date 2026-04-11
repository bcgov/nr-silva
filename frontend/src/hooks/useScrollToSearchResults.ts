import { RefObject, useEffect } from "react";

const SCROLL_OFFSET_PX = 48;

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
      if (target) {
        window.scrollTo({
          top: target.getBoundingClientRect().top + window.scrollY - SCROLL_OFFSET_PX,
          behavior: 'smooth',
        });
      }
    }
  }, [isLoading, data]);
};

export default useScrollToSearchResults;
