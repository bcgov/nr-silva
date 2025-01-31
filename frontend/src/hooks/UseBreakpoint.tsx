import { useEffect, useState } from 'react';
import { BreakpointType } from '../types/BreakpointType';

const getBreakpoint = (width: number): BreakpointType => {
  if (width >= 1584) return 'max';
  if (width >= 1312) return "xlg";
  if (width >= 1056) return "lg";
  if (width >= 672) return "md";
  return "sm";
};

/**
 * Custom React hook to track the current Carbon Design System breakpoint.
 * This hook listens for window resize events and updates the breakpoint dynamically.
 * @returns {string} The current Carbon breakpoint (`"max"`, `"xlg"`, `"lg"`, `"md"`, or `"sm"`).
 */
const useBreakpoint = () => {
  const [breakpoint, setBreakpoint] = useState(getBreakpoint(window.innerWidth));

  useEffect(() => {
    const updateBreakpoint = () => {
      setBreakpoint(getBreakpoint(window.innerWidth));
    };

    window.addEventListener('resize', updateBreakpoint);
    return () => {
      window.removeEventListener('resize', updateBreakpoint);
    };
  }, []);

  return breakpoint;
};

export default useBreakpoint;
