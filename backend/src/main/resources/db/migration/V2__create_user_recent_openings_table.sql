-- Create sequence if it doesn't exist
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
