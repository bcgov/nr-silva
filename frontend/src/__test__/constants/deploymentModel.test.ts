import { describe, expect, it } from 'vitest';
import {
  DEPLOYMENT_MODELS,
  getDeploymentModel,
  getDeploymentModelDisplayName
} from '../../constants/deploymentModel';

describe('Deployment Model Configuration', () => {
  describe('DEPLOYMENT_MODELS', () => {
    it('should have hybrid model constant', () => {
      expect(DEPLOYMENT_MODELS.HYBRID).toBe('hybrid');
    });

    it('should have postgres model constant', () => {
      expect(DEPLOYMENT_MODELS.POSTGRES).toBe('postgres');
    });
  });

  describe('getDeploymentModel', () => {
    it('should return postgres when postgres is passed', () => {
      expect(getDeploymentModel('postgres')).toBe('postgres');
    });

    it('should return postgres when POSTGRES is passed (case insensitive)', () => {
      expect(getDeploymentModel('POSTGRES')).toBe('postgres');
    });

    it('should return hybrid when hybrid is passed', () => {
      expect(getDeploymentModel('hybrid')).toBe('hybrid');
    });

    it('should return hybrid as default when undefined', () => {
      expect(getDeploymentModel(undefined)).toBe('hybrid');
    });

    it('should return hybrid as default when empty string', () => {
      expect(getDeploymentModel('')).toBe('hybrid');
    });

    it('should return hybrid as default for unknown values', () => {
      expect(getDeploymentModel('unknown')).toBe('hybrid');
    });

    it('should handle whitespace', () => {
      expect(getDeploymentModel('  postgres  ')).toBe('postgres');
    });
  });

  describe('getDeploymentModelDisplayName', () => {
    it('should return "Postgres Model" for postgres', () => {
      expect(getDeploymentModelDisplayName('postgres')).toBe('Postgres Model');
    });

    it('should return "Hybrid Model" for hybrid', () => {
      expect(getDeploymentModelDisplayName('hybrid')).toBe('Hybrid Model');
    });
  });
});
