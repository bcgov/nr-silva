import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import useDeepLinkScroll from '@/hooks/useDeepLinkScroll';
import * as scrollUtils from '@/utils/ScrollUtils';

describe('useDeepLinkScroll', () => {
  let scrollToTargetSpy: ReturnType<typeof vi.spyOn>;
  let scrollToSpy: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    vi.useFakeTimers();
    scrollToTargetSpy = vi.spyOn(scrollUtils, 'scrollToTarget').mockImplementation(() => {});
    scrollToSpy = vi.fn();
    window.scrollTo = scrollToSpy;
    document.body.innerHTML = '';
  });

  afterEach(() => {
    vi.clearAllTimers();
    vi.useRealTimers();
    vi.restoreAllMocks();
    document.body.innerHTML = '';
  });

  it('does nothing if elementId is null', () => {
    renderHook(() => useDeepLinkScroll(null, true));
    expect(scrollToTargetSpy).not.toHaveBeenCalled();
  });

  it('does nothing if isReady is false', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    document.body.appendChild(el);

    renderHook(() => useDeepLinkScroll('target-el', false));
    expect(scrollToTargetSpy).not.toHaveBeenCalled();
  });

  it('scrolls to element when isReady is true and element exists', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    el.getBoundingClientRect = () => ({ top: 120 } as any);
    document.body.appendChild(el);

    renderHook(() => useDeepLinkScroll('target-el', true));
    expect(scrollToTargetSpy).toHaveBeenCalledWith(el, { highlight: true });
  });

  it('polls layout drift and corrects scroll position', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    // SCROLL_OFFSET_PX is 48, drift threshold is 30.
    // If top is 200, |200 - 48| = 152 > 30, so window.scrollTo should be called.
    el.getBoundingClientRect = () => ({ top: 200 } as any);
    document.body.appendChild(el);

    renderHook(() => useDeepLinkScroll('target-el', true));
    expect(scrollToTargetSpy).toHaveBeenCalled();

    act(() => {
      vi.advanceTimersByTime(160); // LAYOUT_POLL_MS is 150
    });

    expect(scrollToSpy).toHaveBeenCalled();
  });

  it('stops layout watch when user scrolls via wheel or touch', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    el.getBoundingClientRect = () => ({ top: 200 } as any);
    document.body.appendChild(el);

    renderHook(() => useDeepLinkScroll('target-el', true));

    // Simulate user wheel event
    window.dispatchEvent(new Event('wheel'));

    scrollToSpy.mockClear();
    act(() => {
      vi.advanceTimersByTime(300);
    });

    expect(scrollToSpy).not.toHaveBeenCalled();
  });

  it('stops layout watch on touchstart', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    el.getBoundingClientRect = () => ({ top: 200 } as any);
    document.body.appendChild(el);

    renderHook(() => useDeepLinkScroll('target-el', true));

    // Simulate user touchstart event
    window.dispatchEvent(new Event('touchstart'));

    scrollToSpy.mockClear();
    act(() => {
      vi.advanceTimersByTime(300);
    });

    expect(scrollToSpy).not.toHaveBeenCalled();
  });

  it('uses MutationObserver when element is not immediately in DOM', () => {
    let observerCallback: (() => void) | null = null;
    const disconnectMock = vi.fn();
    class MockObserver {
      constructor(cb: any) {
        observerCallback = cb;
      }
      observe = vi.fn();
      disconnect = disconnectMock;
      takeRecords = vi.fn();
    }
    const observerSpy = vi.spyOn(window, 'MutationObserver').mockImplementation(MockObserver as any);

    renderHook(() => useDeepLinkScroll('delayed-el', true));
    expect(scrollToTargetSpy).not.toHaveBeenCalled();

    const el = document.createElement('div');
    el.id = 'delayed-el';
    el.getBoundingClientRect = () => ({ top: 120 } as any);
    document.body.appendChild(el);

    act(() => {
      observerCallback?.();
    });

    expect(scrollToTargetSpy).toHaveBeenCalledWith(el, { highlight: true });
    expect(disconnectMock).toHaveBeenCalled();
    observerSpy.mockRestore();
  });

  it('re-starts layout watch if already scrolled on re-render/Strict-Mode remount', () => {
    const el = document.createElement('div');
    el.id = 'target-el';
    el.getBoundingClientRect = () => ({ top: 200 } as any);
    document.body.appendChild(el);

    const { rerender } = renderHook(
      ({ id, ready }) => useDeepLinkScroll(id, ready),
      { initialProps: { id: 'target-el', ready: true } }
    );

    expect(scrollToTargetSpy).toHaveBeenCalledTimes(1);

    // Simulate dependency change (or StrictMode remount) after initial scroll has already fired
    rerender({ id: 'target-el', ready: false });
    rerender({ id: 'target-el', ready: true });

    // Initial scrollToTarget should not be called again because hasScrolled.current is true
    expect(scrollToTargetSpy).toHaveBeenCalledTimes(1);

    scrollToSpy.mockClear();
    // Advance timers by LAYOUT_POLL_MS (150ms) to ensure startLayoutWatch is running
    act(() => {
      vi.advanceTimersByTime(160);
    });

    expect(scrollToSpy).toHaveBeenCalled();
  });
});
