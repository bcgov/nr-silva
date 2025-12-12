SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_comment_source_code;

COPY silv_comment_source_code (silv_comment_source_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
ALL	All	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
AUDT	Audit	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
ECO	SP Eco-Stratum	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
OPEN	Opening	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
PHSP	Pre-Harvest Silviculture Prescription	2003-11-26 00:00:00	2003-11-26 00:00:00	2003-11-26 15:07:36
PLAN	Plans and Goals	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
PROJ	Project	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
RIP	Riparian Prescription	1998-07-13 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
RSLT	Record Activities/Surveys	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
SP	Silviculture Prescription	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
SSYS	Silvicultural System	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
SU	Standards Unit	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-26 15:07:36
\.
