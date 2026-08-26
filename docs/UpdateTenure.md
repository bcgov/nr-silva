# Update Opening Tenures — PUT /api/openings/{openingId}/tenures

## Business Context

An opening must always have one, and only one, **primary tenure**. This endpoint replaces the
opening's complete tenure list with the list saved from the edit screen. It supports adding a
tenure, changing a tenure, changing the primary tenure, and removing a tenure.

A tenure is identified by its forest file, cutting permit (when applicable), and cut block.
Changing any of those values is treated as removing the old tenure and adding a new one.

This endpoint is available while SILVA runs with PostgreSQL as its primary database.

---

## Request

`PUT /api/openings/{openingId}/tenures?clientNumber={clientNumber}`

The request body is the final list of tenures for the opening.

```json
[
  {
    "cboaId": 98765,
    "revisionCount": 3,
    "fileId": "A12345",
    "cuttingPermit": "CP1",
    "cutBlock": "1",
    "isPrimary": true
  },
  {
    "fileId": "B67890",
    "cuttingPermit": null,
    "cutBlock": "2",
    "isPrimary": false
  }
]
```

| Field | Required | Meaning |
|-------|----------|---------|
| `cboaId` | Existing tenure only | The CBOA record returned by the opening tenure list. Omit for a new tenure. |
| `revisionCount` | Existing tenure only | Version of the CBOA record when the edit screen was loaded. |
| `fileId` | Yes | Forest file ID. |
| `cuttingPermit` | No | Cutting permit ID, when the tenure has one. |
| `cutBlock` | Yes | Cut block ID. |
| `isPrimary` | Yes | Identifies the one primary tenure. |

---

## Business Rules

1. **At least one tenure is required.** An opening cannot be left without a tenure.
2. **Exactly one tenure must be primary.** No primary or more than one primary is rejected.
3. **The caller must be authorised for the client number.** The caller's role must match the
   supplied client number.
4. **Every tenure must be valid for that client.** The cut block must exist and the client must be
   a licensee for it.
5. **Duplicate tenure keys are not allowed.** Two submitted rows cannot have the same forest file,
   cutting permit, and cut block.
6. **A tenure cannot be allocated to two openings.** A current tenure on this opening is allowed to
   remain; the same tenure already allocated to a different opening is rejected.
7. **Existing rows must still belong to this opening.** A submitted `cboaId` from another opening,
   or a repeated `cboaId` in the request, is rejected.
8. **Existing rows must not be stale.** A row is stale when its submitted `revisionCount` differs
   from the current saved revision count. This means it changed after the edit screen loaded.
9. **Disturbance work protects tenure membership.** If the opening has an assigned disturbance
   activity with base code `DN`, no tenure can be removed or replaced. Changing only the primary
   designation is still allowed.

---

## Update Flow

1. Confirm that the opening exists.
2. Confirm the final list is not empty and contains exactly one primary tenure.
3. Validate the submitted tenure keys, the caller's client authority, cut blocks, licensee
   membership, and duplicate allocation rules.
4. Compare each submitted existing tenure with the opening's current tenure records. Reject rows
   that do not belong to the opening or have changed since the screen was loaded.
5. Determine which existing tenures are retained, removed, or replaced. A changed tenure key is a
   replacement.
6. If anything is being removed or replaced, check for an assigned `DN` disturbance activity. If
   one exists, reject the removal or replacement.
7. Update retained tenures when their primary designation changed.
8. Associate added or replacement tenures. SILVA reuses an available unassociated tenure record
   when one exists; otherwise it creates a new record.
9. Unassociate removed tenures from the opening. The tenure source record remains available, but
   its opening-specific values are cleared.
10. Record every association and unassociation in tenure-association history, including who made
    the change and when.

All accepted changes are saved together. A failed validation leaves the opening's tenure list
unchanged.

---

## Response

### Success — HTTP 204

The complete replacement was saved. There is no response body.

### Validation failure — HTTP 422

The response identifies field-level tenure validation failures and, when applicable, the existing
tenures that cannot be removed because of a `DN` disturbance activity.

### Other outcomes

| HTTP | Meaning |
|------|---------|
| 403 | Caller is not authorised for `clientNumber`. |
| 404 | Opening does not exist. |
| 422 | Tenure list violates a business rule, such as no primary tenure, stale data, duplicate allocation, or a blocked removal. |
