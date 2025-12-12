SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE harvest_type_code;

COPY harvest_type_code (harvest_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
F	Fort St. John	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:30
G	Green	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:30
M	Multi-Mark	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:30
R	Road	2006-10-06 00:00:00	9999-12-31 00:00:00	2007-06-02 09:58:30
\.
