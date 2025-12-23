-- silva.age_class_code definition

-- Drop table

-- DROP TABLE silva.age_class_code;

CREATE TABLE IF NOT EXISTS silva.age_class_code (
	age_class_code varchar(1) NOT NULL, -- The age classification of the primary tree species in the previous stand (for the area under the prescription).
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT age_class_code_pkey PRIMARY KEY (age_class_code)
);

-- Column comments

COMMENT ON COLUMN silva.age_class_code.age_class_code IS 'The age classification of the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.age_class_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.age_class_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.age_class_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.age_class_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.block_status_code definition

-- Drop table

-- DROP TABLE silva.block_status_code;

CREATE TABLE IF NOT EXISTS silva.block_status_code (
	block_status_code varchar(3) NOT NULL, -- The before status image of the Cut Block. The status indicates the point the Cut Block is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT block_status_code_pkey PRIMARY KEY (block_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.block_status_code.block_status_code IS 'The before status image of the Cut Block. The status indicates the point the Cut Block is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.';
COMMENT ON COLUMN silva.block_status_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.block_status_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.block_status_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.block_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.cascade_split_code definition

-- Drop table

-- DROP TABLE silva.cascade_split_code;

CREATE TABLE IF NOT EXISTS silva.cascade_split_code (
	cascade_split_code varchar(1) NOT NULL, -- Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT cascade_split_code_pkey PRIMARY KEY (cascade_split_code)
);

-- Column comments

COMMENT ON COLUMN silva.cascade_split_code.cascade_split_code IS 'Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information.';
COMMENT ON COLUMN silva.cascade_split_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.cascade_split_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.cascade_split_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.cascade_split_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.crew_contract_hire_code definition

-- Drop table

-- DROP TABLE silva.crew_contract_hire_code;

CREATE TABLE IF NOT EXISTS silva.crew_contract_hire_code (
	crew_contract_hire_code varchar(1) NOT NULL, -- A code to indicate the tendering process used to select a contractor.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT crew_contract_hire_code_pkey PRIMARY KEY (crew_contract_hire_code)
);

-- Column comments

COMMENT ON COLUMN silva.crew_contract_hire_code.crew_contract_hire_code IS 'A code to indicate the tendering process used to select a contractor.';
COMMENT ON COLUMN silva.crew_contract_hire_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.crew_contract_hire_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.crew_contract_hire_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.crew_contract_hire_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.crown_lands_region_code definition

-- Drop table

-- DROP TABLE silva.crown_lands_region_code;

CREATE TABLE IF NOT EXISTS silva.crown_lands_region_code (
	crown_lands_region_code varchar(1) NOT NULL, -- Describes the CROWN LANDS REGION
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT crown_lands_region_code_pkey PRIMARY KEY (crown_lands_region_code)
);

-- Column comments

COMMENT ON COLUMN silva.crown_lands_region_code.crown_lands_region_code IS 'Describes the CROWN LANDS REGION';
COMMENT ON COLUMN silva.crown_lands_region_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.crown_lands_region_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.crown_lands_region_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.crown_lands_region_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.cut_block_client_type_code definition

-- Drop table

-- DROP TABLE silva.cut_block_client_type_code;

CREATE TABLE IF NOT EXISTS silva.cut_block_client_type_code (
	cut_block_client_type_code varchar(1) NOT NULL, -- Identifes the client types codes applicable for assignment to cut blocks.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT cut_block_client_type_code_pkey PRIMARY KEY (cut_block_client_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.cut_block_client_type_code.cut_block_client_type_code IS 'Identifes the client types codes applicable for assignment to cut blocks.';
COMMENT ON COLUMN silva.cut_block_client_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.cut_block_client_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.cut_block_client_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.cut_block_client_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.cut_regulation_code definition

-- Drop table

-- DROP TABLE silva.cut_regulation_code;

CREATE TABLE IF NOT EXISTS silva.cut_regulation_code (
	cut_regulation_code varchar(3) NOT NULL, -- Identifies the type of regulation applicable to a given cut block. Valid values include "Bark Beetle Regulation", and "Small Scale Salvage".
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT cut_regulation_code_pkey PRIMARY KEY (cut_regulation_code)
);

-- Column comments

COMMENT ON COLUMN silva.cut_regulation_code.cut_regulation_code IS 'Identifies the type of regulation applicable to a given cut block. Valid values include "Bark Beetle Regulation", and "Small Scale Salvage".';
COMMENT ON COLUMN silva.cut_regulation_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.cut_regulation_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.cut_regulation_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.cut_regulation_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.data_source_code definition

-- Drop table

-- DROP TABLE silva.data_source_code;

CREATE TABLE IF NOT EXISTS silva.data_source_code (
	data_source_code varchar(10) NOT NULL, -- Describes the origin of a feature image. E.g. Air Photo, Forest Cover Maps, Satelite.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT data_source_code_pkey PRIMARY KEY (data_source_code)
);

-- Column comments

COMMENT ON COLUMN silva.data_source_code.data_source_code IS 'Describes the origin of a feature image. E.g. Air Photo, Forest Cover Maps, Satelite.';
COMMENT ON COLUMN silva.data_source_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.data_source_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.data_source_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.data_source_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.disturbance_code definition

-- Drop table

-- DROP TABLE silva.disturbance_code;

CREATE TABLE IF NOT EXISTS silva.disturbance_code (
	disturbance_code varchar(3) NOT NULL, -- A code indicating the reason for the disturbance within the opening.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT disturbance_code_pkey PRIMARY KEY (disturbance_code)
);

-- Column comments

COMMENT ON COLUMN silva.disturbance_code.disturbance_code IS 'A code indicating the reason for the disturbance within the opening.';
COMMENT ON COLUMN silva.disturbance_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.disturbance_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.disturbance_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.disturbance_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.file_client_type_code definition

-- Drop table

-- DROP TABLE silva.file_client_type_code;

CREATE TABLE IF NOT EXISTS silva.file_client_type_code (
	file_client_type_code varchar(1) NOT NULL, -- Identifes the client types codes applicable for assignment to forest files.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT file_client_type_code_pkey PRIMARY KEY (file_client_type_code)
);
COMMENT ON TABLE silva.file_client_type_code IS 'Identifes the client types codes applicable for assignment to forest files.';

-- Column comments

COMMENT ON COLUMN silva.file_client_type_code.file_client_type_code IS 'Identifes the client types codes applicable for assignment to forest files.';
COMMENT ON COLUMN silva.file_client_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.file_client_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.file_client_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.file_client_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.file_type_code definition

-- Drop table

-- DROP TABLE silva.file_type_code;

CREATE TABLE IF NOT EXISTS silva.file_type_code (
	file_type_code varchar(10) NOT NULL, -- The FTAS code to indicate the type of file, and often synonymous with a tenure or a project.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT file_type_code_pkey PRIMARY KEY (file_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.file_type_code.file_type_code IS 'The FTAS code to indicate the type of file, and often synonymous with a tenure or a project.';
COMMENT ON COLUMN silva.file_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.file_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.file_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.file_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.fire_harvesting_reason_code definition

-- Drop table

-- DROP TABLE silva.fire_harvesting_reason_code;

CREATE TABLE IF NOT EXISTS silva.fire_harvesting_reason_code (
	fire_harvesting_reason_code varchar(10) NOT NULL, -- FIRE_HARVESTING_REASON_CODE is a code that identifies a reason for harvesting related to fire activities (prevention, clean up).
	description varchar(120) NOT NULL,
	effective_date timestamp(0) NOT NULL,
	expiry_date timestamp(0) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	CONSTRAINT fire_harvesting_reason_code_pkey PRIMARY KEY (fire_harvesting_reason_code)
);
COMMENT ON TABLE silva.fire_harvesting_reason_code IS 'A code or abbreviated text uniquely identifies a reason for harvesting related to fire activities (prevention, clean up)';

-- Column comments

COMMENT ON COLUMN silva.fire_harvesting_reason_code.fire_harvesting_reason_code IS 'FIRE_HARVESTING_REASON_CODE is a code that identifies a reason for harvesting related to fire activities (prevention, clean up).';


-- silva.forest_cover_layer_code definition

-- Drop table

-- DROP TABLE silva.forest_cover_layer_code;

CREATE TABLE IF NOT EXISTS silva.forest_cover_layer_code (
	forest_cover_layer_code varchar(2) NOT NULL, -- A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT forest_cover_layer_code_pkey PRIMARY KEY (forest_cover_layer_code)
);

-- Column comments

COMMENT ON COLUMN silva.forest_cover_layer_code.forest_cover_layer_code IS 'A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.';
COMMENT ON COLUMN silva.forest_cover_layer_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.forest_cover_layer_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.forest_cover_layer_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.forest_cover_layer_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.fsp_status_code definition

-- Drop table

-- DROP TABLE silva.fsp_status_code;

CREATE TABLE IF NOT EXISTS silva.fsp_status_code (
	fsp_status_code varchar(4) NOT NULL, -- A code indicating the status of the Forest Stewardship Plan. Examples include but are not limited to SUB (submitted), REJ (rejected) and APP (approved).
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT fsp_status_code_pkey PRIMARY KEY (fsp_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.fsp_status_code.fsp_status_code IS 'A code indicating the status of the Forest Stewardship Plan. Examples include but are not limited to SUB (submitted), REJ (rejected) and APP (approved).';
COMMENT ON COLUMN silva.fsp_status_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.fsp_status_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.fsp_status_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.fsp_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.harvest_auth_extend_reas_code definition

-- Drop table

-- DROP TABLE silva.harvest_auth_extend_reas_code;

CREATE TABLE IF NOT EXISTS silva.harvest_auth_extend_reas_code (
	harvest_auth_extend_reas_code varchar(1) NOT NULL, -- Code to indicate the reason to extend the life of the timber mark.
	description varchar(120) NOT NULL, -- Description of the code
	effective_date timestamp(0) NOT NULL, -- Date the code is effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT harvest_auth_extend_reas_code_pkey PRIMARY KEY (harvest_auth_extend_reas_code)
);

-- Column comments

COMMENT ON COLUMN silva.harvest_auth_extend_reas_code.harvest_auth_extend_reas_code IS 'Code to indicate the reason to extend the life of the timber mark.';
COMMENT ON COLUMN silva.harvest_auth_extend_reas_code.description IS 'Description of the code';
COMMENT ON COLUMN silva.harvest_auth_extend_reas_code.effective_date IS 'Date the code is effective';
COMMENT ON COLUMN silva.harvest_auth_extend_reas_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.harvest_auth_extend_reas_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.harvest_auth_status_code definition

-- Drop table

-- DROP TABLE silva.harvest_auth_status_code;

CREATE TABLE IF NOT EXISTS silva.harvest_auth_status_code (
	harvest_auth_status_code varchar(3) NOT NULL, -- The after status image of the harvesting authority. The status indicates the point the harvesting authority is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT harvest_auth_status_code_pkey PRIMARY KEY (harvest_auth_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.harvest_auth_status_code.harvest_auth_status_code IS 'The after status image of the harvesting authority. The status indicates the point the harvesting authority is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.';
COMMENT ON COLUMN silva.harvest_auth_status_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.harvest_auth_status_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.harvest_auth_status_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.harvest_auth_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.harvest_type_code definition

-- Drop table

-- DROP TABLE silva.harvest_type_code;

CREATE TABLE IF NOT EXISTS silva.harvest_type_code (
	harvest_type_code varchar(1) NOT NULL, -- Identifies the type of harvesting authority. Valid values include "Green", "Road", "Multi-Mark", and "Fort St. John".
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT harvest_type_code_pkey PRIMARY KEY (harvest_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.harvest_type_code.harvest_type_code IS 'Identifies the type of harvesting authority. Valid values include "Green", "Road", "Multi-Mark", and "Fort St. John".';
COMMENT ON COLUMN silva.harvest_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.harvest_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.harvest_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.harvest_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.height_class_code definition

-- Drop table

-- DROP TABLE silva.height_class_code;

CREATE TABLE IF NOT EXISTS silva.height_class_code (
	height_class_code varchar(1) NOT NULL, -- The height clan code (or the primary tree species in the previous stand (for the area under the prescription).
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT height_class_code_pkey PRIMARY KEY (height_class_code)
);

-- Column comments

COMMENT ON COLUMN silva.height_class_code.height_class_code IS 'The height clan code (or the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.height_class_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.height_class_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.height_class_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.height_class_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.licence_to_cut_code definition

-- Drop table

-- DROP TABLE silva.licence_to_cut_code;

CREATE TABLE IF NOT EXISTS silva.licence_to_cut_code (
	licence_to_cut_code varchar(2) NOT NULL, -- The purpose or reason that the licence to cut was granted, eg., right of way.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT licence_to_cut_code_pkey PRIMARY KEY (licence_to_cut_code)
);

-- Column comments

COMMENT ON COLUMN silva.licence_to_cut_code.licence_to_cut_code IS 'The purpose or reason that the licence to cut was granted, eg., right of way.';
COMMENT ON COLUMN silva.licence_to_cut_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.licence_to_cut_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.licence_to_cut_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.licence_to_cut_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.mark_extension_reason_code definition

-- Drop table

-- DROP TABLE silva.mark_extension_reason_code;

CREATE TABLE IF NOT EXISTS silva.mark_extension_reason_code (
	mark_extension_reason_code varchar(1) NOT NULL, -- Code to indicate the reason to extend the life of the timber mark.
	description varchar(120) NOT NULL,
	effective_date timestamp(0) NOT NULL, -- Date the code is effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT mark_extension_reason_code_pkey PRIMARY KEY (mark_extension_reason_code)
);

-- Column comments

COMMENT ON COLUMN silva.mark_extension_reason_code.mark_extension_reason_code IS 'Code to indicate the reason to extend the life of the timber mark.';
COMMENT ON COLUMN silva.mark_extension_reason_code.effective_date IS 'Date the code is effective';
COMMENT ON COLUMN silva.mark_extension_reason_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.mark_extension_reason_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.marking_instrument_code definition

-- Drop table

-- DROP TABLE silva.marking_instrument_code;

CREATE TABLE IF NOT EXISTS silva.marking_instrument_code (
	marking_instrument_code varchar(1) NOT NULL, -- Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.
	description varchar(120) NOT NULL, -- Description of the code
	effective_date timestamp(0) NOT NULL, -- Date the code is effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT marking_instrument_code_pkey PRIMARY KEY (marking_instrument_code)
);

-- Column comments

COMMENT ON COLUMN silva.marking_instrument_code.marking_instrument_code IS 'Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.';
COMMENT ON COLUMN silva.marking_instrument_code.description IS 'Description of the code';
COMMENT ON COLUMN silva.marking_instrument_code.effective_date IS 'Date the code is effective';
COMMENT ON COLUMN silva.marking_instrument_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.marking_instrument_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.marking_method_code definition

-- Drop table

-- DROP TABLE silva.marking_method_code;

CREATE TABLE IF NOT EXISTS silva.marking_method_code (
	marking_method_code varchar(1) NOT NULL, -- Code indicating the method of marking the truckload of harvested timber.
	description varchar(120) NOT NULL, -- Description of the code
	effective_date timestamp(0) NOT NULL, -- Date the code is effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT marking_method_code_pkey PRIMARY KEY (marking_method_code)
);

-- Column comments

COMMENT ON COLUMN silva.marking_method_code.marking_method_code IS 'Code indicating the method of marking the truckload of harvested timber.';
COMMENT ON COLUMN silva.marking_method_code.description IS 'Description of the code';
COMMENT ON COLUMN silva.marking_method_code.effective_date IS 'Date the code is effective';
COMMENT ON COLUMN silva.marking_method_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.marking_method_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.mgmt_unit_type_code definition

-- Drop table

-- DROP TABLE silva.mgmt_unit_type_code;

CREATE TABLE IF NOT EXISTS silva.mgmt_unit_type_code (
	mgmt_unit_type_code varchar(1) NOT NULL, -- The unique identifier for the management unit type.
	description varchar(120) NOT NULL, -- The full description of the value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date after which the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT mgmt_unit_type_code_pkey PRIMARY KEY (mgmt_unit_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.mgmt_unit_type_code.mgmt_unit_type_code IS 'The unique identifier for the management unit type.';
COMMENT ON COLUMN silva.mgmt_unit_type_code.description IS 'The full description of the value.';
COMMENT ON COLUMN silva.mgmt_unit_type_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.mgmt_unit_type_code.expiry_date IS 'The date after which the value is no longer available for use.';
COMMENT ON COLUMN silva.mgmt_unit_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.mime_type_code definition

-- Drop table

-- DROP TABLE silva.mime_type_code;

CREATE TABLE IF NOT EXISTS silva.mime_type_code (
	mime_type_code varchar(3) NOT NULL, -- Multipurpose Internet Mail Extension (MIME) type is the method used by Web browsers to associate files of a certain type with helper applications that display files of that type.
	description varchar(120) NOT NULL, -- The full description of the value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date after which the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT mime_type_code_pkey PRIMARY KEY (mime_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.mime_type_code.mime_type_code IS 'Multipurpose Internet Mail Extension (MIME) type is the method used by Web browsers to associate files of a certain type with helper applications that display files of that type.';
COMMENT ON COLUMN silva.mime_type_code.description IS 'The full description of the value.';
COMMENT ON COLUMN silva.mime_type_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.mime_type_code.expiry_date IS 'The date after which the value is no longer available for use.';
COMMENT ON COLUMN silva.mime_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.open_category_code definition

-- Drop table

-- DROP TABLE silva.open_category_code;

CREATE TABLE IF NOT EXISTS silva.open_category_code (
	open_category_code varchar(7) NOT NULL, -- A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT open_category_code_pkey PRIMARY KEY (open_category_code)
);

-- Column comments

COMMENT ON COLUMN silva.open_category_code.open_category_code IS 'A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.';
COMMENT ON COLUMN silva.open_category_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.open_category_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.open_category_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.open_category_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.opening_status_code definition

-- Drop table

-- DROP TABLE silva.opening_status_code;

CREATE TABLE IF NOT EXISTS silva.opening_status_code (
	opening_status_code varchar(3) NOT NULL, -- A code indicating the status of the prescription. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT opening_status_code_pkey PRIMARY KEY (opening_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.opening_status_code.opening_status_code IS 'A code indicating the status of the prescription. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.';
COMMENT ON COLUMN silva.opening_status_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.opening_status_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.opening_status_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.opening_status_code.update_timestamp IS 'The date and time the value was last modified.';

-- silva.quota_type_code definition

-- Drop table

-- DROP TABLE silva.quota_type_code;

CREATE TABLE IF NOT EXISTS silva.quota_type_code (
	quota_type_code varchar(1) NOT NULL, -- A Timber Supply Area will have volumes assigned to one or more partitions, which are in effect different timber type classifications, or geographic locations. Under each partition are the different apportionments. Examples of Partitions are Conventional
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT quota_type_code_pkey PRIMARY KEY (quota_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.quota_type_code.quota_type_code IS 'A Timber Supply Area will have volumes assigned to one or more partitions, which are in effect different timber type classifications, or geographic locations. Under each partition are the different apportionments. Examples of Partitions are Conventional';
COMMENT ON COLUMN silva.quota_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.quota_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.quota_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.quota_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.reforest_declare_type_code definition

-- Drop table

-- DROP TABLE silva.reforest_declare_type_code;

CREATE TABLE IF NOT EXISTS silva.reforest_declare_type_code (
	reforest_declare_type_code varchar(3) NOT NULL, -- Identifies the reforestation declaration type. E.g. Coniferous, Deciduos, Mix.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT reforest_declare_type_code_pkey PRIMARY KEY (reforest_declare_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.reforest_declare_type_code.reforest_declare_type_code IS 'Identifies the reforestation declaration type. E.g. Coniferous, Deciduos, Mix.';
COMMENT ON COLUMN silva.reforest_declare_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.reforest_declare_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.reforest_declare_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.reforest_declare_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.results_audit_action_code definition

-- Drop table

-- DROP TABLE silva.results_audit_action_code;

CREATE TABLE IF NOT EXISTS silva.results_audit_action_code (
	results_audit_action_code varchar(3) NOT NULL, -- The action code is a code associated with the action. I, -- Insert
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT results_audit_action_code_pkey PRIMARY KEY (results_audit_action_code)
);

-- Column comments

COMMENT ON COLUMN silva.results_audit_action_code.results_audit_action_code IS 'The action code is a code associated with the action. I -- Insert';
COMMENT ON COLUMN silva.results_audit_action_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.results_audit_action_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.results_audit_action_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.results_audit_action_code.update_timestamp IS 'The date and time the value was last modified.';

-- silva.salvage_type_code definition

-- Drop table

-- DROP TABLE silva.salvage_type_code;

CREATE TABLE IF NOT EXISTS silva.salvage_type_code (
	salvage_type_code varchar(3) NOT NULL, -- Code describing the salvage type.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT salvage_type_code_pkey PRIMARY KEY (salvage_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.salvage_type_code.salvage_type_code IS 'Code describing the salvage type.';
COMMENT ON COLUMN silva.salvage_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.salvage_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.salvage_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.salvage_type_code.update_timestamp IS 'The date and time the value was last modified.';

-- silva.silv_activity_measurement_code definition

-- Drop table

-- DROP TABLE silva.silv_activity_measurement_code;

CREATE TABLE IF NOT EXISTS silva.silv_activity_measurement_code (
	silv_activity_measurement_code varchar(3) NOT NULL, -- A code indicating the measurement code for the activity.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_activity_measurement_code_pkey PRIMARY KEY (silv_activity_measurement_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_activity_measurement_code.silv_activity_measurement_code IS 'A code indicating the measurement code for the activity.';
COMMENT ON COLUMN silva.silv_activity_measurement_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_activity_measurement_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_activity_measurement_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_activity_measurement_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_base_code definition

-- Drop table

-- DROP TABLE silva.silv_base_code;

CREATE TABLE IF NOT EXISTS silva.silv_base_code (
	silv_base_code varchar(2) NOT NULL, -- Identifies a primary category of Silviculture Activity.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_base_code_pkey PRIMARY KEY (silv_base_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_base_code.silv_base_code IS 'Identifies a primary category of Silviculture Activity.';
COMMENT ON COLUMN silva.silv_base_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_base_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_base_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_base_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_comment_source_code definition

-- Drop table

-- DROP TABLE silva.silv_comment_source_code;

CREATE TABLE IF NOT EXISTS silva.silv_comment_source_code (
	silv_comment_source_code varchar(4) NOT NULL, -- Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_comment_source_code_pkey PRIMARY KEY (silv_comment_source_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_comment_source_code.silv_comment_source_code IS 'Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.';
COMMENT ON COLUMN silva.silv_comment_source_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_comment_source_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_comment_source_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_comment_source_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_comment_type_code definition

-- Drop table

-- DROP TABLE silva.silv_comment_type_code;

CREATE TABLE IF NOT EXISTS silva.silv_comment_type_code (
	silv_comment_type_code varchar(8) NOT NULL, -- Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_comment_type_code_pkey PRIMARY KEY (silv_comment_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_comment_type_code.silv_comment_type_code IS 'Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.';
COMMENT ON COLUMN silva.silv_comment_type_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_comment_type_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_comment_type_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_comment_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_cut_phase_code definition

-- Drop table

-- DROP TABLE silva.silv_cut_phase_code;

CREATE TABLE IF NOT EXISTS silva.silv_cut_phase_code (
	silv_cut_phase_code varchar(5) NOT NULL, -- The code for the actual silvicultural system cut phase. The cut phase of a silvicultural system variant describes the function of a harvest to extract merchantable timber and achieve a silviculture treatment.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_cut_phase_code_pkey PRIMARY KEY (silv_cut_phase_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_cut_phase_code.silv_cut_phase_code IS 'The code for the actual silvicultural system cut phase. The cut phase of a silvicultural system variant describes the function of a harvest to extract merchantable timber and achieve a silviculture treatment.';
COMMENT ON COLUMN silva.silv_cut_phase_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_cut_phase_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_cut_phase_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_cut_phase_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_damage_agent_code definition

-- Drop table

-- DROP TABLE silva.silv_damage_agent_code;

CREATE TABLE IF NOT EXISTS silva.silv_damage_agent_code (
	silv_damage_agent_code varchar(3) NOT NULL, -- A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_damage_agent_code_pkey PRIMARY KEY (silv_damage_agent_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_damage_agent_code.silv_damage_agent_code IS 'A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.';
COMMENT ON COLUMN silva.silv_damage_agent_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_damage_agent_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_damage_agent_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_damage_agent_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_fund_srce_code definition

-- Drop table

-- DROP TABLE silva.silv_fund_srce_code;

CREATE TABLE IF NOT EXISTS silva.silv_fund_srce_code (
	silv_fund_srce_code varchar(3) NOT NULL, -- A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_fund_srce_code_pkey PRIMARY KEY (silv_fund_srce_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_fund_srce_code.silv_fund_srce_code IS 'A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.';
COMMENT ON COLUMN silva.silv_fund_srce_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_fund_srce_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_fund_srce_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_fund_srce_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_method_code definition

-- Drop table

-- DROP TABLE silva.silv_method_code;

CREATE TABLE IF NOT EXISTS silva.silv_method_code (
	silv_method_code varchar(5) NOT NULL, -- Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_method_code_pkey PRIMARY KEY (silv_method_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_method_code.silv_method_code IS 'Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.';
COMMENT ON COLUMN silva.silv_method_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_method_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_method_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_method_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_milestone_type_code definition

-- Drop table

-- DROP TABLE silva.silv_milestone_type_code;

CREATE TABLE IF NOT EXISTS silva.silv_milestone_type_code (
	silv_milestone_type_code varchar(3) NOT NULL, -- A means of identifying if the associated milestone is for post harvest, regeneration or free growing.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_milestone_type_code_pkey PRIMARY KEY (silv_milestone_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_milestone_type_code.silv_milestone_type_code IS 'A means of identifying if the associated milestone is for post harvest, regeneration or free growing.';
COMMENT ON COLUMN silva.silv_milestone_type_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_milestone_type_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_milestone_type_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_milestone_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_objective_code definition

-- Drop table

-- DROP TABLE silva.silv_objective_code;

CREATE TABLE IF NOT EXISTS silva.silv_objective_code (
	silv_objective_code varchar(3) NOT NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_objective_code_pkey PRIMARY KEY (silv_objective_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_objective_code.silv_objective_code IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.silv_objective_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_objective_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_objective_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_objective_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_project_status_code definition

-- Drop table

-- DROP TABLE silva.silv_project_status_code;

CREATE TABLE IF NOT EXISTS silva.silv_project_status_code (
	silv_project_status_code varchar(3) NOT NULL, -- A code to indicate the current status of a SILVICULTURE PROJECT.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_project_status_code_pkey PRIMARY KEY (silv_project_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_project_status_code.silv_project_status_code IS 'A code to indicate the current status of a SILVICULTURE PROJECT.';
COMMENT ON COLUMN silva.silv_project_status_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.silv_project_status_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_project_status_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.silv_project_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_relief_appl_status_code definition

-- Drop table

-- DROP TABLE silva.silv_relief_appl_status_code;

CREATE TABLE IF NOT EXISTS silva.silv_relief_appl_status_code (
	silv_relief_appl_status_code varchar(3) NOT NULL, -- Code identifying the status of the relief application.
	description varchar(120) NOT NULL, -- A full description of the code value.
	effective_date timestamp(0) NOT NULL, -- Date that the code is available for use.
	expiry_date timestamp(0) NOT NULL, -- Date that the code has been expired and is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	CONSTRAINT silv_relief_appl_status_code_pkey PRIMARY KEY (silv_relief_appl_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_relief_appl_status_code.silv_relief_appl_status_code IS 'Code identifying the status of the relief application.';
COMMENT ON COLUMN silva.silv_relief_appl_status_code.description IS 'A full description of the code value.';
COMMENT ON COLUMN silva.silv_relief_appl_status_code.effective_date IS 'Date that the code is available for use.';
COMMENT ON COLUMN silva.silv_relief_appl_status_code.expiry_date IS 'Date that the code has been expired and is no longer available for use.';
COMMENT ON COLUMN silva.silv_relief_appl_status_code.update_timestamp IS 'The date and time of the last update.';


-- silva.silv_reserve_code definition

-- Drop table

-- DROP TABLE silva.silv_reserve_code;

CREATE TABLE IF NOT EXISTS silva.silv_reserve_code (
	silv_reserve_code varchar(1) NOT NULL, -- A code for the type of reserve. Values are group (b), uniform (u) or variable (v).
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_reserve_code_pkey PRIMARY KEY (silv_reserve_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_reserve_code.silv_reserve_code IS 'A code for the type of reserve. Values are group (b), uniform (u) or variable (v).';
COMMENT ON COLUMN silva.silv_reserve_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_reserve_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_reserve_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_reserve_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_reserve_objective_code definition

-- Drop table

-- DROP TABLE silva.silv_reserve_objective_code;

CREATE TABLE IF NOT EXISTS silva.silv_reserve_objective_code (
	silv_reserve_objective_code varchar(3) NOT NULL, -- A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_reserve_objective_code_pkey PRIMARY KEY (silv_reserve_objective_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_reserve_objective_code.silv_reserve_objective_code IS 'A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .';
COMMENT ON COLUMN silva.silv_reserve_objective_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_reserve_objective_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_reserve_objective_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_reserve_objective_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_statute_code definition

-- Drop table

-- DROP TABLE silva.silv_statute_code;

CREATE TABLE IF NOT EXISTS silva.silv_statute_code (
	silv_statute_code varchar(3) NOT NULL, -- A legislative statute based on a current Act, Regulation or policy
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT silv_statute_code_pkey PRIMARY KEY (silv_statute_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_statute_code.silv_statute_code IS 'A legislative statute based on a current Act, Regulation or policy';
COMMENT ON COLUMN silva.silv_statute_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.silv_statute_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.silv_statute_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.silv_statute_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.silv_system_code definition

-- Drop table

-- DROP TABLE silva.silv_system_code;

CREATE TABLE IF NOT EXISTS silva.silv_system_code (
	silv_system_code varchar(5) NOT NULL, -- The code signifying the silviculture system that the post logging treatment code is to be applied to. (Ex. "B" for Basic silviculture).
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_system_code_pkey PRIMARY KEY (silv_system_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_system_code.silv_system_code IS 'The code signifying the silviculture system that the post logging treatment code is to be applied to. (Ex. "B" for Basic silviculture).';
COMMENT ON COLUMN silva.silv_system_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_system_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_system_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_system_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_system_variant_code definition

-- Drop table

-- DROP TABLE silva.silv_system_variant_code;

CREATE TABLE IF NOT EXISTS silva.silv_system_variant_code (
	silv_system_variant_code varchar(3) NOT NULL, -- The code for the silvicultural system variant, which further describes the functional attributes of the particular silvicultural system
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_system_variant_code_pkey PRIMARY KEY (silv_system_variant_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_system_variant_code.silv_system_variant_code IS 'The code for the silvicultural system variant, which further describes the functional attributes of the particular silvicultural system';
COMMENT ON COLUMN silva.silv_system_variant_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_system_variant_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_system_variant_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_system_variant_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_technique_code definition

-- Drop table

-- DROP TABLE silva.silv_technique_code;

CREATE TABLE IF NOT EXISTS silva.silv_technique_code (
	silv_technique_code varchar(2) NOT NULL, -- Describes the broad category of technique, used for the base activity (eg.BU = BURN).
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_technique_code_pkey PRIMARY KEY (silv_technique_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_technique_code.silv_technique_code IS 'Describes the broad category of technique, used for the base activity (eg.BU = BURN).';
COMMENT ON COLUMN silva.silv_technique_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_technique_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_technique_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_technique_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.silv_tree_species_code definition

-- Drop table

-- DROP TABLE silva.silv_tree_species_code;

CREATE TABLE IF NOT EXISTS silva.silv_tree_species_code (
	silv_tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT silv_tree_species_code_pkey PRIMARY KEY (silv_tree_species_code)
);

-- Column comments

COMMENT ON COLUMN silva.silv_tree_species_code.silv_tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.silv_tree_species_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.silv_tree_species_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.silv_tree_species_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.silv_tree_species_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.site_class_code definition

-- Drop table

-- DROP TABLE silva.site_class_code;

CREATE TABLE IF NOT EXISTS silva.site_class_code (
	site_class_code varchar(1) NOT NULL, -- This code indicates the quality of the land. Note: This column is not maintained by the system.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT site_class_code_pkey PRIMARY KEY (site_class_code)
);

-- Column comments

COMMENT ON COLUMN silva.site_class_code.site_class_code IS 'This code indicates the quality of the land. Note: This column is not maintained by the system.';
COMMENT ON COLUMN silva.site_class_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.site_class_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.site_class_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.site_class_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.site_index_source_code definition

-- Drop table

-- DROP TABLE silva.site_index_source_code;

CREATE TABLE IF NOT EXISTS silva.site_index_source_code (
	site_index_source_code varchar(1) NOT NULL, -- A code describing the source, or origin, of the estimated site index.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT site_index_source_code_pkey PRIMARY KEY (site_index_source_code)
);

-- Column comments

COMMENT ON COLUMN silva.site_index_source_code.site_index_source_code IS 'A code describing the source, or origin, of the estimated site index.';
COMMENT ON COLUMN silva.site_index_source_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.site_index_source_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.site_index_source_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.site_index_source_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.standards_regime_status_code definition

-- Drop table

-- DROP TABLE silva.standards_regime_status_code;

CREATE TABLE IF NOT EXISTS silva.standards_regime_status_code (
	standards_regime_status_code varchar(3) NOT NULL, -- The status of the STANDARD REGIME. Starts out as submitted and must be approved by an authorized resource prior to being used for a submission.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT standards_regime_status_code_pkey PRIMARY KEY (standards_regime_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.standards_regime_status_code.standards_regime_status_code IS 'The status of the STANDARD REGIME. Starts out as submitted and must be approved by an authorized resource prior to being used for a submission.';
COMMENT ON COLUMN silva.standards_regime_status_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.standards_regime_status_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.standards_regime_status_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.standards_regime_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.stocking_class_code definition

-- Drop table

-- DROP TABLE silva.stocking_class_code;

CREATE TABLE IF NOT EXISTS silva.stocking_class_code (
	stocking_class_code varchar(1) NOT NULL, -- The stocking class code for the primary tree species in the previous stand (for the area under the prescription).
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT stocking_class_code_pkey PRIMARY KEY (stocking_class_code)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_class_code.stocking_class_code IS 'The stocking class code for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.stocking_class_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.stocking_class_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.stocking_class_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.stocking_class_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.stocking_layer_code definition

-- Drop table

-- DROP TABLE silva.stocking_layer_code;

CREATE TABLE IF NOT EXISTS silva.stocking_layer_code (
	stocking_layer_code varchar(2) NOT NULL, -- A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT stocking_layer_code_pkey PRIMARY KEY (stocking_layer_code)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_layer_code.stocking_layer_code IS 'A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.';
COMMENT ON COLUMN silva.stocking_layer_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.stocking_layer_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.stocking_layer_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.stocking_layer_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.stocking_status_code definition

-- Drop table

-- DROP TABLE silva.stocking_status_code;

CREATE TABLE IF NOT EXISTS silva.stocking_status_code (
	stocking_status_code varchar(3) NOT NULL, -- A code to describe the previous stand type for the area under the prescription.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT stocking_status_code_pkey PRIMARY KEY (stocking_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_status_code.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.stocking_status_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.stocking_status_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.stocking_status_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.stocking_status_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.stocking_type_code definition

-- Drop table

-- DROP TABLE silva.stocking_type_code;

CREATE TABLE IF NOT EXISTS silva.stocking_type_code (
	stocking_type_code varchar(3) NOT NULL, -- A further classification of the stocking status for the polygon.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT stocking_type_code_pkey PRIMARY KEY (stocking_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_type_code.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.stocking_type_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.stocking_type_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.stocking_type_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.stocking_type_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.tenure_file_status_code definition

-- Drop table

-- DROP TABLE silva.tenure_file_status_code;

CREATE TABLE IF NOT EXISTS silva.tenure_file_status_code (
	tenure_file_status_code varchar(3) NOT NULL, -- Tenure file status code
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT tenure_file_status_code_pkey PRIMARY KEY (tenure_file_status_code)
);

-- Column comments

COMMENT ON COLUMN silva.tenure_file_status_code.tenure_file_status_code IS 'Tenure file status code';
COMMENT ON COLUMN silva.tenure_file_status_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.tenure_file_status_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.tenure_file_status_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.tenure_file_status_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.tree_cover_pattern_code definition

-- Drop table

-- DROP TABLE silva.tree_cover_pattern_code;

CREATE TABLE IF NOT EXISTS silva.tree_cover_pattern_code (
	tree_cover_pattern_code varchar(1) NOT NULL, -- The dispursion of trees over the area.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT tree_cover_pattern_code_pkey PRIMARY KEY (tree_cover_pattern_code)
);

-- Column comments

COMMENT ON COLUMN silva.tree_cover_pattern_code.tree_cover_pattern_code IS 'The dispursion of trees over the area.';
COMMENT ON COLUMN silva.tree_cover_pattern_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.tree_cover_pattern_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.tree_cover_pattern_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.tree_cover_pattern_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.tree_size_unit_code definition

-- Drop table

-- DROP TABLE silva.tree_size_unit_code;

CREATE TABLE silva.tree_size_unit_code (
	tree_size_unit_code varchar(3) NOT NULL, -- The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT tree_size_unit_code_pkey PRIMARY KEY (tree_size_unit_code)
);

-- Column comments

COMMENT ON COLUMN silva.tree_size_unit_code.tree_size_unit_code IS 'The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).';
COMMENT ON COLUMN silva.tree_size_unit_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.tree_size_unit_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.tree_size_unit_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.tree_size_unit_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.tree_species_code definition

-- Drop table

-- DROP TABLE silva.tree_species_code;

CREATE TABLE IF NOT EXISTS silva.tree_species_code (
	tree_species_code varchar(3) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is lo longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified
	CONSTRAINT tree_species_code_pkey PRIMARY KEY (tree_species_code)
);

-- Column comments

COMMENT ON COLUMN silva.tree_species_code.tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.tree_species_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.tree_species_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.tree_species_code.expiry_date IS 'The date the value is lo longer available for use.';
COMMENT ON COLUMN silva.tree_species_code.update_timestamp IS 'The date and time the value was last modified';


-- silva.tsb_number_code definition

-- Drop table

-- DROP TABLE silva.tsb_number_code;

CREATE TABLE IF NOT EXISTS silva.tsb_number_code (
	tsb_number_code varchar(3) NOT NULL, -- The unique code identifying a Timber Supply Block.
	description varchar(120) NOT NULL, -- The full description of the code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date after which the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT tsb_number_code_pkey PRIMARY KEY (tsb_number_code)
);

-- Column comments

COMMENT ON COLUMN silva.tsb_number_code.tsb_number_code IS 'The unique code identifying a Timber Supply Block.';
COMMENT ON COLUMN silva.tsb_number_code.description IS 'The full description of the code value.';
COMMENT ON COLUMN silva.tsb_number_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.tsb_number_code.expiry_date IS 'The date after which the value is no longer available for use.';
COMMENT ON COLUMN silva.tsb_number_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.unit_bid_code definition

-- Drop table

-- DROP TABLE silva.unit_bid_code;

CREATE TABLE IF NOT EXISTS silva.unit_bid_code (
	unit_bid_code varchar(3) NOT NULL, -- A code to denote the unit basis on which work was tendered (e.g. days, hectares). Subset of UNIT OF MEASURE CODE.
	description varchar(120) NOT NULL, -- Full description of code value.
	effective_date timestamp(0) NOT NULL, -- The date the value is available for use.
	expiry_date timestamp(0) NOT NULL, -- The date the value is no longer available for use.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT unit_bid_code_pkey PRIMARY KEY (unit_bid_code)
);

-- Column comments

COMMENT ON COLUMN silva.unit_bid_code.unit_bid_code IS 'A code to denote the unit basis on which work was tendered (e.g. days, hectares). Subset of UNIT OF MEASURE CODE.';
COMMENT ON COLUMN silva.unit_bid_code.description IS 'Full description of code value.';
COMMENT ON COLUMN silva.unit_bid_code.effective_date IS 'The date the value is available for use.';
COMMENT ON COLUMN silva.unit_bid_code.expiry_date IS 'The date the value is no longer available for use.';
COMMENT ON COLUMN silva.unit_bid_code.update_timestamp IS 'The date and time the value was last modified.';
