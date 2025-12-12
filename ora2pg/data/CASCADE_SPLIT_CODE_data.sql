SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE cascade_split_code;

COPY cascade_split_code (cascade_split_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
E	East	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:04:56
W	West	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:04:56
\.
