SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE feature_classes;

COPY feature_classes (feature_class_skey,group_name_cd,name,feature_type_cd,data_maturity_cd,overlapping_feature_ind,span_tile_ind,derived_data_ind,mandatory_metadata_ind,custodian_org_unit_cd,data_standards_manager,steward_org_unit_cd,extended_org_unit_cd,extended_data_contact,discipline_standard_cd,collection_process,entry_timestamp,entry_userid,update_timestamp,update_userid,description,comments) FROM STDIN;
\.
