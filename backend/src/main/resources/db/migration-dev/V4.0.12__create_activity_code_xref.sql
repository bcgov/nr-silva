-- Cross-reference tables saying which technique, method, objectives and funding sources can be used with an
-- activity base. They mirror the RESULTS (Oracle) tables THE.SILV_ACTIVITY_XREF, THE.SILV_BASE_OBJECTIVE_XREF
-- and THE.SILV_BASE_FUNDING_XREF. Their data is loaded separately, like the other reference tables.
-- The foreign keys to the code tables are in V4.0.13.

-- silva.silv_activity_xref: valid (base, technique, method) combinations.
-- A NULL technique or method is itself a valid value of the combination, hence NULLS NOT DISTINCT.
CREATE TABLE IF NOT EXISTS silva.silv_activity_xref (
	silv_activity_xref_id int8 NOT NULL,
	silv_activity_measurement_code varchar(3) NOT NULL,
	silv_base_code varchar(2) NOT NULL,
	silv_technique_code varchar(2) NULL,
	silv_method_code varchar(5) NULL,
	estimated_unit_cost numeric(9, 2) NOT NULL,
	entry_userid varchar(30) NOT NULL,
	entry_timestamp timestamp(0) NOT NULL,
	update_userid varchar(30) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	revision_count int4 NOT NULL,
	CONSTRAINT silv_activity_xref_pkey PRIMARY KEY (silv_activity_xref_id),
	CONSTRAINT sax_uk UNIQUE NULLS NOT DISTINCT (silv_base_code, silv_technique_code, silv_method_code)
);

COMMENT ON TABLE silva.silv_activity_xref IS 'Valid combinations of activity base, technique and method.';
COMMENT ON COLUMN silva.silv_activity_xref.silv_activity_measurement_code IS 'Unit in which the activity is measured.';
COMMENT ON COLUMN silva.silv_activity_xref.silv_technique_code IS 'Technique of the combination; NULL when the base is used without a technique.';
COMMENT ON COLUMN silva.silv_activity_xref.silv_method_code IS 'Method of the combination; NULL when the base and technique are used without a method.';
COMMENT ON COLUMN silva.silv_activity_xref.estimated_unit_cost IS 'Estimated cost per measurement unit.';

-- silva.silv_base_objective_xref: objectives that can be used with a base.
CREATE TABLE IF NOT EXISTS silva.silv_base_objective_xref (
	silv_base_code varchar(2) NOT NULL,
	silv_objective_code varchar(3) NOT NULL,
	CONSTRAINT silv_base_objective_xref_pkey PRIMARY KEY (silv_base_code, silv_objective_code)
);

COMMENT ON TABLE silva.silv_base_objective_xref IS 'Objectives that can be used with an activity base.';

-- silva.silv_base_funding_xref: funding sources that can be used with a base.
CREATE TABLE IF NOT EXISTS silva.silv_base_funding_xref (
	silv_fund_srce_code varchar(3) NOT NULL,
	silv_base_code varchar(2) NOT NULL,
	CONSTRAINT silv_base_funding_xref_pkey PRIMARY KEY (silv_fund_srce_code, silv_base_code)
);

COMMENT ON TABLE silva.silv_base_funding_xref IS 'Funding sources that can be used with an activity base.';
