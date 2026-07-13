import { useEffect, useRef } from "react";
import { scrollToTarget } from "@/utils/ScrollUtils";

const SCROLL_OFFSET_PX = 48; // must match ScrollUtils.ts
const LAYOUT_WATCH_MS = 5000;
const LAYOUT_POLL_MS = 150;
const DRIFT_PX = 30;

const useDeepLinkScroll = (
  elementId: string | null,
  isReady: boolean
): void => {
  const hasScrolled = useRef(false);

  useEffect(() => {
    if (!isReady || !elementId) return;

    let intervalId: ReturnType<typeof setInterval> | null = null;
    let watchTimer: ReturnType<typeof setTimeout> | null = null;
    let onWheel: (() => void) | null = null;
    let onTouch: (() => void) | null = null;

    const stopWatching = () => {
      if (intervalId !== null) { clearInterval(intervalId); intervalId = null; }
      if (watchTimer !== null) { clearTimeout(watchTimer); watchTimer = null; }
      if (onWheel) { window.removeEventListener('wheel', onWheel); onWheel = null; }
      if (onTouch) { window.removeEventListener('touchstart', onTouch); onTouch = null; }
    };

    /**
     * After the initial scroll, poll every LAYOUT_POLL_MS for LAYOUT_WATCH_MS to
     * correct the scroll position if a layout shift (e.g. map loading, skeleton →
     * content transition) has pushed the target element away from the viewport.
     * Stops immediately when the user initiates a scroll (wheel / touch).
     */
    const startLayoutWatch = () => {
      onWheel = stopWatching;
      onTouch = stopWatching;
      window.addEventListener('wheel', onWheel, { passive: true });
      window.addEventListener('touchstart', onTouch, { passive: true });

      intervalId = setInterval(() => {
        const el = document.getElementById(elementId);
        if (!el) return;
        const elTop = el.getBoundingClientRect().top;
        if (Math.abs(elTop - SCROLL_OFFSET_PX) > DRIFT_PX) {
          window.scrollTo({
            top: elTop + window.scrollY - SCROLL_OFFSET_PX,
            behavior: 'instant' as ScrollBehavior,
          });
        }
      }, LAYOUT_POLL_MS);

      watchTimer = setTimeout(stopWatching, LAYOUT_WATCH_MS);
    };

    // React StrictMode double-invokes effects (setup→cleanup→setup) in development.
    // If the scroll already fired in the first setup, the cleanup killed the polling.
    // Re-start the layout drift watch here so corrections still happen after map/content loads.
    if (hasScrolled.current) {
      const el = document.getElementById(elementId);
      if (el) startLayoutWatch();
      return stopWatching;
    }

    const tryScroll = (): boolean => {
      const element = document.getElementById(elementId);
      if (!element) return false;
      hasScrolled.current = true;
      scrollToTarget(element, { highlight: true });
      startLayoutWatch();
      return true;
    };

    if (tryScroll()) {
      return stopWatching;
    }

    const observer = new MutationObserver(() => {
      if (tryScroll()) observer.disconnect();
    });

    observer.observe(document.body, { childList: true, subtree: true });
    return () => {
      observer.disconnect();
      stopWatching();
    };
  }, [isReady, elementId]);
};

export default useDeepLinkScroll;
