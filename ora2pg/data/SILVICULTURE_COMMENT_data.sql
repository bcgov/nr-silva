SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silviculture_comment;

COPY silviculture_comment (silviculture_comment_id,comment_date,silv_comment_source_code,silv_comment_type_code,comment_text,entry_userid,entry_timestamp,update_userid,update_timestamp,revision_count) FROM STDIN;
1	2025-04-07 14:42:07	OPEN	GENERAL	All good so far	IDIR\\JAKETHEDOG	2025-04-07 14:42:07	IDIR\\JAKETHEDOG	2025-04-07 14:42:07	1
2	2019-04-16 13:31:46	OPEN	GENERAL	In a forest far far away	BCEID\\ASKYWALKER	2019-04-16 13:31:46	BCEID\\ASKYWALKER	2019-04-16 13:31:46	1
3	2016-01-19 13:42:26	OPEN	GENERAL	In a forest far far away	IDIR\\PANAMERICAN	2016-01-19 13:42:26	IDIR\\PANAMERICAN	2016-01-19 13:42:26	1
\.
