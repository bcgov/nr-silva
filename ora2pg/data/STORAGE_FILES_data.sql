SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE storage_files;

COPY storage_files (group_name_cd,name,local_directory,tile_size,data_maturity_cd,steward_org_unit_cd,update_responsibility,entry_timestamp,entry_userid,update_timestamp,update_userid,description,special_concerns,data_availability) FROM STDIN;
\.
