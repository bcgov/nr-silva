SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_statute_code;

COPY silv_statute_code (silv_statute_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
\.
