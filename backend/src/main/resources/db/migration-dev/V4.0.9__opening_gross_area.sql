ALTER TABLE silva.opening
  ADD COLUMN opening_gross_area numeric(11, 4);

COMMENT ON COLUMN silva.opening.opening_gross_area IS
  'Silva-owned source of truth for the total opening gross area in hectares.';

COMMENT ON COLUMN silva.opening_geometry.feature_area IS
  'Area of the opening geometry in hectares.';
