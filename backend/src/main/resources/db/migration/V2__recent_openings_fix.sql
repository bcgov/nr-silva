-- We need to make sure that we removed any duplicates from the table,
-- using the user_id and opening_id columns as the unique key
DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'silva'
      AND table_name = 'user_recent_openings'
      AND column_name = 'id'
  ) THEN
    DELETE FROM silva.user_recent_openings
    WHERE id NOT IN (
      SELECT MIN(id)
      FROM silva.user_recent_openings
      GROUP BY user_id, opening_id
    );
  END IF;
END $$;


-- Update user_recent_openings to set a composite primary key with user_id and opening_id,
-- and remove the existing primary key constraint
DO $$
BEGIN
  -- Check and drop the existing primary key constraint
  IF EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_schema = 'silva'
      AND table_name = 'user_recent_openings'
      AND constraint_name = 'user_recent_openings_pkey'
  ) THEN
    ALTER TABLE silva.user_recent_openings DROP CONSTRAINT user_recent_openings_pkey;
  END IF;

  -- Check and drop the 'id' column if it exists
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'silva'
      AND table_name = 'user_recent_openings'
      AND column_name = 'id'
  ) THEN
    ALTER TABLE silva.user_recent_openings DROP COLUMN id;
  END IF;

  -- Check and drop the sequence if it exists
  IF EXISTS (
    SELECT 1 FROM pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relkind = 'S'
      AND n.nspname = 'silva'
      AND c.relname = 'user_recent_openings_seq'
  ) THEN
    DROP SEQUENCE silva.user_recent_openings_seq;
  END IF;

  -- Check if the composite primary key already exists before adding it
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_schema = 'silva'
      AND table_name = 'user_recent_openings'
      AND constraint_type = 'PRIMARY KEY'
  ) THEN
    ALTER TABLE silva.user_recent_openings
      ADD CONSTRAINT user_recent_openings_pk PRIMARY KEY (opening_id, user_id);
  END IF;
END $$;


