# Client Number and Location Determination for Opening ID

## Overview

When looking up client information for an opening, the system must navigate through multiple database tables to identify the responsible party. An opening can have relationships with multiple client types, so a priority hierarchy determines which client to select.

## Client Type Priority Hierarchy

The system recognizes three types of client relationships, evaluated in this priority order:

1. **Responsibility (R)** - Highest priority. The party responsible for executing the work on the opening.
2. **Obligation (O)** - Middle priority. A party with an obligation related to the opening.
3. **Licensee (A)** - Lowest priority. The forest license holder if no R or O client exists.

## Data Flow and Joins

### Step 1: Locate the Opening's Cut Block

Start with the `OPENING` table using the opening ID, then join to `CUT_BLOCK_OPEN_ADMIN` with a filter for the prime license:

```sql
WHERE cboa.opening_prime_licence_ind = 'Y'
```

This ensures we're working with the primary license holder relationship for the opening.

### Step 2: Join to Client Tables

From the cut block admin record, join to three client sources:

- **Responsibility Client**: `CUT_BLOCK_CLIENT` where `cut_block_client_type_code = 'R'`
- **Obligation Client**: `CUT_BLOCK_CLIENT` where `cut_block_client_type_code = 'O'`
- **Licensee Client**: `FOREST_FILE_CLIENT` where `forest_file_client_type_code = 'A'`

All joins use LEFT JOIN to ensure the query returns results even if some client types don't exist.

### Step 3: Apply Priority Logic

Use the `COALESCE()` function to select the client number and location code in priority order:

```sql
COALESCE(cbc_r.client_number, cbc_o.client_number, ffc.client_number) as client_number
COALESCE(cbc_r.client_locn_code, cbc_o.client_locn_code, ffc.client_locn_code) as client_locn_code
```

This returns the first non-null value, enforcing the R > O > A priority.

## Example Scenarios

**Scenario 1: Opening has a Responsibility client**

- R client found: `COALESCE(R_value, O_value, A_value)` returns R_value
- Result: Responsibility client is displayed

**Scenario 2: Opening has no Responsibility client but has an Obligation client**

- R client is NULL, O client found: `COALESCE(NULL, O_value, A_value)` returns O_value
- Result: Obligation client is displayed

**Scenario 3: Opening has neither Responsibility nor Obligation client**

- R and O are NULL, A client found: `COALESCE(NULL, NULL, A_value)` returns A_value
- Result: Licensee client is displayed

## Where This Logic Applies

This client determination logic is used across six key queries:

- Silviculture Search
- Opening Tombstone Details
- Activity Search
- Disturbance Search
- Standard Unit Search

Both client number and location code follow the same priority hierarchy in all queries.
