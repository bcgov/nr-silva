-- silva.standards_regime_layer definition
-- DROP TABLE silva.standards_regime_layer;
CREATE TABLE IF NOT EXISTS silva.standards_regime_layer (
    standards_regime_layer_id int8 NOT NULL,
    standards_regime_id int8 NOT NULL,
    stocking_layer_code varchar(2) NOT NULL,
    tree_size_unit_code varchar(3) NULL,
    residual_basal_area int4 NULL,
    min_horizontal_distance numeric(3,1) NULL,
    min_pref_stocking_standard int4 NULL,
    min_stocking_standard int4 NULL,
    target_stocking int4 NULL,
    min_post_spacing int4 NULL,
    max_post_spacing int4 NULL,
    max_conifer int4 NULL,
    hght_relative_to_comp int4 NULL,
    entry_userid varchar(30) NOT NULL,
    entry_timestamp timestamp(0) NOT NULL,
    update_userid varchar(30) NOT NULL,
    update_timestamp timestamp(0) NOT NULL,
    revision_count int4 NOT NULL,
    CONSTRAINT standards_regime_layer_pkey PRIMARY KEY (standards_regime_layer_id)
);
CREATE INDEX IF NOT EXISTS srl_sr_fk_i ON silva.standards_regime_layer USING btree (standards_regime_id);
CREATE INDEX IF NOT EXISTS srl_slc_fk_i ON silva.standards_regime_layer USING btree (stocking_layer_code);

-- silva.standards_regime_layer_species definition
-- DROP TABLE silva.standards_regime_layer_species;
CREATE TABLE IF NOT EXISTS silva.standards_regime_layer_species (
    standards_regime_layer_id int8 NOT NULL,
    silv_tree_species_code varchar(8) NOT NULL,
    species_order int4 NOT NULL,
    preferred_ind varchar(1) NOT NULL,
    min_height numeric(3,1) NULL,
    entry_userid varchar(30) NOT NULL,
    entry_timestamp timestamp(0) NOT NULL,
    update_userid varchar(30) NOT NULL,
    update_timestamp timestamp(0) NOT NULL,
    revision_count int4 NOT NULL,
    CONSTRAINT standards_regime_layer_species_pkey PRIMARY KEY (standards_regime_layer_id, silv_tree_species_code)
);

-- silva.standards_regime_client definition
-- DROP TABLE silva.standards_regime_client;
CREATE TABLE IF NOT EXISTS silva.standards_regime_client (
    standards_regime_id int8 NOT NULL,
    client_number varchar(8) NOT NULL,
    entry_userid varchar(30) NOT NULL,
    entry_timestamp timestamp(0) NOT NULL,
    update_userid varchar(30) NOT NULL,
    update_timestamp timestamp(0) NOT NULL,
    revision_count int4 NOT NULL,
    CONSTRAINT standards_regime_client_pkey PRIMARY KEY (standards_regime_id, client_number)
);
CREATE INDEX IF NOT EXISTS srcli_fc2_fk_i ON silva.standards_regime_client USING btree (client_number);

-- silva.standards_regime_org_unit definition
-- DROP TABLE silva.standards_regime_org_unit;
CREATE TABLE IF NOT EXISTS silva.standards_regime_org_unit (
    standards_regime_id int8 NOT NULL,
    org_unit_no int8 NOT NULL,
    entry_userid varchar(30) NOT NULL,
    entry_timestamp timestamp(0) NOT NULL,
    update_userid varchar(30) NOT NULL,
    update_timestamp timestamp(0) NOT NULL,
    revision_count int4 NOT NULL,
    CONSTRAINT standards_regime_org_unit_pkey PRIMARY KEY (standards_regime_id, org_unit_no)
);
CREATE INDEX IF NOT EXISTS srorg_ou_fk_i ON silva.standards_regime_org_unit USING btree (org_unit_no);

-- silva.standards_regime_site_series definition
-- DROP TABLE silva.standards_regime_site_series;
CREATE TABLE IF NOT EXISTS silva.standards_regime_site_series (
    standard_regime_site_series_id int8 NOT NULL,
    standards_regime_id int8 NOT NULL,
    bec_region_code varchar(3) NULL,
    bgc_zone_code varchar(4) NULL,
    bgc_subzone_code varchar(3) NULL,
    bgc_variant varchar(1) NULL,
    bgc_phase varchar(1) NULL,
    bec_site_series varchar(4) NULL,
    bec_site_type varchar(3) NULL,
    bec_seral varchar(4) NULL,
    entry_userid varchar(30) NOT NULL,
    entry_timestamp timestamp(0) NOT NULL,
    update_userid varchar(30) NOT NULL,
    update_timestamp timestamp(0) NOT NULL,
    revision_count int4 NOT NULL,
    CONSTRAINT standards_regime_site_series_pkey PRIMARY KEY (standard_regime_site_series_id)
);
CREATE INDEX IF NOT EXISTS srss_sr_fk_i ON silva.standards_regime_site_series USING btree (standards_regime_id);

-- silva.standards_regime_layer foreign keys
ALTER TABLE silva.standards_regime_layer ADD CONSTRAINT srl_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);
ALTER TABLE silva.standards_regime_layer ADD CONSTRAINT srl_slc_fk FOREIGN KEY (stocking_layer_code) REFERENCES silva.stocking_layer_code(stocking_layer_code);

-- silva.standards_regime_layer_species foreign keys
ALTER TABLE silva.standards_regime_layer_species ADD CONSTRAINT srls_srl_fk FOREIGN KEY (standards_regime_layer_id) REFERENCES silva.standards_regime_layer(standards_regime_layer_id);
ALTER TABLE silva.standards_regime_layer_species ADD CONSTRAINT srls_stsc_fk FOREIGN KEY (silv_tree_species_code) REFERENCES silva.silv_tree_species_code(silv_tree_species_code);

-- silva.standards_regime_client foreign keys
ALTER TABLE silva.standards_regime_client ADD CONSTRAINT srcli_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);
ALTER TABLE silva.standards_regime_client ADD CONSTRAINT srcli_fc_fk FOREIGN KEY (client_number) REFERENCES silva.forest_client(client_number);

-- silva.standards_regime_org_unit foreign keys
ALTER TABLE silva.standards_regime_org_unit ADD CONSTRAINT srorg_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);
ALTER TABLE silva.standards_regime_org_unit ADD CONSTRAINT srorg_ou_fk FOREIGN KEY (org_unit_no) REFERENCES silva.org_unit(org_unit_no);

-- silva.standards_regime_site_series foreign keys
ALTER TABLE silva.standards_regime_site_series ADD CONSTRAINT srss_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);
