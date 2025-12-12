SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE opening_geometry;

COPY opening_geometry (opening_id,geometry,feature_area,feature_perimeter,feature_class_skey,capture_method_code,data_source_code,observation_date,data_quality_comment,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
\.
