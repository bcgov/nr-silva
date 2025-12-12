SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE site_index_source_code;

COPY site_index_source_code (site_index_source_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
A	Site index from adjacent stand	1999-03-16 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
C	SI from site index curve	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
E	SI from Biogeoclimatic Ecosystem Classification	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
H	SI from stand before harvest	1999-03-16 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
I	SI from growth intercept	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
M	SI from G, M, P, L site class conversion	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
O	SI from provincial SIBEC rollover, Nov 1998	1998-11-30 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
S	Site index assigned by District Silviculture Section	1999-03-16 00:00:00	9999-12-31 00:00:00	2003-06-17 14:16:37
\.
