SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE stocking_class_code;

COPY stocking_class_code (stocking_class_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
0	Immature Stands	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
1	Stocking Class 1	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
2	Stocking Class 2	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
3	Stocking Class 3	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
4	Stocking Class 4	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
5	Stocking Class 5	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
R	Mature Residual	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
\.
