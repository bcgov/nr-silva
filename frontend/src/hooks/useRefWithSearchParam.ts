import { useEffect, RefObject } from 'react';

/**
 * Syncs a ref's value or checked state with a search parameter value.
 * @param ref - The input/checkbox element ref to update
 * @param value - The value to sync to the ref
 * @param isCheckbox - Whether to update checked state (true) or value (false). Defaults to false.
 */
const useRefWithSearchParam = (ref: RefObject<HTMLInputElement | null>, value: any, isCheckbox = false) => {
  useEffect(() => {
    if (!ref.current) return;

    if (isCheckbox) {
      (ref.current as HTMLInputElement).checked = value ?? false;
    } else {
      ref.current.value = value ? String(value) : '';
    }
  }, [value, ref]);
};

export default useRefWithSearchParam;
