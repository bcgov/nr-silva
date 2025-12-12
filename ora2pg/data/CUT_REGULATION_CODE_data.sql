SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE cut_regulation_code;

COPY cut_regulation_code (cut_regulation_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
BBR	Bark Beetle Regulation	2006-08-09 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:28
\.
