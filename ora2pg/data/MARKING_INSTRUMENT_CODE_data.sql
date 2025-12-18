SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE marking_instrument_code;

COPY marking_instrument_code (marking_instrument_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
B	Paint and/or Crayon	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
C	Crayon	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
H	Hammer	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
P	Paint	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
T	Tag	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:02
\.
