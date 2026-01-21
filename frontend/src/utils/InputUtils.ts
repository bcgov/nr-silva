import { TextInputEvent } from "../types/GeneralTypes";

/**
 * Creates a mock TextInputEvent with the specified value.
 *
 * @param {string} value - The value to set in the target input.
 * @returns {TextInputEvent} A synthetic input event with the given value.
 */
export const createTextInputEvent = (value: string): TextInputEvent => {
  return {
    target: { value } as HTMLInputElement,
  } as TextInputEvent;
};


export const scrollToSection = (id?: string) => {
  if (!id) return;
  const el = document.getElementById(id);
  if (el) {
    el.scrollIntoView({ behavior: "smooth", block: "start" });
  }
};

/**
 * Sanitize a string to digits only.
 */
export const sanitizeDigits = (value: string): string => {
  const input = value ?? '';
  const out: string[] = [];
  for (let i = 0; i < input.length; i++) {
    const code = input.charCodeAt(i);
    // '0'..'9' => 48..57
    if (code >= 48 && code <= 57) {
      out.push(String.fromCharCode(code));
    }
  }
  return out.join('');
};

/**
 * Enforce numeric-only input on keydown for an uncontrolled input.
 * Use as an `onKeyDown` handler on the input element.
 * Allows navigation keys and common shortcuts (ctrl/cmd + ...).
 *
 * Example: `<input onKeyDown={enforceNumberInputOnKeyDown} />`
 */
export const enforceNumberInputOnKeyDown = (e: any) => {
  const allowed = [
    'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', 'Home', 'End',
  ];
  if (allowed.includes(e.key)) return;
  if (e.ctrlKey || e.metaKey) return; // allow copy/paste shortcuts
  // allow digits without using a regular expression
  const key = e.key;
  if (typeof key === 'string' && key.length === 1) {
    const code = key.charCodeAt(0);
    if (code >= 48 && code <= 57) return; // '0'..'9'
  }
  e.preventDefault();
};

/**
 * Enforce numeric-only paste for an uncontrolled input by sanitizing clipboard
 * text to digits and inserting it at the current caret position.
 *
 * Call from an `onPaste` handler, passing the input element reference and
 * the paste event: `onPaste={(e) => enforceNumberInputOnPaste(inputRef.current, e)}`
 */
export const enforceNumberInputOnPaste = (el: HTMLInputElement | null, e: any) => {
  const text = e.clipboardData?.getData('text') ?? '';
  const digits = sanitizeDigits(text);
  if (!digits) {
    e.preventDefault();
    return;
  }
  if (!el) return;
  e.preventDefault();
  const start = el.selectionStart ?? el.value.length;
  const end = el.selectionEnd ?? el.value.length;
  const newValue = el.value.slice(0, start) + digits + el.value.slice(end);
  el.value = newValue;
  const cursorPos = start + digits.length;
  el.setSelectionRange(cursorPos, cursorPos);
};


export const getMultiSelectedCodes = (selected: { selectedItems: Array<any> }) => (
  selected.selectedItems
    .filter((item): item is NonNullable<typeof item> => item !== undefined && !!item.code)
    .map(item => item.code) as string[]
);
