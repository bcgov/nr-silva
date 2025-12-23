INSERT INTO silva.org_unit (org_unit_no, org_unit_code, org_unit_name, location_code, area_code, telephone_no, org_level_code, office_name_code, rollup_region_no, rollup_region_code, rollup_dist_no, rollup_dist_code, effective_date, expiry_date, update_timestamp) VALUES
(1          , 'DAS'       , 'Development Unit'        , '001'       , '778'   , '9999', 'A'          , 'DU'           , 111            , '1Code'          , 222          , '22Code'       , TIMESTAMP '2020-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2024-09-03 00:00:00'),
(2          , 'TWO'       , 'Design Unit'             , '002'       , '002'   , '9999', 'B'          , 'DS'           , 333            , '3Code'          , 444          , '44Code'       , TIMESTAMP '2020-01-01 00:00:00', TIMESTAMP '2022-12-31 00:00:00', TIMESTAMP '2022-12-31 00:00:00'),
(70         , 'HVA'       , 'Timber Pricing Branch'   , '070'       , '250'   , '9999', 'H'          , 'VA'           , 12             , '2Code'          , 23           , '33Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(10         , 'TSB'       , 'Timber Sale Branch'      , '010'       , '100'   , '9999', 'C'          , 'SB'           , 34             , '9Code'          , 45           , '10Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(11         , 'TAB'       , 'Timber Appraisal Branch' , '011'       , '110'   , '9999', 'D'          , 'AB'           , 19             , '4Code'          , 30           , '55Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(12         , 'TWB'       , 'Timber Wood Branch'      , '012'       , '120'   , '9999', 'E'          , 'WB'           , 40             , '77Code'         , 51           , '88Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(13         , 'WSB'       , 'Wood Sale Branch'        , '013'       , '130'   , '9999', 'F'          , 'WS'           , 724            , '20Code'         , 835          , '34Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(14         , 'LSB'       , 'Lumber Sale Branch'      , '014'       , '140'   , '9999', 'G'          , 'LS'           , 236            , '67Code'         , 347          , '73Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08'),
(15         , 'VWC'       , 'Victoria Working Center' , '015'       , '150'   , '9999', 'I'          , 'WC'           , 417            , '8Code'          , 528          , '91Code'       , TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2011-08-29 15:49:08');


INSERT INTO silva.payment_method_code
(payment_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('A', 'Automatic Invoice', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-15 19:05:03');
INSERT INTO silva.payment_method_code
(payment_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('C', 'Cash', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-15 19:05:03');
INSERT INTO silva.payment_method_code
(payment_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('M', 'Manual Invoice', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-15 19:05:03');

INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('1', 'Non-milling', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('2', 'Milling', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('3', 'Proposed Mill', TIMESTAMP '1999-10-26 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('9', 'District Manager', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('A', 'Any/All Registrant Categories', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('N', 'Non-Registrant', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2003-11-18 12:42:43');
INSERT INTO silva.sb_category_code
(sb_category_code, description, effective_date, expiry_date, update_timestamp)
VALUES('4', 'Value Added', TIMESTAMP '1905-02-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2024-02-27 15:23:20');

INSERT INTO silva.sale_method_code
(sale_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('A', 'Auction, Oral', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2008-11-03 16:10:09');
INSERT INTO silva.sale_method_code
(sale_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('B', 'Bid Proposal', TIMESTAMP '2002-12-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2008-11-03 16:10:09');
INSERT INTO silva.sale_method_code
(sale_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('D', 'Direct Award', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2008-11-03 16:10:09');
INSERT INTO silva.sale_method_code
(sale_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('N', 'Direct Award First Nations', TIMESTAMP '2008-07-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2008-11-03 16:10:09');
INSERT INTO silva.sale_method_code
(sale_method_code, description, effective_date, expiry_date, update_timestamp)
VALUES('T', 'Auction, Sealed Tender', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2008-11-03 16:10:09');

INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('CA', 'Conventional Application', TIMESTAMP '2004-10-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('H1', 'H1 SSS for Forest Health reasons 0 - 50 m3', TIMESTAMP '2000-07-05 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('H2', 'H2 SSS for Forest Health reasons 51 - 500 m3', TIMESTAMP '2000-07-05 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('H3', 'H3 SSS for Forest Health reasons 501 - 2,000 m3', TIMESTAMP '2000-07-05 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('H4', 'H4 SSS for Forest Health reasons >2000 m3', TIMESTAMP '2000-07-05 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('HL', 'Handlog', TIMESTAMP '1998-07-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('LY', 'Log Yard', TIMESTAMP '1905-01-01 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('PA', 'Professional Application', TIMESTAMP '2004-10-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('PW', 'Pulpwood', TIMESTAMP '1998-07-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('S1', 'S1 Small Scale Salvage 0 - 50 m3', TIMESTAMP '1998-11-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('S2', 'S2 Small Scale Salvage 51 - 500 m3', TIMESTAMP '1998-11-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('S3', 'S3 Small Scale Salvage 501 - 2,000 m3', TIMESTAMP '1998-11-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('S4', 'S4 Small Scale Salvage 2,000 m3', TIMESTAMP '1998-11-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('SL', 'Sawlog', TIMESTAMP '1998-07-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('SP', 'Special Forest Products', TIMESTAMP '1998-07-23 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
INSERT INTO silva.sale_type_code
(sale_type_code, description, effective_date, expiry_date, update_timestamp)
VALUES('XM', 'Christmas Tree', TIMESTAMP '1998-11-25 00:00:00', TIMESTAMP '9999-12-31 00:00:00', TIMESTAMP '2004-12-21 13:35:21');
