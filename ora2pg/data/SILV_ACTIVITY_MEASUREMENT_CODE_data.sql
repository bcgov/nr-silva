SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_activity_measurement_code;

COPY silv_activity_measurement_code (silv_activity_measurement_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
DY	Days	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:57
HA	Hectares	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:57
HL	Hectolitres	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:57
HR	Hours	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:57
KM	Kilometers	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:57
\.
