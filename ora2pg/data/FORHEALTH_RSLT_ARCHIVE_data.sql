SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forhealth_rslt_archive;

COPY forhealth_rslt_archive (forest_cover_id,archive_date,forest_cover_layer_id,forhealth_rslt_id,opening_id,silv_damage_agent_code,incidence_pct,incidence_area,observation_date,forest_cover_archive_date,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
\.
