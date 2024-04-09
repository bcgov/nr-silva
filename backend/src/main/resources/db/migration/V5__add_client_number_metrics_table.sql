alter table silva.openings_last_year
  add column client_number   VARCHAR(8) NOT NULL;

-- drop table and recreate fixing the table name and columns
drop table silva.openings_activitiy;

create table IF NOT EXISTS silva.openings_activity (
  opening_id         DECIMAL(10,0) NOT NULL,
  activity_type_code VARCHAR(3),
  activity_type_desc VARCHAR(120),
  status_code        VARCHAR(3) NOT NULL,
  status_desc        VARCHAR(120) NOT NULL,
  last_updated       TIMESTAMP NOT NULL,
  entry_userid       VARCHAR(30) NOT NULL,
  constraint openings_activitiy_pk
    primary key(opening_id)
);
