-- Cross-system FK cboa_cb_fk (cb_skey -> silva.cut_block) is replaced by command-path
-- verification through TenureProvider.verifyCutBlock(). Retaining cb_skey as an external
-- reference allows persisting cut blocks resolved from MockTenureAdapter and external FTA Oracle
-- without replicating rows into silva.cut_block.
ALTER TABLE silva.cut_block_open_admin DROP CONSTRAINT IF EXISTS cboa_cb_fk;
