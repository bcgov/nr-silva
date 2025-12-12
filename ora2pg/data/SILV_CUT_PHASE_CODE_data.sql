SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_cut_phase_code;

COPY silv_cut_phase_code (silv_cut_phase_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
ESTAB	Establishment Cut	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
PREPC	Preparatory Cut	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
RECRU	Recruitment	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
REMOV	Removal	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
SALVF	Salvage - Fire	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
SALVO	Salvage - Other	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
SALVP	Salvage - Pest	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
SALVW	Salvage - Wind	1905-01-01 00:00:00	9999-12-31 00:00:00	2003-11-15 19:23:58
\.
