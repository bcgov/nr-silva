SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE data_source_code;

COPY data_source_code (data_source_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
AP	Air Photo	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
CDMS	CDMS	1999-10-27 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
FC1	Forest Cover Maps	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
SAT	Satelite	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
SUR	Survey	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
TRIM	TRIM	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
UNK	Unknown	1998-12-10 00:00:00	9999-12-31 00:00:00	2007-08-09 09:06:47
\.
