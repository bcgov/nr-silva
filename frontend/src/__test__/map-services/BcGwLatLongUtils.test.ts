import { describe, expect, it } from 'vitest';
import {
  shiftBcGwLngLat2LatLng,
  shiftLineStringCoordinates
} from '../../map-services/BcGwLatLongUtils';

describe('BcGwLatLongUtils test', () => {
  it('should swift Geoemtry lat long successfully - shiftBcGwLngLat2LatLng', () => {
    const coordinates = [[
      [-1, 15],
      [-2, 25]
    ]];

    const mockedCoordinates = [[
      [15, -1],
      [25, -2]
    ]];

    const openingGeometry = shiftBcGwLngLat2LatLng(coordinates);
    expect(JSON.stringify(openingGeometry)).to.equal(JSON.stringify(mockedCoordinates));
  });
});
