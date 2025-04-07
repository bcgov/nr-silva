INSERT INTO THE.ORG_UNIT (ORG_UNIT_NO,ORG_UNIT_CODE,ORG_UNIT_NAME,LOCATION_CODE,AREA_CODE,TELEPHONE_NO,ORG_LEVEL_CODE,OFFICE_NAME_CODE,ROLLUP_REGION_NO,ROLLUP_REGION_CODE,ROLLUP_DIST_NO,ROLLUP_DIST_CODE,EFFECTIVE_DATE,EXPIRY_DATE,UPDATE_TIMESTAMP) VALUES
(1          , 'DAS'       , 'Development Unit'        , '001'       , '778'   , '9999', 'A'          , 'DU'           , 111            , '1Code'          , 222          , '22Code'       , TO_DATE('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS')),
(2          , 'TWO'       , 'Design Unit'             , '002'       , '002'   , '9999', 'B'          , 'DS'           , 333            , '3Code'          , 444          , '44Code'       , TO_DATE('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS')),
(70         , 'HVA'       , 'Timber Pricing Branch'   , '070'       , '250'   , '9999', 'H'          , 'VA'           , 12             , '2Code'          , 23           , '33Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(10         , 'TSB'       , 'Timber Sale Branch'      , '010'       , '100'   , '9999', 'C'          , 'SB'           , 34             , '9Code'          , 45           , '10Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(11         , 'TAB'       , 'Timber Appraisal Branch' , '011'       , '110'   , '9999', 'D'          , 'AB'           , 19             , '4Code'          , 30           , '55Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(12         , 'TWB'       , 'Timber Wood Branch'      , '012'       , '120'   , '9999', 'E'          , 'WB'           , 40             , '77Code'         , 51           , '88Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(13         , 'WSB'       , 'Wood Sale Branch'        , '013'       , '130'   , '9999', 'F'          , 'WS'           , 724            , '20Code'         , 835          , '34Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(14         , 'LSB'       , 'Lumber Sale Branch'      , '014'       , '140'   , '9999', 'G'          , 'LS'           , 236            , '67Code'         , 347          , '73Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS')),
(15         , 'VWC'       , 'Victoria Working Center' , '015'       , '150'   , '9999', 'I'          , 'WC'           , 417            , '8Code'          , 528          , '91Code'       , TO_DATE('1905-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2011-08-29 15:49:08', 'YYYY-MM-DD HH24:MI:SS'));


INSERT INTO THE.PAYMENT_METHOD_CODE
(PAYMENT_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('A', 'Automatic Invoice', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-15 19:05:03.000000');
INSERT INTO THE.PAYMENT_METHOD_CODE
(PAYMENT_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('C', 'Cash', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-15 19:05:03.000000');
INSERT INTO THE.PAYMENT_METHOD_CODE
(PAYMENT_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('M', 'Manual Invoice', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-15 19:05:03.000000');

INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('1', 'Non-milling', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('2', 'Milling', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('3', 'Proposed Mill', TIMESTAMP '1999-10-26 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('9', 'District Manager', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('A', 'Any/All Registrant Categories', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('N', 'Non-Registrant', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2003-11-18 12:42:43.000000');
INSERT INTO THE.SB_CATEGORY_CODE
(SB_CATEGORY_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('4', 'Value Added', TIMESTAMP '1905-02-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2024-02-27 15:23:20.000000');

INSERT INTO THE.SALE_METHOD_CODE
(SALE_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('A', 'Auction, Oral', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2008-11-03 16:10:09.000000');
INSERT INTO THE.SALE_METHOD_CODE
(SALE_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('B', 'Bid Proposal', TIMESTAMP '2002-12-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2008-11-03 16:10:09.000000');
INSERT INTO THE.SALE_METHOD_CODE
(SALE_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('D', 'Direct Award', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2008-11-03 16:10:09.000000');
INSERT INTO THE.SALE_METHOD_CODE
(SALE_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('N', 'Direct Award First Nations', TIMESTAMP '2008-07-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2008-11-03 16:10:09.000000');
INSERT INTO THE.SALE_METHOD_CODE
(SALE_METHOD_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('T', 'Auction, Sealed Tender', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2008-11-03 16:10:09.000000');

INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('CA', 'Conventional Application', TIMESTAMP '2004-10-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('H1', 'H1 SSS for Forest Health reasons 0 - 50 m3', TIMESTAMP '2000-07-05 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('H2', 'H2 SSS for Forest Health reasons 51 - 500 m3', TIMESTAMP '2000-07-05 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('H3', 'H3 SSS for Forest Health reasons 501 - 2,000 m3', TIMESTAMP '2000-07-05 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('H4', 'H4 SSS for Forest Health reasons >2000 m3', TIMESTAMP '2000-07-05 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('HL', 'Handlog', TIMESTAMP '1998-07-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('LY', 'Log Yard', TIMESTAMP '1905-01-01 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('PA', 'Professional Application', TIMESTAMP '2004-10-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('PW', 'Pulpwood', TIMESTAMP '1998-07-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('S1', 'S1 Small Scale Salvage 0 - 50 m3', TIMESTAMP '1998-11-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('S2', 'S2 Small Scale Salvage 51 - 500 m3', TIMESTAMP '1998-11-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('S3', 'S3 Small Scale Salvage 501 - 2,000 m3', TIMESTAMP '1998-11-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('S4', 'S4 Small Scale Salvage 2,000 m3', TIMESTAMP '1998-11-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('SL', 'Sawlog', TIMESTAMP '1998-07-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('SP', 'Special Forest Products', TIMESTAMP '1998-07-23 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
INSERT INTO THE.SALE_TYPE_CODE
(SALE_TYPE_CODE, DESCRIPTION, EFFECTIVE_DATE, EXPIRY_DATE, UPDATE_TIMESTAMP)
VALUES('XM', 'Christmas Tree', TIMESTAMP '1998-11-25 00:00:00.000000', TIMESTAMP '9999-12-31 00:00:00.000000', TIMESTAMP '2004-12-21 13:35:21.000000');
