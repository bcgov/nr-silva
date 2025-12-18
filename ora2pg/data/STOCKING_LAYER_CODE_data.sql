SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE stocking_layer_code;

COPY stocking_layer_code (stocking_layer_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
1	Mature	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:01
2	Pole	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:01
3	Sapling	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:01
4	Regen	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:01
I	Inventory Layer	2003-05-30 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:01
\.
