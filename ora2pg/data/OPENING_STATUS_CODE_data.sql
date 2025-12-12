SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE opening_status_code;

COPY opening_status_code (opening_status_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
AMG	Amalgamate	2005-04-07 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
FG	Free Growing	2003-04-10 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
AMD	Amended	1905-01-01 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
APP	Approved	1905-01-01 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
DFT	Draft	1905-01-01 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
RET	Retired	2013-10-21 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
RMD	Removed	2006-08-31 00:00:00	2015-05-11 00:00:00	2015-05-12 09:38:33
SUB	Submitted	1905-01-01 00:00:00	9999-12-31 00:00:00	2015-05-12 09:38:33
\.
