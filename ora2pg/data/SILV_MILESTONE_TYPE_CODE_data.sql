SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_milestone_type_code;

COPY silv_milestone_type_code (silv_milestone_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
RG	Regeneration	2003-04-10 00:00:00	9999-12-31 00:00:00	2003-12-22 13:30:06
FG	Free Growing	2003-04-10 00:00:00	9999-12-31 00:00:00	2003-12-22 13:30:06
PH	Post Harvest	2003-04-10 00:00:00	9999-12-31 00:00:00	2003-12-22 13:30:06
NR	No Regeneration	2003-04-10 00:00:00	9999-12-31 00:00:00	2003-12-22 13:30:06
\.
