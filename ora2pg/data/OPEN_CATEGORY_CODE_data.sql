SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE open_category_code;

COPY open_category_code (open_category_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
BLCF	Backlog SP Area - Community Forest	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
BLFS	Backlog SP Area - Forest Service	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
BLML	Backlog SP Area - Major Licensee	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
BLWL	Backlog SP Area - Woodlot License	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
CONT	SP as a part of contractual agreement	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
EXCLU	Openings excluded from Crown managed forests	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FFTFLTC	Forest For Tomorrow - Forestry Licence to Cut	2009-11-16 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FFTITSL	Forest For Tomorrow - Innovative Timber Sale Licence	2009-11-16 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FG	Opening that has achieved free growing	2000-04-17 00:00:00	2010-07-07 00:00:00	2013-07-31 13:41:13
FTCF	Forest Tenure - Site Plan under Community Forest	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTFSM	Forest Tenure - Forest Stand Management FPC s.71	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTLEVY	Forest Stand Levy under FSM Fund Reg.	2000-09-14 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTML	Forest Tenure - Major Licensee	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTMSL	Forest Tenure Ministry Silviculture Liability	2009-12-09 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTNOLVY	Post April 1/09 blocks (1-5 ha) for which there is no opportunity within the appraisal manual to collect a levy.	2011-05-27 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTPI	Forest Tenure - pilot agreement	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTSBF	Forest Tenure - Small Business Forest Enterprise Program	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
FTWL	Forest Tenure - Woodlot: Site Plan	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NDAML	Natural Disturbance - area-based Major Licensee	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NDCF	Natural Disturbance - Community Forest	2001-02-01 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NDFS	Natural Disturbance - forest service	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NDVML	Natural Disturbance - volume-based Major Licensee	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NDWL	Natural Disturbance - Woodlot License	2001-02-01 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
NREQ	Areas where SP/SMP's are not required by law	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
P87	Pre-87 areas that is either NSR, or stocked but not FG	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SMPCF	Stand Management Prescription - Community Forest	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SMPEX	Areas where exemptions to SMP's apply	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SMPFS	Stand Management Prescription - Forest service	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SMPML	Stand Management Prescription - Major Licensee	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SMPWL	Stand Management Prescription - Woodlot License	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
SPEX	Areas with no reforestation or FG obligations and the area is exempt from a site plan requirement	2000-04-17 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
UHCF	Unauthorized Harvest - Community Forest	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
UHFS	Unauthorized Harvesting - Forest Service	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
UHML	Unauthorized Harvesting - Major Licensee	2000-04-17 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
UHRV	Unauthorized Harvesting	2013-07-05 00:00:00	9999-12-31 00:00:00	2013-07-31 13:41:13
UHWL	Unauthorized Harvest - Woodlot License	2001-02-01 00:00:00	2013-07-05 00:00:00	2013-07-31 13:41:13
UNKN	Unknown	2003-07-03 00:00:00	2002-12-17 00:00:00	2013-07-31 13:41:13
\.
