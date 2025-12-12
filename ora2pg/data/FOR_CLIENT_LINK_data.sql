SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE for_client_link;

COPY for_client_link (for_client_link_skey,forest_file_id,cutting_permit_id,cut_block_id,file_client_type,client_number,client_locn_code,licensee_start_dt,licensee_end_date,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
\.
