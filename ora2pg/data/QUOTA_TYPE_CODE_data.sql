SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE quota_type_code;

COPY quota_type_code (quota_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
A	Normal replaceable AAC	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
B	Temporary AAC approved by Chief Forester	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
C	Forest Service Reserve	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
D	Small Business Apportionment	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
J	Non-Quota Timber sold in a TSA/TFL, includes salvage or opportunity wood and Private Timber Marks.	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
K	Undercut from TFLs/FLs (Small Business)	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
L	Undercut of TFLs/FLs (OTHER) (expired 89 /06/	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
M	Non-QUOTA, Small Business Only (expired  89/0	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
O	Opportunity wood (Timber outside net lan d ba	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
P	Any Timber sale sold under a Pulpwood Ag reem	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:05
\.
