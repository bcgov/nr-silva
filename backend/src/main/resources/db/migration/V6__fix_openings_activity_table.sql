-- drop tables and recreate fixing primary keys and null columns
DROP TABLE silva.openings_activity;

CREATE TABLE IF NOT EXISTS silva.openings_activity (
  activity_id        INTEGER NOT NULL,
  opening_id         DECIMAL(10,0) NOT NULL,
  activity_type_code VARCHAR(3),
  activity_type_desc VARCHAR(120),
  status_code        VARCHAR(3),
  status_desc        VARCHAR(120),
  last_updated       TIMESTAMP NOT NULL,
  entry_userid       VARCHAR(30) NOT NULL,
  CONSTRAINT openings_activitiy_pk
    PRIMARY KEY(activity_id, opening_id)
);

DROP TABLE silva.openings_last_year;

CREATE TABLE IF NOT EXISTS silva.openings_last_year (
  opening_id           DECIMAL(10,0) NOT NULL,
  opening_entry_userid VARCHAR(30) NOT NULL,
  entry_timestamp      TIMESTAMP NOT NULL,
  update_timestamp     TIMESTAMP NOT NULL,
  status_code          VARCHAR(3),
  org_unit_code        VARCHAR(6),
  org_unit_name        VARCHAR(120),
  client_number        VARCHAR(8),
  CONSTRAINT openings_per_year_pk
    PRIMARY KEY(opening_id)
);
