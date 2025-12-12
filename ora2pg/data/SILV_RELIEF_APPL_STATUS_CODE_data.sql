SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_relief_appl_status_code;

COPY silv_relief_appl_status_code (silv_relief_appl_status_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
APP	Approved	1905-01-01 00:00:00	9999-12-31 00:00:00	2005-05-16 10:38:55
DFT	Draft	1905-01-01 00:00:00	9999-12-31 00:00:00	2005-05-16 10:38:55
SUB	Submitted	1905-01-01 00:00:00	9999-12-31 00:00:00	2005-05-16 10:38:55
\.
