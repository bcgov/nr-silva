CREATE SEQUENCE silva.opening_id_seq;
SELECT setval('silva.opening_id_seq', COALESCE((SELECT MAX(opening_id) FROM silva.opening), 1));

CREATE SEQUENCE silva.cut_block_open_admin_id_seq;
SELECT setval('silva.cut_block_open_admin_id_seq', COALESCE((SELECT MAX(cut_block_open_admin_id) FROM silva.cut_block_open_admin), 1));
