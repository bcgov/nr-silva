SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE open_viewable_category;

COPY open_viewable_category (open_category_code,opening_status_code,viewable_ind) FROM STDIN;
\.
