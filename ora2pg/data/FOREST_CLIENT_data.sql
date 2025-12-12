SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_client;

COPY forest_client (client_number,client_name,legal_first_name,legal_middle_name,client_status_code,client_type_code,birthdate,client_id_type_code,client_identification,registry_company_type_code,corp_regn_nmbr,client_acronym,wcb_firm_number,ocg_supplier_nmbr,client_comment,add_timestamp,add_userid,add_org_unit,update_timestamp,update_userid,update_org_unit,revision_count) FROM STDIN;
00010002	SKYWALKERS RANCH	\N	\N	ACT	C	\N	\N	\N	BC	0010002	\N	\N	\N	\N	2025-08-25 11:40:18	BCEID\\ASKYWALKER	1	2025-08-25 11:40:18	BCEID\\ASKYWALKER	1	1
00010003	THE CONTINENTAL	\N	\N	ACT	C	\N	\N	\N	BC	0010003	\N	\N	\N	\N	2025-08-25 11:40:18	IDIR\\BOWERY	1	2025-08-25 11:40:18	IDIR\\BOWERY	1	3
00010004	PARABELLUM	\N	\N	ACT	C	\N	\N	\N	BC	0010004	\N	\N	\N	\N	2025-08-25 11:40:18	IDIR\\BOWERY	1	2025-08-25 11:40:18	IDIR\\BOWERY	1	3
00010005	BALLERINA	\N	\N	ACT	C	\N	\N	\N	BC	0010005	\N	\N	\N	\N	2025-08-25 11:40:18	IDIR\\BOWERY	1	2025-08-25 11:40:18	IDIR\\BOWERY	1	3
00010006	THOUSAND SUNNY	\N	\N	ACT	C	\N	\N	\N	BC	0010006	\N	\N	\N	\N	2025-08-25 11:40:18	IDIR\\BOWERY	1	2025-08-25 11:40:18	IDIR\\BOWERY	1	3
00010008	GOING MERRY	\N	\N	ACT	C	\N	\N	\N	BC	0010008	\N	\N	\N	\N	2025-08-25 11:40:18	IDIR\\BOWERY	1	2025-08-25 11:40:18	IDIR\\BOWERY	1	3
\.
