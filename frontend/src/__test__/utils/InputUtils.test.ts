import { describe, it, expect, vi } from 'vitest';

import {
  createTextInputEvent,
  sanitizeDigits,
  enforceNumberInputOnKeyDown,
  enforceNumberInputOnPaste,
  getMultiSelectedCodes,
  handleAutoUpperInput,
  handleAutoUpperPaste,
  comboBoxStringFilter,
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

  it('comboBoxStringFilter matches case-insensitively using itemToString if provided', () => {
    const opt1 = { item: 'Hello', inputValue: 'he', itemToString: undefined } as any;
    expect(comboBoxStringFilter(opt1)).toBe(true);

    const opt2 = { item: 'Hello', inputValue: 'zz', itemToString: undefined } as any;
    expect(comboBoxStringFilter(opt2)).toBe(false);

    const opt3 = { item: 'X', inputValue: 'x', itemToString: (s: string) => s } as any;
    expect(comboBoxStringFilter(opt3)).toBe(true);
  });
});
