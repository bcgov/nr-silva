SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_admin_zone;

COPY silv_admin_zone (dist_admin_zone,org_unit_no,admin_zone_desc,revision_count) FROM STDIN;
ER	1	ZONE R	1
\.
