SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE activity_tu_comment_link;

COPY activity_tu_comment_link (silviculture_comment_id,activity_treatment_unit_id) FROM STDIN;
\.
