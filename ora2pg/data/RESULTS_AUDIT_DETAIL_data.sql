SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE results_audit_detail;

COPY results_audit_detail (results_audit_detail_id,results_audit_event_id,business_identifier,table_name,column_name,old_value,new_value,entry_userid,entry_timestamp) FROM STDIN;
\.
