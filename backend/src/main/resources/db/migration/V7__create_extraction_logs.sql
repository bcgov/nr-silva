CREATE TABLE IF NOT EXISTS silva.oracle_extraction_logs (
  id                 SERIAL,
  log_message        VARCHAR(2000) NOT NULL,
  logged_at          TIMESTAMP NOT NULL,
  created_at         TIMESTAMP NOT NULL,
  manually_triggered BOOLEAN NOT NULL,
  CONSTRAINT oracle_extraction_logs_pk
    PRIMARY KEY(id)
);
