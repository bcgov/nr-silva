SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE crew_contract_hire_code;

COPY crew_contract_hire_code (crew_contract_hire_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
D	Direct Contract	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
F	RPF - Ministry Staff	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
L	Local Tender	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
O	Own Crew	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
P	Provincial Tender	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
Q	Request for Proposal	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
R	Region Tender	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
S	Select Invitation	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
T	Tech - Ministry Staff	2006-08-31 00:00:00	9999-12-31 00:00:00	2007-03-11 17:50:43
\.
