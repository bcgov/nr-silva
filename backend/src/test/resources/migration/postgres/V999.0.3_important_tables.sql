-- All forest clients

INSERT INTO silva.forest_client (client_number, client_type_code, client_name, legal_first_name, legal_middle_name, client_status_code, birthdate, registry_company_type_code, corp_regn_nmbr, client_acronym, client_id_type_code, client_identification, wcb_firm_number, ocg_supplier_nmbr, client_comment, add_timestamp, add_userid, add_org_unit, update_timestamp, update_userid, update_org_unit, revision_count) VALUES
('00010002', 'C', 'SKYWALKERS RANCH', null, null, 'ACT', null, 'BC', '0010002', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'BCEID\ASKYWALKER', 1, CURRENT_TIMESTAMP(6), 'BCEID\ASKYWALKER', 1, 1),
('00010003', 'C', 'THE CONTINENTAL'  ,null, null, 'ACT', null, 'BC', '0010003', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, 3),
('00010004', 'C', 'PARABELLUM'       ,null, null, 'ACT', null, 'BC', '0010004', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, 3),
('00010005', 'C', 'BALLERINA'        ,null, null, 'ACT', null, 'BC', '0010005', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, 3),
('00010006', 'C', 'THOUSAND SUNNY'   ,null, null, 'ACT', null, 'BC', '0010006', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, 3),
('00010008', 'C', 'GOING MERRY'      ,null, null, 'ACT', null, 'BC', '0010008', null, null, null, null, null, null, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, CURRENT_TIMESTAMP(6), 'IDIR\BOWERY'     , 1, 3);

INSERT INTO silva.client_location (client_number, client_locn_code, client_locn_name, hdbs_company_code, address_1, address_2, address_3, city, province, postal_code, country, business_phone, home_phone, cell_phone, fax_number, email_address, locn_expired_ind, returned_mail_date, trust_location_ind, cli_locn_comment, update_timestamp, update_userid, update_org_unit, add_timestamp, add_userid, add_org_unit, revision_count) VALUES
('00010002', '03', 'OAKWOOD PARK ADDRESS', null   , '510 FULTON PLAZA'    , null              , null, 'FORT MCMURRAY'  , 'AB', 'T9J9R1', 'CANADA', '8823899685', '2911911672', '5229472760', null        , 'RGRIMSTON9@DMOZ.ORG', 'Y', null, 'Y', NULL, CURRENT_TIMESTAMP(6), 'IDIR\\IBEWICKE9', 70, CURRENT_TIMESTAMP(6), 'IDIR\\CSIDDEN9', 70, 617),
('00010003', '13', 'THE OTHER ONE'       , '1'    , 'BOX 86131'           , NULL              , NULL, 'NORTH VANCOUVER', 'BC', 'V7L4J5', 'CANADA', '6049999999', NULL        , NULL        , '6049999998', NULL                 , 'N', NULL, 'N', NULL, CURRENT_TIMESTAMP(6), 'ITSWATTLES', 70, CURRENT_TIMESTAMP(6), 'CONV', 70, 1),
('00010004', '00', 'THAT ADDRESS'        , '40000', 'ATTN: JOHNATHAN WICK', 'BOX 42999'       , NULL, 'SQUAMISH'       , 'BC', 'V0N3G0', 'CANADA', NULL        , NULL        , NULL        , NULL        , NULL                 , 'N', NULL, 'N', NULL, CURRENT_TIMESTAMP(6), 'MILESM', 70, CURRENT_TIMESTAMP(6), 'MILESM', 70, 1),
('00010005', '00', '04 FARM'             , ' '    , 'SEE "02" LOCATION'   , NULL              , NULL, 'SQUAMISH'       , 'BC', 'A1A2B2', 'CANADA', NULL        , NULL        , NULL        , NULL        , NULL                 , 'Y', NULL, 'Y', NULL, CURRENT_TIMESTAMP(6), 'IDIR\\TOOMUCH', 70, CURRENT_TIMESTAMP(6), 'IDIR\\ALFRED', 70, 1),
('00010006', '00', 'STOMP FORTRESS'      , '40000', 'ATTN: JOHNATHAN WICK', 'BOX 42999'       , NULL, 'SQUAMISH'       , 'BC', 'V0N3G0', 'CANADA', NULL        , NULL        , NULL        , NULL        , NULL                 , 'N', NULL, 'N', NULL, CURRENT_TIMESTAMP(6), 'MILESM', 70, CURRENT_TIMESTAMP(6), 'MILESM', 70, 1),
('00010008', '00', 'ALABASTA'       , '1'    , 'BOX 86131'           , NULL              , NULL, 'NORTH VANCOUVER', 'BC', 'V7L4J5', 'CANADA', '6049999999', NULL        , NULL        , '6049999998', NULL                 , 'N', NULL, 'N', NULL, CURRENT_TIMESTAMP(6), 'ITSWATTLES', 70, CURRENT_TIMESTAMP(6), 'CONV', 70, 1);

-- TFL 47
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
(45, 'T', '47', 'TFL 47  Duncan Bay', 'FTA_CONV', TIMESTAMP '2003-11-15 18:37:18', 'FTA_CONV', TIMESTAMP '2003-11-15 18:37:18', 0, TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00'),
(424,'U','47','GBR South TSA','IDIR\CABANILL',TIMESTAMP'2017-01-03 14:47:46','IDIR\CABANILL',TIMESTAMP'2017-01-03 14:47:46',0,TIMESTAMP'2016-07-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid)
VALUES('TFL47', 'HI', TIMESTAMP '1993-07-07 00:00:00', 'A02', 2, NULL, 'N', NULL, 'T', '47', 5, 'L', TIMESTAMP '1991-07-12 19:00:00', 'IDIR\ACARLSON', TIMESTAMP '2024-10-09 14:30:08', 'B994A7AB1AA22408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp)
VALUES('TFL47', 'N', 'A', 'SL', TIMESTAMP '1985-01-01 00:00:00', NULL, 'N', 'N', NULL, NULL, NULL, NULL, 'A', 'N', NULL, 'N', 'N', 14, NULL, NULL, 1, 'FTA_CONV', TIMESTAMP '2003-11-15 11:00:39', 'HDBSMIG', TIMESTAMP '1991-11-05 03:33:15');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
(116838, 'TFL47', '12K', NULL, 1, 'C6', 1, '47', 'T', NULL, 'G', 'HC', 24, TIMESTAMP '2004-09-07 00:00:00', TIMESTAMP '2001-07-03 00:00:00', TIMESTAMP '2003-07-02 00:00:00', NULL, 0, NULL, 'A', NULL, NULL, 'W', 'N', 'N', 'N', 'N', NULL, 'QUADRA ISLAND', NULL, NULL, TIMESTAMP '2006-10-12 00:00:00', 4, 'LDALEXAN', TIMESTAMP '2001-06-07 16:06:57', 'DATAFIX_06072007_134C', TIMESTAMP '2007-07-11 11:06:48', 'U', 'U', 'U', 'U', 'B9B090AC82BB645AE05332B3228EA5A5'),
(779356,'TFL47','6AK',NULL,2,'C&E',2,NULL,NULL,NULL,'G','HI',48,TIMESTAMP'2020-05-27 13:55:39',TIMESTAMP'2020-05-25 00:00:00',TIMESTAMP'2024-05-24 00:00:00',NULL,0,NULL,'A','1',NULL,'W','N','N','N','N',NULL,'Hardwick Is. Gowland & Jackson Bay',NULL,NULL,NULL,4,'BCEID\TRACYNG',TIMESTAMP'2020-03-10 14:22:26','IDIR\LOSTASHE',TIMESTAMP'2020-05-27 13:55:39','U','U','U','Y','B9B090AE6920645AE05332B3228EA5A5'),
(125896,'TFL47','12U',NULL,1,'C6',1,'47','T',NULL,'G','HC',48,TIMESTAMP'2005-11-10 00:00:00',TIMESTAMP'2004-02-24 00:00:00',TIMESTAMP'2008-02-23 00:00:00',NULL,0,NULL,'A','1',NULL,'W','N','N','N','N',NULL,'Quadra Island',NULL,NULL,TIMESTAMP'2006-10-12 00:00:00',6,'IDIR\cloggie',TIMESTAMP'2004-02-23 14:23:09','DATAFIX_06072007_134C',TIMESTAMP'2007-07-11 11:09:06','U','U','U','U','B9B090ACA5CA645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
('47/12K', 'TFL47', 'S', 'H', 2, TIMESTAMP '2001-06-07 16:06:57', 'IDIR\SBONNETT', TIMESTAMP '2005-09-22 14:41:06', 'LDALEXAN', 'B994A76C51772408E05332B3228E5273'),
('47/6AK','TFL47','S','H',1,TIMESTAMP'2020-03-10 14:22:26','IDIR\LOSTASHE',TIMESTAMP'2020-05-27 13:50:24','BCEID\TRACYNG','B994A76C52532408E05332B3228E5273'),
('47/12U','TFL47','S','H',4,TIMESTAMP'2004-02-23 14:23:09','IDIR\SBONNETT',TIMESTAMP'2006-04-05 16:05:41','IDIR\cloggie','B994A76C517F2408E05332B3228E5273');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
(440645, 'TFL47', '00010003', '13', 'A', TIMESTAMP '2012-02-21 13:04:06', NULL, 1, 'CLIENT_REORG', TIMESTAMP '2012-02-21 13:04:06', 'CLIENT_REORG', TIMESTAMP '2012-02-21 13:04:06');

-- TFL 44
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date)
VALUES(42, 'T', '44', 'TFL 44  Alberni', 'FTA_CONV', TIMESTAMP '2003-11-15 18:37:18', 'FTA_CONV', TIMESTAMP '2003-11-15 18:37:18', 0, TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid)
VALUES('TFL44', 'HI', TIMESTAMP '1992-04-23 00:00:00', 'A02', 2, NULL, 'N', NULL, 'T', '44', 9, 'L', TIMESTAMP '1991-07-12 19:00:00', 'IDIR\ASKYWALKER', TIMESTAMP '2024-10-09 14:29:46', 'B994A7AB1A9F2408E05332B3228E5273');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid)
VALUES(123731, 'TFL44', '270', NULL, 1, '1', 1, '44', 'T', NULL, 'G', 'HC', 36, TIMESTAMP '2008-12-04 00:00:00', TIMESTAMP '2003-11-19 00:00:00', TIMESTAMP '2006-11-18 00:00:00', NULL, 0, NULL, 'A', NULL, NULL, 'W', 'N', 'N', 'N', 'N', NULL, 'Klanawa River', NULL, 38.7655, TIMESTAMP '2018-08-29 16:10:24', 4, 'MBAUTO', TIMESTAMP '2003-11-04 10:30:08', 'IDIR\ASKYWALKER', TIMESTAMP '2018-08-29 16:10:24', 'U', 'U', 'U', 'U', 'B9B090AC9D96645AE05332B3228EA5A5');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp)
VALUES('TFL44', 'N', 'A', 'SL', TIMESTAMP '1984-08-01 00:00:00', NULL, 'N', 'N', NULL, NULL, NULL, NULL, 'A', 'N', NULL, 'N', 'N', 11, NULL, NULL, 2, 'FTA_CONV', TIMESTAMP '2003-11-15 11:00:39', 'IDIR\JAKETHEDOG', TIMESTAMP '2024-12-04 13:31:35');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid)
VALUES('44/270', 'TFL44', 'S', 'H', 2, TIMESTAMP '2003-11-04 10:30:08', 'IDIR\MBAUTO', TIMESTAMP '2008-12-04 10:52:21', 'MBAUTO', 'B994A76C45E22408E05332B3228E5273');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
(501472, 'TFL44', '00010002', '03', 'A', TIMESTAMP '2019-03-29 00:00:00', NULL, 0, 'IDIR\ASKYWALKER', TIMESTAMP '2019-04-16 13:31:46', 'IDIR\ASKYWALKER', TIMESTAMP '2019-04-16 13:31:46');

-- K1A
INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
('K1A','HI',TIMESTAMP'2000-07-07 00:00:00','A28',10,NULL,'N',NULL,'C',NULL,14,'JAKETHEDOG',TIMESTAMP'1999-08-30 10:33:26','IDIR\JAKETHEDOG',TIMESTAMP'2020-05-14 09:24:18','B994A7A8E9092408E05332B3228E5273');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
(562321,'K1A','EEE',NULL,2,'A',2,NULL,NULL,NULL,'G','HC',48,TIMESTAMP'2019-06-13 00:00:00',TIMESTAMP'2012-11-02 00:00:00',TIMESTAMP'2016-11-01 00:00:00',NULL,0,NULL,'A',NULL,NULL,'E','N','N','N','N',NULL,'CP EEE',NULL,NULL,TIMESTAMP'2021-08-31 12:20:37',5,'BCEID\JAKETHEDOG',TIMESTAMP'2012-10-25 10:55:10','IDIR\JAKETHEDOG',TIMESTAMP'2021-08-31 12:20:37','U','U','U','U','B9B090ADF7D5645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
('K1AEEE','K1A','1','H',2,TIMESTAMP'2012-10-25 10:55:11','IDIR\JAKETHEDOG',TIMESTAMP'2019-06-13 10:22:39','BCEID\JAKETHEDOG','B994A76E7C562408E05332B3228E5273');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
(122540,'K1A','00010005','00','A',NULL,NULL,1,'FTA_CONV',TIMESTAMP'2003-11-15 10:52:04','FTA_CONV',TIMESTAMP'2003-11-15 10:52:04');

-- A20212
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (369,'Q','13','Selkirk','FTA_04_09',TIMESTAMP'2014-12-02 08:34:56','FTA_04_09',TIMESTAMP'2014-12-02 08:34:56',0,TIMESTAMP'2014-12-02 08:34:56',TIMESTAMP'9999-12-31 00:00:00'),
	 (24,'T','13','TFL 13  Blue River','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (67,'U','13','Kootenay Lake TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
('A20212','HI',TIMESTAMP'1982-12-01 00:00:00','A01',14,NULL,'N',NULL,'U','13',8,'L',TIMESTAMP'1991-07-12 19:00:00','IDIR\LUFFY',TIMESTAMP'2024-12-30 08:06:09','B994A7A7212F2408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
('A20212','N','A','SL',TIMESTAMP'1982-12-01 00:00:00',NULL,'N','N',NULL,NULL,NULL,NULL,'A','N',NULL,'N','N',NULL,NULL,NULL,8,'FTA_CONV',TIMESTAMP'2003-11-15 11:00:22','IDIR\HSOLO',TIMESTAMP'2024-12-30 08:06:09');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
 (444326,'A20212','00010006','00','A',TIMESTAMP'2012-07-17 00:00:00',NULL,0,'IDIR\LUFFY',TIMESTAMP'2012-07-17 11:20:59','IDIR\LUFFY',TIMESTAMP'2012-07-17 11:20:59'),
 (24965,'A20212','00010003','13','C',NULL,TIMESTAMP'2012-07-17 00:00:00',2,'FTA_CONV',TIMESTAMP'2003-11-15 10:50:47','IDIR\LUFFY',TIMESTAMP'2012-07-17 11:20:59');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
(744308,'A20212','210',NULL,14,'1',14,'13','U',NULL,'G','HC',48,TIMESTAMP'2021-11-23 00:00:00',TIMESTAMP'2017-11-24 00:00:00',TIMESTAMP'2021-11-23 00:00:00',NULL,0,NULL,'A','4',NULL,'E','N','Y','N','N',NULL,'Shorty Creek',NULL,NULL,TIMESTAMP'2023-01-26 11:47:11',7,'BCEID\KAIDO',TIMESTAMP'2017-10-19 06:49:49','IDIR\JIMBEI',TIMESTAMP'2023-01-26 11:47:11','U','U','U','U','B9B090AE44F3645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('EZ7210','A20212','4','H',2,TIMESTAMP'2017-10-19 06:49:49','IDIR\SHANKS',TIMESTAMP'2023-01-26 11:47:08','BCEID\USSOP','B994A76E36872408E05332B3228E5273');

--A77623
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (12,'N','01','NISGA TREATY SETTLEMENT LANDS','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1999-12-07 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (357,'Q','01','100 Mile','FTA_04_09',TIMESTAMP'2014-12-02 08:34:55','FTA_04_09',TIMESTAMP'2014-12-02 08:34:55',0,TIMESTAMP'2014-12-02 08:34:55',TIMESTAMP'9999-12-31 00:00:00'),
	 (18,'T','01','TFL 01  Port Edward','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (55,'U','01','Arrow TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (339,'W','01','Greater Vancouver Watershed Lease','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('A77623','HC',TIMESTAMP'2008-07-28 00:00:00','B20',13,12,'Y','1','U','01',17,'IDIR\DHAIGH',TIMESTAMP'2005-02-18 08:45:11','&&datafix_userid',TIMESTAMP'2014-06-28 08:49:30','B994A7A7BC742408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 ('A77623','Y','T','SL',TIMESTAMP'2005-12-15 00:00:00',TIMESTAMP'2005-12-15 00:00:00','A','A',1,NULL,NULL,NULL,'A','N',17015,'N','N',12,NULL,NULL,13,'IDIR\DHAIGH',TIMESTAMP'2005-02-18 08:45:11','IDIR\HEDOMINE',TIMESTAMP'2008-07-28 13:16:27');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (311062,'A77623','00010008','00','A',TIMESTAMP'2005-12-15 00:00:00',NULL,0,'IDIR\LMASTROB',TIMESTAMP'2005-12-15 11:28:53','IDIR\LMASTROB',TIMESTAMP'2005-12-15 11:28:53'),
	 (290377,'A77623','00010008','00','C',TIMESTAMP'2005-02-18 00:00:00',TIMESTAMP'2005-12-15 00:00:00',1,'IDIR\DHAIGH',TIMESTAMP'2005-02-18 08:45:11','IDIR\LMASTROB',TIMESTAMP'2005-12-15 11:28:53');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (79707,'A77623',NULL,NULL,13,'1',13,'01','U',NULL,'G','HC',12,TIMESTAMP'2008-07-28 00:00:00',TIMESTAMP'2005-12-15 00:00:00',TIMESTAMP'2006-12-14 00:00:00',TIMESTAMP'2007-12-14 00:00:00',1,'X','D','4',NULL,'E','N','N','N','N',NULL,'Oscar Creek',NULL,89.2354,TIMESTAMP'2011-07-22 12:43:11',15,'IDIR\DMcMulli',TIMESTAMP'2005-11-03 13:18:10','&&datafix_userid',TIMESTAMP'2014-06-28 08:49:43','U','U','U','U','B9B090ABF3FF645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('77623','A77623','4','H',12,TIMESTAMP'2005-11-03 13:18:10','IDIR\HEDOMINE',TIMESTAMP'2008-07-28 13:16:27','IDIR\DMcMulli','B994A76CB9B92408E05332B3228E5273');

-- A1687
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (46,'T','48','TFL 48  Chetwynd','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (425,'U','48','North Island TSA','IDIR\CABANILL',TIMESTAMP'2017-01-03 14:47:46','IDIR\CABANILL',TIMESTAMP'2017-01-03 14:47:46',0,TIMESTAMP'2016-07-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('TA1687','LC',TIMESTAMP'2023-06-26 00:00:00','B20',2,14,'Y','CR1','U','48',9,'IDIR\DMSUMNER',TIMESTAMP'2020-11-02 14:22:17','IDIR\JKENNING',TIMESTAMP'2023-09-27 12:01:10','B994A7AB4FDF2408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 ('TA1687','Y','T','SL',TIMESTAMP'2022-12-05 00:00:00',TIMESTAMP'2022-12-05 00:00:00','2','2',6,NULL,NULL,NULL,'A','N',17840,'N','N',14,NULL,NULL,7,'IDIR\DMSUMNER',TIMESTAMP'2020-11-02 14:22:17','IDIR\JKENNING',TIMESTAMP'2023-09-27 12:01:10');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (529434,'TA1687','00010004','00','A',TIMESTAMP'2022-12-05 00:00:00',NULL,0,'IDIR\LOSTASHE',TIMESTAMP'2022-12-05 17:15:20','IDIR\LOSTASHE',TIMESTAMP'2022-12-05 17:15:20'),
	 (513632,'TA1687','00010003','13','C',TIMESTAMP'2020-11-02 00:00:00',TIMESTAMP'2022-12-05 00:00:00',1,'IDIR\DMSUMNER',TIMESTAMP'2020-11-02 14:22:17','IDIR\LOSTASHE',TIMESTAMP'2022-12-05 17:15:19');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (800140,'TA1687',NULL,NULL,2,'CR1',2,'48','U',NULL,'G','LC',24,TIMESTAMP'2023-06-26 00:00:00',TIMESTAMP'2022-12-12 00:00:00',TIMESTAMP'2024-12-11 00:00:00',NULL,0,NULL,'D','1',NULL,'W','N','N','N','N',NULL,'Snowden',NULL,28.4625,NULL,7,'IDIR\JPINDROC',TIMESTAMP'2022-08-03 13:31:21','IDIR\JKENNING',TIMESTAMP'2023-09-27 12:01:10','Y','U','U','N','E55D1D9FA5862A67E05332B3228E33C1');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('A1687','TA1687','S','H',5,TIMESTAMP'2022-08-03 13:31:21','IDIR\JKENNING',TIMESTAMP'2023-09-27 12:01:10','IDIR\JPINDROC','E55D1D9FA58D2A67E05332B3228E33C1');

--TFL14
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (370,'Q','14','Skeena Stikine','FTA_04_09',TIMESTAMP'2014-12-02 08:34:56','FTA_04_09',TIMESTAMP'2014-12-02 08:34:56',0,TIMESTAMP'2014-12-02 08:34:56',TIMESTAMP'9999-12-31 00:00:00'),
	 (25,'T','14','TFL 14  Spillimacheen','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (68,'U','14','Lakes TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('A70881','HC',TIMESTAMP'2010-05-04 00:00:00','A01',11,NULL,'N',NULL,'U','14',7,'PMADELEY',TIMESTAMP'2002-10-09 13:35:59','&&datafix_userid',TIMESTAMP'2014-06-28 09:01:41','B994A7A7A4B42408E05332B3228E5273'),
	 ('A72929','HC',TIMESTAMP'2010-05-04 00:00:00','A01',11,NULL,'N',NULL,'U','14',8,'JFITZSIM',TIMESTAMP'2003-09-18 11:15:26','&&datafix_userid',TIMESTAMP'2014-06-28 09:01:41','B994A7A7ABD82408E05332B3228E5273');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (114530,'A72929','001',NULL,1,'A',11,'14','U',NULL,'G','HC',24,TIMESTAMP'2005-07-26 00:00:00',TIMESTAMP'2003-12-03 00:00:00',TIMESTAMP'2005-12-02 00:00:00',NULL,0,NULL,'A',NULL,NULL,'E','Y','N','N','N',NULL,'CUTTING PERMITS',NULL,NULL,TIMESTAMP'2006-09-13 00:00:00',6,'IDIR\jcballar',TIMESTAMP'2004-01-07 14:33:24','DATAFIX_06072007_134C',TIMESTAMP'2007-07-11 10:57:16','U','U','U','U','B9B090AC79CD645AE05332B3228EA5A5'),
	 (111525,'A70881','001',NULL,1,'A',11,'14','U',NULL,'G','HC',24,TIMESTAMP'2005-07-26 00:00:00',TIMESTAMP'2003-12-03 00:00:00',TIMESTAMP'2005-12-02 00:00:00',NULL,0,NULL,'A',NULL,NULL,'E','Y','N','N','N',NULL,'TYEEWOOD FOREST RESOURCES\nCUTTING PERMITS',NULL,NULL,TIMESTAMP'2006-09-13 00:00:00',6,'IDIR\jcballar',TIMESTAMP'2004-01-07 14:28:53','DATAFIX_06072007_134C',TIMESTAMP'2007-07-11 10:54:22','U','U','U','U','B9B090AC6E62645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('AY5001','A72929','5','H',4,TIMESTAMP'2004-01-07 14:33:24','IDIR\JSILLES',TIMESTAMP'2005-07-26 11:14:34','IDIR\jcballar','B994A76D01672408E05332B3228E5273'),
	 ('AU6001','A70881','5','H',4,TIMESTAMP'2004-01-07 14:28:53','IDIR\JSILLES',TIMESTAMP'2005-07-26 10:22:13','IDIR\jcballar','B994A76D00BE2408E05332B3228E5273');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (67667,'A70881','00010006','00','A',TIMESTAMP'2003-11-01 00:00:00',NULL,1,'FTA_CONV',TIMESTAMP'2003-11-15 10:51:21','FTA_CONV',TIMESTAMP'2003-11-15 10:51:21'),
	 (67668,'A70881','00010005','00','C',TIMESTAMP'2003-04-01 00:00:00',TIMESTAMP'2003-10-31 00:00:00',1,'FTA_CONV',TIMESTAMP'2003-11-15 10:51:21','FTA_CONV',TIMESTAMP'2003-11-15 10:51:21'),
	 (69689,'A72929','00010004','00','A',TIMESTAMP'2003-11-01 00:00:00',NULL,1,'FTA_CONV',TIMESTAMP'2003-11-15 10:51:23','FTA_CONV',TIMESTAMP'2003-11-15 10:51:23'),
	 (69690,'A72929','00010008','00','C',TIMESTAMP'2003-09-08 00:00:00',TIMESTAMP'2003-10-31 00:00:00',1,'FTA_CONV',TIMESTAMP'2003-11-15 10:51:23','FTA_CONV',TIMESTAMP'2003-11-15 10:51:23');

--TFL41
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (39,'T','41','TFL 41  Kitimat','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (94,'U','41','Dawson Creek TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('A13840','HI',TIMESTAMP'1992-02-10 00:00:00','A01',13,NULL,'N',NULL,'U','41',2,'L',TIMESTAMP'1991-07-12 19:00:00','IDIR\CRMARSH',TIMESTAMP'2016-10-07 14:48:17','B994A7A7160B2408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 ('A13840','N','D','SL',TIMESTAMP'1983-01-01 00:00:00',NULL,'N','N',NULL,NULL,NULL,NULL,'A','N',NULL,'N','N',NULL,NULL,NULL,8,'FTA_CONV',TIMESTAMP'2003-11-15 11:00:22','IDIR\CRMARSH',TIMESTAMP'2016-10-07 14:48:17');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (289860,'A13840','00010006','00','A',TIMESTAMP'2005-01-01 00:00:00',NULL,0,'IDIR\JFITZSIM',TIMESTAMP'2005-02-10 11:28:34','IDIR\JFITZSIM',TIMESTAMP'2005-02-10 11:28:34'),
	 (7344,'A13840','00010008','00','C',NULL,TIMESTAMP'2005-01-01 00:00:00',2,'FTA_CONV',TIMESTAMP'2003-11-15 10:50:33','IDIR\JFITZSIM',TIMESTAMP'2005-02-10 11:28:34');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (99592,'A13840','145',NULL,2,'DEAD',12,'41','U',NULL,'G','HC',24,TIMESTAMP'2000-03-09 00:00:00',TIMESTAMP'1997-06-15 00:00:00',TIMESTAMP'1999-06-14 00:00:00',NULL,0,NULL,'A','8',NULL,'E','N','N','N','N',NULL,'Quality Creek',NULL,NULL,TIMESTAMP'2018-12-03 11:21:22',3,'TECUTLER',TIMESTAMP'1996-12-06 08:20:54','IDIR\DURICHAR',TIMESTAMP'2018-12-03 11:21:22','U','U','U','U','B9B090AC3FE3645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('FA9145','A13840','4','P',1,TIMESTAMP'1996-12-06 08:20:54','BHENNEY',TIMESTAMP'2003-10-24 14:36:10','TECUTLER','B994A76E40BC2408E05332B3228E5273');

-- TFL39

INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (38,'T','39','TFL 39  Haida','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00'),
	 (92,'U','39','Sunshine Coast TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('TFL39','HI',TIMESTAMP'1992-07-17 00:00:00','A02',1,NULL,'N',NULL,'T','39',3,'L',TIMESTAMP'1991-07-12 19:00:00','IDIR\RYEE',TIMESTAMP'2018-04-27 16:44:23','B43E3E3D0C862EF0E0533A54228EF0D3');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 ('TFL39','N','A','SL',TIMESTAMP'1980-01-01 00:00:00',NULL,'N','N',NULL,NULL,NULL,NULL,'A','N',NULL,'N','N',NULL,NULL,NULL,1,'FTA_CONV',TIMESTAMP'2003-06-10 14:16:51','HDBSMIG',TIMESTAMP'1991-11-05 03:39:18');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (551866,'TFL39','00010004','00','A',TIMESTAMP'2014-09-22 09:50:28',NULL,1,'CLIENT_REORG',TIMESTAMP'2014-09-22 09:50:28','CLIENT_REORG',TIMESTAMP'2014-09-22 09:50:28'),
	 (548992,'TFL39','00010005','00','C',TIMESTAMP'2009-11-04 00:00:00',TIMESTAMP'2014-09-22 09:50:28',1,'IDIR\LEKEATIN',TIMESTAMP'2009-11-04 09:08:44','CLIENT_REORG',TIMESTAMP'2014-09-22 09:50:28'),
	 (511226,'TFL39','00010006','00','C',TIMESTAMP'1999-11-01 00:00:00',TIMESTAMP'2009-11-04 00:00:00',2,'FTA_CONV',TIMESTAMP'2003-06-10 08:43:17','IDIR\LEKEATIN',TIMESTAMP'2009-11-04 09:08:44'),
	 (511227,'TFL39','00010008','00','C',TIMESTAMP'1961-10-27 00:00:00',TIMESTAMP'1999-11-01 00:00:00',1,'FTA_CONV',TIMESTAMP'2003-06-10 08:43:17','FTA_CONV',TIMESTAMP'2003-06-10 08:43:17'),
	 (511228,'TFL39','00010002','03','L',TIMESTAMP'2002-11-07 00:00:00',NULL,1,'FTA_CONV',TIMESTAMP'2003-06-10 08:43:17','FTA_CONV',TIMESTAMP'2003-06-10 08:43:17');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (172563,'TFL39','27Y',NULL,1,'S',1,NULL,NULL,NULL,'G','HI',48,TIMESTAMP'2008-08-19 00:00:00',TIMESTAMP'2007-08-19 00:00:00',TIMESTAMP'2011-08-18 00:00:00',NULL,0,NULL,'A',NULL,NULL,'W','N','Y','N','N',NULL,'Phillips River',NULL,228.0154,NULL,4,'IDIR\RYEE',TIMESTAMP'2008-08-19 14:12:46','IDIR\RYEE',TIMESTAMP'2008-08-19 16:14:36','U','U','U','U','B4E2130199DF4578E0533A54228E0360');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('39/27Y','TFL39','4','H',2,TIMESTAMP'2008-08-19 14:12:51','IDIR\RYEE',TIMESTAMP'2008-08-19 16:14:38','IDIR\RYEE','B43E3E27DD312EF0E0533A54228EF0D3');

--- A18667
INSERT INTO silva.forest_mgmt_unit (mgmt_skey, mgmt_unit_type_code, mgmt_unit_id, description, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, effective_date, expiry_date) VALUES
	 (76,'U','22','Okanagan TSA','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18','FTA_CONV',TIMESTAMP'2003-11-15 18:37:18',0,TIMESTAMP'1905-01-01 00:00:00',TIMESTAMP'9999-12-31 00:00:00');

INSERT INTO silva.prov_forest_use (forest_file_id, file_status_st, file_status_date, file_type_code, forest_region, bcts_org_unit, sb_funded_ind, district_admin_zone, mgmt_unit_type, mgmt_unit_id, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, forest_tenure_guid) VALUES
	 ('A18667','HI',TIMESTAMP'1982-11-15 00:00:00','A01',1,NULL,'N',NULL,'U','22',7,'L',TIMESTAMP'1991-07-12 19:00:00','IDIR\LEJACKSO',TIMESTAMP'2017-05-17 15:49:11','B994A7A71DAA2408E05332B3228E5273');

INSERT INTO silva.harvest_sale (forest_file_id, sb_fund_ind, sale_method_code, sale_type_cd, planned_sale_date, tender_opening_dt, plnd_sb_cat_code, sold_sb_cat_code, total_bidders, lumpsum_bonus_amt, cash_sale_est_vol, cash_sale_tot_dol, payment_method_cd, salvage_ind, sale_volume, admin_area_ind, minor_facility_ind, bcts_org_unit, fta_bonus_bid, fta_bonus_offer, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 ('A18667','N','A','SL',TIMESTAMP'1982-11-15 00:00:00',NULL,'N','N',NULL,NULL,NULL,NULL,'A','N',NULL,'N','N',NULL,NULL,NULL,8,'FTA_CONV',TIMESTAMP'2003-11-15 11:00:22','IDIR\LEJACKSO',TIMESTAMP'2017-05-17 15:49:11');

INSERT INTO silva.forest_file_client (forest_file_client_skey, forest_file_id, client_number, client_locn_code, forest_file_client_type_code, licensee_start_date, licensee_end_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (316882,'A18667','00010008','00','A',TIMESTAMP'2006-03-09 16:06:19',NULL,1,'DATAFIX AL#90359',TIMESTAMP'2006-03-09 16:06:19','DATAFIX AL#90359',TIMESTAMP'2006-03-09 16:06:19'),
	 (16361,'A18667','00010005','00','C',NULL,NULL,1,'FTA_CONV',TIMESTAMP'2003-11-15 10:50:40','FTA_CONV',TIMESTAMP'2003-11-15 10:50:40'),
	 (16360,'A18667','00010006','00','C',NULL,TIMESTAMP'2006-03-09 16:06:27',2,'FTA_CONV',TIMESTAMP'2003-11-15 10:50:40','DATAFIX AL#90359',TIMESTAMP'2006-03-09 16:06:27');

INSERT INTO silva.harvesting_authority (hva_skey, forest_file_id, cutting_permit_id, harvesting_authority_id, forest_district, district_admn_zone, geographic_district, mgmt_unit_id, mgmt_unit_type_code, licence_to_cut_code, harvest_type_code, harvest_auth_status_code, tenure_term, status_date, issue_date, expiry_date, extend_date, extend_count, harvest_auth_extend_reas_code, quota_type_code, crown_lands_region_code, salvage_type_code, cascade_split_code, catastrophic_ind, crown_granted_ind, cruise_based_ind, deciduous_ind, bcaa_folio_number, location, higher_level_plan_reference, harvest_area, retirement_date, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, is_cp_extensn_appl_fee_waived, is_cp_extension_appl_fee_paid, is_within_fibre_recovery_zone, harvesting_authority_guid) VALUES
	 (106665,'A18667','55',NULL,1,'RIVE',1,'22','U',NULL,'G','HC',12,TIMESTAMP'1997-02-12 00:00:00',TIMESTAMP'1990-10-01 00:00:00',TIMESTAMP'1991-09-30 00:00:00',TIMESTAMP'1996-09-30 00:00:00',4,'X','A',NULL,NULL,'E','N','Y','Y','N',NULL,'BEAKCREEK',NULL,NULL,TIMESTAMP'2018-11-29 15:01:48',5,'RDMILLER',TIMESTAMP'9999-12-31 00:00:00','IDIR\DURICHAR',TIMESTAMP'2018-11-29 15:01:48','U','U','U','U','B9B090AC5B74645AE05332B3228EA5A5');

INSERT INTO silva.hauling_authority (timber_mark, forest_file_id, marking_method_code, marking_instrument_code, revision_count, entry_timestamp, update_userid, update_timestamp, entry_userid, hauling_authority_guid) VALUES
	 ('ER4055','A18667','S','H',1,TIMESTAMP'9999-12-31 00:00:00','SLZUPP',TIMESTAMP'2003-11-13 08:56:23','RDMILLER','B994A76E10BD2408E05332B3228E5273');


-- Others
INSERT INTO silva.silv_admin_zone (dist_admin_zone, org_unit_no, admin_zone_desc, revision_count) VALUES ('ER', 1, 'ZONE R', 1);
