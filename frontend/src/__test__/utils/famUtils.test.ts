import { describe, it, expect } from 'vitest';
import { formatRolesArray, extractGroups } from '../../utils/famUtils';

describe('famUtils', () => {
  describe('formatRolesArray', () => {
    it('should return an empty array if decodedIdToken is undefined', () => {
      const result = formatRolesArray(undefined);
      expect(result).toEqual([]);
    });

    it('should return an empty array if decodedIdToken does not contain cognito:groups', () => {
      const decodedIdToken = {};
      const result = formatRolesArray(decodedIdToken);
      expect(result).toEqual([]);
    });

    it('should return formatted roles array if decodedIdToken contains cognito:groups', () => {
      const decodedIdToken = {
        'cognito:groups': ['admin_123', 'user_456', 'editor_123']
      };
      const result = formatRolesArray(decodedIdToken);
      expect(result).toEqual([
        {
          clientId: '123',
          roles: ['admin', 'editor'],
          clientName: 'Client Number 123'
        },
        {
          clientId: '456',
          roles: ['user'],
          clientName: 'Client Number 456'
        }
      ]);
    });

    it('should handle groups without underscores correctly', () => {
      const decodedIdToken = {
        'cognito:groups': ['admin', 'user_456', 'editor_123']
      };
      const result = formatRolesArray(decodedIdToken);
      expect(result).toEqual([
        {
          clientId: '123',
          roles: ['editor'],
          clientName: 'Client Number 123'
        },
        {
          clientId: '456',
          roles: ['user'],
          clientName: 'Client Number 456'
        }        
      ]);
    });
  });

  describe('extractGroups', () => {
    it('should return an empty array if decodedIdToken is undefined', () => {
      const result = extractGroups(undefined);
      expect(result).toEqual([]);
    });

    it('should return an empty array if decodedIdToken does not contain cognito:groups', () => {
      const decodedIdToken = {};
      const result = extractGroups(decodedIdToken);
      expect(result).toEqual([]);
    });

    it('should return groups array if decodedIdToken contains cognito:groups', () => {
      const decodedIdToken = {
        'cognito:groups': ['admin', 'user', 'editor']
      };
      const result = extractGroups(decodedIdToken);
      expect(result).toEqual(['admin', 'user', 'editor']);
    });
  });
});