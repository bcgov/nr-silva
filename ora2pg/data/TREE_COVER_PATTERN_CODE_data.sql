SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE tree_cover_pattern_code;

COPY tree_cover_pattern_code (tree_cover_pattern_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
1	Single to very few (<4) occurrences of limited extent, circular to irregular shape.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
2	Single to very few (<4) occurrences of limited extent, linear or elongated shape.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
3	Several (>3) sporadic occurrences of limited extent, circular to irregular shape.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
4	Several (>3) sporadic occurrences of limited extent, linear or elongated shape.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
5	Intimately intermixed units, often with gradiational transitions from one to the other.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
6	Discontinuous but extensive occurrences, parallel to sub-parallel elongated in shape.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
7	Limited continuous occurrence with few inclusions.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
8	Continuous occurrence with several inclusions.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
9	Continuous occurrence with very few inclusions.	1998-12-03 00:00:00	9999-12-31 00:00:00	2003-11-15 19:24:02
\.
