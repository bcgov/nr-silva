-- Silva activities can carry up to five objectives and several funding sources, while
-- silva.activity_treatment_unit only has three objective columns and one funding source column.
-- Legacy columns stay as-is for historical data; new activities write to these child tables.
-- results_ind follows the activity_treatment_unit convention: 'N' = planned set, 'Y' = actual set.

CREATE TABLE IF NOT EXISTS silva.activity_tu_objective (
	activity_treatment_unit_id int8 NOT NULL,
	results_ind varchar(1) NOT NULL,
	silv_objective_code varchar(3) NOT NULL,
	objective_order int2 NOT NULL,
	entry_userid varchar(30) NOT NULL,
	entry_timestamp timestamp(0) NOT NULL,
	CONSTRAINT activity_tu_objective_pkey PRIMARY KEY (activity_treatment_unit_id, results_ind, silv_objective_code),
	CONSTRAINT atuo_results_ind_ck CHECK (results_ind IN ('Y', 'N')),
	CONSTRAINT atuo_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id),
	CONSTRAINT atuo_soc_fk FOREIGN KEY (silv_objective_code) REFERENCES silva.silv_objective_code(silv_objective_code)
);

COMMENT ON TABLE silva.activity_tu_objective IS 'Silviculture objectives of an Activity Treatment Unit, one row per objective for the planned or actual set.';
COMMENT ON COLUMN silva.activity_tu_objective.results_ind IS 'N for the planned set of objectives, Y for the actual set.';
COMMENT ON COLUMN silva.activity_tu_objective.objective_order IS 'Position of the objective as entered by the user, starting at 1.';

CREATE TABLE IF NOT EXISTS silva.activity_tu_fund_srce (
	activity_treatment_unit_id int8 NOT NULL,
	results_ind varchar(1) NOT NULL,
	silv_fund_srce_code varchar(3) NOT NULL,
	fund_srce_order int2 NOT NULL,
	entry_userid varchar(30) NOT NULL,
	entry_timestamp timestamp(0) NOT NULL,
	CONSTRAINT activity_tu_fund_srce_pkey PRIMARY KEY (activity_treatment_unit_id, results_ind, silv_fund_srce_code),
	CONSTRAINT atufs_results_ind_ck CHECK (results_ind IN ('Y', 'N')),
	CONSTRAINT atufs_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id),
	CONSTRAINT atufs_sfsc_fk FOREIGN KEY (silv_fund_srce_code) REFERENCES silva.silv_fund_srce_code(silv_fund_srce_code)
);

COMMENT ON TABLE silva.activity_tu_fund_srce IS 'Funding sources of an Activity Treatment Unit, one row per funding source for the planned or actual set.';
COMMENT ON COLUMN silva.activity_tu_fund_srce.results_ind IS 'N for the planned set of funding sources, Y for the actual set.';
COMMENT ON COLUMN silva.activity_tu_fund_srce.fund_srce_order IS 'Position of the funding source as entered by the user, starting at 1.';

CREATE TABLE IF NOT EXISTS silva.activity_treatment_geometry (
	activity_treatment_unit_id int8 NOT NULL,
	geometry geometry(geometry, 3005) NOT NULL,
	feature_area numeric(11, 4) NOT NULL,
	feature_perimeter numeric(11, 4) NOT NULL,
	capture_method_code varchar(30) NULL,
	feature_class_skey int8 NOT NULL,
	data_source_code varchar(10) NULL,
	observation_date timestamp(0) NULL,
	data_quality_comment varchar(255) NULL,
	entry_userid varchar(30) NOT NULL,
	entry_timestamp timestamp(0) NOT NULL,
	update_userid varchar(30) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	revision_count int4 NOT NULL,
	CONSTRAINT activity_treatment_geometry_pkey PRIMARY KEY (activity_treatment_unit_id),
	CONSTRAINT atug_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id)
);
CREATE INDEX IF NOT EXISTS atug_geometry_i ON silva.activity_treatment_geometry USING gist (geometry);

COMMENT ON TABLE silva.activity_treatment_geometry IS 'The spatial polygon component of an Activity Treatment Unit.';
COMMENT ON COLUMN silva.activity_treatment_geometry.geometry IS 'Spatial coordinate representation of the activity area, in BC Albers (EPSG:3005).';
COMMENT ON COLUMN silva.activity_treatment_geometry.feature_area IS 'The area of the activity polygon measured in hectares.';
COMMENT ON COLUMN silva.activity_treatment_geometry.feature_perimeter IS 'The perimeter of the activity polygon measured in metres.';

-- Sequences needed to create new activities and their comments. They start after the current maximum
-- id; the ETL later loads rows with their original ids, so the application also realigns them with
-- the table maximum every time it allocates an id (see the nextActivityId and nextCommentId queries).
CREATE SEQUENCE IF NOT EXISTS silva.activity_treatment_unit_id_seq;
SELECT setval('silva.activity_treatment_unit_id_seq', COALESCE((SELECT MAX(activity_treatment_unit_id) FROM silva.activity_treatment_unit), 1));

CREATE SEQUENCE IF NOT EXISTS silva.silviculture_comment_id_seq;
SELECT setval('silva.silviculture_comment_id_seq', COALESCE((SELECT MAX(silviculture_comment_id) FROM silva.silviculture_comment), 1));
