SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_cover_layer_code;

COPY forest_cover_layer_code (forest_cover_layer_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
 	Inventory Layer	1905-01-01 00:00:00	2003-05-30 00:00:00	2003-11-15 19:23:56
1	Mature	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
1S	Silviculture Layer 1 - uneven aged	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
2	Pole	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
2S	Silviculture Layer 2 - uneven aged	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
3	Sapling	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
3S	Silviculture Layer 3 - uneven aged	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
4	Regen	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
4S	Silviculture Layer 4 - uneven aged	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
I	Inventory Layer	2003-05-30 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
S	Silviculture Layer - even aged	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
V	Veteran	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:56
\.
