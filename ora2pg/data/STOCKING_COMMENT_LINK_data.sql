SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE stocking_comment_link;

COPY stocking_comment_link (stocking_standard_unit_id,silviculture_comment_id) FROM STDIN;
\.
