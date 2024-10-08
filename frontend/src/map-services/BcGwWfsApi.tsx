import { MapLayer } from '../types/MapLayer';
import { OpeningPolygon } from '../types/OpeningPolygon';
import { shiftBcGwLngLat2LatLng, shiftLineStringCoordinates } from './BcGwLatLongUtils';

interface AppendProps {
  featureProps: object;
  displayName: string; // Region
  name: string | null; // REGION_NAME
  code: string | null; // REGION_CODE
}

const formatDate = (input: string): string => {
  const day = parseInt(input.substring(8, 10));
  const month = parseInt(input.substring(5, 7));
  const year = parseInt(input.substring(0, 4));
  switch (month) {
    case 1: return `January ${day}, ${year}`;
    case 2: return `February ${day}, ${year}`;
    case 3: return `March ${day}, ${year}`;
    case 4: return `April ${day}, ${year}`;
    case 5: return `May ${day}, ${year}`;
    case 6: return `June ${day}, ${year}`;
    case 7: return `July ${day}, ${year}`;
    case 8: return `August ${day}, ${year}`;
    case 9: return `September ${day}, ${year}`;
    case 10: return `October ${day}, ${year}`;
    case 11: return `November ${day}, ${year}`;
    case 12: return `December ${day}, ${year}`;
  }
  return input;
};

const getObjectValue = (props: object, propkey: string): string => {
  if (Object.keys(props).includes(propkey)) {
    for (const key of Object.keys(props)) {
      if (key === propkey) {
        if (propkey.includes('OPENING_WHEN_CREATED')) {
          return formatDate(`${props[key as keyof typeof props]}`);
        }
        return `${props[key as keyof typeof props]}`;
      }
    }
  }
  return '';
};

const appendProperties = (props: AppendProps): JSX.Element | null => {
  if (!props.name || !props.code) {
    return null;
  }
  if (!Object.keys(props.featureProps).includes(props.name)) {
    return null;
  }
  
  return (
    <li>
      - {props.displayName}: {getObjectValue(props.featureProps, props.name)} ({getObjectValue(props.featureProps, props.code)})
    </li>
  );
}

export const createPopupFromProps = (props: object): JSX.Element => (
  <>
    {Object.keys(props).includes('OPENING_ID') && (
      <>
        <h3>Opening ID {getObjectValue(props, 'OPENING_ID')}</h3>
        <hr />
      </>
    )}
    <ul>
      {appendProperties({
        featureProps: props,
        displayName: 'Region',
        name: 'REGION_NAME',
        code: 'REGION_CODE'
      })}
      {appendProperties({
        featureProps: props,
        displayName: 'District',
        name: 'DISTRICT_NAME',
        code: 'DISTRICT_CODE'
      })}
      {appendProperties({
        featureProps: props,
        displayName: 'Client',
        name: 'CLIENT_NAME',
        code: 'CLIENT_NUMBER'
      })}
    </ul>
    {Object.keys(props).includes('OPENING_WHEN_CREATED') && (
      <>
        <hr />
        <p>Created: {getObjectValue(props, 'OPENING_WHEN_CREATED')}</p>
      </>
    )}
  </>
);

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
  uri += '?service=WMS';
  // version
  uri += '&version=1.1.1';
  // request
  uri += '&request=GetMap'; // GetMap, GetCapabilities, GetFeatureInfo
  // layers
  uri += '&layers=WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW';
  // format
  uri += '&format=application/json;type=geojson';
  // Srs name
  uri += '&srs=EPSG:4326';
  // Properties name
  uri += '&PROPERTYNAME=OPENING_ID,GEOMETRY,REGION_NAME,REGION_CODE,DISTRICT_NAME,DISTRICT_CODE,CLIENT_NAME,CLIENT_NUMBER,OPENING_WHEN_CREATED';
  // CQL Filters
  uri += `&CQL_FILTER=OPENING_ID=${openingId}`;
  // bbox
  //uri += '&bbox=-15028131.257091936,6261721.357121641,-14401959.121379772,6887893.492833805';

  // console.log('uri', uri);

  const resultJson = await fetch(uri, {
    method: "GET",
    mode: "cors"
  });
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
            positionLat: (json.bbox[1] + json.bbox[3]) / 2,
            positionLong: (json.bbox[0] + json.bbox[2]) / 2,
            popup: createPopupFromProps(json.features[i].properties)
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

