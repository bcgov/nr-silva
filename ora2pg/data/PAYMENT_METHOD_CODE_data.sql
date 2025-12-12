SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE payment_method_code;

COPY payment_method_code (payment_method_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
A	Automatic Invoice	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:03
C	Cash	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:03
M	Manual Invoice	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:05:03
\.
