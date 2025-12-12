SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE stocking_type_code;

COPY stocking_type_code (stocking_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
 	No stocking type required	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
ART	Artificial	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
BR	Brush	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
FOR	Forested	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
NAT	Natural	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
NPL	Non-plantable	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
PL	Plantable	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
RD	Road	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
RHR	Rehabilitated Roads	2001-02-05 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
UNN	Unnatural	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
\.
