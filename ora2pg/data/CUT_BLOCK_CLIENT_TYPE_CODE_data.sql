SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE cut_block_client_type_code;

COPY cut_block_client_type_code (cut_block_client_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
D	Applicant	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
E	Contractor	2006-08-09 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
L	Licensee Operating Division	2006-08-09 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
M	Previous Licensee Operating Division	2006-08-09 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
O	Silviculture Obligation	2006-08-09 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
S	Appraisals & Billing Mailing	2006-08-09 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
R	RESULTS Silviculture Obligation	2014-02-11 00:00:00	9999-12-31 00:00:00	2014-08-22 08:14:43
\.
