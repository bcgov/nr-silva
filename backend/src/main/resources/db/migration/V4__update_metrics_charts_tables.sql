alter table silva.openings_last_year
  add column status_code   VARCHAR(3) NOT NULL,
  add column org_unit_code VARCHAR(6) NOT NULL;
