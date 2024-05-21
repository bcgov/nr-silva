import { Mock, describe, expect, it, vi } from 'vitest';
import { getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';
import { OpeningPolygon } from '../../types/OpeningPolygon';

const mockedResponse = {
  features: [
    {
      id: 'id-1',
      properties: {
        prop1: 'value1',
      },
      geometry: {
        coordinates: []
      }
    }
  ],
  bbox: [1, 2, 3, 4]
};

global.fetch = vi.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve(mockedResponse),
  }),
) as Mock;

describe('BcGwWfsApi test', () => {
  it('get opening polygons from WFS - getOpeningsPolygonFromWfs', async () => {
    const openingId: number | null = 123;
    
    const response: OpeningPolygon | null = await getOpeningsPolygonFromWfs(openingId);

    expect(response?.key).toStrictEqual("id-1");
    expect(response?.properties).toStrictEqual(mockedResponse.features[0].properties);
    expect(response?.id).toStrictEqual("id-1");
    expect(response?.positionLat).toStrictEqual(3);
    expect(response?.positionLong).toStrictEqual(2);
  });
});