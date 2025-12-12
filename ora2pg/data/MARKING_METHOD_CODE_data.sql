SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE marking_method_code;

COPY marking_method_code (marking_method_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
0	100% Marking	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
1	100% One End	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
2	2 Marks Front, 2 on Sides	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
3	2 Marks Front, 2 on Sides to Site; 100% at Site	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
4	Four Corner Marking	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
5	4 Corner Marking to Site; 100% at Site	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
8	Eight corner marking	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
A	35% With Mark Painted Orange on Each Side of Load	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
B	35% With Mark Painted Blue on Each Side of Load	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
C	20% With 16 Log Minimum	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
D	Bottom Tier Front and Back on Area, 100% at Scale Site	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
E	Exempt From Marking	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
S	Full, Standard Marking	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
\.
