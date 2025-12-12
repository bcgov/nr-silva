SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE file_client_type_code;

COPY file_client_type_code (file_client_type_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
A	Licensee	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
B	Joint Venture/Partner	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
C	Previous Main Licensee	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
D	Applicant	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
E	Contractor	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
F	Non-Licensed Commercial Operator	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
G	Non-Commercial Licensed Operator	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
H	Licensed Commercial Rec. Operator	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
I	Club/Association/Society	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
J	General Public	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
K	Volunteer	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
L	Licensee Operating Division	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
M	Previous Licensee Operating Division	2002-12-05 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
O	Silviculture Obligation	2003-06-02 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
P	Previous Joint Venture/Partner	2002-10-22 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
R	RESULTS Silviculture Obligation	2014-02-11 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
S	Appraisals & Billing Mailing	1905-01-01 00:00:00	9999-12-31 00:00:00	2014-08-22 08:12:54
\.
