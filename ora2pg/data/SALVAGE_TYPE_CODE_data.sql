SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE salvage_type_code;

COPY salvage_type_code (salvage_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
BBR	Bark Beetle Regulation	2005-04-06 00:00:00	9999-12-31 00:00:00	2005-06-23 11:23:19
SSS	Small Scale Salvage	2005-04-06 00:00:00	9999-12-31 00:00:00	2005-06-23 11:23:19
\.
