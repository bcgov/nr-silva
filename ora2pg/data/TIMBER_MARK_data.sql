SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE timber_mark;

COPY timber_mark (timber_mark,forest_file_id,cutting_permit_id,forest_district,geographic_distrct,cascade_split_code,quota_type_code,deciduous_ind,catastrophic_ind,crown_granted_ind,cruise_based_ind,certificate,hdbs_timber_mark,vm_timber_mark,tenure_term,bcaa_folio_number,activated_userid,amended_userid,district_admn_zone,granted_acqrd_date,lands_region,crown_granted_acq_desc,mark_status_st,mark_status_date,mark_amend_date,mark_appl_date,mark_cancel_date,mark_extend_date,mark_extend_rsn_cd,mark_extend_count,mark_issue_date,mark_expiry_date,markng_instrmnt_cd,marking_method_cd,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count,small_patch_salvage_ind,salvage_type_code) FROM STDIN;
47/12K	TFL47	12K	1	1	W	A	N	N	N	N	\N	\N	47/12K	24	\N	\N	\N	C6	\N	\N	\N	HC	2004-09-07 00:00:00	\N	\N	\N	\N	\N	0	2001-07-03 00:00:00	2003-07-02 00:00:00	H	S	LDALEXAN	2001-06-07 16:06:57	IDIR\\SBONNETT	2005-09-22 14:41:06	2	N	\N
44/270	TFL44	270	2	2	W	A	N	N	N	N	\N	\N	44/270	36	\N	\N	\N	1	\N	\N	\N	HC	2008-12-04 00:00:00	\N	\N	\N	\N	\N	0	2003-11-19 00:00:00	2006-11-18 00:00:00	H	S	JAKETHEDOG	2003-11-04 10:30:08	IDIR\\JAKETHEDOG	2008-12-04 10:52:21	2	N	\N
K1AEEE	K1A	EEE	2	2	E	A	N	N	N	N	\N	\N	\N	48	\N	\N	\N	A	\N	\N	\N	HC	2019-06-13 00:00:00	\N	\N	\N	\N	\N	0	2012-11-02 00:00:00	2016-11-01 00:00:00	H	1	BCEID\\OTWIST	2012-10-25 10:55:10	IDIR\\MUGIWARA	2021-08-31 12:20:37	3	N	\N
EZ7210	A20212	210	14	14	E	A	N	N	Y	N	\N	\N	\N	48	\N	\N	\N	1	\N	4	\N	HC	2021-11-23 00:00:00	\N	\N	\N	\N	\N	0	2017-11-24 00:00:00	2021-11-23 00:00:00	H	4	BCEID\\LOLA	2017-10-19 06:49:49	IDIR\\FRANKY	2023-01-26 11:47:11	3	N	\N
77623	A77623	 	14	12	E	D	N	N	N	N	\N	77623	77623	12	\N	\N	\N	1	\N	4	\N	HC	2008-07-28 00:00:00	\N	\N	\N	2007-12-14 00:00:00	X	1	2005-12-15 00:00:00	2006-12-14 00:00:00	H	4	IDIR\\DMcMulli	2005-11-03 13:18:10	IDIR\\HEDOMINE	2008-07-28 13:16:27	12	N	\N
A1687	TA1687	 	2	2	W	D	N	N	N	N	\N	\N	\N	24	\N	\N	\N	CR1	\N	1	\N	LC	2023-06-26 00:00:00	\N	\N	\N	\N	\N	0	2022-12-12 00:00:00	2024-12-11 00:00:00	H	S	IDIR\\JPINDROC	2022-08-03 13:31:21	IDIR\\JKENNING	2023-09-27 12:01:10	6	N	\N
47/6AK	TFL47	6AK	2	15	W	A	N	N	N	N	\N	\N	\N	48	\N	\N	\N	C&E	\N	1	\N	HI	2020-05-27 13:55:39	\N	\N	\N	\N	\N	0	2020-05-25 00:00:00	2024-05-24 00:00:00	H	S	BCEID\\TRACYNG	2020-03-10 14:22:26	IDIR\\LOSTASHE	2020-05-27 13:50:24	1	N	\N
AU6001	A70881	001	1	13	E	A	N	Y	N	N	\N	\N	AU6001	24	\N	\N	\N	A	\N	\N	\N	HC	2005-07-26 00:00:00	\N	\N	\N	\N	\N	0	2003-12-03 00:00:00	2005-12-02 00:00:00	H	5	IDIR\\jcballar	2004-01-07 14:28:53	IDIR\\JSILLES	2005-07-26 10:22:13	4	N	\N
AY5001	A72929	001	1	13	E	A	N	Y	N	N	\N	\N	AY5001	24	\N	\N	\N	A	\N	\N	\N	HC	2005-07-26 00:00:00	\N	\N	\N	\N	\N	0	2003-12-03 00:00:00	2005-12-02 00:00:00	H	5	IDIR\\jcballar	2004-01-07 14:33:24	IDIR\\JSILLES	2005-07-26 11:14:34	4	N	\N
FA9145	A13840	145	2	12	E	A	N	N	N	N	\N	\N	FA9145	24	\N	\N	\N	DEAD	\N	8	\N	HC	2000-03-09 00:00:00	\N	\N	\N	\N	\N	0	1997-06-15 00:00:00	1999-06-14 00:00:00	P	4	TECUTLER	1996-12-06 08:20:54	BHENNEY	2003-10-24 14:36:10	1	N	\N
39/27Y	TFL39	27Y	1	1	W	A	N	N	Y	N	\N	\N	\N	48	\N	\N	\N	S	\N	\N	\N	HI	2008-08-19 00:00:00	\N	\N	\N	\N	\N	0	2007-08-19 00:00:00	2011-08-18 00:00:00	H	4	IDIR\\RYEE	2008-08-19 14:12:46	IDIR\\RYEE	2008-08-19 16:14:38	2	N	\N
47/12U	TFL47	12U	1	1	W	A	N	N	N	N	\N	\N	47/12U	48	\N	\N	\N	C6	\N	1	\N	HC	2005-11-10 00:00:00	\N	\N	\N	\N	\N	0	2004-02-24 00:00:00	2008-02-23 00:00:00	H	S	IDIR\\cloggie	2004-02-23 14:23:09	IDIR\\SBONNETT	2006-04-05 16:05:41	4	N	\N
ER4055	A18667	55	1	1	E	A	N	N	Y	Y	\N	ER4055	ER4055	12	\N	\N	\N	RIVE	\N	\N	\N	HC	1997-02-12 00:00:00	\N	\N	\N	1996-09-30 00:00:00	X	4	1990-10-01 00:00:00	1991-09-30 00:00:00	H	S	RDMILLER	9999-12-31 00:00:00	SLZUPP	2003-11-13 08:56:23	1	N	\N
\.
