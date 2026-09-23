import { describe, it, expect, vi } from 'vitest';

import {
  createTextInputEvent,
  sanitizeDigits,
  enforceNumberInputOnKeyDown,
  enforceNumberInputOnPaste,
  enforceDecimalInputOnKeyDown,
  enforceDecimalInputOnPaste,
  getMultiSelectedCodes,
  handleAutoUpperInput,
  handleAutoUpperPaste,
  comboBoxStringFilter,
  isValidDecimalInput,
  scrollToSection,
} from '@/utils/InputUtils';

describe('InputUtils', () => {
  it('createTextInputEvent returns event with target value', () => {
    const ev = createTextInputEvent('abc');
    expect((ev as any).target.value).toBe('abc');
  });

  it('sanitizeDigits keeps only digits', () => {
    expect(sanitizeDigits('a1b2c3')).toBe('123');
    expect(sanitizeDigits('0123')).toBe('0123');
    expect(sanitizeDigits('no digits')).toBe('');
  });

  it('isValidDecimalInput enforces integer and decimal digit limits', () => {
    expect(isValidDecimalInput('99.9', 2, 1)).toBe(true);
    expect(isValidDecimalInput('12345678', 7, 4)).toBe(false);
    expect(isValidDecimalInput('999', 2, 1)).toBe(false);
    expect(isValidDecimalInput('.1234', 7, 4)).toBe(true);
    expect(isValidDecimalInput('.', 7, 4)).toBe(false);
    expect(isValidDecimalInput('1234567.1234', 7, 4)).toBe(true);
    expect(isValidDecimalInput('12345678.1234', 7, 4)).toBe(false);
    expect(isValidDecimalInput('1234567.12345', 7, 4)).toBe(false);
  });

  it('enforceNumberInputOnKeyDown allows navigation and digits but prevents letters', () => {
    const evNav: any = { key: 'Backspace', preventDefault: vi.fn(), ctrlKey: false, metaKey: false };
    enforceNumberInputOnKeyDown(evNav);
    expect(evNav.preventDefault).not.toHaveBeenCalled();

    const evDigit: any = { key: '5', preventDefault: vi.fn(), ctrlKey: false, metaKey: false };
    enforceNumberInputOnKeyDown(evDigit);
    expect(evDigit.preventDefault).not.toHaveBeenCalled();

    const evLetter: any = { key: 'a', preventDefault: vi.fn(), ctrlKey: false, metaKey: false };
    enforceNumberInputOnKeyDown(evLetter);
    expect(evLetter.preventDefault).toHaveBeenCalled();

    const evCtrl: any = { key: 'a', preventDefault: vi.fn(), ctrlKey: true, metaKey: false };
    enforceNumberInputOnKeyDown(evCtrl);
    expect(evCtrl.preventDefault).not.toHaveBeenCalled();
  });

  it('enforceNumberInputOnKeyDown respects maxLen constraint', () => {
    const input: any = { value: '12345', length: 5 };
    const evDigit: any = {
      key: '6',
      preventDefault: vi.fn(),
      ctrlKey: false,
      metaKey: false,
      currentTarget: input
    };

    enforceNumberInputOnKeyDown(evDigit, 5);
    expect(evDigit.preventDefault).toHaveBeenCalled();

    // But navigation keys should still work
    const evNav: any = {
      key: 'Backspace',
      preventDefault: vi.fn(),
      ctrlKey: false,
      metaKey: false,
      currentTarget: input
    };
    enforceNumberInputOnKeyDown(evNav, 5);
    expect(evNav.preventDefault).not.toHaveBeenCalled();
  });

  it('enforceDecimalInputOnKeyDown allows valid numeric(2,1) input after a trailing decimal', () => {
    const input: any = { value: '99.', selectionStart: 3, selectionEnd: 3 };
    const ev: any = {
      key: '9',
      preventDefault: vi.fn(),
      ctrlKey: false,
      metaKey: false,
      currentTarget: input,
    };

    enforceDecimalInputOnKeyDown(ev, 2, 1);
    expect(ev.preventDefault).not.toHaveBeenCalled();
  });

  it('enforceDecimalInputOnKeyDown prevents invalid numeric(2,1) input', () => {
    const input: any = { value: '99', selectionStart: 2, selectionEnd: 2 };
    const ev: any = {
      key: '9',
      preventDefault: vi.fn(),
      ctrlKey: false,
      metaKey: false,
      currentTarget: input,
    };

    enforceDecimalInputOnKeyDown(ev, 2, 1);
    expect(ev.preventDefault).toHaveBeenCalled();
  });

  it('enforceDecimalInputOnPaste enforces numeric(2,1) limits', () => {
    const input = document.createElement('input');
    input.value = '99';
    input.selectionStart = 2;
    input.selectionEnd = 2;

    const clipboard = { getData: () => '.9' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn(), currentTarget: input };

    enforceDecimalInputOnPaste(input, ev as any, 2, 1);
    expect(input.value).toBe('99.9');

    const invalidClipboard = { getData: () => '9' };
    const evInvalid: any = { clipboardData: invalidClipboard, preventDefault: vi.fn(), currentTarget: input };
    enforceDecimalInputOnPaste(input, evInvalid as any, 2, 1);
    expect(input.value).toBe('99.9');
  });

  it('enforceNumberInputOnPaste sanitizes digits and inserts into element', () => {
    const input = document.createElement('input');
    input.value = '12';
    // set caret between 1 and 2
    input.selectionStart = 1;
    input.selectionEnd = 1;

    const clipboard = { getData: () => '3a4' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn() };

    enforceNumberInputOnPaste(input, ev as any);

    expect(ev.preventDefault).toHaveBeenCalled();
    expect(input.value).toBe('1342');
    // cursor should be placed after inserted digits (start + 2)
    expect(input.selectionStart).toBe(3);
    expect(input.selectionEnd).toBe(3);
  });

  it('enforceNumberInputOnPaste prevents paste when no digits', () => {
    const input = document.createElement('input');
    input.value = 'a';
    const clipboard = { getData: () => 'abc' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn() };

    enforceNumberInputOnPaste(input, ev as any);
    expect(ev.preventDefault).toHaveBeenCalled();
    // value unchanged
    expect(input.value).toBe('a');
  });

  it('enforceNumberInputOnPaste respects maxLen constraint', () => {
    const input = document.createElement('input');
    input.value = '123';
    input.selectionStart = 3;
    input.selectionEnd = 3;

    const clipboard = { getData: () => '456789' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn() };

    enforceNumberInputOnPaste(input, ev as any, 5);

    // Only 2 digits can fit (5 - 3 before - 0 after = 2)
    expect(input.value).toBe('12345');
    expect(input.selectionStart).toBe(5);
    expect(input.selectionEnd).toBe(5);
  });

  it('enforceNumberInputOnPaste does not paste when maxLen exceeded', () => {
    const input = document.createElement('input');
    input.value = '12345';
    input.selectionStart = 5;
    input.selectionEnd = 5;

    const clipboard = { getData: () => '6789' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn() };

    enforceNumberInputOnPaste(input, ev as any, 5);

    // Nothing should be pasted
    expect(input.value).toBe('12345');
  });

  it('getMultiSelectedCodes extracts codes', () => {
    const res = getMultiSelectedCodes({ selectedItems: [{ code: 'A' }, undefined, { code: 'B' }, { some: 'x' }] as any });
    expect(res).toEqual(['A', 'B']);
  });

  it('handleAutoUpperInput uppercases and removes spaces preserving caret', () => {
    const input = document.createElement('input');
    input.value = 'a b c';
    // caret at end
    input.selectionStart = 5;
    input.selectionEnd = 5;

    // call handler
    (handleAutoUpperInput as any)({ currentTarget: input });

    expect(input.value).toBe('ABC');
    expect(input.selectionStart).toBe(3);
    expect(input.selectionEnd).toBe(3);
  });

  it('handleAutoUpperInput respects maxLen constraint', () => {
    const input = document.createElement('input');
    input.value = 'a b c d e';
    input.selectionStart = 9;
    input.selectionEnd = 9;

    (handleAutoUpperInput as any)({ currentTarget: input }, 3);

    expect(input.value).toBe('ABC');
    expect(input.selectionStart).toBe(3);
    expect(input.selectionEnd).toBe(3);
  });

  it('handleAutoUpperInput adjusts caret when maxLen causes truncation', () => {
    const input = document.createElement('input');
    input.value = 'a b c d';
    // Caret at position 5 (would be after 'a c d' before uppercase)
    input.selectionStart = 5;
    input.selectionEnd = 5;

    (handleAutoUpperInput as any)({ currentTarget: input }, 2);

    expect(input.value).toBe('AB');
    // Caret should be at or before end of value
    expect(input.selectionStart).toBeLessThanOrEqual(2);
  });

  it('handleAutoUpperPaste inserts uppercase pasted text without spaces', () => {
    const input = document.createElement('input');
    input.value = 'XX';
    input.selectionStart = 1;
    input.selectionEnd = 1;

    const clipboard = { getData: () => ' y z ' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn(), currentTarget: input };

    handleAutoUpperPaste(ev as any);

    expect(ev.preventDefault).toHaveBeenCalled();
    // inserted 'YZ' at position 1 => 'X' + 'YZ' + 'X' = 'XYZX'
    expect(input.value).toBe('XYZX');
    expect(input.selectionStart).toBe(3);
    expect(input.selectionEnd).toBe(3);
  });

  it('handleAutoUpperPaste respects maxLen constraint', () => {
    const input = document.createElement('input');
    input.value = 'AB';
    input.selectionStart = 2;
    input.selectionEnd = 2;

    const clipboard = { getData: () => 'cdefgh' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn(), currentTarget: input };

    handleAutoUpperPaste(ev as any, 4);

    expect(ev.preventDefault).toHaveBeenCalled();
    // Only 2 chars can fit (4 - 2 before - 0 after = 2)
    expect(input.value).toBe('ABCD');
    expect(input.selectionStart).toBe(4);
    expect(input.selectionEnd).toBe(4);
  });

  it('handleAutoUpperPaste does not paste when maxLen exceeded', () => {
    const input = document.createElement('input');
    input.value = 'ABCD';
    input.selectionStart = 4;
    input.selectionEnd = 4;

    const clipboard = { getData: () => 'efgh' };
    const ev: any = { clipboardData: clipboard, preventDefault: vi.fn(), currentTarget: input };

    handleAutoUpperPaste(ev as any, 4);

    expect(ev.preventDefault).toHaveBeenCalled();
    // Nothing pasted
    expect(input.value).toBe('ABCD');
  });

  it('comboBoxStringFilter matches case-insensitively using itemToString if provided', () => {
    const opt1 = { item: 'Hello', inputValue: 'he', itemToString: undefined } as any;
    expect(comboBoxStringFilter(opt1)).toBe(true);

    const opt2 = { item: 'Hello', inputValue: 'zz', itemToString: undefined } as any;
    expect(comboBoxStringFilter(opt2)).toBe(false);

    const opt3 = { item: 'X', inputValue: 'x', itemToString: (s: string) => s } as any;
    expect(comboBoxStringFilter(opt3)).toBe(true);
  });

  describe('scrollToSection', () => {
    it('does nothing when id is undefined or empty', () => {
      expect(() => scrollToSection(undefined)).not.toThrow();
      expect(() => scrollToSection('')).not.toThrow();
    });

    it('scrolls to element when element exists', () => {
      const scrollIntoView = vi.fn();
      const div = document.createElement('div');
      div.id = 'target-section';
      div.scrollIntoView = scrollIntoView;
      document.body.appendChild(div);

      scrollToSection('target-section');
      expect(scrollIntoView).toHaveBeenCalledWith({ behavior: 'smooth', block: 'start' });

      document.body.removeChild(div);
    });

    it('does nothing when element is not found', () => {
      expect(() => scrollToSection('non-existent')).not.toThrow();
    });
  });

  describe('handleAutoUpperInput and handleAutoUpperPaste with allowWhitespace', () => {
    it('preserves spaces when allowWhitespace is true in handleAutoUpperInput', () => {
      const input = document.createElement('input');
      input.value = 'hello world';
      input.selectionStart = 11;
      input.selectionEnd = 11;

      handleAutoUpperInput({ currentTarget: input } as any, undefined, true);
      expect(input.value).toBe('HELLO WORLD');
    });

    it('preserves spaces when allowWhitespace is true in handleAutoUpperPaste', () => {
      const input = document.createElement('input');
      input.value = 'A ';
      input.selectionStart = 2;
      input.selectionEnd = 2;

      const clipboard = { getData: () => 'b c' };
      const ev: any = { clipboardData: clipboard, preventDefault: vi.fn(), currentTarget: input };

      handleAutoUpperPaste(ev, undefined, true);
      expect(input.value).toBe('A B C');
    });
  });

  describe('enforceNumberInputOnPaste edge cases', () => {
    it('returns early when pasted text has no digits', () => {
      const input = document.createElement('input');
      input.value = '123';
      const ev: any = {
        clipboardData: { getData: () => 'abc' },
        preventDefault: vi.fn(),
      };

      enforceNumberInputOnPaste(input, ev);
      expect(ev.preventDefault).toHaveBeenCalled();
      expect(input.value).toBe('123');
    });

    it('returns early when input element is null', () => {
      const ev: any = {
        clipboardData: { getData: () => '123' },
        preventDefault: vi.fn(),
      };

      expect(() => enforceNumberInputOnPaste(null, ev)).not.toThrow();
    });

    it('returns early when availableLength <= 0 due to maxLen', () => {
      const input = document.createElement('input');
      input.value = '12345';
      input.selectionStart = 5;
      input.selectionEnd = 5;

      const ev: any = {
        clipboardData: { getData: () => '678' },
        preventDefault: vi.fn(),
      };

      enforceNumberInputOnPaste(input, ev, 5);
      expect(input.value).toBe('12345');
    });
  });

  describe('enforceDecimalInputOnPaste edge cases', () => {
    it('returns early when pasted string is empty or just a dot', () => {
      const input = document.createElement('input');
      input.value = '10';
      const ev: any = {
        clipboardData: { getData: () => '.' },
        preventDefault: vi.fn(),
        currentTarget: input,
      };

      enforceDecimalInputOnPaste(input, ev);
      expect(input.value).toBe('10');
    });

    it('handles multiple decimal dots and truncates excess integer and fraction digits', () => {
      const input = document.createElement('input');
      input.value = '';
      input.selectionStart = 0;
      input.selectionEnd = 0;

      const ev: any = {
        clipboardData: { getData: () => '12345.67891' },
        preventDefault: vi.fn(),
        currentTarget: input,
      };

      enforceDecimalInputOnPaste(input, ev, 3, 2);
      expect(input.value).toBe('123.67');
    });

    it('falls back to e.currentTarget when el is null', () => {
      const input = document.createElement('input');
      input.value = '';
      input.selectionStart = 0;
      input.selectionEnd = 0;

      const ev: any = {
        clipboardData: { getData: () => '50.5' },
        preventDefault: vi.fn(),
        currentTarget: input,
      };

      enforceDecimalInputOnPaste(null, ev);
      expect(input.value).toBe('50.5');
    });
  });
});
