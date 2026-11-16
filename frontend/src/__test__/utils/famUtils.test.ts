import { describe, it, expect } from 'vitest';

import { USER_PRIVILEGE_TYPE } from '@/types/AuthTypes';
import {
  extractGroups,
  extractUniqueClients,
  hasCreateOpeningPrivilege,
  parseCssRole,
  parsePrivileges,
} from '@/utils/famUtils';

describe('parseCssRole', () => {
  it('parses unscoped roles cleanly', () => {
    expect(parseCssRole('SILVA_VIEWER')).toEqual({
      baseRoleName: 'SILVA_VIEWER',
      scopes: [],
    });
  });

  it('parses single-scope FOREST_CLIENT roles', () => {
    expect(parseCssRole('SILVA_SUBMITTER_FOREST_CLIENT-00012797')).toEqual({
      baseRoleName: 'SILVA_SUBMITTER',
      scopes: [{ type: 'FOREST_CLIENT', value: '00012797' }],
    });
  });

  it('parses compound multi-scope roles left-to-right', () => {
    expect(
      parseCssRole('PLANNER_FOREST_CLIENT-00001012_REGION-KOOTENAY_BOUNDARY')
    ).toEqual({
      baseRoleName: 'PLANNER',
      scopes: [
        { type: 'FOREST_CLIENT', value: '00001012' },
        { type: 'REGION', value: 'KOOTENAY_BOUNDARY' },
      ],
    });
  });
});

describe('parsePrivileges', () => {
  it('parses concrete roles in both legacy TitleCase and CSS UPPERCASE formats', () => {
    const input = ['Viewer'];
    const expected: USER_PRIVILEGE_TYPE = {
      Viewer: null,
    };
    expect(parsePrivileges(input)).toEqual(expected);
    expect(parsePrivileges(['SILVA_VIEWER'])).toEqual({ Viewer: null });
    expect(parsePrivileges(['VIEWER'])).toEqual({ Viewer: null });
  });

  it('parses CSS FOREST_CLIENT scoped roles and rejects compound multi-scope roles', () => {
    const input = [
      'SUBMITTER_FOREST_CLIENT-00012797',
      'SILVA_PLANNER_FOREST_CLIENT-00001012_REGION-KOOTENAY_BOUNDARY',
      'SILVA_PLANNER_DISTRICT-DCC_FOREST_CLIENT-00001012',
      'SILVA_APPROVER_FOREST_CLIENT-00002176',
    ];
    expect(parsePrivileges(input)).toEqual({
      Submitter: ['00012797'],
      Approver: ['00002176'],
    });
  });

  it('deduplicates repeated client IDs and ignores FAM metadata sidecars', () => {
    const input = [
      'SILVA_SUBMITTER_FOREST_CLIENT-00012797',
      'SUBMITTER_FOREST_CLIENT-00012797',
      'FAM:LABEL:Submitter',
      'FAM:DESC:Allows submissions',
      'FAM:EXPIRES:2026-12-31T00:00:00Z',
      'PLANNER_DISTRICT-DRM',
    ];
    expect(parsePrivileges(input)).toEqual({
      Submitter: ['00012797'],
    });
  });

  it('parses legacy abstract roles with forest client IDs', () => {
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

  it('ignores unknown and badly formed roles', () => {
    const input = ['UnknownRole', 'Submitter_', 'Planner_abc', 'Submitter_000123'];
    const expected: USER_PRIVILEGE_TYPE = {
      Submitter: ['000123'],
    };
    expect(parsePrivileges(input)).toEqual(expected);
  });
});

describe('extractUniqueClients & hasCreateOpeningPrivilege', () => {
  it('extracts unique client numbers while skipping FAM sidecars, non-client scopes, and compound scopes', () => {
    expect(
      extractUniqueClients([
        'SILVA_VIEWER',
        'SUBMITTER_FOREST_CLIENT-00012797',
        'PLANNER_FOREST_CLIENT-00001012',
        'PLANNER_FOREST_CLIENT-00003333_REGION-RKO',
        'SUBMITTER_FOREST_CLIENT-00012797',
        'PLANNER_DISTRICT-DRM',
        'FAM:EXPIRES:2026-01-01',
      ])
    ).toEqual(['00012797', '00001012']);
  });

  it('checks create opening privileges', () => {
    expect(hasCreateOpeningPrivilege({ Viewer: null })).toBe(false);
    expect(hasCreateOpeningPrivilege({ Submitter: ['0001'] })).toBe(true);
    expect(hasCreateOpeningPrivilege({ Admin: null })).toBe(true);
  });
});

describe('extractGroups', () => {
  it('extracts from client_roles and filters FAM sidecar entries', () => {
    const decoded = {
      client_roles: [
        'SILVA_VIEWER',
        'SUBMITTER_FOREST_CLIENT-00012797',
        'FAM:LABEL:Submitter',
        'FAM:DESC:desc',
        'FAM:EXPIRES:2026-12-31',
      ],
    };
    expect(extractGroups(decoded)).toEqual([
      'SILVA_VIEWER',
      'SUBMITTER_FOREST_CLIENT-00012797',
    ]);
  });

  it('falls back to resource_access[azp].roles when client_roles is empty', () => {
    const decoded = {
      azp: 'nr-silva',
      client_roles: [],
      resource_access: {
        'nr-silva': {
          roles: ['SILVA_PLANNER_FOREST_CLIENT-00001012', 'FAM:EXPIRES:2026-06-01'],
        },
        other: { roles: ['ADMIN'] },
      },
    };
    expect(extractGroups(decoded)).toEqual(['SILVA_PLANNER_FOREST_CLIENT-00001012']);
  });

  it('falls back to cognito:groups when Keycloak role claims are absent', () => {
    const decoded = {
      'cognito:groups': ['Viewer', 'Submitter_0001'],
    };
    expect(extractGroups(decoded)).toEqual(['Viewer', 'Submitter_0001']);
  });

  it('returns empty array if token is undefined or has no role claims', () => {
    expect(extractGroups(undefined)).toEqual([]);
    expect(extractGroups({ foo: 'bar' })).toEqual([]);
  });
});
