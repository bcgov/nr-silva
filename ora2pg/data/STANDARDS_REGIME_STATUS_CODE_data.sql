SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE standards_regime_status_code;

COPY standards_regime_status_code (standards_regime_status_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
APP	Approved	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-05-27 11:21:43
DFT	Draft	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-05-27 11:21:43
EXP	Expired	2008-03-19 00:00:00	9999-12-31 00:00:00	2008-05-27 11:21:43
REJ	Rejected	2008-03-19 00:00:00	9999-12-31 00:00:00	2008-05-27 11:21:43
SUB	Submitted	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-05-27 11:21:43
\.
