SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_system_variant_code;

COPY silv_system_variant_code (silv_system_variant_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
CTN	Commercial thin	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
GRP	Group	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
IRR	Irregular	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
NAT	Natural	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
NUR	Nurse	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
POL	Pole removal	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
SIN	Single	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
STR	Strip	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
UNI	Uniform	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:00
\.
