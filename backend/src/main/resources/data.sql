CREATE SCHEMA IF NOT EXISTS silva;

-- Example
CREATE TABLE IF NOT EXISTS silva.person (
  id INT NOT NULL,
  name VARCHAR(30) NOT NULL,
  CONSTRAINT person_pk
    PRIMARY KEY(id)
);

DROP TABLE silva.person;
