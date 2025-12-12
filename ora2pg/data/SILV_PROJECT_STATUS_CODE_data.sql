SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_project_status_code;

COPY silv_project_status_code (silv_project_status_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
ACT	Active	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:32
CAN	Cancelled	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:32
COM	Complete	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:32
PLN	Planning	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:32
\.
