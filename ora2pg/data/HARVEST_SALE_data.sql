SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE harvest_sale;

COPY harvest_sale (forest_file_id,sb_fund_ind,sale_method_code,sale_type_cd,planned_sale_date,tender_opening_dt,plnd_sb_cat_code,sold_sb_cat_code,total_bidders,lumpsum_bonus_amt,cash_sale_est_vol,cash_sale_tot_dol,payment_method_cd,salvage_ind,sale_volume,admin_area_ind,minor_facility_ind,bcts_org_unit,fta_bonus_bid,fta_bonus_offer,revision_count,entry_userid,entry_timestamp,update_userid,update_timestamp) FROM STDIN;
TFL47	N	A	SL	1985-01-01 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	14	\N	\N	1	FTA_CONV	2003-11-15 11:00:39	HDBSMIG	1991-11-05 03:33:15
TFL44	N	A	SL	1984-08-01 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	11	\N	\N	2	FTA_CONV	2003-11-15 11:00:39	IDIR\\JAKETHEDOG	2024-12-04 13:31:35
A20212	N	A	SL	1982-12-01 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	\N	\N	\N	8	FTA_CONV	2003-11-15 11:00:22	IDIR\\HSOLO	2024-12-30 08:06:09
A77623	Y	T	SL	2005-12-15 00:00:00	2005-12-15 00:00:00	A	A	1	\N	\N	\N	A	N	17015	N	N	12	\N	\N	13	IDIR\\DHAIGH	2005-02-18 08:45:11	IDIR\\HEDOMINE	2008-07-28 13:16:27
TA1687	Y	T	SL	2022-12-05 00:00:00	2022-12-05 00:00:00	2	2	6	\N	\N	\N	A	N	17840	N	N	14	\N	\N	7	IDIR\\DMSUMNER	2020-11-02 14:22:17	IDIR\\JKENNING	2023-09-27 12:01:10
A13840	N	D	SL	1983-01-01 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	\N	\N	\N	8	FTA_CONV	2003-11-15 11:00:22	IDIR\\CRMARSH	2016-10-07 14:48:17
TFL39	N	A	SL	1980-01-01 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	\N	\N	\N	1	FTA_CONV	2003-06-10 14:16:51	HDBSMIG	1991-11-05 03:39:18
A18667	N	A	SL	1982-11-15 00:00:00	\N	N	N	\N	\N	\N	\N	A	N	\N	N	N	\N	\N	\N	8	FTA_CONV	2003-11-15 11:00:22	IDIR\\LEJACKSO	2017-05-17 15:49:11
\.
