SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE org_unit;

COPY org_unit (org_unit_no,org_unit_code,org_unit_name,location_code,area_code,telephone_no,org_level_code,office_name_code,rollup_region_no,rollup_region_code,rollup_dist_no,rollup_dist_code,effective_date,expiry_date,update_timestamp) FROM STDIN;
1	DAS	Development Unit	001	778	9999	A	DU	111	1Code	222	22Code	2020-01-01 00:00:00	9999-12-31 00:00:00	2024-09-03 00:00:00
2	TWO	Design Unit	002	002	9999	B	DS	333	3Code	444	44Code	2020-01-01 00:00:00	2022-12-31 00:00:00	2022-12-31 00:00:00
70	HVA	Timber Pricing Branch	070	250	9999	H	VA	12	2Code	23	33Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
10	TSB	Timber Sale Branch	010	100	9999	C	SB	34	9Code	45	10Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
11	TAB	Timber Appraisal Branch	011	110	9999	D	AB	19	4Code	30	55Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
12	TWB	Timber Wood Branch	012	120	9999	E	WB	40	77Code	51	88Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
13	WSB	Wood Sale Branch	013	130	9999	F	WS	724	20Code	835	34Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
14	LSB	Lumber Sale Branch	014	140	9999	G	LS	236	67Code	347	73Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
15	VWC	Victoria Working Center	015	150	9999	I	WC	417	8Code	528	91Code	1905-01-01 00:00:00	9999-12-31 00:00:00	2011-08-29 15:49:08
\.
