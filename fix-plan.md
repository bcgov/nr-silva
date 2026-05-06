# Issue 1293 - Standards Unit FSP Logic Fix

## Root Cause

`GET_OPENING_SS` query LEFT JOINs `FSP_STANDARDS_REGIME_XREF`. One `standards_regime_id`
can map to multiple `fsp_id` rows, so the same standards unit is returned once per FSP ID,
producing duplicates. Opening 1282255: 4 unique SUs but 26 rows returned.

---

## Key Files

### Backend Java
- `backend/src/main/java/.../oracle/SilvaOracleQueryConstants.java` — GET_OPENING_SS (Oracle)
- `backend/src/main/java/.../postgres/SilvaPostgresQueryConstants.java` — GET_OPENING_SS (Postgres)
- `.../common/service/opening/details/impl/AbstractOpeningDetailsStockingService.java` — no dedup today
- `.../common/projection/opening/OpeningStockingDetailsProjection.java` — getFspId()
- `.../common/dto/opening/OpeningDetailsStockingDetailsDto.java` — fspId field

### Oracle DB Source (THE schema)
- `nr-mof-db/scripts/THE/PACKAGE_BODIES/R__06394_FSP_300_INFORMATION.sql` — get_licenses()
- `nr-mof-db/scripts/THE/PACKAGE_BODIES/R__06411_FSP_COMMON.sql` — get_agreement_holder_fdus()
- `nr-results/database/ddl/pkg/RESULTS_310_STOCKING_STDS.PKS` — get_fsp_id_list() with client matching
- `nr-results/database/ddl/fnc/RESULTS_GET_FSP_ID_LIST.FNC` — display only, rownum < 5, no client matching

---

## Table Chain for Fix

### Opening's client (Silva already has access)
```
CUT_BLOCK_OPEN_ADMIN (opening_prime_licence_ind = 'Y')
  → CUT_BLOCK_CLIENT type R or O   (priority: R > O > A)
  → FOREST_FILE_CLIENT type A
```

### FSP's client — NEEDS ACCESS
```
FSP_AGREEMENT_HOLDER (fsp_id + fsp_amendment_number → client_number)
```

### FSP's tenure/forest_file_id — NEEDS ACCESS
```
FDU_LICENCE_XREF (fsp_id + fsp_amendment_number → forest_file_id)
  JOIN FOREST_FILE_CLIENT (forest_file_id + client_number + type = 'A')
```

### Opening's primary tenure (Silva already has access)
```
CUT_BLOCK_OPEN_ADMIN.forest_file_id WHERE opening_prime_licence_ind = 'Y'
```

---

## Fix Strategy

1. Uniquify standards units per opening (straightforward)
2. Client matching: match FSP_AGREEMENT_HOLDER.client_number to opening's resolved client
3. Tenure tie-breaker (if more than 1 FSP remains after step 2): match
   FDU_LICENCE_XREF.forest_file_id to opening's primary forest_file_id

---

## Oracle Access Required (not yet granted)

| Table | Purpose |
|---|---|
| `THE.FSP_AGREEMENT_HOLDER` | FSP to client_number mapping |
| `THE.FDU_LICENCE_XREF` | FSP to forest_file_id mapping |

---

## Open Questions (cannot resolve without prod data access)

1. A standards unit with no FSP is valid — show null fspId (already works today)
2. Whether tenure tie-breaker is ever triggered in prod data
3. What to do if more than 1 FSP remains after both steps — needs business decision
4. FSP_STATUS_CODE filter (APP/INE): current GET_OPENING_SS has no filter and neither
   does the RESULTS display function. Adding it could drop valid FSP IDs.
   Cannot decide without querying prod data.

---

## Important: RESULTS FSP Display is Not a Reliable Reference

`RESULTS_GET_FSP_ID_LIST.FNC` used for the RESULTS310 display:
- `rownum < 5` hard cap — shows at most 4 FSP IDs
- No client matching at all
- Just raw rows from `fsp_standards_regime_xref` ordered by `fsp_id`

The "4 standards units in RESULTS" for opening 1282255 is coincidental. RESULTS
is not doing intelligent FSP selection on the display screen.

---

## Reference Docs
- `docs/OpeningClient.md` — R > O > A client priority hierarchy and join logic
