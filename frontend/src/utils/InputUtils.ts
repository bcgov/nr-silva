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
