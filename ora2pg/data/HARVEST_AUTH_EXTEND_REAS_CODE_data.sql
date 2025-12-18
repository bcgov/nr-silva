SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE harvest_auth_extend_reas_code;

COPY harvest_auth_extend_reas_code (harvest_auth_extend_reas_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
E	Equipment Failure	2006-10-06 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
H	Health/Death	2006-10-07 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
L	Legislation	2013-01-30 00:00:00	9999-12-31 00:00:00	2014-03-12 12:25:24
M	Markets	2006-10-08 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
P	Management Discretion	2006-10-09 00:00:00	9999-12-31 00:00:00	2014-03-12 12:25:24
T	Transfer	2006-10-10 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
W	Weather	2006-10-11 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
X	Miscellaneous	2006-10-12 00:00:00	2013-01-30 00:00:00	2014-03-12 12:25:24
\.
