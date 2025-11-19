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
 * sanitize a string to digits only
 */
export const sanitizeDigits = (value: string): string => {
  const input = value ?? '';
  let digitsOnly = '';
  for (let i = 0; i < input.length; i++) {
    const ch = input.charAt(i);
    if (ch >= '0' && ch <= '9') digitsOnly += ch;
  }
  return digitsOnly;
};
