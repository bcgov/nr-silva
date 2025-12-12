SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE forest_cover_non_mapped_area;

COPY forest_cover_non_mapped_area (forest_cover_id,non_mapped_area_id,non_mapped_area,stocking_status_code,stocking_type_code,revision_count) FROM STDIN;
2648982	A1	1	NP	UNN	1
4656974	R	.5	NP	RD	1
4401624	A1	1.6	NP	UNN	1
4401625	B5	.1	NP	UNN	1
4401626	C4	.1	NP	UNN	1
4401627	A1	1.6	NP	UNN	1
4401628	B5	.1	NP	UNN	1
4401629	C4	.1	NP	UNN	1
1039802	1	1	C	UNN	1
3416434	A1	3.4	NP	UNN	1
367798	A	3.9	NP	UNN	1
\.
