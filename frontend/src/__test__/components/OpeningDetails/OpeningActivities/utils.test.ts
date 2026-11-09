import { describe, it, expect } from 'vitest';
import {
  formatActivityFundingSources,
  formatActivityObjective,
} from '../../../../components/OpeningDetails/OpeningActivities/utils';
import { OpeningDetailsActivitiesActivitiesDto } from '../../../../services/OpenApi';

const dto = (partial: Partial<OpeningDetailsActivitiesActivitiesDto>) =>
  partial as OpeningDetailsActivitiesActivitiesDto;

describe('formatActivityObjective', () => {
  it('returns placeholder for null data', () => {
    expect(formatActivityObjective(null)).toEqual({ tooltipDefinition: null, displayText: '--' });
  });

  it('uses objectives list when present', () => {
    const result = formatActivityObjective(
      dto({
        objectives: [
          { code: 'A', description: 'Alpha' },
          { code: 'B', description: 'Beta' },
        ],
      })
    );
    expect(result.displayText).toBe('A \u2022 B');
    expect(result.tooltipDefinition).toEqual(['A - Alpha', 'B - Beta']);
  });

  it('falls back to objective1..3 and skips empties', () => {
    const result = formatActivityObjective(
      dto({
        objective1: { code: 'A', description: 'Alpha' },
        objective2: null,
        objective3: { code: 'C', description: 'Gamma' },
      })
    );
    expect(result.displayText).toBe('A \u2022 C');
    expect(result.tooltipDefinition).toHaveLength(2);
  });

  it('returns placeholder when nothing valid', () => {
    const result = formatActivityObjective(dto({}));
    expect(result.displayText).toBe('--');
    expect(result.tooltipDefinition).toEqual([]);
  });
});

describe('formatActivityFundingSources', () => {
  it('returns placeholder for null data', () => {
    expect(formatActivityFundingSources(null)).toEqual({ tooltipDefinition: null, displayText: '--' });
  });

  it('uses fundingSources when present', () => {
    const result = formatActivityFundingSources(
      dto({
        fundingSources: [
          { code: 'F1', description: 'One' },
          { code: 'F2', description: 'Two' },
        ],
      })
    );
    expect(result.displayText).toBe('F1 \u2022 F2');
  });

  it('falls back to the single funding value', () => {
    const result = formatActivityFundingSources(dto({ funding: { code: 'F1', description: 'One' } }));
    expect(result.displayText).toBe('F1');
    expect(result.tooltipDefinition).toEqual(['F1 - One']);
  });

  it('returns placeholder when no funding', () => {
    expect(formatActivityFundingSources(dto({})).displayText).toBe('--');
  });
});
