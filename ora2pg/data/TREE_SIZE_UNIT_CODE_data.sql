SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE tree_size_unit_code;

COPY tree_size_unit_code (tree_size_unit_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
%	Percent	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
CM	Centimeters	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
\.
