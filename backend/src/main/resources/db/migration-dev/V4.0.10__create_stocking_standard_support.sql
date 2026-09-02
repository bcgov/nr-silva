-- Support for POST /api/stocking-standards (create stocking standard, postgres-only).

-- silva.standards_regime new columns
ALTER TABLE silva.standards_regime ADD COLUMN IF NOT EXISTS alternative_method_ind varchar(1) DEFAULT 'N' NOT NULL;
COMMENT ON COLUMN silva.standards_regime.alternative_method_ind IS 'A yes/no indicator whether an alternative method (rather than BEC information) is used to define ecology for this standard.';

ALTER TABLE silva.standards_regime ADD COLUMN IF NOT EXISTS alternate_info varchar(4000) NULL;
COMMENT ON COLUMN silva.standards_regime.alternate_info IS 'Optional free-format note recorded under Stocking Progression when no layer-level detail is supplied.';

-- silva.standards_regime_layer_species: replace preferred_ind (Y/N) with a 3-way species_type_code
ALTER TABLE silva.standards_regime_layer_species RENAME COLUMN preferred_ind TO species_type_code;
ALTER TABLE silva.standards_regime_layer_species ALTER COLUMN species_type_code TYPE varchar(3);
UPDATE silva.standards_regime_layer_species
	SET species_type_code = CASE species_type_code WHEN 'Y' THEN 'PRF' WHEN 'N' THEN 'ACC' ELSE species_type_code END;
COMMENT ON COLUMN silva.standards_regime_layer_species.species_type_code IS 'Replaces the legacy Y/N preferred_ind indicator. Species classification: PRF = Preferred, ACC = Acceptable, ECO = Ecologically Suitable.';

ALTER TABLE silva.standards_regime_layer_species ADD COLUMN IF NOT EXISTS regen_milestone_ind varchar(1) DEFAULT 'Y' NOT NULL;
COMMENT ON COLUMN silva.standards_regime_layer_species.regen_milestone_ind IS 'A yes/no indicator whether this species counts toward the Regeneration milestone.';

ALTER TABLE silva.standards_regime_layer_species ADD COLUMN IF NOT EXISTS free_growing_milestone_ind varchar(1) DEFAULT 'Y' NOT NULL;
COMMENT ON COLUMN silva.standards_regime_layer_species.free_growing_milestone_ind IS 'A yes/no indicator whether this species counts toward the Free Growing milestone.';

-- silva.biogeoclimatic_catalogue definition (base table behind legacy BEC_CODE_TABLE view)
CREATE TABLE IF NOT EXISTS silva.biogeoclimatic_catalogue (
	biogeoclimatic_catalogue_id int8 NOT NULL,
	bec_zone_code varchar(4) NOT NULL,
	subzone varchar(3) NOT NULL,
	variant varchar(1) NULL,
	phase varchar(1) NULL,
	bec_natural_disturbance_code varchar(4) NOT NULL,
	zone_name varchar(35) NOT NULL,
	subzone_name varchar(35) NOT NULL,
	variant_name varchar(20) NULL,
	phase_name varchar(15) NULL,
	notes varchar(72) NULL,
	effective_date date NOT NULL,
	expiry_date date NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	update_userid varchar(30) NOT NULL,
	CONSTRAINT biogeoclimatic_catalogue_pkey PRIMARY KEY (biogeoclimatic_catalogue_id)
);
COMMENT ON TABLE silva.biogeoclimatic_catalogue IS 'The complete set of unique valid combinations of biogeoclimatic Zone, Subzone, Variant and Phase. Any other combinations are not valid.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.biogeoclimatic_catalogue_id IS 'Assigned numeric identifier for a biogeoclimatic catalog entry unique within a biogeoclimatic unit.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.bec_zone_code IS 'A code uniquely identifying a Biogeoclimatic Zone of interest to MOF.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.subzone IS 'A code uniquely identifying a Biogeoclimatic SubZone; only meaningful in conjunction with a Zone.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.variant IS 'A one digit number used to denote a division of a specific biogeoclimatic subzone; only meaningful within a Zone and Subzone.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.phase IS 'A one character code denoting an atypical area, resulting from local relief, in the regional climate of the subzones and variants.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.bec_natural_disturbance_code IS 'The Natural Disturbance code of interest to MOF (NDT1-NDT5).';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.zone_name IS 'Descriptive name given to a large geographic area with similar macroclimate; zone names describe the major vegetation contained within the zone.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.subzone_name IS 'Descriptive name given to a division of the biogeoclimatic zone.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.variant_name IS 'Descriptive name given to a division of the biogeoclimatic subzone.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.phase_name IS 'Descriptive name given to an atypical area, resulting from local relief, in the regional climate of the subzones and variants.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.notes IS 'A comment on the Zone, Subzone, Variant, Phase.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.effective_date IS 'The date that this entry is effective.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.expiry_date IS 'The date that this entry is/becomes expired.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.update_timestamp IS 'The date and time that this entry was last updated.';
COMMENT ON COLUMN silva.biogeoclimatic_catalogue.update_userid IS 'The userid of the person who last updated the entry.';

-- silva.site_series_catalogue definition (base table behind legacy BEC_SITE_SERIES view)
CREATE TABLE IF NOT EXISTS silva.site_series_catalogue (
	site_series_catalogue_id int8 NOT NULL,
	bec_region_code varchar(3) NOT NULL,
	biogeoclimatic_catalogue_id int8 NOT NULL,
	site_series varchar(4) NOT NULL,
	site_series_phase varchar(3) NULL,
	site_series_variation int2 NULL,
	seral varchar(4) NULL,
	description varchar(80) NOT NULL,
	effective_date date NOT NULL,
	expiry_date date NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	update_userid varchar(30) NOT NULL,
	CONSTRAINT site_series_catalogue_pkey PRIMARY KEY (site_series_catalogue_id),
	CONSTRAINT ssc_bc_fk FOREIGN KEY (biogeoclimatic_catalogue_id) REFERENCES silva.biogeoclimatic_catalogue(biogeoclimatic_catalogue_id)
);
CREATE INDEX IF NOT EXISTS ssc_bc_fk_i ON silva.site_series_catalogue USING btree (biogeoclimatic_catalogue_id);
COMMENT ON TABLE silva.site_series_catalogue IS 'Within the BEC system, all sites capable of producing the same mature or climax plant communities within a biogeoclimatic subzone or variant.';
COMMENT ON COLUMN silva.site_series_catalogue.site_series_catalogue_id IS 'Assigned numeric identifier for a site series unique within a biogeoclimatic unit.';
COMMENT ON COLUMN silva.site_series_catalogue.bec_region_code IS 'A code uniquely identifying a BEC Region.';
COMMENT ON COLUMN silva.site_series_catalogue.biogeoclimatic_catalogue_id IS 'Assigned numeric identifier for the biogeoclimatic catalog entry (Zone/Subzone/Variant/Phase) this site series belongs to.';
COMMENT ON COLUMN silva.site_series_catalogue.site_series IS 'A number from 01 to 99 representing the soil moisture regime and soil nutrient regime of a site series, relative to other site series within a subzone or variant.';
COMMENT ON COLUMN silva.site_series_catalogue.site_series_phase IS 'A subdivision of a site series used when site or soil properties differ sufficiently to affect management prescriptions.';
COMMENT ON COLUMN silva.site_series_catalogue.site_series_variation IS 'Site variation describes divergent vegetative trends or floristic features, usually related to short-term successional factors and recent stand history.';
COMMENT ON COLUMN silva.site_series_catalogue.seral IS 'Seral code; may be seral association (4 lower case alphabetic) or seral developmental/structural stage (2 upper case alphabetic).';
COMMENT ON COLUMN silva.site_series_catalogue.description IS 'A connotative label, made up of the names of the potentially dominant plant species for a site series.';
COMMENT ON COLUMN silva.site_series_catalogue.effective_date IS 'The date that this entry is effective.';
COMMENT ON COLUMN silva.site_series_catalogue.expiry_date IS 'The date that this entry is/becomes expired.';
COMMENT ON COLUMN silva.site_series_catalogue.update_timestamp IS 'The date and time that this entry was last updated.';
COMMENT ON COLUMN silva.site_series_catalogue.update_userid IS 'The userid of the person who last updated this entry.';

-- Sequences needed to create new standards regime rows
CREATE SEQUENCE IF NOT EXISTS silva.standards_regime_id_seq;
SELECT setval('silva.standards_regime_id_seq', COALESCE((SELECT MAX(standards_regime_id) FROM silva.standards_regime), 1));

CREATE SEQUENCE IF NOT EXISTS silva.standards_regime_layer_id_seq;
SELECT setval('silva.standards_regime_layer_id_seq', COALESCE((SELECT MAX(standards_regime_layer_id) FROM silva.standards_regime_layer), 1));

CREATE SEQUENCE IF NOT EXISTS silva.standard_regime_site_series_id_seq;
SELECT setval('silva.standard_regime_site_series_id_seq', COALESCE((SELECT MAX(standard_regime_site_series_id) FROM silva.standards_regime_site_series), 1));
