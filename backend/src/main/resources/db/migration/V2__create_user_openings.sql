CREATE TABLE IF NOT EXISTS silva.user_openings (
  user_id    VARCHAR(70) NOT NULL,
  opening_id DECIMAL(10,0) NOT NULL,
  CONSTRAINT user_opening_pk
    primary key(user_id, opening_id)
);
