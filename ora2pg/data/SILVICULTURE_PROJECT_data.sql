SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silviculture_project;

COPY silviculture_project (silviculture_project_id,silv_base_code,fiscal_year,org_unit_no,project_sequence,project_location,silv_project_status_code,client_number,start_date,view_date,coordinator_userid,overhead_cost,crew_contract_hire_code,dist_admin_zone,unit_bid_code,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
7	SU	2010	1	001	BurnsLake /SquareLake	COM	\N	2009-05-01 00:00:00	\N	stevens	\N	T	\N	HA	IDIR\\CSTEVENS	2009-03-23 20:05:42	BCEID\\SJONES12	2017-11-07 10:26:28	326
14	SU	2014	1	001	NRFL Square Lake area	COM	00010006	2013-05-06 00:00:00	2013-05-01 00:00:00	Stevens SU14845-001	\N	T	\N	HA	IDIR\\CSTEVENS	2009-08-18 10:18:14	IDIR\\CSTEVENS	2014-03-18 16:34:53	229
15	SU	2018	1	001	NRFL south & Square Lake area	COM	00010008	2017-06-15 00:00:00	2017-04-25 00:00:00	Stevens/DND	\N	T	\N	HA	IDIR\\CSTEVENS	2009-08-18 10:36:44	IDIR\\CSTEVENS	2018-01-12 11:21:28	471
16	BR	2011	1	001	NRFL SquareLake area	COM	00010005	2010-06-28 00:00:00	\N	Stevens	\N	T	\N	HA	IDIR\\CSTEVENS	2009-08-18 13:07:34	IDIR\\CSTEVENS	2014-02-25 11:26:04	29
25	PL	2012	1	002	square Lk	COM	00010004	2011-05-13 00:00:00	2011-05-11 00:00:00	\N	\N	D	\N	TRE	IDIR\\CSTEVENS	2011-05-11 12:43:11	IDIR\\CSTEVENS	2014-02-25 11:40:57	200
26	PL	2012	1	003	\N	COM	00010006	2011-05-13 00:00:00	2011-05-11 00:00:00	Stevens	\N	D	\N	TRE	IDIR\\CSTEVENS	2011-05-11 12:46:26	IDIR\\CSTEVENS	2014-02-25 11:40:57	106
\.
