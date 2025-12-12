SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE height_class_code;

COPY height_class_code (height_class_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
1	0 - 10.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
2	10.5 - 19.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
3	19.5 - 28.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
4	28.5 - 37.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
5	37.5 - 46.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
6	46.5 - 55.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
7	55.5 - 64.4 Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
8	64.5 + Meters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:35
\.
