import { describe, expect, it, vi, beforeEach } from 'vitest';
import { isNonProduction, gatePostgresFeature } from '../../utils/featureFlags';
import * as envModule from '@/env';

vi.mock('@/env', () => ({
  env: {
    VITE_ZONE: 'dev',
    VITE_DEPLOYMENT_MODEL: 'hybrid'
  }
}));

describe('Feature Flags', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('isNonProduction', () => {
    it('should return true for dev zone', () => {
      (envModule.env as any).VITE_ZONE = 'dev';
      expect(isNonProduction()).toBe(true);
    });

    it('should return true for test zone', () => {
      (envModule.env as any).VITE_ZONE = 'test';
      expect(isNonProduction()).toBe(true);
    });

    it('should return false for prod zone', () => {
      (envModule.env as any).VITE_ZONE = 'prod';
      expect(isNonProduction()).toBe(false);
    });

    it('should be case-insensitive', () => {
      (envModule.env as any).VITE_ZONE = 'PROD';
      expect(isNonProduction()).toBe(false);

      (envModule.env as any).VITE_ZONE = 'DEV';
      expect(isNonProduction()).toBe(true);
    });

    it('should return true when VITE_ZONE is undefined', () => {
      (envModule.env as any).VITE_ZONE = undefined;
      expect(isNonProduction()).toBe(true);
    });
  });

  describe('gatePostgresFeature', () => {
    it('should return true in prod with hybrid deployment', () => {
      (envModule.env as any).VITE_ZONE = 'prod';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'hybrid';
      expect(gatePostgresFeature()).toBe(true);
    });

    it('should return true in prod with postgres deployment', () => {
      (envModule.env as any).VITE_ZONE = 'prod';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';
      expect(gatePostgresFeature()).toBe(true);
    });

    it('should return true in non-prod with hybrid deployment', () => {
      (envModule.env as any).VITE_ZONE = 'dev';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'hybrid';
      expect(gatePostgresFeature()).toBe(true);
    });

    it('should return false in non-prod with postgres deployment (gated)', () => {
      (envModule.env as any).VITE_ZONE = 'dev';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';
      expect(gatePostgresFeature()).toBe(false);
    });

    it('should handle case-insensitive VITE_ZONE', () => {
      (envModule.env as any).VITE_ZONE = 'PROD';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';
      expect(gatePostgresFeature()).toBe(true);

      (envModule.env as any).VITE_ZONE = 'DEV';
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';
      expect(gatePostgresFeature()).toBe(false);
    });

    it('should return false when zone is undefined (treated as non-prod postgres)', () => {
      (envModule.env as any).VITE_ZONE = undefined;
      (envModule.env as any).VITE_DEPLOYMENT_MODEL = 'postgres';
      expect(gatePostgresFeature()).toBe(false);
    });
  });
});
