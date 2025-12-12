SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE corp_capture_method;

COPY corp_capture_method (capture_method_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
\.
