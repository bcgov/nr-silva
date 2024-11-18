CREATE SCHEMA IF NOT EXISTS silva;

CREATE TABLE IF NOT EXISTS silva.user_openings (
  user_id    VARCHAR(70) NOT NULL,
  opening_id DECIMAL(10,0) NOT NULL,
  CONSTRAINT user_opening_pk primary key(user_id, opening_id)
);

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


-- Activity type comes from THE.RESULTS_AUDIT_ACTION_CODE
-- Status comes from THE.OPENING_STATUS_CODE
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

CREATE TABLE IF NOT EXISTS silva.oracle_extraction_logs (
  id                 SERIAL,
  log_message        VARCHAR(2000) NOT NULL,
  logged_at          TIMESTAMP NOT NULL,
  created_at         TIMESTAMP NOT NULL,
  manually_triggered BOOLEAN NOT NULL,
  CONSTRAINT oracle_extraction_logs_pk
    PRIMARY KEY(id)
);

-- Create sequence if it doesn't exist for User Recent Openings
CREATE SEQUENCE IF NOT EXISTS silva.user_recent_openings_seq
START WITH 1
INCREMENT BY 1
MINVALUE 1
NO MAXVALUE
CACHE 30;

-- Use the sequence in your table creation or insert statements
CREATE TABLE IF NOT EXISTS silva.user_recent_openings (
    id BIGINT PRIMARY KEY DEFAULT nextval('silva.user_recent_openings_seq'),
    opening_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    last_viewed TIMESTAMP DEFAULT NOW()
);
