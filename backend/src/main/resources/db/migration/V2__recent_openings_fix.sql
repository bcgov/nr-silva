-- We need to make sure that we removed any duplicates from the table,
-- using the user_id and opening_id columns as the unique key
DELETE FROM silva.user_recent_openings
WHERE id NOT IN (
  SELECT MIN(id)
  FROM silva.user_recent_openings
  GROUP BY user_id, opening_id
);

-- Update user_recent_openings to set a composite primary key with user_id and opening_id,
-- and remove the existing primary key constraint
ALTER TABLE silva.user_recent_openings DROP CONSTRAINT user_recent_openings_pkey;
ALTER TABLE silva.user_recent_openings DROP COLUMN id;

-- Also drop the sequence because it's unnecessary
DROP SEQUENCE silva.user_recent_openings_seq;

-- Add the composite primary key constraint
ALTER TABLE silva.user_recent_openings ADD CONSTRAINT user_recent_openings_pk PRIMARY KEY (opening_id,user_id);

