SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE stocking_milestone_cmt_link;

COPY stocking_milestone_cmt_link (stocking_standard_unit_id,silv_milestone_type_code,silviculture_comment_id) FROM STDIN;
\.
