import { RefObject, useEffect } from "react";

const SCROLL_OFFSET_PX = 48;

const useScrollToSearchResults = (
  resultsRef: RefObject<HTMLDivElement | null>,
  shouldScrollRef: RefObject<boolean>,
  isLoading: boolean,
  data: unknown,
) => {
  useEffect(() => {
    if (!isLoading && data && shouldScrollRef.current) {
      shouldScrollRef.current = false;
      if (resultsRef.current) {
        window.scrollTo({
          top: resultsRef.current.getBoundingClientRect().top + window.scrollY - SCROLL_OFFSET_PX,
          behavior: 'smooth',
        });
      }
    }
  }, [isLoading, data]);
};

export default useScrollToSearchResults;
