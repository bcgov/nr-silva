SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_cover_archive_geometry;

COPY forest_cover_archive_geometry (forest_cover_id,archive_date,geometry,feature_area,feature_perimeter,feature_class_skey,data_source_code,capture_method_code,observation_date,data_quality_comment,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
\.
