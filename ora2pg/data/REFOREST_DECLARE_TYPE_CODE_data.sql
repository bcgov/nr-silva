SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE reforest_declare_type_code;

COPY reforest_declare_type_code (reforest_declare_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
CON	Coniferous	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:55
DEC	Deciduous	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:55
MIX	Mix	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:55
\.
