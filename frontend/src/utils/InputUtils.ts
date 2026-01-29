import { ClipboardEvent, FormEvent } from "react";
import { TextInputEvent } from "@/types/GeneralTypes";
import { ItemToStringHandler } from "@carbon/react/lib/components/ComboBox/ComboBox";

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
 * Enforce maximum length constraint on a string.
 * @param value - The string to constrain
 * @param maxLen - The maximum allowed length
 * @returns The string truncated to maxLen if necessary
 */
const enforceMaxLength = (value: string, maxLen: number): string => {
  return value.length > maxLen ? value.slice(0, maxLen) : value;
};

/**
 * Calculate available length for insertion at a specific position within a string,
 * given a maximum length constraint.
 * @param currentValue - The current string value
 * @param selectionStart - The position where insertion would begin
 * @param selectionEnd - The position where insertion would end (for replacements)
 * @param maxLen - The maximum allowed length
 * @returns The number of characters that can be inserted; 0 or negative if no space available
 */
const calculateAvailableLength = (
  currentValue: string,
  selectionStart: number,
  selectionEnd: number,
  maxLen: number
): number => {
  const beforeLength = selectionStart;
  const afterLength = currentValue.length - selectionEnd;
  return maxLen - beforeLength - afterLength;
};

/**
 * Enforce numeric-only input on keydown for an uncontrolled input.
 * Use as an `onKeyDown` handler on the input element.
 * Allows navigation keys and common shortcuts (ctrl/cmd + ...).
 *
 * @param {number} [maxLen] - Optional maximum length constraint. If set, prevents input beyond this length.
 * Example: `<input onKeyDown={(e) => enforceNumberInputOnKeyDown(e, 10)} />`
 */
export const enforceNumberInputOnKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, maxLen?: number) => {
  const allowed = [
    'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', 'Home', 'End',
  ];
  if (allowed.includes(e.key)) return;
  if (e.ctrlKey || e.metaKey) return; // allow copy/paste shortcuts

  // Check max length constraint
  if (maxLen !== undefined && e.currentTarget.value.length >= maxLen) {
    e.preventDefault();
    return;
  }

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
 * @param {HTMLInputElement | null} el - The input element reference.
 * @param {React.ClipboardEvent<HTMLInputElement>} e - The paste event.
 * @param {number} [maxLen] - Optional maximum length constraint.
 * Call from an `onPaste` handler, passing the input element reference and
 * the paste event: `onPaste={(e) => enforceNumberInputOnPaste(inputRef.current, e, 10)}`
 */
export const enforceNumberInputOnPaste = (el: HTMLInputElement | null, e: React.ClipboardEvent<HTMLInputElement>, maxLen?: number) => {
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
  let pastedDigits = digits;

  // Enforce max length constraint
  if (maxLen !== undefined) {
    const availableLength = calculateAvailableLength(el.value, start, end, maxLen);
    if (availableLength <= 0) {
      return;
    }
    pastedDigits = pastedDigits.slice(0, availableLength);
  }

  const newValue = el.value.slice(0, start) + pastedDigits + el.value.slice(end);
  el.value = newValue;
  const cursorPos = start + pastedDigits.length;
  el.setSelectionRange(cursorPos, cursorPos);
};
/**
 * Convert selected item objects from a multi-select into an array of code strings.
 * Filters out undefined/invalid items and returns the `code` property for each.
 * @param selected - Object containing `selectedItems` array from the multi-select component
 * @returns Array of selected code strings
 */
export const getMultiSelectedCodes = (selected: { selectedItems: Array<any> }) => (
  selected.selectedItems
    .filter((item): item is NonNullable<typeof item> => item !== undefined && !!item.code)
    .map(item => item.code) as string[]
);


/**
 * Transform the current input value to upper-case, remove space characters,
 * and preserve the caret position. Use as an `onInput` handler on
 * uncontrolled text inputs.
 * @param e - The input form event
 * @param {number} [maxLen] - Optional maximum length constraint.
 */
export const handleAutoUpperInput = (e: FormEvent<HTMLInputElement>, maxLen?: number) => {
  const el = e.currentTarget;
  const orig = el.value ?? "";
  let pos = el.selectionStart ?? orig.length;

  const out: string[] = [];
  for (let i = 0; i < orig.length; i++) {
    const ch = orig.charAt(i);
    if (ch === ' ') {
      // remove space; if the removed character is before the caret, shift caret left
      if (i < pos) pos--;
      continue;
    }
    out.push(ch);
  }

  let newVal = out.join('').toUpperCase();

  // Enforce max length constraint
  if (maxLen !== undefined) {
    newVal = enforceMaxLength(newVal, maxLen);
    if (pos > newVal.length) pos = newVal.length;
  }

  el.value = newVal;
  if (pos < 0) pos = 0;
  if (pos > newVal.length) pos = newVal.length;
  el.setSelectionRange(pos, pos);
};

/**
 * Handle paste into an uncontrolled input by inserting an upper-case version
 * of the pasted text with spaces removed, at the current caret position.
 * Preserves the caret after the inserted text. Use as an `onPaste` handler.
 * @param e - The clipboard event for the paste
 * @param {number} [maxLen] - Optional maximum length constraint.
 */
export const handleAutoUpperPaste = (e: ClipboardEvent<HTMLInputElement>, maxLen?: number) => {
  e.preventDefault();
  const raw = (e.clipboardData?.getData('text') ?? '');
  const out: string[] = [];
  for (let i = 0; i < raw.length; i++) {
    const ch = raw.charAt(i);
    if (ch === ' ') continue;
    out.push(ch);
  }
  let paste = out.join('').toUpperCase();
  const el = e.currentTarget;
  const start = el.selectionStart ?? 0;
  const end = el.selectionEnd ?? 0;

  // Enforce max length constraint
  if (maxLen !== undefined) {
    const availableLength = calculateAvailableLength(el.value, start, end, maxLen);
    if (availableLength <= 0) {
      return;
    }
    paste = paste.slice(0, availableLength);
  }

  const newVal = el.value.slice(0, start) + paste + el.value.slice(end);
  el.value = newVal;
  const pos = start + paste.length;
  el.setSelectionRange(pos, pos);
};

export const comboBoxStringFilter = (options: {
  item: string;
  itemToString?: ItemToStringHandler<string>;
  inputValue: string | null;
}) => {
  const itemStr = options.itemToString ? options.itemToString(options.item) : options.item;
  if (!options.inputValue) return true;
  return itemStr.toLowerCase().includes(options.inputValue.toLowerCase());
}
