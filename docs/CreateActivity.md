# Create an Activity - Draft

## 1. Purpose

Users can create activities under an Opening in Silva. Activities are created and submitted one at a time.

This document describes:

- The common activity creation flow
- Planned and Completed activity requirements
- Basic activity bases
- Non-basic activity bases (Planting)
- Form validation and submission behaviour
- Planting-specific requirements
- API contract for the create activity endpoint

---

## 2. Activity Types

### 2.1 Activity Status

The user must first select an activity status.

An activity can be:

- Planned
- Completed

There is no default status. The user must make a selection before continuing.

### 2.2 Activity Base

After the user selects an activity status, the Activity Base field appears.

The user must select exactly one activity base from a dropdown.

After an activity base is selected, the applicable form sections appear.

### 2.3 Basic and Non-basic Activity Bases

Basic activity bases do not require an additional base-specific information section.

Basic activity bases: AU, BR, CC, CM, EP, FE, HC, HI, JS, LB, PC, PR, RC, RD, SP, ST, SU, SX.

The following are non-basic activity bases:

- Planting
- Direct seeding
- Native species planting — a new activity base code introduced in Silva

Non-basic activity bases require an additional information section after the Activity Information section.

This draft includes the additional requirements for Planting. Requirements for the other non-basic activity bases will be added later.

The create activity endpoint currently accepts the basic bases only. Any other base code is rejected.

---

## 3. Changing the Activity Status

The user can switch between Planned and Completed.

### Empty Form

If the form is empty, the user can switch the status normally.

### Form Contains Information

If the form has been partially or fully completed, switching the status displays a confirmation modal.

This applies when switching:

- From Planned to Completed
- From Completed to Planned

The modal warns the user that the information currently entered in the form will be cleared.

---

## 4. Planned Activity

When the user selects Planned, the following sections appear after an activity base has been selected:

- Activity Information
- Spatial Information, when applicable
- A base-specific section when a non-basic activity base is selected

### 4.1 Planned Activity Information

| Field | Requirement | Input | Business rules |
|---|---|---|---|
| Technique | Optional | Single-select | The user can select up to one technique. Only options currently in effect can be selected. |
| Method | Optional | Single-select | The user can select up to one method. Only options currently in effect can be selected. |
| Objectives | Optional | Multi-select | The user can select up to five objectives. Only options currently in effect can be selected. |
| Funding sources | Required | Multi-select | The user can select up to five funding sources. Only options currently in effect can be selected. |
| Project number | Optional | Text | This replaces the term “Intra-agency Number” used in RESULTS. |
| Planned date | Required | Date | The date must be later than the current date. |
| Planned area | Required | Number, in hectares | The area cannot exceed the gross area of the Opening under which the activity is being created. |
| Planned cost | Optional | Number, in dollars | Records the expected cost of the activity. |
| Licensee activity ID | Optional | Text | Allows users to record an identifier used as a reference in their own systems. |
| Comment | Optional | Text | Allows the user to provide additional information about the activity. |

### 4.2 Funding Sources

Silva allows users to select multiple funding sources for one activity.

This differs from RESULTS, where users can select only one funding source.

---

## 5. Completed Activity

When the user selects Completed, the user must select an activity base in the same way as for a Planned activity.

The Activity Information section contains the same fields as the Planned activity form, plus the following fields.

### 5.1 Additional Completed Activity Information

| Field | Requirement | Input | Business rules |
|---|---|---|---|
| Completion date | Required | Date | The date can be any point in time. No past or future date restriction has been specified. |
| Treated area | Required | Number, in hectares | The treated area cannot exceed the Opening’s gross area. |
| Actual cost | Optional | Number, in dollars | Records the actual cost of completing the activity. |

### 5.2 Treated Area Validation

If the treated area exceeds the Opening’s gross area:

- The field is invalid.
- An input-level error message is displayed.
- The error message includes the actual gross area of the Opening.

---

## 6. Spatial Information

The Spatial Information section has the same behaviour for Planned and Completed activities.

### 6.1 Availability

The activity-level spatial file upload is optional.

The upload field is not displayed when the activity base is:

- Planting
- Native Plants

### 6.2 Supported Files

Users can select a file or drag and drop it into the upload field.

| Requirement | Rule |
|---|---|
| Supported formats | GeoJSON and GML |
| Maximum file size | 25 MB |
| Upload requirement | Optional |

### 6.3 Spatial File Validation

Validation begins automatically after the user selects a file.

The validation returns either:

- A successful result; or
- An error result
- Outside of the opening boundary

If validation is successful, Silva displays a map preview of the activity area.

A file that fails validation causes the spatial input to remain invalid until the user provides a valid file or removes the optional file.

---

## 7. Basic Activity Form Actions

For a basic activity base, the common Activity Information and applicable Spatial Information sections make up the complete form.

Two actions are displayed at the bottom of the form:

- Cancel
- Create activity

### 7.1 Cancel

If the form is empty, selecting Cancel returns the user to the Activity tab on the Opening details page.

If the form has been touched, selecting Cancel displays a confirmation modal before the user leaves the form.

### 7.2 Create Activity

When the user selects Create activity, Silva validates the entire form before submitting it.

#### Validation Fails

The form is not submitted if one or more inputs:

- Contain an error
- Are required but empty
- Have failed validation

Silva displays the relevant error state or message for each invalid input.

The user remains on the form and can correct the errors.

#### Validation Succeeds

If the form is valid, Silva submits the activity for server-side processing.

##### Submission Fails

If submission fails:

- The user remains on the activity creation page.
- Silva displays an error notification.
- The information entered in the form is retained.
- The user can try submitting the form again.

##### Submission Succeeds

If submission succeeds:

1. The user is returned to the Opening details page.
2. The page automatically scrolls to the Silviculture Activities section.
3. The Silviculture Activities accordion is expanded.
4. The newly created activity row is highlighted in the activities table.

---

## 8. Non-basic Activity Bases

A non-basic activity includes:

- The common Activity Information section
- The applicable Spatial Information section
- An additional section containing information specific to the selected activity base

The following sections currently document the Planting activity base. They are not yet supported by the create activity endpoint.

---

## 9. Planting Activity

When the selected activity base is Planting, Silva displays a Planting Specifications section below the Activity Information section.

The Planting Specifications section contains one or more Planting Units.

### 9.1 Planting Unit

Each Planting Unit contains the following information.

| Field | Requirement | Input | Business rules |
|---|---|---|---|
| Name | Required | Text | The name can contain a maximum of four characters. |
| Planted area | Required | Number, in hectares | Records the area associated with the Planting Unit. |
| Total planted | Display only | Calculated value | Displays the sum of Number planted across all Planting Stocks in the Planting Unit. |
| Planting Stocks | Required | Repeatable section | Each Planting Unit must contain at least one Planting Stock. |
| Upload planting unit spatial file | Optional | Spatial file upload | Records the spatial area associated with the individual Planting Unit. |

### 9.2 Planting Unit Spatial Rules

Polygons belonging to different Planting Units under the same activity cannot overlap.

If an uploaded polygon overlaps a polygon belonging to another Planting Unit in the activity, the spatial input is invalid.

### 9.3 Adding and Removing Planting Units

When the Planting form is initialized:

- One Planting Unit is displayed.
- An activity must always contain at least one Planting Unit.
- The initial Planting Unit cannot be removed while it is the only unit.

The user can select Add planting unit to add another Planting Unit.

When the activity contains more than one Planting Unit:

- A Remove planting unit button appears on every Planting Unit.
- The user can remove a Planting Unit.
- The last remaining Planting Unit cannot be removed.

---

## 10. Planting Stock

Each Planting Unit must contain one or more Planting Stocks.

### 10.1 Planting Stock Information

| Field | Requirement | Input | Business rules |
|---|---|---|---|
| Additional stock information | TBD | TBD | Additional Planting Stock fields remain to be defined. |
| Seedlot number | Required | Number | The seedlot must exist in SPAR. The seedlot number must be unique within the Planting Unit. |
| Species | Display only | Derived value | The species is derived from the selected seedlot information. |
| Number planted | Required | Number | Included in the Total planted calculation for the Planting Unit. |
| Number beyond transfer limit | Optional | Number | Records the number planted beyond the applicable transfer limit. |

### 10.2 Seedlot Validation

The entered seedlot number must:

- Exist in SPAR
- Not already be used by another Planting Stock in the same Planting Unit

After the seedlot has been validated, Silva displays the species associated with it.

### 10.3 Total Planted

Total planted is calculated separately for each Planting Unit.

It is the sum of Number planted across all Planting Stocks in that Planting Unit.

### 10.4 Adding and Removing Planting Stocks

When a Planting Unit is initialized:

- One Planting Stock is displayed.
- The Planting Unit must always contain at least one Planting Stock.
- The initial Planting Stock cannot be removed while it is the only stock.

The user can select Add planting stock to add another Planting Stock.

When a Planting Unit contains more than one Planting Stock:

- A Remove planting stock button appears on every Planting Stock.
- The user can remove a Planting Stock.
- The last remaining Planting Stock cannot be removed.

---

## 11. Saving Planting Spatial Information

Planting does not use the activity-level spatial file upload.

Instead, spatial information can be uploaded separately for each Planting Unit.

When the Planting activity is saved:

1. Each Planting Unit’s spatial data is saved individually.
2. Silva combines the available Planting Unit polygons into a single multipolygon.
3. The combined multipolygon is saved as the activity-level spatial data.

When a Planting Stock is saved, don't set CBST flag. 

---

## 12. API Contract

`POST /api/openings/{openingId}/activities` (multipart/form-data, PostgreSQL mode only).

| Part | Requirement | Description |
|---|---|---|
| `data` | Required | JSON request body (below) |
| `file` | Optional | Spatial file, GeoJSON or GML, up to 25 MB. An empty file is ignored. |

Request body fields:

| Field | Applies to | Notes |
|---|---|---|
| `status` | All | `PLANNED` or `COMPLETED`, required |
| `baseCode` | All | Required; a basic base |
| `techniqueCode`, `methodCode` | All | Optional; must be in effect |
| `objectiveCodes` | All | Optional, up to 5; must be in effect |
| `fundingSourceCodes` | All | Required, 1 to 5; must be in effect |
| `projectNumber`, `licenseeActivityId`, `comment` | All | Optional |
| `plannedDate`, `plannedArea` | Planned required; Completed optional | Planned date must be later than today; planned area greater than 0 |
| `plannedCost` | All | Optional, whole dollars |
| `completionDate`, `treatedArea` | Completed required; not allowed when Planned | Treated area greater than 0 |
| `actualCost` | Completed only | Optional, whole dollars |

Responses: `201` created, `400` validation errors (each listed as `field: message`), `403` no allowed role for the opening's primary licensee client, `404` opening not found, `422` file rejected by the virus scanner.

---

## 13. Information Still to Be Defined

The following items require further clarification or additional requirements:

- The remaining Planting Stock fields currently marked as TBD
- The requirements for Direct seeding
- The requirements for Native species planting
- Whether “Native Plants” and “Native species planting” refer to the same activity base
- Whether Planting specifications apply identically to Planned and Completed activities
- How the activity-level Planting geometry is created when some Planting Units do not have spatial files
- Whether the sum of Planting Unit planted areas must equal the activity’s Planned area or Treated area
- Whether the sum of Planting Unit areas must remain within the Opening’s gross area
- Minimum values, decimal precision, and formatting rules for area, cost, and quantity fields
- The exact confirmation actions and outcomes for status-change and cancellation modals