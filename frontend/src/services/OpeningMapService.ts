import axios from 'axios';
import { FeatureCollection } from "geojson";
import { MapKindType } from "@/types/MapLayer";
import { getAuthIdToken } from '@/services/AuthService';
import { API_ENDPOINTS, defaultHeaders } from '@/services/apiConfig';

export const fetchMapPoligons = async (
  openingId: number,
  kind: MapKindType
): Promise<FeatureCollection> => {
  return axios.get(API_ENDPOINTS.openingMap(openingId, `kind=${kind}` ), defaultHeaders(getAuthIdToken()))
    .then((res) => res.data);
}
