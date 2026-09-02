# Create Opening — POST /api/openings

## Business Context

An **opening** is the administrative boundary of an area of land on which silviculture activities are planned and completed. This endpoint allows an authorised client to register a new opening in the SILVA system.

Newly created openings are assigned the status **SUB** (submitted).

### Who submits an opening?

An authenticated user acting on behalf of a client (identified by `clientNumber`). The caller must hold a role matching `*_{clientNumber}` (e.g. `PLANNER_00001012`); otherwise the request is rejected with HTTP 403.

---

## Request

`POST /api/openings` accepts `multipart/form-data` with two parts (same endpoint as file upload; `data` part distinguishes the operation):

| Part | Content-Type | Description |
|------|-------------|-------------|
| `data` | `application/json` | JSON object matching `CreateOpeningRequestDto` |
| `file` | `application/octet-stream` | Spatial file (GeoJSON or GML; max 25 MB) |

### `CreateOpeningRequestDto` fields

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| `openingGrossArea` | BigDecimal | Yes | ≤7 integer + 4 decimal digits | Total opening gross area in hectares |
| `maxAllowablePermAccessPerc` | BigDecimal | Yes | ≤2 integer + 1 decimal digit | Max % of total area that may be permanent access structures |
| `clientNumber` | String | Yes | Max 8 chars | Ministry client number |
| `clientLocationCode` | String | Yes | Max 2 chars | Client location code |
| `orgUnitCode` | String | Yes | Max 6 chars | Org unit code of the managing district (e.g. `"DCC"`) |
| `openingCategoryCode` | String | Yes | Max 7 chars | Opening category (e.g. `"FTML"`, `"PRIVATE"`) |
| `licenseeOpeningId` | String | No | Max 30 chars | Licensee-provided identifier for the opening |
| `tenures` | List | Yes | Non-empty; exactly one must be primary; no duplicate fileId + cuttingPermit + cutBlock combinations | Cut-block tenures to associate with this opening |

### `TenureRequestDto` fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `fileId` | String | Yes | Forest file ID (`forest_file_id`) |
| `cuttingPermit` | String | No | Cutting permit ID; omit or null if none |
| `cutBlock` | String | Yes | Cut block ID |
| `isPrimary` | boolean | Yes | `true` for the primary licence holder tenure |

---

## Response (HTTP 201)

```json
{ "openingId": 123456 }
```

---

## Processing Steps

1. **Virus scan** — the uploaded file is scanned. Infected files → HTTP 422.
2. **Spatial file processing** — validates CRS (EPSG:4326 or EPSG:3005), geometry type (Polygon / MultiPolygon), topology, BC extents, and vertex count; reprojects to EPSG:4326 if needed. The file must contain **exactly one feature** (per RISS-ls §5.4.4 — an opening has a single boundary); files with multiple features are rejected with HTTP 400.
3. **Geometry reprojection and measurement** — the opening geometry (EPSG:4326) is reprojected to **EPSG:3005** (BC Albers) for storage. `feature_area` is calculated from that geometry in **hectares**, rounded to four decimal places; `feature_perimeter` is measured in metres.
4. **Mapsheet derivation** — the geometry centroid (EPSG:4326) is passed to the BC OpenMaps WFS to derive the BCGS 1:20K mapsheet key (see §Mapsheet Key below). WFS failure → HTTP 422.
5. **Opening number** — the next sequential `opening_number` within the mapsheet tile is computed (`MAX + 1`, capped at 9999).
6. **Validation** — opening category code and org unit code must exist; call must be authorised for `clientNumber`; exactly one primary tenure required; no duplicate tenure combinations (same fileId + cuttingPermit + cutBlock); each tenure's cut block must exist in `silva.cut_block` and the caller must be a licensee for that cut block.
7. **Insertion** — three rows are inserted atomically in a single transaction:
   - `silva.opening` — opening header, including the Silva-owned `opening_gross_area` source of truth
   - `silva.opening_geometry` — reprojected geometry (PostGIS; EPSG:3005)
   - `silva.cut_block_open_admin` — one row per tenure; its opening gross-area value is a derived compatibility copy of the opening value
8. **Tenure reconciliation** — the associated tenure rows are synchronized with the opening gross area and with any derived disturbance values. An association-history record is written for each tenure.

---

## Error Reference

### Spatial File Errors (both `POST /api/openings/create/upload` and `POST /api/openings`)

| HTTP | Message | Cause |
|------|---------|-------|
| 400 | `Uploaded file has no name` | File part has no filename |
| 400 | `Uploaded file is null or empty` | File part is empty |
| 400 | `File exceeds 25MB size limit` | File larger than 25 MB |
| 400 | `Unsupported file type: <name>` | Extension is not `.geojson`, `.json`, or `.gml` |
| 400 | `Spatial file must contain exactly one feature (found N)` | File contains more than one feature; RISS-ls §5.4.4 requires a single opening boundary |
| 400 | `GeoJSON CRS must be EPSG:4326 or EPSG:3005 (BC Albers)` | CRS declared in the GeoJSON `crs` property is not supported |
| 400 | `Feature N missing geometry` | A feature node has no `geometry` field |
| 400 | `Feature N geometry missing 'type'` | Geometry node has no `type` field |
| 400 | `Feature N: Only Polygon and MultiPolygon geometries are supported (got '<type>')` | Geometry is a Point, LineString, etc. |
| 400 | `Feature N: Too few points in ring at feature N (coord=…)` | Polygon ring has fewer than 4 coordinates |
| 400 | `Feature N: Ring is not closed at feature N (coord=…)` | Polygon ring first and last coordinate do not match |
| 400 | `Feature N: Polygon has zero or collapsed surface area` | Geometry area rounds to zero after topology validation |
| 400 | `Invalid geometry at feature N: <detail>` | OGC topology check failed for another reason |
| 400 | `Feature N is not fully within the Province of BC boundary.` | Geometry extends outside the BC provincial boundary |
| 400 | `Invalid GeoJSON file: <detail>` | File content cannot be parsed as JSON |
| 400 | `No valid GML geometry found in file` | GML file contains no recognisable geometry element |
| 400 | `Failed to process GML file: <detail>` | GML parse error |
| 422 | *(virus scan rejection message)* | ClamAV detected malware in the uploaded file |

### Form Data Errors (`POST /api/openings` only)

| HTTP | Message | Cause |
|------|---------|-------|
| 400 | `Invalid JSON in 'data': <detail>` | The `data` part cannot be parsed as JSON |
| 400 | `<field>: <constraint message>[, …]` | Bean Validation failure on `CreateOpeningRequestDto` fields |
| 400 | `Spatial file contains no features` | Processed GeoJSON has an empty `features` array |
| 400 | `Failed to parse spatial geometry` | Geometry JSON cannot be read into a JTS object after processing |
| 400 | `Exactly one primary tenure is required; none supplied` | No tenure has `isPrimary: true` |
| 400 | `Exactly one primary tenure is required; N supplied` | More than one tenure has `isPrimary: true` |
| 400 | `Duplicate tenure at indices [i, j, …]: fileId=<id>, cuttingPermit=<id>, cutBlock=<id>` | Two or more tenures have the same fileId + cuttingPermit + cutBlock combination |
| 400 | `Cut block not found for fileId=<id>, cutBlock=<id>` | Tenure references a cut block / client combination that does not exist |
| 403 | `Not authorised for client number <number>` | Caller's JWT roles do not include `*_<clientNumber>` |
| 404 | `OpeningCategoryNotFoundException` | `openingCategoryCode` does not exist |
| 404 | `OrgUnit not found` | `orgUnitCode` does not exist |
| 422 | *(mapsheet WFS failure)* | BC OpenMaps WFS did not return a mapsheet for the geometry centroid |
| 500 | `Failed to read file: <detail>` | I/O error reading multipart bytes |
| 500 | `Failed to reproject geometry` | GeoTools reprojection from EPSG:4326 → EPSG:3005 failed |

---

## Mapsheet Key

SILVA uses the **BCGS 1:20K grid** to locate an opening on a mapsheet. The mapsheet key is composed of five components stored in separate columns:

| Column | Length | Example | Meaning |
|--------|--------|---------|---------|
| `mapsheet_grid` | 3 | `092` | NTS 1:250K sheet number (NTS series, e.g. 82, 83, 92, 93) |
| `mapsheet_letter` | 1 | `L` | NTS 1:50K block letter (A–P, W) |
| `mapsheet_square` | 3 | `057` | BCGS 1:20K block number (001–100) |
| `mapsheet_quad` | 1 | `0` | Quadrant within the 20K block (0 = full tile, 1–4 = quadrant) |
| `mapsheet_sub_quad` | 1 | `0` | Sub-quadrant (0 = full tile, 1–4 = sub-quadrant) |

The concatenated tile key for the above example is **`092L057`** — this is the 7-character `MAP_TILE` value returned by the WFS.

### How the mapsheet is derived

1. The centroid of the opening geometry (EPSG:4326) is computed using JTS `getCentroid()`.
2. A WFS `GetFeature` request is made against the BC OpenMaps layer `WHSE_BASEMAPPING.BCGS_20K_GRID` using a spatial `INTERSECTS` filter on the centroid point.
3. The `MAP_TILE` attribute of the returned feature is parsed: characters 0–2 → `grid`, character 3 → `letter`, characters 4–6 → `square`. `quad` and `subQuad` default to `"0"` (full 20K tile resolution is sufficient for opening registration).

### Opening number

Within a mapsheet tile (identified by all five components above), each opening receives a sequential `opening_number` (varchar 4, zero-padded, e.g. `"0014"`). The service computes `MAX(existing numeric values) + 1` using only rows where `opening_number ~ '^[0-9]+$'`, capped at 9999.

---

## Technical Notes

| Topic | Detail |
|-------|--------|
| Geometry storage | EPSG:3005 (BC Albers), PostGIS `geometry(Geometry,3005)` via Hibernate Spatial |
| Geometry area | `opening_geometry.feature_area` is a geometry-derived area in hectares, not the form's `openingGrossArea` value |
| Opening gross area | `opening.opening_gross_area` is the source of truth; CBOA stores a derived copy for compatibility |
| Mapsheet WFS | `https://openmaps.gov.bc.ca/geo/ows`, layer `WHSE_BASEMAPPING.BCGS_20K_GRID` |
| Opening ID | Allocated from `silva.opening_id_seq` via `SELECT nextval(...)` before the transaction; sequence gaps on rollback are acceptable |
| `feature_class_skey` | Hardcoded `2409` (RESULTS spatial utility constant) |
| `geo_district_no = admin_district_no = org_unit_no` | All three set to the same `org_unit_no` value looked up from `silva.org_unit` by `orgUnitCode` |
| Audit fields | `entry_userid` / `update_userid` use format `IDP\username` (e.g. `IDIR\RHOWELL`) in all three inserted rows |
