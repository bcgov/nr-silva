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

const isDigit = (char: string): boolean => {
  if (char.length !== 1) return false;
  const code = char.charCodeAt(0);
  return code >= 48 && code <= 57;
};

const splitDecimalString = (value: string): { integer: string; fraction: string } | null => {
  let dotCount = 0;
  let integer = '';
  let fraction = '';

  for (let i = 0; i < value.length; i++) {
    const ch = value.charAt(i);
    if (ch === '.') {
      dotCount += 1;
      if (dotCount > 1) {
        return null;
      }
      continue;
    }
    if (!isDigit(ch)) {
      return null;
    }
    if (dotCount === 0) {
      integer += ch;
    } else {
      fraction += ch;
    }
  }

  return { integer, fraction };
};

export const isValidDecimalInput = (
  value: string,
  maxInteger: number,
  maxDecimals: number,
  allowLeadingDot = true
): boolean => {
  const input = value?.trim() ?? '';
  if (!input || input === '.') {
    return false;
  }

  const parts = splitDecimalString(input);
  if (!parts) {
    return false;
  }

  const { integer, fraction } = parts;
  if (!allowLeadingDot && integer.length === 0) {
    return false;
  }
  if (integer.length > maxInteger) {
    return false;
  }
  if (fraction.length > maxDecimals) {
    return false;
  }
  return integer.length > 0 || fraction.length > 0;
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
 * Transform the current input value to upper-case and preserve the caret
 * position. Spaces are removed unless `allowWhitespace` is true. Use as an
 * `onInput` handler on uncontrolled text inputs.
 * @param e - The input form event
 * @param {number} [maxLen] - Optional maximum length constraint.
 * @param {boolean} [allowWhitespace=false] - Whether to preserve spaces.
 */
export const handleAutoUpperInput = (e: FormEvent<HTMLInputElement>, maxLen?: number, allowWhitespace = false) => {
  const el = e.currentTarget;
  const orig = el.value ?? "";
  let pos = el.selectionStart ?? orig.length;

  const out: string[] = [];
  for (let i = 0; i < orig.length; i++) {
    const ch = orig.charAt(i);
    if (ch === ' ' && !allowWhitespace) {
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
 * of the pasted text at the current caret position. Spaces in the pasted text
 * are removed unless `allowWhitespace` is true. Preserves the caret after the
 * inserted text. Use as an `onPaste` handler.
 * @param e - The clipboard event for the paste
 * @param {number} [maxLen] - Optional maximum length constraint.
 * @param {boolean} [allowWhitespace=false] - Whether to preserve spaces.
 */
export const handleAutoUpperPaste = (e: ClipboardEvent<HTMLInputElement>, maxLen?: number, allowWhitespace = false) => {
  e.preventDefault();
  const raw = (e.clipboardData?.getData('text') ?? '');
  const out: string[] = [];
  for (let i = 0; i < raw.length; i++) {
    const ch = raw.charAt(i);
    if (ch === ' ' && !allowWhitespace) continue;
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

/**
 * Enforce decimal numeric input with separate integer/fraction limits on keydown.
 * Backend rules use @Digits(integer, fraction) and allow a single decimal separator.
 *
 * @param {number} [maxLen=11] - Maximum integer digits
 * @param {number} [maxDecimals=4] - Maximum decimal places
 * Example: `<input onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, 7, 4)} />`
 */
export const enforceDecimalInputOnKeyDown = (
  e: React.KeyboardEvent<HTMLInputElement>,
  maxLen: number = 11,
  maxDecimals: number = 4
) => {
  const allowed = [
    'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', 'Home', 'End',
  ];
  if (allowed.includes(e.key)) return;
  if (e.ctrlKey || e.metaKey) return; // allow copy/paste shortcuts

  const key = e.key;
  const currentValue = e.currentTarget.value;
  const start = e.currentTarget.selectionStart ?? 0;
  const end = e.currentTarget.selectionEnd ?? 0;

  if (key === '.') {
    if (currentValue.includes('.')) {
      e.preventDefault();
    }
    return;
  }

  if (typeof key === 'string' && key.length === 1) {
    const code = key.charCodeAt(0);
    if (code >= 48 && code <= 57) {
      const before = currentValue.slice(0, start);
      const after = currentValue.slice(end);
      const proposedValue = before + key + after;

      if (!isValidDecimalInput(proposedValue, maxLen, maxDecimals)) {
        e.preventDefault();
      }
      return;
    }
  }
  e.preventDefault();
};

/**
 * Enforce decimal numeric paste with separate integer/fraction limits.
 * Backend rules use @Digits(integer, fraction) and allow a single decimal separator.
 *
 * @param {HTMLInputElement | null} el - The input element reference.
 * @param {React.ClipboardEvent<HTMLInputElement>} e - The paste event.
 * @param {number} [maxLen=11] - Maximum integer digits
 * @param {number} [maxDecimals=4] - Maximum decimal places
 * Call from an `onPaste` handler: `onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, 7, 4)}`
 */
export const enforceDecimalInputOnPaste = (
  el: HTMLInputElement | null,
  e: React.ClipboardEvent<HTMLInputElement>,
  maxLen: number = 11,
  maxDecimals: number = 4
) => {
  e.preventDefault();
  const input = el ?? e.currentTarget;

  const text = e.clipboardData?.getData('text') ?? '';
  let paste = '';
  let decimalCount = 0;
  for (let i = 0; i < text.length; i++) {
    const ch = text.charAt(i);
    if (ch === '.') {
      if (decimalCount === 0) {
        paste += ch;
        decimalCount++;
      }
    } else {
      const code = ch.charCodeAt(0);
      if (code >= 48 && code <= 57) {
        paste += ch;
      }
    }
  }

  if (!paste || paste === '.') return;

  const start = input.selectionStart ?? 0;
  const end = input.selectionEnd ?? 0;
  const before = input.value.slice(0, start);
  const after = input.value.slice(end);
  let newValue = before + paste + after;

  const parts = newValue.split('.');
  if (parts.length > 2) {
    const part0 = parts[0] ?? '';
    newValue = part0 + '.' + parts.slice(1).join('');
  }

  const normalizedParts = newValue.split('.');
  let integerPart = normalizedParts[0] ?? '';
  let fractionPart = normalizedParts[1] ?? '';

  if (integerPart.length > maxLen) {
    integerPart = integerPart.slice(0, maxLen);
  }
  if (fractionPart.length > maxDecimals) {
    fractionPart = fractionPart.slice(0, maxDecimals);
  }

  newValue = normalizedParts.length === 2 ? `${integerPart}.${fractionPart}` : integerPart;

  if (!isValidDecimalInput(newValue, maxLen, maxDecimals)) return;

  input.value = newValue;
  const cursorPos = Math.min(start + paste.length, input.value.length);
  input.setSelectionRange(cursorPos, cursorPos);
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
