SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE sale_method_code;

COPY sale_method_code (sale_method_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
A	Auction, Oral	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-11-03 16:10:09
B	Bid Proposal	2002-12-23 00:00:00	9999-12-31 00:00:00	2008-11-03 16:10:09
D	Direct Award	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-11-03 16:10:09
N	Direct Award First Nations	2008-07-23 00:00:00	9999-12-31 00:00:00	2008-11-03 16:10:09
T	Auction, Sealed Tender	1905-01-01 00:00:00	9999-12-31 00:00:00	2008-11-03 16:10:09
\.
