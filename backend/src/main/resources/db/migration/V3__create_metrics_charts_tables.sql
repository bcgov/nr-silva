CREATE TABLE IF NOT EXISTS silva.openings_last_year (
  opening_id           DECIMAL(10,0) NOT NULL,
  opening_entry_userid VARCHAR(30) NOT NULL,
  entry_timestamp      TIMESTAMP NOT NULL,
  update_timestamp     TIMESTAMP NOT NULL,
  CONSTRAINT openings_per_year_pk
    primary key(opening_id)
);

-- Activity type comes from THE.RESULTS_AUDIT_ACTION_CODE
-- Status comes from THE.OPENING_STATUS_CODE
CREATE TABLE IF NOT EXISTS silva.openings_activitiy (
  opening_id               DECIMAL(10,0) NOT NULL,
  activity_type_code       VARCHAR(3),
  activity_type_desc       VARCHAR(120),
  status_code              VARCHAR(3) NOT NULL,
  status_desc              VARCHAR(120) NOT NULL,
  opening_entry_timestamp  TIMESTAMP NOT NULL,
  opening_update_timestamp TIMESTAMP,
  opening_entry_userid     VARCHAR(30) NOT NULL,
  stocking_entry_timestamp TIMESTAMP,
  stocking_entry_userid    VARCHAR(30),
  audit_timestamp          TIMESTAMP,
  audit_entry_userid       VARCHAR(30),
  CONSTRAINT openings_activitiy_pk
    primary key(opening_id)
);
