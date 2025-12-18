SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE request_item;

COPY request_item (request_skey,item_id,standard_workplan_skey,vegetation_code,sample_id,family_lot_number,rqst_sdlngs_rsrvd,rqst_sdlngs_srpls,cone_ind,no_of_containers,no_of_cones,cone_volume,seed_quantity,sample_elevation,shipto_cli_number,shipto_cli_locn_cd,required_by_date,latest_actioned_date,rcmnded_sowing_date,spcfied_sowing_date,tender_number,tender_bid_group,tender_issued_date,assigned_price,nrsry_obligatn_ind,nrsry_cli_number,nrsry_cli_locn_cd,facility_code,extrct_cli_number,extrct_cli_locn_cd,authorization_code,srv_blng_create_ts,sample_loc_desc,interim_storge_loc,authorized_by_prsn,test_approved_userid,test_approved_date,test_update_userid,test_update_timestamp,test_comment,seedlot_qa_test_status_code) FROM STDIN;
484573	A	2	\N	\N	\N	0	215	\N	\N	\N	\N	\N	\N	\N	\N	\N	2018-01-24 00:00:00	2018-03-01 00:00:00	\N	\N	\N	\N	0	Y	00163874	00	GH	\N	\N	\N	2018-02-27 05:35:09	\N	\N	\N	\N	\N	\N	\N	\N	\N
482715	A	129295	\N	\N	\N	0	50	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-09-28 00:00:00	2018-02-01 00:00:00	\N	\N	\N	\N	0	Y	00167120	10	GH	\N	\N	\N	2018-01-24 05:35:08	\N	\N	\N	IDIR\\LKLADE	2017-09-12 00:00:00	IDIR\\LKLADE	2017-09-12 09:55:36	\N	ACT
484402	A	2	\N	\N	\N	0	699.8	\N	\N	\N	\N	\N	\N	\N	\N	\N	2018-03-27 00:00:00	2018-05-10 00:00:00	2018-05-02 00:00:00	\N	\N	\N	0	Y	00167120	02	GH	\N	\N	\N	2018-04-24 05:35:09	\N	\N	\N	IDIR\\LKLADE	2018-01-04 00:00:00	IDIR\\LKLADE	2018-01-04 15:00:24	\N	ACT
266432	A	3	\N	\N	\N	0	56	\N	\N	\N	\N	\N	\N	\N	\N	\N	2010-03-14 00:00:00	2010-04-26 00:00:00	\N	2010F	084	\N	17.5	Y	00020589	00	GH	\N	\N	\N	2010-05-04 15:19:28	\N	\N	\N	\N	\N	\N	\N	\N	\N
266434	A	2	\N	\N	\N	0	73.6	\N	\N	\N	\N	\N	\N	\N	\N	\N	2010-01-20 00:00:00	2010-02-25 00:00:00	\N	2010E	096	\N	22.3	Y	00150102	11	GH	\N	\N	\N	2010-05-04 15:19:28	\N	\N	\N	\N	\N	\N	\N	\N	\N
\.
