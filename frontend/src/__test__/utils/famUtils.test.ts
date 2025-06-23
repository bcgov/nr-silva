import { describe, it, expect } from "vitest";
import { parsePrivileges, extractGroups } from '../../utils/famUtils';
import { USER_PRIVILEGE_TYPE } from '../../types/AuthTypes';

describe('parsePrivileges', () => {
  it('parses concrete roles correctly', () => {
    const input = ['Viewer'];
    const expected: USER_PRIVILEGE_TYPE = {
      Viewer: null,
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });

  it('parses abstract roles with forest client IDs', () => {
    const input = ['Submitter_00012345', 'Planner_00099999'];
    const expected: USER_PRIVILEGE_TYPE = {
      Submitter: ['00012345'],
      Planner: ['00099999'],
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });

  it('handles mix of concrete and abstract roles', () => {
    const input = ['Viewer', 'Planner_00011111', 'Submitter_00022222'];
    const expected: USER_PRIVILEGE_TYPE = {
      Viewer: null,
      Planner: ['00011111'],
      Submitter: ['00022222'],
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });

  it('ignores unknown roles', () => {
    const input = ['UnknownRole', 'Submitter_000123'];
    const expected: USER_PRIVILEGE_TYPE = {
      Submitter: ['000123'],
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });

  it('merges multiple IDs under the same role', () => {
    const input = ['Planner_0001', 'Planner_0002'];
    const expected: USER_PRIVILEGE_TYPE = {
      Planner: ['0001', '0002'],
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });

  it('ignores badly formed entries', () => {
    const input = ['Submitter_', 'Planner_abc'];
    expect(parsePrivileges(input)).toEqual({});
  });
});

describe('extractGroups', () => {
  it('returns groups from decoded token', () => {
    const decoded = {
      'cognito:groups': ['Viewer', 'Submitter_0001'],
    };
    expect(extractGroups(decoded)).toEqual(['Viewer', 'Submitter_0001']);
  });

  it('returns empty array if token is undefined', () => {
    expect(extractGroups(undefined)).toEqual([]);
  });

  it('returns empty array if no cognito:groups', () => {
    const decoded = { foo: 'bar' };
    expect(extractGroups(decoded)).toEqual([]);
  });
});
