SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE mark_extension_reason_code;

COPY mark_extension_reason_code (mark_extension_reason_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
E	Equipment Failure	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
H	Health/Death	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
L	Legislation	2013-01-30 00:00:00	9999-12-31 00:00:00	2014-03-12 12:25:26
M	Markets	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
P	Management Discretion	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-03-12 12:25:26
T	Transfer	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
W	Weather	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
X	Miscellaneous	1905-01-01 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:26
\.
