SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_cover_non_mapped_arc;

COPY forest_cover_non_mapped_arc (forest_cover_id,non_mapped_area_id,archive_date,non_mapped_area,stocking_status_code,stocking_type_code,revision_count) FROM STDIN;
\.
