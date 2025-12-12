SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_cover_geometry;

COPY forest_cover_geometry (forest_cover_id,geometry,feature_area,feature_perimeter,capture_method_code,data_source_code,feature_class_skey,observation_date,data_quality_comment,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
\.
