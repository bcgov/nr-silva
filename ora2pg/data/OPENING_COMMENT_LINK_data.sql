SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE opening_comment_link;

COPY opening_comment_link (opening_id,silviculture_comment_id) FROM STDIN;
101017	1
1009974	2
1524010	3
\.
