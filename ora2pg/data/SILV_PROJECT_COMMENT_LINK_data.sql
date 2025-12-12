SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_project_comment_link;

COPY silv_project_comment_link (silviculture_project_id,silviculture_comment_id) FROM STDIN;
\.
