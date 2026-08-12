# Add Tenure — Validation Endpoint

## Overview

The tenure validation endpoint checks one or more tenure (cut block) entries before an opening is created. It verifies that:

1. All required fields are present and within allowed lengths.
2. Each cut block exists in `silva.cut_block`.
3. The requesting client is a licensee of each cut block (`silva.cut_block_client`).
4. No cut block already has an allocated opening (`silva.cut_block_open_admin`).
5. No two tenures in the same request describe the same cut block.

The caller must provide their `clientNumber`; the service verifies the JWT contains a role ending in `_<clientNumber>` and returns **403 Forbidden** if not.

---

## Endpoint

```
POST /api/tenures/validate?clientNumber={clientNumber}
Content-Type: application/json
Authorization: Bearer <JWT>
```

### Query Parameters

| Parameter      | Type   | Required | Description                         |
|----------------|--------|----------|-------------------------------------|
| `clientNumber` | String | Yes      | Ministry client number (≤ 8 chars). |

### Request Body

A JSON array of tenure objects:

```json
[
  {
    "fileId":        "TFL001",
    "cuttingPermit": "CP01",
    "cutBlock":      "CB001",
    "isPrimary":     true
  },
  {
    "fileId":        "TFL002",
    "cuttingPermit": null,
    "cutBlock":      "CB002",
    "isPrimary":     false
  }
]
```

| Field            | Type    | Required | Max Length | Notes                              |
|------------------|---------|----------|------------|------------------------------------|
| `fileId`         | String  | Yes      | 10         | Licence / Tree Farm Licence ID.    |
| `cuttingPermit`  | String  | No       | 3          | Null allowed; matched as NULL in DB.|
| `cutBlock`       | String  | Yes      | 10         | Cut block identifier.              |
| `isPrimary`      | Boolean | Yes      | —          | Exactly one tenure must be `true`. |
## Response

### Success (HTTP 200)

```json
{
  "validationResults": [
    { "tenureIndex": 0, "isValid": true,  "errorCode": null,             "errorMessage": null },
    { "tenureIndex": 1, "isValid": false, "errorCode": "CLIENT_NOT_LICENSEE", "errorMessage": "Client 12345678 is not a licensee of cut block 1042." }
  ],
  "duplicateConflicts": [],
  "isValid": false
}
```

| Field                | Type    | Description                                          |
|----------------------|---------|------------------------------------------------------|
| `validationResults`  | Array   | Per-tenure result. Index aligns with request array.  |
| `duplicateConflicts` | Array   | Duplicate groups found within the request.           |
| `isValid`            | Boolean | `true` only when all results and no conflicts exist. |

### TenureValidationResultDto

| Field          | Type    | Description                                      |
|----------------|---------|--------------------------------------------------|
| `tenureIndex`  | Integer | Zero-based index into the request array.         |
| `isValid`      | Boolean | `false` means this tenure failed validation.     |
| `errorCode`    | String  | Machine-readable code (see table below). Null if valid. |
| `errorMessage` | String  | Human-readable description. Null if valid.       |

### DuplicateConflictDto

| Field             | Type         | Description                                              |
|-------------------|--------------|----------------------------------------------------------|
| `duplicateIndices`| Integer[]    | Indices of the tenures that form this duplicate group.   |
| `reason`          | String       | Human-readable duplicate description.                    |
| `conflictCode`    | String       | `DUPLICATE_IN_REQUEST`                                   |

---

## Error Codes

| Code                      | HTTP layer | Meaning                                                                  |
|---------------------------|------------|--------------------------------------------------------------------------|
| `FIELD_INVALID`           | 200 body   | A required field is blank or exceeds max length.                         |
| `TENURE_NOT_FOUND`        | 200 body   | No matching row in `silva.cut_block`.                                    |
| `CLIENT_NOT_LICENSEE`     | 200 body   | The client is not recorded as a licensee in `silva.cut_block_client`.    |
| `TENURE_DUPLICATE_OPENING`| 200 body   | A `cut_block_open_admin` row already exists with a non-null `opening_id`.|
| `DUPLICATE_IN_REQUEST`    | 200 body   | Two or more tenures in the same request resolve to the same cut block.   |
| *(HTTP 403)*              | 403        | JWT does not contain role ending in `_<clientNumber>`.                   |

---

## Business Validation Flow

```
For each tenure in request:
  1. Field validation (forestFileId, cutBlockId required; length checks)
     → FIELD_INVALID on failure
  2. Look up silva.cut_block WHERE forest_file_id / cut_block_id / cutting_permit_id
     → TENURE_NOT_FOUND if no row
  3. Check silva.cut_block_client WHERE cb_skey = <found> AND client_number = <clientNumber>
     → CLIENT_NOT_LICENSEE if no row
  4. Check silva.cut_block_open_admin WHERE same key AND opening_id IS NOT NULL
     → TENURE_DUPLICATE_OPENING if row exists

After per-tenure checks:
  5. Detect duplicates: group by (forestFileId, cutBlockId, cuttingPermitId)
     → DUPLICATE_IN_REQUEST conflict for each group with > 1 member

isValid = all validationResults.isValid AND duplicateConflicts.isEmpty()
```

---

## Examples

### Valid Request

**Request:**
```http
POST /api/tenures/validate?clientNumber=00012345
Authorization: Bearer eyJ...
Content-Type: application/json

[
  { "forestFileId": "TFL001", "cuttingPermitId": "CP1", "cutBlockId": "CB01", "isPrimaryTenure": true }
]
```

**Response (200):**
```json
{
  "validationResults": [
    { "tenureIndex": 0, "isValid": true, "errorCode": null, "errorMessage": null }
  ],
  "duplicateConflicts": [],
  "isValid": true
}
```

### Invalid — Block Not Found

**Response (200):**
```json
{
  "validationResults": [
    {
      "tenureIndex": 0,
      "isValid": false,
      "errorCode": "TENURE_NOT_FOUND",
      "errorMessage": "Cut block TFL001/CB01 (permit CP1) not found."
    }
  ],
  "duplicateConflicts": [],
  "isValid": false
}
```

### Invalid — Duplicate In Request

**Response (200):**
```json
{
  "validationResults": [
    { "tenureIndex": 0, "isValid": true, "errorCode": null, "errorMessage": null },
    { "tenureIndex": 1, "isValid": true, "errorCode": null, "errorMessage": null }
  ],
  "duplicateConflicts": [
    {
      "duplicateIndices": [0, 1],
      "reason": "Tenures at indices [0, 1] refer to the same cut block.",
      "conflictCode": "DUPLICATE_IN_REQUEST"
    }
  ],
  "isValid": false
}
```

### Unauthorized — JWT Role Missing (HTTP 403)

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Client number 00012345 not found in JWT roles."
}
```

---

## Related Classes

| Class | Package | Role |
|-------|---------|------|
| `TenureEndpoint` | `postgres.endpoint` | REST controller — exposes `POST /api/tenures/validate` |
| `TenureValidationService` | `postgres.service` | Core validation logic |
| `CutBlockPostgresRepository` | `postgres.repository` | Queries `silva.cut_block` |
| `CutBlockClientPostgresRepository` | `postgres.repository` | Queries `silva.cut_block_client` |
| `CutBlockOpenAdminPostgresRepository` | `postgres.repository` | Queries `silva.cut_block_open_admin` |
| `CutBlockEntity` | `postgres.entity` | JPA entity for `silva.cut_block` |
| `CutBlockClientEntity` | `postgres.entity` | JPA entity for `silva.cut_block_client` |
| `TenureValidationResponseDto` | `postgres.dto` | Top-level response shape |
| `TenureValidationResultDto` | `postgres.dto` | Per-tenure result |
| `DuplicateConflictDto` | `postgres.dto` | Duplicate group descriptor |
