SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE sb_category_code;

COPY sb_category_code (sb_category_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
1	Non-milling	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
2	Milling	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
3	Proposed Mill	1999-10-26 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
9	District Manager	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
A	Any/All Registrant Categories	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
N	Non-Registrant	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-18 12:42:43
4	Value Added	1905-02-23 00:00:00	9999-12-31 00:00:00	2024-02-27 15:23:20
\.
