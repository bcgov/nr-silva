import { useQuery } from "@tanstack/react-query";
import { GeoJsonProperties } from "geojson";
import { FeatureCollection } from "geojson";
import API from "@/services/API";
import { MapKindType } from "@/types/MapLayer";
import { MAP_KINDS } from "@/constants/mapKindConstants";

/**
 * Per-kind extractor: given a feature's properties, returns the compound ID used
 * to match against selected IDs (e.g. "12345-SP"), or null if this feature is
 * not relevant to the kind.
 *
 * Note: MAP_KINDS.activityTreatment covers both silviculture activities (non-DN base codes)
 * and disturbance events (DN base code). Both share the same compound-ID format:
 * `${ACTIVITY_TREATMENT_UNIT_ID}-${SILV_BASE_CODE}`.
 */
const compoundIdExtractors: Partial<Record<MapKindType, (props: GeoJsonProperties) => string | null>> = {
  [MAP_KINDS.activityTreatment]: (props) =>
    props?.ACTIVITY_TREATMENT_UNIT_ID != null && props?.SILV_BASE_CODE
      ? `${props.ACTIVITY_TREATMENT_UNIT_ID}-${props.SILV_BASE_CODE}`
      : null,

  [MAP_KINDS.forestCoverInventory]: (props) =>
    props?.FOREST_COVER_ID != null && props?.SILV_POLYGON_NUMBER != null
      ? `${props.FOREST_COVER_ID}-${props.SILV_POLYGON_NUMBER}`
      : null,

  [MAP_KINDS.forestCoverReserve]: (props) =>
    props?.FOREST_COVER_ID != null && props?.SILV_POLYGON_NUMBER != null
      ? `${props.FOREST_COVER_ID}-${props.SILV_POLYGON_NUMBER}`
      : null,

  [MAP_KINDS.forestCoverSilviculture]: (props) =>
    props?.FOREST_COVER_ID != null && props?.SILV_POLYGON_NUMBER != null
      ? `${props.FOREST_COVER_ID}-${props.SILV_POLYGON_NUMBER}`
      : null,

  [MAP_KINDS.opening]: (props) =>
    props?.OPENING_ID != null ? `${props.OPENING_ID}` : null,

  [MAP_KINDS.planting]: (props) =>
    props?.ACTIVITY_TREATMENT_UNIT_ID != null && props?.SILV_BASE_CODE
      ? `${props.ACTIVITY_TREATMENT_UNIT_ID}-${props.SILV_BASE_CODE}`
      : null,

  [MAP_KINDS.standardsUnit]: (props) =>
    props?.STOCKING_STANDARD_UNIT_ID != null
      ? `${props.STOCKING_STANDARD_UNIT_ID}`
      : null,

  [MAP_KINDS.cutBlock]: (props) =>
    props?.CUT_BLOCK_FOREST_FILE_ID != null && props?.CUT_BLOCK_ID != null
      ? `${props.CUT_BLOCK_FOREST_FILE_ID}-${props.CUT_BLOCK_ID}`
      : null,
};

type UsePolygonAvailabilityResult = {
  isAvailable: boolean;
  isLoading: boolean;
};

/**
 * Checks whether a polygon with the given compoundId exists in the GeoServer
 * response for the given openingId and mapKind.
 *
 * Uses the same query key as OpeningsMap so TanStack Query serves from cache
 * when both are mounted simultaneously — no duplicate network requests.
 */
const usePolygonAvailability = (
  openingId: number,
  kind: MapKindType,
  compoundId: string | null,
): UsePolygonAvailabilityResult => {
  const extractor = compoundIdExtractors[kind];

  const query = useQuery({
    queryKey: ["opening", "map", openingId, { kinds: [kind] }],
    queryFn: () =>
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties(
        openingId.toString(),
        kind,
      ) as unknown as Promise<FeatureCollection>,
    enabled: !!extractor && compoundId !== null,
  });

  if (!extractor || compoundId === null || query.isLoading || !query.data) {
    return { isAvailable: false, isLoading: query.isLoading && compoundId !== null };
  }

  const available = (query.data.features ?? []).some(
    (feature) => extractor(feature.properties) === compoundId && feature.geometry != null
  );

  return { isAvailable: available, isLoading: false };
};

export default usePolygonAvailability;
