SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE fire_harvesting_reason_code;

COPY fire_harvesting_reason_code (fire_harvesting_reason_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
PREVENTION	Harvesting of materials for fire prevention	2024-04-01 00:00:00	9999-12-31 00:00:00	2024-04-18 11:54:20
RESTORE	Harvesting of materials for restoration	2024-04-01 00:00:00	9999-12-31 00:00:00	2024-04-18 11:54:20
SALVAGE	Salvage of burned timber	2024-04-01 00:00:00	9999-12-31 00:00:00	2024-04-18 11:54:20
\.
