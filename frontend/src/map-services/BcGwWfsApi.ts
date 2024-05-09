import { OpeningPolygon } from "../types/OpeningPolygon";
import { shiftBcGwLngLat2LatLng } from "./BcGwLatLongUtils";

/**
 * Gets Openings geometry from the BC GW WFS public dataset.
 *
 * @param openingId The opening id to query or null.
 * @returns A promise with the opening polygon or null.
 */
export const getOpeningsPolygonFromWfs = async (openingId: number | null): Promise<OpeningPolygon | null> => {
  if (!openingId) {
    return Promise.resolve(null);
  }

  // NEXT STEPS:
  // - Work with dynamic properties
  // - Work with different SRS !? (to be discussed)
  // - Work with CQL filters

  let uri = 'https://openmaps.gov.bc.ca/geo/ows';
  // service
  uri += '?service=WFS';
  // version
  uri += '&version=2.0.0';
  // request
  uri += '&request=GetFeature';
  // typeName (layer !?)
  uri += '&typeName=WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW';
  // output format
  uri += '&outputFormat=application/json';
  // Srs name
  uri += '&SrsName=EPSG:4326';
  // Properties name
  uri += '&PROPERTYNAME=OPENING_ID,APPROVE_DATE,LICENSEE_OPENING_ID,GEOMETRY';
  // CQL Filters
  uri += `&CQL_FILTER=OPENING_ID=${openingId}`;

  const resultJson = await fetch(uri);
  if (resultJson.ok) {
    const json = await resultJson.json();

    if (json.features && json.features.length) {
      const openingsList: OpeningPolygon[] = [];
      for (let i = 0, len = json.features.length; i < len; i++) {
        if (json.features[i].geometry) {
          const openingGeometry = shiftBcGwLngLat2LatLng(json.features[i].geometry.coordinates);

          const openingObj: OpeningPolygon = {
            key: json.features[i].id,
            bounds: openingGeometry,
            properties: json.features[i].properties,
            id: json.features[i].id,
            positionLat: json.bbox[1],
            positionLong: json.bbox[0],
          };
          openingsList.push(openingObj);
        }
      }

      // Handling arrays to allow quering more than 1 at once.
      if (openingsList.length) {
        return openingsList[0];
      }
    }
  }
  return null;
};