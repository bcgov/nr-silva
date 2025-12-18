SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE site_class_code;

COPY site_class_code (site_class_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
G	Good	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
L	Low	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
M	Medium	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
P	Poor	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
\.
