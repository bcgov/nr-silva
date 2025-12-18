SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_reserve_code;

COPY silv_reserve_code (silv_reserve_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
C	Carbon Offset	2011-09-20 00:00:00	9999-12-31 00:00:00	2013-09-13 09:29:41
D	Dispersed	2001-07-09 00:00:00	9999-12-31 00:00:00	2013-09-13 09:29:41
G	Group	1905-01-01 00:00:00	9999-12-31 00:00:00	2013-09-13 09:29:41
M	Mixed	1905-01-01 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
N	No Reserve	1905-01-01 00:00:00	9999-12-31 00:00:00	2013-09-13 09:29:41
O	Other Reserve	1999-10-15 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
R	Riparian Reserve	1999-10-15 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
U	Uniform	1905-01-01 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
V	Variable	1905-01-01 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
W	Wildlife Tree Patch Reserve	1999-10-15 00:00:00	2009-11-30 00:00:00	2013-09-13 09:29:41
\.
