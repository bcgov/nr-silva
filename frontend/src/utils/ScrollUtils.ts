const SCROLL_OFFSET_PX = 48;
const HIGHLIGHT_CLASS = "default-deep-link-highlight";

export type ScrollToTargetOptions = {
  offset?: number;
  highlight?: boolean;
};

export const scrollToTarget = (
  element: HTMLElement | null,
  options?: ScrollToTargetOptions
): void => {
  if (!element) return;

  const offset = options?.offset ?? SCROLL_OFFSET_PX;

  window.scrollTo({
    top: element.getBoundingClientRect().top + window.scrollY - offset,
    behavior: "smooth",
  });

  if (options?.highlight) {
    element.classList.add(HIGHLIGHT_CLASS);
    element.addEventListener(
      "animationend",
      () => element.classList.remove(HIGHLIGHT_CLASS),
      { once: true }
    );
  }
};
