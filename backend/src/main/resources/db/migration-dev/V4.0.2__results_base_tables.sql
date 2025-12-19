-- silva.corp_capture_method definition

-- Drop table

-- DROP TABLE silva.corp_capture_method;

CREATE TABLE IF NOT EXISTS silva.corp_capture_method (
	capture_method_code varchar(90) NOT NULL, -- The method of data capture for the given tenure application map feature image. E.g. Photogrammetric, orthoPhotography, sketchMap.
	description varchar(150) NOT NULL,
	effective_date timestamp(0) NOT NULL,
	expiry_date timestamp(0) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	CONSTRAINT corp_capture_method_pkey PRIMARY KEY (capture_method_code)
);

-- Column comments

COMMENT ON COLUMN silva.corp_capture_method.capture_method_code IS 'The method of data capture for the given tenure application map feature image. E.g. Photogrammetric, orthoPhotography, sketchMap.';

-- silva.request_item definition

-- Drop table

-- DROP TABLE silva.request_item;

CREATE TABLE IF NOT EXISTS silva.request_item (
	request_skey int8 NOT NULL, -- A surrogate key to uniquely identify each Spar Request.
	item_id varchar(1) NOT NULL, -- A unique identifier for each item within a request. It is alphabetically assigned (A, B,...Z).
	standard_workplan_skey int8 NULL, -- A surrogate key which uniquely identifies a standard workplan.
	vegetation_code varchar(8) NULL, -- A code which represents a species of tree or brush.
	sample_id varchar(5) NULL, -- The identifier of a sample if it is not a seedlot.
	family_lot_number varchar(13) NULL, -- The unique alphanumeric (key) assigned to a quantity of seed or cones of a particular species and quality from a given location collected at a given time.
	rqst_sdlngs_rsrvd numeric(6, 1) NOT NULL, -- The number, in thousands, of seedlings that were requested from an owner"s Reserve portion of a lot.
	rqst_sdlngs_srpls numeric(6, 1) NOT NULL, -- The number, in thousands, of seedlings that were requested from an owner"s Surplus portion of a lot.
	cone_ind varchar(1) NULL, -- An indicator which represents whether a sample is for cone ("Y" yes) or not ("N" no).
	no_of_containers numeric(6, 2) NULL, -- The number of containers (sacks of cones/bags of seed) that are part of the request item.
	no_of_cones int4 NULL, -- The number of cones that are part of the request item if it is a sample.
	cone_volume numeric(6, 2) NULL, -- The total volume of cones, in hectolitres, that are part of the request item.
	seed_quantity int8 NULL, -- The total volume of seed, in grams, that are part of the request item.
	sample_elevation int4 NULL, -- The representative elevation in meters where the sample originated.
	shipto_cli_number varchar(8) NULL, -- A sequentially assigned number which uniquely identifies a Ministry client.
	shipto_cli_locn_cd varchar(2) NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	required_by_date timestamp(0) NULL, -- The date (year, month and day) on which the requestor would like the services completed.
	latest_actioned_date timestamp(0) NULL, -- The latest date (year, month and day) for which any changes can be applied to the requested item before it is scheduled to be actioned.
	rcmnded_sowing_date timestamp(0) NULL, -- The system"s recommended date in the given sowing year for which sowing should take place for a Seedling Request.
	spcfied_sowing_date timestamp(0) NULL, -- The date in the given sowing year which is specified by the assigned nursery for which sowing should take place for a Seedling Request. This date will override the recommended sowing day.
	tender_number varchar(7) NULL, -- A number assigned by Silviculture Branch which indicates the tender on which the request item has been assigned.
	tender_bid_group varchar(3) NULL, -- An identifier within a Tender Number which is assigned by Silviculture Branch which indicates the part of a Tender on which the request item has been assigned.
	tender_issued_date timestamp(0) NULL, -- The date (year, month and day) the request item was assigned to the Tender.
	assigned_price numeric(6, 2) NULL, -- The assigned price assigned from the tendering process.
	nrsry_obligatn_ind varchar(1) NULL, -- An indicator which identifies whether or not a nursery assignment has been finalized and obligated.
	nrsry_cli_number varchar(8) NULL, -- A sequentially assigned number which uniquely identifies a Ministry client.
	nrsry_cli_locn_cd varchar(2) NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	facility_code varchar(2) NULL, -- A code which represents the type of facility that the seedlings will be grown in.
	extrct_cli_number varchar(8) NULL, -- A sequentially assigned number which uniquely identifies a Ministry client.
	extrct_cli_locn_cd varchar(2) NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	authorization_code varchar(3) NULL, -- a code which represents the form by which authority for the request was received.
	srv_blng_create_ts timestamp(0) NULL, -- The time and date when service billing rows were created for the Request Item.
	sample_loc_desc varchar(30) NULL, -- A free format text description of the location where the sample originated.
	interim_storge_loc varchar(50) NULL, -- The location where the Request Item will be stored before it reaches the location where the services are to be performed.
	authorized_by_prsn varchar(30) NULL, -- The name of the person who authorized the request.
	test_approved_userid varchar(30) NULL, -- The date that the Request Test Services were approved for processing by the Tree Seed Centre.
	test_approved_date timestamp(0) NULL, -- The userid of the user who approved the Request Test Services for processing at the Tree Seed Centre.
	test_update_userid varchar(30) NULL, -- The date that the Request Test Services were approved for processing by the Tree Seed Centre.
	test_update_timestamp timestamp(0) NULL, -- The date and time that Request Test Services for the Request Item were last updated.
	test_comment varchar(2000) NULL, -- A general comment about the Request Test Services for the Request Item.
	seedlot_qa_test_status_code varchar(3) NULL,
	CONSTRAINT request_item_pkey PRIMARY KEY (request_skey, item_id)
);
CREATE UNIQUE INDEX "i1$_request_item" ON silva.request_item USING btree (nrsry_cli_number, nrsry_cli_locn_cd, request_skey, item_id);
CREATE UNIQUE INDEX "i2$_request_item" ON silva.request_item USING btree (tender_number, tender_bid_group, request_skey, item_id);
CREATE INDEX "i3$_request_item" ON silva.request_item USING btree (request_skey);
CREATE INDEX "i4$_request_item" ON silva.request_item USING btree (standard_workplan_skey);
CREATE INDEX "i5$_request_item" ON silva.request_item USING btree (family_lot_number);
CREATE INDEX spr_ri_sqatscd_fk_i ON silva.request_item USING btree (seedlot_qa_test_status_code);
COMMENT ON TABLE silva.request_item IS 'A particular item within a request for which services are being requested.';

-- Column comments

COMMENT ON COLUMN silva.request_item.request_skey IS 'A surrogate key to uniquely identify each Spar Request.';
COMMENT ON COLUMN silva.request_item.item_id IS 'A unique identifier for each item within a request. It is alphabetically assigned (A, B,...Z).';
COMMENT ON COLUMN silva.request_item.standard_workplan_skey IS 'A surrogate key which uniquely identifies a standard workplan.';
COMMENT ON COLUMN silva.request_item.vegetation_code IS 'A code which represents a species of tree or brush.';
COMMENT ON COLUMN silva.request_item.sample_id IS 'The identifier of a sample if it is not a seedlot.';
COMMENT ON COLUMN silva.request_item.family_lot_number IS 'The unique alphanumeric (key) assigned to a quantity of seed or cones of a particular species and quality from a given location collected at a given time.';
COMMENT ON COLUMN silva.request_item.rqst_sdlngs_rsrvd IS 'The number, in thousands, of seedlings that were requested from an owner"s Reserve portion of a lot.';
COMMENT ON COLUMN silva.request_item.rqst_sdlngs_srpls IS 'The number, in thousands, of seedlings that were requested from an owner"s Surplus portion of a lot.';
COMMENT ON COLUMN silva.request_item.cone_ind IS 'An indicator which represents whether a sample is for cone ("Y" yes) or not ("N" no).';
COMMENT ON COLUMN silva.request_item.no_of_containers IS 'The number of containers (sacks of cones/bags of seed) that are part of the request item.';
COMMENT ON COLUMN silva.request_item.no_of_cones IS 'The number of cones that are part of the request item if it is a sample.';
COMMENT ON COLUMN silva.request_item.cone_volume IS 'The total volume of cones, in hectolitres, that are part of the request item.';
COMMENT ON COLUMN silva.request_item.seed_quantity IS 'The total volume of seed, in grams, that are part of the request item.';
COMMENT ON COLUMN silva.request_item.sample_elevation IS 'The representative elevation in meters where the sample originated.';
COMMENT ON COLUMN silva.request_item.shipto_cli_number IS 'A sequentially assigned number which uniquely identifies a Ministry client.';
COMMENT ON COLUMN silva.request_item.shipto_cli_locn_cd IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.request_item.required_by_date IS 'The date (year, month and day) on which the requestor would like the services completed.';
COMMENT ON COLUMN silva.request_item.latest_actioned_date IS 'The latest date (year, month and day) for which any changes can be applied to the requested item before it is scheduled to be actioned.';
COMMENT ON COLUMN silva.request_item.rcmnded_sowing_date IS 'The system"s recommended date in the given sowing year for which sowing should take place for a Seedling Request.';
COMMENT ON COLUMN silva.request_item.spcfied_sowing_date IS 'The date in the given sowing year which is specified by the assigned nursery for which sowing should take place for a Seedling Request. This date will override the recommended sowing day.';
COMMENT ON COLUMN silva.request_item.tender_number IS 'A number assigned by Silviculture Branch which indicates the tender on which the request item has been assigned.';
COMMENT ON COLUMN silva.request_item.tender_bid_group IS 'An identifier within a Tender Number which is assigned by Silviculture Branch which indicates the part of a Tender on which the request item has been assigned.';
COMMENT ON COLUMN silva.request_item.tender_issued_date IS 'The date (year, month and day) the request item was assigned to the Tender.';
COMMENT ON COLUMN silva.request_item.assigned_price IS 'The assigned price assigned from the tendering process.';
COMMENT ON COLUMN silva.request_item.nrsry_obligatn_ind IS 'An indicator which identifies whether or not a nursery assignment has been finalized and obligated.';
COMMENT ON COLUMN silva.request_item.nrsry_cli_number IS 'A sequentially assigned number which uniquely identifies a Ministry client.';
COMMENT ON COLUMN silva.request_item.nrsry_cli_locn_cd IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.request_item.facility_code IS 'A code which represents the type of facility that the seedlings will be grown in.';
COMMENT ON COLUMN silva.request_item.extrct_cli_number IS 'A sequentially assigned number which uniquely identifies a Ministry client.';
COMMENT ON COLUMN silva.request_item.extrct_cli_locn_cd IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.request_item.authorization_code IS 'a code which represents the form by which authority for the request was received.';
COMMENT ON COLUMN silva.request_item.srv_blng_create_ts IS 'The time and date when service billing rows were created for the Request Item.';
COMMENT ON COLUMN silva.request_item.sample_loc_desc IS 'A free format text description of the location where the sample originated.';
COMMENT ON COLUMN silva.request_item.interim_storge_loc IS 'The location where the Request Item will be stored before it reaches the location where the services are to be performed.';
COMMENT ON COLUMN silva.request_item.authorized_by_prsn IS 'The name of the person who authorized the request.';
COMMENT ON COLUMN silva.request_item.test_approved_userid IS 'The date that the Request Test Services were approved for processing by the Tree Seed Centre.';
COMMENT ON COLUMN silva.request_item.test_approved_date IS 'The userid of the user who approved the Request Test Services for processing at the Tree Seed Centre.';
COMMENT ON COLUMN silva.request_item.test_update_userid IS 'The date that the Request Test Services were approved for processing by the Tree Seed Centre.';
COMMENT ON COLUMN silva.request_item.test_update_timestamp IS 'The date and time that Request Test Services for the Request Item were last updated.';
COMMENT ON COLUMN silva.request_item.test_comment IS 'A general comment about the Request Test Services for the Request Item.';


-- silva.stocking_standard_unit_amd definition

-- Drop table

-- DROP TABLE silva.stocking_standard_unit_amd;

CREATE TABLE IF NOT EXISTS silva.stocking_standard_unit_amd (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	standards_unit_id varchar(4) NOT NULL, -- A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.
	standards_regime_id int8 NULL, -- Unique identifier for the STANDARD REGIME entry.
	net_area numeric(7, 1) NOT NULL, -- A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).
	max_allow_soil_disturbance_pct numeric(3, 1) NULL, -- This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.
	variance_ind varchar(1) NULL, -- A yes/no indicator indicating if this stocking standard is for a variance or not.
	regen_delay_offset_yrs int2 NULL, -- The number of years allowed for the regeneration of tree growth.
	regen_obligation_ind varchar(1) NOT NULL, -- A yes/no indicator indicating if this stocking standard is for a regeneration obligation.
	no_regen_early_offset_yrs int2 NULL, -- The minimum or early regen number of years.
	no_regen_late_offset_yrs int2 NULL, -- The maximum or late years of regen growth.
	free_growing_early_offset_yrs int2 NULL, -- The minimum or early growth number of years.
	free_growing_late_offset_yrs int2 NULL, -- The maximum or late years of tree growth.
	amendment_rationale_comment varchar(2000) NULL, -- A comment describing the rationale for an Amendment to a Stocking Standard.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT avcon_1080773726_regen_000 CHECK (((regen_obligation_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT stocking_standard_unit_amd_pkey PRIMARY KEY (stocking_standard_unit_id)
);
COMMENT ON TABLE silva.stocking_standard_unit_amd IS 'Contains Stocking Standard information for an Opening in Amended Status.';

-- Column comments

COMMENT ON COLUMN silva.stocking_standard_unit_amd.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.standards_unit_id IS 'A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.net_area IS 'A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.max_allow_soil_disturbance_pct IS 'This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.variance_ind IS 'A yes/no indicator indicating if this stocking standard is for a variance or not.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.regen_delay_offset_yrs IS 'The number of years allowed for the regeneration of tree growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.regen_obligation_ind IS 'A yes/no indicator indicating if this stocking standard is for a regeneration obligation.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.no_regen_early_offset_yrs IS 'The minimum or early regen number of years.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.no_regen_late_offset_yrs IS 'The maximum or late years of regen growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.free_growing_early_offset_yrs IS 'The minimum or early growth number of years.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.free_growing_late_offset_yrs IS 'The maximum or late years of tree growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.amendment_rationale_comment IS 'A comment describing the rationale for an Amendment to a Stocking Standard.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_standard_unit_amd.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.storage_files definition

-- Drop table

-- DROP TABLE silva.storage_files;

CREATE TABLE IF NOT EXISTS silva.storage_files (
	group_name_cd varchar(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	local_directory varchar(255) NOT NULL,
	tile_size varchar(11) NOT NULL,
	data_maturity_cd bpchar(1) NOT NULL,
	steward_org_unit_cd varchar(6) NULL,
	update_responsibility varchar(255) NULL,
	entry_timestamp timestamp(0) NOT NULL,
	entry_userid varchar(32) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	update_userid varchar(32) NOT NULL,
	description varchar(2000) NOT NULL,
	special_concerns varchar(2000) NULL,
	data_availability varchar(2000) NULL,
	CONSTRAINT storage_files_pkey PRIMARY KEY (group_name_cd)
);


-- silva.feature_classes definition

-- Drop table

-- DROP TABLE silva.feature_classes;

CREATE TABLE IF NOT EXISTS silva.feature_classes (
	feature_class_skey int8 NOT NULL, -- The unique key assigned to a Feature Class by the Ministry of Forests.
	group_name_cd varchar(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	feature_type_cd varchar(10) NOT NULL,
	data_maturity_cd bpchar(1) NOT NULL,
	overlapping_feature_ind bpchar(1) NOT NULL,
	span_tile_ind bpchar(1) NOT NULL,
	derived_data_ind bpchar(1) NOT NULL,
	mandatory_metadata_ind bpchar(1) NOT NULL,
	custodian_org_unit_cd varchar(6) NULL,
	data_standards_manager varchar(60) NULL,
	steward_org_unit_cd varchar(6) NULL,
	extended_org_unit_cd varchar(6) NULL,
	extended_data_contact varchar(60) NULL,
	discipline_standard_cd varchar(4) NULL,
	collection_process varchar(255) NULL,
	entry_timestamp timestamp(0) NOT NULL,
	entry_userid varchar(32) NOT NULL,
	update_timestamp timestamp(0) NOT NULL,
	update_userid varchar(32) NOT NULL,
	description varchar(2000) NOT NULL,
	"comments" varchar(2000) NULL,
	CONSTRAINT feature_classes_pkey PRIMARY KEY (feature_class_skey),
	CONSTRAINT featur_cls_file_fk FOREIGN KEY (group_name_cd) REFERENCES silva.storage_files(group_name_cd)
);
CREATE UNIQUE INDEX "i2$_featur_cls" ON silva.feature_classes USING btree (name);
CREATE INDEX "i3$_featur_cls" ON silva.feature_classes USING btree (group_name_cd);

-- Column comments

COMMENT ON COLUMN silva.feature_classes.feature_class_skey IS 'The unique key assigned to a Feature Class by the Ministry of Forests.';


-- silva.silv_admin_zone definition

-- Drop table

-- DROP TABLE silva.silv_admin_zone;

CREATE TABLE silva.silv_admin_zone (
	dist_admin_zone varchar(2) NOT NULL, -- A zonal code, district specific in meaning.
	org_unit_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	admin_zone_desc varchar(50) NOT NULL, -- A zonal description, district specific in meaning.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT silv_admin_zone_pkey PRIMARY KEY (org_unit_no, dist_admin_zone)
);
COMMENT ON TABLE silva.silv_admin_zone IS 'An entity identifying the valid administrative zones for organization units (i.e., district).';

-- Column comments

COMMENT ON COLUMN silva.silv_admin_zone.dist_admin_zone IS 'A zonal code, district specific in meaning.';
COMMENT ON COLUMN silva.silv_admin_zone.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.silv_admin_zone.admin_zone_desc IS 'A zonal description, district specific in meaning.';
COMMENT ON COLUMN silva.silv_admin_zone.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.silv_admin_zone foreign keys


-- silva.results_electronic_submission definition

-- Drop table

-- DROP TABLE silva.results_electronic_submission;

CREATE TABLE silva.results_electronic_submission (
	results_submission_id int8 NOT NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	submission_timestamp timestamp(0) NOT NULL, -- Date and time the data submission was read.
	submitted_by varchar(30) NOT NULL, -- The USERID of the person logged on when the submission was made.
	client_number varchar(8) NULL, -- Sequentially assigned number to identify a ministry client.
	client_locn_code varchar(2) NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	org_unit_no int8 NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	user_filename varchar(50) NULL, -- The file name identified by the licensee/Ministry when submitting the data through the Electronic Submission Framework.
	user_reference varchar(240) NULL, -- A reference that the user can optionally supply. This field can be used by the user to search on for outstanding submissions.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT results_electronic_submission_pkey PRIMARY KEY (results_submission_id)
);
COMMENT ON TABLE silva.results_electronic_submission IS 'General information about a data submission file which has been imported through the Electronic Submission Framework.';

-- Column comments

COMMENT ON COLUMN silva.results_electronic_submission.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.results_electronic_submission.submission_timestamp IS 'Date and time the data submission was read.';
COMMENT ON COLUMN silva.results_electronic_submission.submitted_by IS 'The USERID of the person logged on when the submission was made.';
COMMENT ON COLUMN silva.results_electronic_submission.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.results_electronic_submission.client_locn_code IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.results_electronic_submission.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.results_electronic_submission.user_filename IS 'The file name identified by the licensee/Ministry when submitting the data through the Electronic Submission Framework.';
COMMENT ON COLUMN silva.results_electronic_submission.user_reference IS 'A reference that the user can optionally supply. This field can be used by the user to search on for outstanding submissions.';
COMMENT ON COLUMN silva.results_electronic_submission.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.results_electronic_submission.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.results_electronic_submission.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.results_electronic_submission.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.results_electronic_submission.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';

-- silva.activity_treatment_unit definition

-- Drop table

-- DROP TABLE silva.activity_treatment_unit;

CREATE TABLE IF NOT EXISTS silva.activity_treatment_unit (
	activity_treatment_unit_id int8 NOT NULL, -- System generated value uniquely identifying the Activity Treatment Unit.
	activity_tu_seq_no int4 NOT NULL, -- Internal processing variable used to maintain order of the activity treatment units for a specific opening.
	org_unit_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	silviculture_project_id int8 NULL, -- A system generated key which uniquely identifies an occurrence of a Silviculture Project.
	project_unit_id int4 NULL, -- A system generated sequence to uniquely identify each of the units within the project. First activity assigned to the project is unit 1; second is unit 2; and, so on.
	silv_base_code varchar(2) NOT NULL, -- Identifies a primary category of Silviculture Activity.
	silv_technique_code varchar(2) NULL, -- Describes the broad category of technique, used for the base activity (eg.BU = BURN).
	silv_method_code varchar(5) NULL, -- Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.
	silv_objective_code_1 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	silv_objective_code_2 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	silv_objective_code_3 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	silv_fund_srce_code varchar(3) NULL, -- A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.
	silv_activity_measurement_code varchar(3) NOT NULL, -- A code indicating the measurement for the Activity
	activity_licensee_id varchar(30) NULL, -- A unique identifier provided by the Licensee to identify the Activity Number.
	treatment_amount numeric(11, 1) NULL, -- The amount (unit of measure) of work actually completed for the ATU.
	actual_treatment_cost int4 NULL, -- The actual cost incurred to complete the work.
	act_planted_no int8 NULL, -- The actual number seedlings planted for each activity treatment unit. If a planned activity exists, the planned number is defaulted as the actual planted number.
	plan_silv_technique_code varchar(2) NULL, -- Describes the broad category of technique, used for the base activity (eg.BU = BURN).
	plan_silv_method_code varchar(5) NULL, -- Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.
	plan_silv_fund_srce_code varchar(3) NULL, -- A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.
	planned_date timestamp(0) NULL, -- The century, year and month in which work on the ATU is planned to commence. The Day component of the date will be set to "01".
	planned_treatment_amount numeric(11, 1) NULL, -- The planned amount (unit of measure) of work for the ATU.
	planned_treatment_cost int4 NULL, -- The total cost anticipated to complete the work.
	plan_silv_objective_code_1 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	plan_silv_objective_code_2 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	plan_silv_objective_code_3 varchar(3) NULL, -- A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.
	target_prepared_spots int2 NULL,
	pruning_min_crown_pct int2 NULL,
	prune_height numeric(3, 1) NULL,
	stems_to_prune int2 NULL,
	min_acceptable_density int2 NULL, -- The contracted minimum allowable stand density of total countable conifers.
	total_stems_per_ha int2 NULL,
	inter_tree_target_distance numeric(3, 1) NULL,
	inter_tree_variation numeric(3, 1) NULL,
	inter_tree_min_distance numeric(3, 1) NULL,
	max_trees_per_plot int2 NULL,
	max_trees_per_ha int2 NULL,
	survey_planned_num_plots int2 NULL,
	survey_actual_num_plots int2 NULL,
	survey_min_plots_per_stratum int8 NULL,
	silv_tree_species_code varchar(8) NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	atu_start_date timestamp(0) NULL, -- The actual start date of the activity. Currently only pertains to Disturbance activities (SILV BASE CODE = "DN")
	atu_completion_date timestamp(0) NULL, -- The actual date when the activity is completed.
	results_ind varchar(1) NULL, -- A system controlled Yes/No flag indicating if activity results have been recorded for the ATU.
	unit_bid_price numeric(9, 2) NULL, -- Contract bid price per unit of work.
	fia_project_id varchar(10) NULL, -- The Project Id identified for the Forest Investment Account.
	results_submission_id int8 NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	disturbance_code varchar(3) NULL, -- A code indicating the reason for the disturbance within the opening.
	silv_system_code varchar(5) NULL, -- The code signifying the silviculture system that the post logging treatment code is to be applied to. (Ex. "B" for Basic silviculture).
	silv_system_variant_code varchar(3) NULL, -- The code for the silvicultural system variant, which further describes the functional attributes of the particular silvicultural system
	silv_cut_phase_code varchar(5) NULL, -- The code for the actual silvicultural system cut phase. The cut phase of a silvicultural system variant describes the function of a harvest to extract merchantable timber and achieve a silviculture treatment.
	cut_block_open_admin_id int8 NULL, -- System generated value uniquely identifying the Cut Block/Opening.
	disturbance_completed_ind varchar(1) NULL,
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT activity_treatment_unit_pkey PRIMARY KEY (activity_treatment_unit_id)
);
CREATE INDEX atu_atu_i ON silva.activity_treatment_unit USING btree (opening_id, silv_base_code, silv_method_code, silv_technique_code, treatment_amount);
CREATE INDEX atu_base_i ON silva.activity_treatment_unit USING btree (silv_base_code, results_ind);
CREATE INDEX atu_cboa_fk_i ON silva.activity_treatment_unit USING btree (cut_block_open_admin_id);
CREATE INDEX atu_licensee_i ON silva.activity_treatment_unit USING btree (activity_licensee_id);
CREATE INDEX atu_o_fk_i ON silva.activity_treatment_unit USING btree (opening_id);
CREATE INDEX atu_proj_fk_i ON silva.activity_treatment_unit USING btree (silviculture_project_id);
CREATE INDEX atu_project_i ON silva.activity_treatment_unit USING btree (fia_project_id, results_ind);
COMMENT ON TABLE silva.activity_treatment_unit IS 'An Activity Treatment Unit is a Silvicultural treatment that is performed on a mappable area which deliniates a specific area of land. ATUs may not span opening boundaries but an opening may have more than one ATU associated with it.';

-- Column comments

COMMENT ON COLUMN silva.activity_treatment_unit.activity_treatment_unit_id IS 'System generated value uniquely identifying the Activity Treatment Unit.';
COMMENT ON COLUMN silva.activity_treatment_unit.activity_tu_seq_no IS 'Internal processing variable used to maintain order of the activity treatment units for a specific opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.activity_treatment_unit.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.silviculture_project_id IS 'A system generated key which uniquely identifies an occurrence of a Silviculture Project.';
COMMENT ON COLUMN silva.activity_treatment_unit.project_unit_id IS 'A system generated sequence to uniquely identify each of the units within the project. First activity assigned to the project is unit 1; second is unit 2; and, so on.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_base_code IS 'Identifies a primary category of Silviculture Activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_technique_code IS 'Describes the broad category of technique, used for the base activity (eg.BU = BURN).';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_method_code IS 'Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_objective_code_1 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_objective_code_2 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_objective_code_3 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_fund_srce_code IS 'A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_activity_measurement_code IS 'A code indicating the measurement for the Activity';
COMMENT ON COLUMN silva.activity_treatment_unit.activity_licensee_id IS 'A unique identifier provided by the Licensee to identify the Activity Number.';
COMMENT ON COLUMN silva.activity_treatment_unit.treatment_amount IS 'The amount (unit of measure) of work actually completed for the ATU.';
COMMENT ON COLUMN silva.activity_treatment_unit.actual_treatment_cost IS 'The actual cost incurred to complete the work.';
COMMENT ON COLUMN silva.activity_treatment_unit.act_planted_no IS 'The actual number seedlings planted for each activity treatment unit. If a planned activity exists, the planned number is defaulted as the actual planted number.';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_technique_code IS 'Describes the broad category of technique, used for the base activity (eg.BU = BURN).';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_method_code IS 'Describes the specific machinery or method used for the base activity (eg. SHEAR = hand shears). A code for the method category of silvicultural activity planned.';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_fund_srce_code IS 'A code indicating which Funding Program will be the primary funding source for silviculture activities on the opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.planned_date IS 'The century, year and month in which work on the ATU is planned to commence. The Day component of the date will be set to "01".';
COMMENT ON COLUMN silva.activity_treatment_unit.planned_treatment_amount IS 'The planned amount (unit of measure) of work for the ATU.';
COMMENT ON COLUMN silva.activity_treatment_unit.planned_treatment_cost IS 'The total cost anticipated to complete the work.';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_objective_code_1 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_objective_code_2 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.plan_silv_objective_code_3 IS 'A code which defines the silviculture objective for performing the specific (base/technique/method) silvicultural activity.';
COMMENT ON COLUMN silva.activity_treatment_unit.min_acceptable_density IS 'The contracted minimum allowable stand density of total countable conifers.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.activity_treatment_unit.atu_start_date IS 'The actual start date of the activity. Currently only pertains to Disturbance activities (SILV BASE CODE = "DN")';
COMMENT ON COLUMN silva.activity_treatment_unit.atu_completion_date IS 'The actual date when the activity is completed.';
COMMENT ON COLUMN silva.activity_treatment_unit.results_ind IS 'A system controlled Yes/No flag indicating if activity results have been recorded for the ATU.';
COMMENT ON COLUMN silva.activity_treatment_unit.unit_bid_price IS 'Contract bid price per unit of work.';
COMMENT ON COLUMN silva.activity_treatment_unit.fia_project_id IS 'The Project Id identified for the Forest Investment Account.';
COMMENT ON COLUMN silva.activity_treatment_unit.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.activity_treatment_unit.disturbance_code IS 'A code indicating the reason for the disturbance within the opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_system_code IS 'The code signifying the silviculture system that the post logging treatment code is to be applied to. (Ex. "B" for Basic silviculture).';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_system_variant_code IS 'The code for the silvicultural system variant, which further describes the functional attributes of the particular silvicultural system';
COMMENT ON COLUMN silva.activity_treatment_unit.silv_cut_phase_code IS 'The code for the actual silvicultural system cut phase. The cut phase of a silvicultural system variant describes the function of a harvest to extract merchantable timber and achieve a silviculture treatment.';
COMMENT ON COLUMN silva.activity_treatment_unit.cut_block_open_admin_id IS 'System generated value uniquely identifying the Cut Block/Opening.';
COMMENT ON COLUMN silva.activity_treatment_unit.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.activity_treatment_unit.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.activity_treatment_unit.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.activity_treatment_unit.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.activity_treatment_unit.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.activity_tu_comment_link definition

-- Drop table

-- DROP TABLE silva.activity_tu_comment_link;

CREATE TABLE IF NOT EXISTS silva.activity_tu_comment_link (
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	activity_treatment_unit_id int8 NOT NULL, -- System generated value uniquely identifying the Activity Treatment Unit.
	CONSTRAINT activity_tu_comment_link_pkey PRIMARY KEY (activity_treatment_unit_id, silviculture_comment_id)
);

-- Column comments

COMMENT ON COLUMN silva.activity_tu_comment_link.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';
COMMENT ON COLUMN silva.activity_tu_comment_link.activity_treatment_unit_id IS 'System generated value uniquely identifying the Activity Treatment Unit.';


-- silva.cut_block definition

-- Drop table

-- DROP TABLE silva.cut_block;

CREATE TABLE IF NOT EXISTS silva.cut_block (
	cb_skey int8 NOT NULL, -- The unique identifer for a cut block.
	hva_skey int8 NULL, -- The unique identifer for the harvesting authority.
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	cutting_permit_id varchar(3) NULL, -- Identifier for a cutting permit associated with a harvesting tenure.
	timber_mark varchar(10) NULL, -- Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.
	cut_block_id varchar(10) NOT NULL, -- Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).
	sp_exempt_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Only set to "Y" for "Exemption for silviculture Prescriptions" under sec. 30 of the Forest Practices Code of British Columbia Act, eg cut blocks under one hectare, or the use of the land will be incompatible with the establishment of a free growing stand.
	block_status_date timestamp(0) NULL, -- The date on which the current cut block status was set.
	cut_block_description varchar(120) NULL, -- Description of the cut block
	cut_regulation_code varchar(3) NULL, -- Identifies the type of regulation applicable to a given cut block. Valid values include "Bark Beetle Regulation", and "Small Scale Salvage".
	block_status_st varchar(3) NOT NULL, -- The current status of the operational harvesting area, eg., Pending - Planned, Harvesting - Suspended. This is a subset of Timber_status_code.
	reforest_declare_type_code varchar(3) NULL, -- Identifies the reforestation declaration type. E.g. Coniferous, Deciduos, Mix.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the person who performed the last update to the information.
	update_timestamp timestamp(0) NOT NULL, -- The timestamp of the last update to the cut block information.
	is_waste_assessment_required varchar(1) DEFAULT 'U'::character varying NOT NULL, -- A value indicating whether a harvesting Authority record requires waste assessment or not.
	cut_block_guid uuid DEFAULT uuid_generate_v4() NOT NULL, -- Global Unique Identifier. Created as part of CP / FTA 5 Changes.
	fire_harvesting_reason_code varchar(10) NULL, -- FIRE_HARVESTING_REASON_CODE is a code that identifies a reason for harvesting related to fire activities (prevention, clean up).Null is a valid value for this column. Null is not a blank space.
	is_under_partition_order varchar(1) NULL, -- IS_UNDER_PARTITION_ORDER is used as an indicator if the cut block is within a partition order area. Values are null, Y or N. Values may change from Y or N back to Null or vice versa. Null is not a blank space.
	reported_fire_date timestamp(0) NULL, -- REPORTED_FIRE_DATE a date from the provincial FIRE system indicating the date that the fire that caused the FIRE_HARVESTING_REASON_CODE was reported.
	CONSTRAINT avcon_1054232647_sp_ex_000 CHECK (((sp_exempt_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT cblk_is_waste_assess_req_ck CHECK (((is_waste_assessment_required)::text = ANY ((ARRAY['N'::character varying, 'U'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT cut_block_cut_block_guid_key UNIQUE (cut_block_guid),
	CONSTRAINT cut_block_hva_skey_timber_mark_cut_block_id_key UNIQUE (hva_skey, timber_mark, cut_block_id),
	CONSTRAINT cut_block_pkey PRIMARY KEY (cb_skey),
	CONSTRAINT cut_block_timber_mark_forest_file_id_cut_block_id_key UNIQUE (timber_mark, forest_file_id, cut_block_id)
);
CREATE INDEX cblk1_i ON silva.cut_block USING btree (forest_file_id, cutting_permit_id, cut_block_id);
CREATE INDEX cblk2_i ON silva.cut_block USING btree (timber_mark, cut_block_id);
CREATE INDEX cblk_ff_tm_cb_i ON silva.cut_block USING btree (forest_file_id, timber_mark, cut_block_id);
CREATE INDEX cblk_i ON silva.cut_block USING btree (timber_mark, forest_file_id, cutting_permit_id, cut_block_id);
COMMENT ON TABLE silva.cut_block IS 'Information about harvesting that takes place on a cut block within a cutting permit, for a file on a harvesting tenure.';

-- Column comments

COMMENT ON COLUMN silva.cut_block.cb_skey IS 'The unique identifer for a cut block.';
COMMENT ON COLUMN silva.cut_block.hva_skey IS 'The unique identifer for the harvesting authority.';
COMMENT ON COLUMN silva.cut_block.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.cut_block.cutting_permit_id IS 'Identifier for a cutting permit associated with a harvesting tenure.';
COMMENT ON COLUMN silva.cut_block.timber_mark IS 'Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.';
COMMENT ON COLUMN silva.cut_block.cut_block_id IS 'Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).';
COMMENT ON COLUMN silva.cut_block.sp_exempt_ind IS 'Only set to "Y" for "Exemption for silviculture Prescriptions" under sec. 30 of the Forest Practices Code of British Columbia Act, eg cut blocks under one hectare, or the use of the land will be incompatible with the establishment of a free growing stand.';
COMMENT ON COLUMN silva.cut_block.block_status_date IS 'The date on which the current cut block status was set.';
COMMENT ON COLUMN silva.cut_block.cut_block_description IS 'Description of the cut block';
COMMENT ON COLUMN silva.cut_block.cut_regulation_code IS 'Identifies the type of regulation applicable to a given cut block. Valid values include "Bark Beetle Regulation", and "Small Scale Salvage".';
COMMENT ON COLUMN silva.cut_block.block_status_st IS 'The current status of the operational harvesting area, eg., Pending - Planned, Harvesting - Suspended. This is a subset of Timber_status_code.';
COMMENT ON COLUMN silva.cut_block.reforest_declare_type_code IS 'Identifies the reforestation declaration type. E.g. Coniferous, Deciduos, Mix.';
COMMENT ON COLUMN silva.cut_block.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.cut_block.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.cut_block.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.cut_block.update_userid IS 'The userid of the person who performed the last update to the information.';
COMMENT ON COLUMN silva.cut_block.update_timestamp IS 'The timestamp of the last update to the cut block information.';
COMMENT ON COLUMN silva.cut_block.is_waste_assessment_required IS 'A value indicating whether a harvesting Authority record requires waste assessment or not.';
COMMENT ON COLUMN silva.cut_block.cut_block_guid IS 'Global Unique Identifier. Created as part of CP / FTA 5 Changes.';
COMMENT ON COLUMN silva.cut_block.fire_harvesting_reason_code IS 'FIRE_HARVESTING_REASON_CODE is a code that identifies a reason for harvesting related to fire activities (prevention, clean up).Null is a valid value for this column. Null is not a blank space.';
COMMENT ON COLUMN silva.cut_block.is_under_partition_order IS 'IS_UNDER_PARTITION_ORDER is used as an indicator if the cut block is within a partition order area. Values are null, Y or N. Values may change from Y or N back to Null or vice versa. Null is not a blank space.';
COMMENT ON COLUMN silva.cut_block.reported_fire_date IS 'REPORTED_FIRE_DATE a date from the provincial FIRE system indicating the date that the fire that caused the FIRE_HARVESTING_REASON_CODE was reported.';


-- silva.cut_block_client definition

-- Drop table

-- DROP TABLE silva.cut_block_client;

CREATE TABLE IF NOT EXISTS silva.cut_block_client (
	cblk_client_skey int8 NOT NULL,
	cb_skey int8 NOT NULL, -- The unique identifer for a cut block.
	cut_block_client_type_code varchar(1) NOT NULL, -- Identifes the client types codes applicable for assignment to cut blocks.
	licensee_start_date timestamp(0) NULL, -- The licensee start date for a given cut block.
	licensee_end_date timestamp(0) NULL, -- The licensee end date for a given cut block.
	client_locn_code varchar(2) NOT NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	client_number varchar(8) NOT NULL, -- Sequentially assigned number to identify a ministry client.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	CONSTRAINT cut_block_client_cb_skey_client_number_client_locn_code_cut_key UNIQUE (cb_skey, client_number, client_locn_code, cut_block_client_type_code, licensee_start_date),
	CONSTRAINT cut_block_client_client_number_client_locn_code_cb_skey_cut_key UNIQUE (client_number, client_locn_code, cb_skey, cut_block_client_type_code, licensee_start_date),
	CONSTRAINT cut_block_client_pkey PRIMARY KEY (cblk_client_skey)
);
CREATE INDEX cbc_cb_fk_i ON silva.cut_block_client USING btree (cb_skey);
CREATE INDEX cbc_cl_fk_i ON silva.cut_block_client USING btree (client_number, client_locn_code);
COMMENT ON TABLE silva.cut_block_client IS 'Identifies the licencee assigned to a given cut block for a specified duration of time.';

-- Column comments

COMMENT ON COLUMN silva.cut_block_client.cb_skey IS 'The unique identifer for a cut block.';
COMMENT ON COLUMN silva.cut_block_client.cut_block_client_type_code IS 'Identifes the client types codes applicable for assignment to cut blocks.';
COMMENT ON COLUMN silva.cut_block_client.licensee_start_date IS 'The licensee start date for a given cut block.';
COMMENT ON COLUMN silva.cut_block_client.licensee_end_date IS 'The licensee end date for a given cut block.';
COMMENT ON COLUMN silva.cut_block_client.client_locn_code IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.cut_block_client.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.cut_block_client.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.cut_block_client.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.cut_block_client.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.cut_block_client.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.cut_block_client.update_timestamp IS 'The date and time of the last update.';


-- silva.cut_block_open_admin definition

-- Drop table

-- DROP TABLE silva.cut_block_open_admin;

CREATE TABLE IF NOT EXISTS silva.cut_block_open_admin (
	cut_block_open_admin_id int8 NOT NULL, -- Primary Unique identifier for a Cut Block Open Admin.
	forest_file_id varchar(10) NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	timber_mark varchar(10) NULL, -- Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.
	cut_block_id varchar(10) NULL, -- Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).
	cutting_permit_id varchar(3) NULL, -- Identifier for a cutting permit associated with a quota type tenure.
	disturbance_gross_area numeric(11, 4) NULL, -- Current gross area of the cut block, in hectares, including all approved amendments.
	disturbance_start_date timestamp(0) NULL, -- Actual date that harvesting started on the cut block.
	disturbance_end_date timestamp(0) NULL, -- For areas being Harvested, this date is associated with the actual or estimated completion of Harvesting. This date s/b entered as part of the regular logging inspection, and updated as required. The actual date will be when the status moves to S - Silv.
	opening_id int8 NULL, -- System generated value uniquely identifying the opening.
	opening_gross_area numeric(11, 4) NULL, -- The total area that the opening encompasses (e.g., 120 Hectares).
	planned_harvest_date timestamp(0) NULL, -- The date on which the harvest was planned.
	planned_gross_block_area numeric(11, 4) NULL, -- Estimated initial gross area of the cut block, in hectares, from the PHSP.
	planned_net_block_area numeric(11, 4) NULL, -- Estimated planned gross area of the cut block, in hectares, from the PHSP.
	opening_prime_licence_ind varchar(1) NULL, -- A (Y/N) indicator used to show whether or not this is the primary licence holder for the Opening.
	cb_skey int8 NULL, -- The unique identifer for a cut block.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The unique id of the user who last modified this entry.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the entry was last modified.
	CONSTRAINT avcon_1054232647_openi_000 CHECK (((opening_prime_licence_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT cut_block_open_admin_pkey PRIMARY KEY (cut_block_open_admin_id)
);
CREATE INDEX cboa_cblk_fk_i ON silva.cut_block_open_admin USING btree (cut_block_id, timber_mark, forest_file_id);
CREATE INDEX cboa_cbskey_i ON silva.cut_block_open_admin USING btree (cb_skey);
CREATE INDEX cboa_ffid_mark_blk_i ON silva.cut_block_open_admin USING btree (forest_file_id, timber_mark, cut_block_id);
CREATE INDEX cboa_mark_blk_i ON silva.cut_block_open_admin USING btree (timber_mark, cut_block_id);
CREATE INDEX cboa_o_fk_i ON silva.cut_block_open_admin USING btree (opening_id);
COMMENT ON TABLE silva.cut_block_open_admin IS 'Used by FTA and RESULTS to track area and date information associated with a cut block and/or opening.';

-- Column comments

COMMENT ON COLUMN silva.cut_block_open_admin.cut_block_open_admin_id IS 'Primary Unique identifier for a Cut Block Open Admin.';
COMMENT ON COLUMN silva.cut_block_open_admin.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.cut_block_open_admin.timber_mark IS 'Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.';
COMMENT ON COLUMN silva.cut_block_open_admin.cut_block_id IS 'Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).';
COMMENT ON COLUMN silva.cut_block_open_admin.cutting_permit_id IS 'Identifier for a cutting permit associated with a quota type tenure.';
COMMENT ON COLUMN silva.cut_block_open_admin.disturbance_gross_area IS 'Current gross area of the cut block, in hectares, including all approved amendments.';
COMMENT ON COLUMN silva.cut_block_open_admin.disturbance_start_date IS 'Actual date that harvesting started on the cut block.';
COMMENT ON COLUMN silva.cut_block_open_admin.disturbance_end_date IS 'For areas being Harvested, this date is associated with the actual or estimated completion of Harvesting. This date s/b entered as part of the regular logging inspection, and updated as required. The actual date will be when the status moves to S - Silv.';
COMMENT ON COLUMN silva.cut_block_open_admin.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.cut_block_open_admin.opening_gross_area IS 'The total area that the opening encompasses (e.g., 120 Hectares).';
COMMENT ON COLUMN silva.cut_block_open_admin.planned_harvest_date IS 'The date on which the harvest was planned.';
COMMENT ON COLUMN silva.cut_block_open_admin.planned_gross_block_area IS 'Estimated initial gross area of the cut block, in hectares, from the PHSP.';
COMMENT ON COLUMN silva.cut_block_open_admin.planned_net_block_area IS 'Estimated planned gross area of the cut block, in hectares, from the PHSP.';
COMMENT ON COLUMN silva.cut_block_open_admin.opening_prime_licence_ind IS 'A (Y/N) indicator used to show whether or not this is the primary licence holder for the Opening.';
COMMENT ON COLUMN silva.cut_block_open_admin.cb_skey IS 'The unique identifer for a cut block.';
COMMENT ON COLUMN silva.cut_block_open_admin.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.cut_block_open_admin.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.cut_block_open_admin.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.cut_block_open_admin.update_userid IS 'The unique id of the user who last modified this entry.';
COMMENT ON COLUMN silva.cut_block_open_admin.update_timestamp IS 'The date and time the entry was last modified.';


-- silva.for_client_link definition

-- Drop table

-- DROP TABLE silva.for_client_link;

CREATE TABLE IF NOT EXISTS silva.for_client_link (
	for_client_link_skey int8 NOT NULL, -- Sequentially generated primary key
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	cutting_permit_id varchar(3) NULL, -- Identifier for a cutting permit associated with a quota type tenure.
	cut_block_id varchar(10) NULL, -- Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).
	file_client_type varchar(1) NOT NULL, -- Indicates the type of relationship the client has to the file.
	client_number varchar(8) NOT NULL, -- Any company or individual who is dealing, has dealt or plans to deal with the Ministry. This is effectively the name and address list for the Ministry.
	client_locn_code varchar(2) NOT NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	licensee_start_dt timestamp(0) NULL, -- The date the licensee started being a client.
	licensee_end_date timestamp(0) NULL, -- The date the licensee stopped being a client
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The unique id of the user who last modified this entry.
	update_timestamp timestamp(0) NOT NULL, -- The date and time the entry was last modified.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	CONSTRAINT for_client_link_forest_file_id_cutting_permit_id_cut_block__key UNIQUE (forest_file_id, cutting_permit_id, cut_block_id, file_client_type, client_number, client_locn_code, licensee_start_dt),
	CONSTRAINT for_client_link_pkey PRIMARY KEY (for_client_link_skey)
);
CREATE INDEX fcl_i1 ON silva.for_client_link USING btree (forest_file_id);
CREATE UNIQUE INDEX "i1$_for_client_link" ON silva.for_client_link USING btree (client_number, client_locn_code, file_client_type, forest_file_id, cutting_permit_id, cut_block_id, licensee_start_dt);
COMMENT ON TABLE silva.for_client_link IS 'Retrofitted from table FOR_CLIENT_LINK';

-- Column comments

COMMENT ON COLUMN silva.for_client_link.for_client_link_skey IS 'Sequentially generated primary key';
COMMENT ON COLUMN silva.for_client_link.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.for_client_link.cutting_permit_id IS 'Identifier for a cutting permit associated with a quota type tenure.';
COMMENT ON COLUMN silva.for_client_link.cut_block_id IS 'Identifier for a cut block of a harvesting tenure (within a cutting permit for tenures with cp"s).';
COMMENT ON COLUMN silva.for_client_link.file_client_type IS 'Indicates the type of relationship the client has to the file.';
COMMENT ON COLUMN silva.for_client_link.client_number IS 'Any company or individual who is dealing, has dealt or plans to deal with the Ministry. This is effectively the name and address list for the Ministry.';
COMMENT ON COLUMN silva.for_client_link.client_locn_code IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.for_client_link.licensee_start_dt IS 'The date the licensee started being a client.';
COMMENT ON COLUMN silva.for_client_link.licensee_end_date IS 'The date the licensee stopped being a client';
COMMENT ON COLUMN silva.for_client_link.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.for_client_link.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.for_client_link.update_userid IS 'The unique id of the user who last modified this entry.';
COMMENT ON COLUMN silva.for_client_link.update_timestamp IS 'The date and time the entry was last modified.';
COMMENT ON COLUMN silva.for_client_link.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';


-- silva.forest_cover definition

-- Drop table

-- DROP TABLE silva.forest_cover;

CREATE TABLE IF NOT EXISTS silva.forest_cover (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	stocking_standard_unit_id int8 NULL, -- System generated value uniquely identifing the stocking standards.
	silv_polygon_no varchar(30) NOT NULL, -- An unique number identification derived from the IGDS which is assigned to each forest type island, standards unit, eco-strata or survey strata within the opening.
	silv_polygon_area numeric(7, 1) NOT NULL, -- The total area occupied by each polygon number or stratified land type. The sum of all areas for a polygon(s) should not be greater than the area of the SU.
	silv_polygon_net_area numeric(7, 1) NOT NULL, -- Net Area of the Polygon (i.e. SILV POLYGON AREA less non-mapped area (FOREST COVER NON MAPPED AREA))
	stocking_class_code varchar(1) NULL, -- The stocking class code for the primary tree species in the previous stand (for the area under the prescription).
	stocking_status_code varchar(3) NULL, -- A code to describe the previous stand type for the area under the prescription.
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	reference_year int2 NOT NULL, -- The year age and height measurements were recorded.
	reentry_year int2 NULL, -- The time to the next harvest (i.e. the next harvest year).
	site_class_code varchar(1) NULL, -- This code indicates the quality of the land. Note: This column is not maintained by the system.
	site_index int4 NULL, -- The site index for the primary tree species in the previous stand (for the area under the prescription).
	site_index_source_code varchar(1) NULL, -- A code describing the source, or origin, of the estimated site index.
	silv_reserve_code varchar(1) NULL, -- A code for the type of reserve. Values are group (b), uniform (u) or variable (v).
	silv_reserve_objective_code varchar(3) NULL, -- A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .
	tree_species_code varchar(8) NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	tree_cover_pattern_code varchar(1) NULL, -- The dispursion of trees over the area.
	results_submission_id int8 NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_pkey PRIMARY KEY (forest_cover_id)
);
CREATE INDEX fc_o_fk_i ON silva.forest_cover USING btree (opening_id);
COMMENT ON TABLE silva.forest_cover IS 'Describes the opening"s forest cover attributes stratified by Forest Cover Polygon which will satisfy Inventory Program requirements.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.forest_cover.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.forest_cover.silv_polygon_no IS 'An unique number identification derived from the IGDS which is assigned to each forest type island, standards unit, eco-strata or survey strata within the opening.';
COMMENT ON COLUMN silva.forest_cover.silv_polygon_area IS 'The total area occupied by each polygon number or stratified land type. The sum of all areas for a polygon(s) should not be greater than the area of the SU.';
COMMENT ON COLUMN silva.forest_cover.silv_polygon_net_area IS 'Net Area of the Polygon (i.e. SILV POLYGON AREA less non-mapped area (FOREST COVER NON MAPPED AREA))';
COMMENT ON COLUMN silva.forest_cover.stocking_class_code IS 'The stocking class code for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.forest_cover.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover.reference_year IS 'The year age and height measurements were recorded.';
COMMENT ON COLUMN silva.forest_cover.reentry_year IS 'The time to the next harvest (i.e. the next harvest year).';
COMMENT ON COLUMN silva.forest_cover.site_class_code IS 'This code indicates the quality of the land. Note: This column is not maintained by the system.';
COMMENT ON COLUMN silva.forest_cover.site_index IS 'The site index for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.forest_cover.site_index_source_code IS 'A code describing the source, or origin, of the estimated site index.';
COMMENT ON COLUMN silva.forest_cover.silv_reserve_code IS 'A code for the type of reserve. Values are group (b), uniform (u) or variable (v).';
COMMENT ON COLUMN silva.forest_cover.silv_reserve_objective_code IS 'A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .';
COMMENT ON COLUMN silva.forest_cover.tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.forest_cover.tree_cover_pattern_code IS 'The dispursion of trees over the area.';
COMMENT ON COLUMN silva.forest_cover.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.forest_cover.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_archive definition

-- Drop table

-- DROP TABLE silva.forest_cover_archive;

CREATE TABLE IF NOT EXISTS silva.forest_cover_archive (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	stocking_standard_unit_id int8 NULL, -- System generated value uniquely identifing the stocking standards.
	silv_polygon_no varchar(30) NULL, -- An unique number identification derived from the IGDS which is assigned to each forest type island, standards unit, eco-strata or survey strata within the opening.
	silv_polygon_area numeric(7, 1) NOT NULL, -- The total area occupied by each polygon number or stratified land type. The sum of all areas for a polygon(s) should not be greater than the area of the SU.
	silv_polygon_net_area numeric(7, 1) NOT NULL, -- Net Area of the Polygon (i.e. SILV POLYGON AREA less non-mapped area (FOREST COVER NON MAPPED AREA))
	stocking_class_code varchar(1) NULL, -- The stocking class code for the primary tree species in the previous stand (for the area under the prescription).
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	stocking_status_code varchar(3) NULL, -- A code to describe the previous stand type for the area under the prescription.
	reference_year int2 NOT NULL, -- The year age and height measurements were recorded.
	reentry_year int2 NULL, -- The time to the next harvest (i.e. the next harvest year).
	site_class_code varchar(1) NULL, -- This code indicates the quality of the land. Note: This column is not maintained by the system.
	site_index int4 NULL, -- The site index for the primary tree species in the previous stand (for the area under the prescription).
	site_index_source_code varchar(1) NULL, -- A code describing the source, or origin, of the estimated site index.
	silv_reserve_code varchar(1) NULL, -- A code for the type of reserve. Values are group (b), uniform (u) or variable (v).
	silv_reserve_objective_code varchar(3) NULL, -- A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .
	tree_species_code varchar(8) NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	tree_cover_pattern_code varchar(1) NULL, -- The dispursion of trees over the area.
	results_submission_id int8 NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_archive_pkey PRIMARY KEY (forest_cover_id, archive_date)
);
CREATE INDEX fca_o_fk_i ON silva.forest_cover_archive USING btree (opening_id);
COMMENT ON TABLE silva.forest_cover_archive IS 'Forest Cover Archive contains the exact information from the Forest Cover with each update. Existing Forest cover entries are saved down to the Forest Cover Archive instead of being updated, thus maintaining a complete history of all changes.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_archive.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_archive.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forest_cover_archive.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.forest_cover_archive.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.forest_cover_archive.silv_polygon_no IS 'An unique number identification derived from the IGDS which is assigned to each forest type island, standards unit, eco-strata or survey strata within the opening.';
COMMENT ON COLUMN silva.forest_cover_archive.silv_polygon_area IS 'The total area occupied by each polygon number or stratified land type. The sum of all areas for a polygon(s) should not be greater than the area of the SU.';
COMMENT ON COLUMN silva.forest_cover_archive.silv_polygon_net_area IS 'Net Area of the Polygon (i.e. SILV POLYGON AREA less non-mapped area (FOREST COVER NON MAPPED AREA))';
COMMENT ON COLUMN silva.forest_cover_archive.stocking_class_code IS 'The stocking class code for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.forest_cover_archive.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover_archive.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover_archive.reference_year IS 'The year age and height measurements were recorded.';
COMMENT ON COLUMN silva.forest_cover_archive.reentry_year IS 'The time to the next harvest (i.e. the next harvest year).';
COMMENT ON COLUMN silva.forest_cover_archive.site_class_code IS 'This code indicates the quality of the land. Note: This column is not maintained by the system.';
COMMENT ON COLUMN silva.forest_cover_archive.site_index IS 'The site index for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.forest_cover_archive.site_index_source_code IS 'A code describing the source, or origin, of the estimated site index.';
COMMENT ON COLUMN silva.forest_cover_archive.silv_reserve_code IS 'A code for the type of reserve. Values are group (b), uniform (u) or variable (v).';
COMMENT ON COLUMN silva.forest_cover_archive.silv_reserve_objective_code IS 'A code used to define the primary reserve objective. ( e.g. Riparian, Wildlife Habitat) .';
COMMENT ON COLUMN silva.forest_cover_archive.tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.forest_cover_archive.tree_cover_pattern_code IS 'The dispursion of trees over the area.';
COMMENT ON COLUMN silva.forest_cover_archive.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.forest_cover_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_archive.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_archive_geometry definition

-- Drop table

-- DROP TABLE silva.forest_cover_archive_geometry;

CREATE TABLE IF NOT EXISTS silva.forest_cover_archive_geometry (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	geometry geometry(geometry, 3005) NOT NULL, -- Spatial coordinate representation of the polygon related to the Forest Cover.
	feature_area numeric(11, 4) NOT NULL, -- The area of the polygon pertaining to the Forest Cover measured in hectares.
	feature_perimeter numeric(11, 4) NOT NULL, -- The perimeter of the polygon pertaining to the Forest Cover measured in metres.
	feature_class_skey int8 NOT NULL, -- Unique identifier of the feature class.
	data_source_code varchar(10) NULL,
	capture_method_code varchar(30) NULL,
	observation_date timestamp(0) NULL, -- Captures the date of observation for the given feature image.
	data_quality_comment varchar(255) NULL, -- Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.
	entry_userid varchar(30) NOT NULL, -- The userid of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_archive_geometry_pkey PRIMARY KEY (forest_cover_id, archive_date)
);
CREATE INDEX fcag_geometry_i ON silva.forest_cover_archive_geometry USING gist (geometry);
COMMENT ON TABLE silva.forest_cover_archive_geometry IS 'The spatial polygon component for Forest Covers which has been modified.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_archive_geometry.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.geometry IS 'Spatial coordinate representation of the polygon related to the Forest Cover.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.feature_area IS 'The area of the polygon pertaining to the Forest Cover measured in hectares.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.feature_perimeter IS 'The perimeter of the polygon pertaining to the Forest Cover measured in metres.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.feature_class_skey IS 'Unique identifier of the feature class.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.observation_date IS 'Captures the date of observation for the given feature image.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.data_quality_comment IS 'Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.entry_userid IS 'The userid of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.update_userid IS 'The userid of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_archive_geometry.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_geometry definition

-- Drop table

-- DROP TABLE silva.forest_cover_geometry;

CREATE TABLE IF NOT EXISTS silva.forest_cover_geometry (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	geometry geometry(geometry, 3005) NOT NULL, -- Spatial coordinate representation of the polygon related to the Forest Cover.
	feature_area numeric(11, 4) NOT NULL, -- The area of the polygon pertaining to the Forest Cover measured in square metres.
	feature_perimeter numeric(11, 4) NOT NULL, -- The perimeter of the polygon pertaining to the Forest Cover measured in metres.
	capture_method_code varchar(30) NULL,
	data_source_code varchar(10) NULL,
	feature_class_skey int8 NOT NULL, -- Unique identifier of the feature class.
	observation_date timestamp(0) NULL, -- Captures the date of observation for the given feature image.
	data_quality_comment varchar(255) NULL, -- Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.
	entry_userid varchar(30) NOT NULL, -- The userid of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_geometry_pkey PRIMARY KEY (forest_cover_id)
);
CREATE INDEX fcg_geometry_i ON silva.forest_cover_geometry USING gist (geometry);
COMMENT ON TABLE silva.forest_cover_geometry IS 'The spatial polygon component for the Forest Cover.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_geometry.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_geometry.geometry IS 'Spatial coordinate representation of the polygon related to the Forest Cover.';
COMMENT ON COLUMN silva.forest_cover_geometry.feature_area IS 'The area of the polygon pertaining to the Forest Cover measured in square metres.';
COMMENT ON COLUMN silva.forest_cover_geometry.feature_perimeter IS 'The perimeter of the polygon pertaining to the Forest Cover measured in metres.';
COMMENT ON COLUMN silva.forest_cover_geometry.feature_class_skey IS 'Unique identifier of the feature class.';
COMMENT ON COLUMN silva.forest_cover_geometry.observation_date IS 'Captures the date of observation for the given feature image.';
COMMENT ON COLUMN silva.forest_cover_geometry.data_quality_comment IS 'Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.';
COMMENT ON COLUMN silva.forest_cover_geometry.entry_userid IS 'The userid of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_geometry.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_geometry.update_userid IS 'The userid of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_geometry.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_geometry.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_layer definition

-- Drop table

-- DROP TABLE silva.forest_cover_layer;

CREATE TABLE IF NOT EXISTS silva.forest_cover_layer (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	forest_cover_layer_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover layer.
	forest_cover_layer_code varchar(2) NOT NULL, -- A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	stocking_status_code varchar(3) NULL, -- A code to describe the previous stand type for the area under the prescription.
	total_stems_per_ha int8 NULL, -- The total stems counted per hectare.
	total_well_spaced_stems_per_ha int8 NULL, -- A new business rule around maximum cap on well spaced stems.
	well_spaced_stems_per_ha int8 NULL, -- The total well spaced stems per hectare.
	free_growing_stems_per_ha int8 NULL, -- The total free growing stems per hectare.
	crown_closure_pct int2 NULL, -- The percentage of ground area covered by vertically projected tree crown areas.
	basal_area int4 NULL, -- For inventory label, this field refers to all total stem above 12.5 cm DBH. For silviculture label, this field refers to basal area for well spaced preferred and acceptable stems above 12.5 cm DBH.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_layer_pkey PRIMARY KEY (forest_cover_id, forest_cover_layer_id)
);
COMMENT ON TABLE silva.forest_cover_layer IS 'The various levels of growth within the forest cover. Typically defined in four layers.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_layer.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_layer.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forest_cover_layer.forest_cover_layer_code IS 'A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.';
COMMENT ON COLUMN silva.forest_cover_layer.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover_layer.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover_layer.total_stems_per_ha IS 'The total stems counted per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer.total_well_spaced_stems_per_ha IS 'A new business rule around maximum cap on well spaced stems.';
COMMENT ON COLUMN silva.forest_cover_layer.well_spaced_stems_per_ha IS 'The total well spaced stems per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer.free_growing_stems_per_ha IS 'The total free growing stems per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer.crown_closure_pct IS 'The percentage of ground area covered by vertically projected tree crown areas.';
COMMENT ON COLUMN silva.forest_cover_layer.basal_area IS 'For inventory label, this field refers to all total stem above 12.5 cm DBH. For silviculture label, this field refers to basal area for well spaced preferred and acceptable stems above 12.5 cm DBH.';
COMMENT ON COLUMN silva.forest_cover_layer.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_layer.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_layer.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_layer.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_layer.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_layer_archive definition

-- Drop table

-- DROP TABLE silva.forest_cover_layer_archive;

CREATE TABLE IF NOT EXISTS silva.forest_cover_layer_archive (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	forest_cover_layer_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover layer.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	forest_cover_layer_code varchar(2) NOT NULL, -- A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.
	stocking_status_code varchar(3) NULL, -- A code to describe the previous stand type for the area under the prescription.
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	total_stems_per_ha int8 NULL, -- The total stems counted per hectare.
	total_well_spaced_stems_per_ha int8 NULL, -- A new business rule around maximum cap on well spaced stems.
	well_spaced_stems_per_ha int8 NULL, -- The total well spaced stems per hectare.
	free_growing_stems_per_ha int8 NULL, -- The total free growing stems per hectare.
	crown_closure_pct int4 NULL, -- The percentage of ground area covered by vertically projected tree crown areas.
	basal_area int4 NULL, -- For inventory label, this field refers to all total stem above 12.5 cm DBH. For silviculture label, this field refers to basal area for well spaced preferred and acceptable stems above 12.5 cm DBH.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_layer_archive_pkey PRIMARY KEY (forest_cover_id, archive_date, forest_cover_layer_id)
);
COMMENT ON TABLE silva.forest_cover_layer_archive IS 'The various levels of growth within the forest cover. Typically defined in four layers.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_layer_archive.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.forest_cover_layer_code IS 'A code that uniquely identifies each layer, horizontal stratum, in a stand. Each layer is normally characterized as a distinct canopy containing a common forest cover structure with timber of similiar ages and heights.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.total_stems_per_ha IS 'The total stems counted per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.total_well_spaced_stems_per_ha IS 'A new business rule around maximum cap on well spaced stems.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.well_spaced_stems_per_ha IS 'The total well spaced stems per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.free_growing_stems_per_ha IS 'The total free growing stems per hectare.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.crown_closure_pct IS 'The percentage of ground area covered by vertically projected tree crown areas.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.basal_area IS 'For inventory label, this field refers to all total stem above 12.5 cm DBH. For silviculture label, this field refers to basal area for well spaced preferred and acceptable stems above 12.5 cm DBH.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_layer_archive.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_layer_species definition

-- Drop table

-- DROP TABLE silva.forest_cover_layer_species;

CREATE TABLE IF NOT EXISTS silva.forest_cover_layer_species (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	forest_cover_layer_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover layer.
	species_order int2 NOT NULL, -- The order in which the species will be planted within the Forest Cover Layer
	tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	tree_species_pct int2 NULL, -- The percent of the total number of trees within the layer which the species comprises.
	avg_age int4 NULL, -- The average age of the tree species expressed in years.
	avg_height numeric(3, 1) NULL, -- The average height expressed in metres.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_layer_species_pkey PRIMARY KEY (forest_cover_id, forest_cover_layer_id, species_order, tree_species_code)
);
COMMENT ON TABLE silva.forest_cover_layer_species IS 'The species of trees and vegetation contained within the FOREST COVER LAYER.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_layer_species.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_layer_species.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forest_cover_layer_species.species_order IS 'The order in which the species will be planted within the Forest Cover Layer';
COMMENT ON COLUMN silva.forest_cover_layer_species.tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.forest_cover_layer_species.tree_species_pct IS 'The percent of the total number of trees within the layer which the species comprises.';
COMMENT ON COLUMN silva.forest_cover_layer_species.avg_age IS 'The average age of the tree species expressed in years.';
COMMENT ON COLUMN silva.forest_cover_layer_species.avg_height IS 'The average height expressed in metres.';
COMMENT ON COLUMN silva.forest_cover_layer_species.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_layer_species.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_layer_species.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_layer_species.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_layer_species.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_layer_species_arc definition

-- Drop table

-- DROP TABLE silva.forest_cover_layer_species_arc;

CREATE TABLE IF NOT EXISTS silva.forest_cover_layer_species_arc (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	forest_cover_layer_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover layer.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	species_order int2 NOT NULL, -- The order in which the species will be planted within the Forest Cover Layer
	tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	tree_species_pct int2 NULL, -- The percent of the total number of trees within the layer which the species comprises.
	avg_age int4 NULL, -- The average age of the tree species expressed in years.
	avg_height numeric(3, 1) NULL, -- The average height expressed in metres.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_layer_species_arc_pkey PRIMARY KEY (forest_cover_id, archive_date, forest_cover_layer_id, species_order, tree_species_code)
);
COMMENT ON TABLE silva.forest_cover_layer_species_arc IS 'The species of trees and vegetation contained within the FOREST COVER LAYER.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_layer_species_arc.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.species_order IS 'The order in which the species will be planted within the Forest Cover Layer';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.tree_species_pct IS 'The percent of the total number of trees within the layer which the species comprises.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.avg_age IS 'The average age of the tree species expressed in years.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.avg_height IS 'The average height expressed in metres.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_cover_layer_species_arc.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_non_mapped_arc definition

-- Drop table

-- DROP TABLE silva.forest_cover_non_mapped_arc;

CREATE TABLE IF NOT EXISTS silva.forest_cover_non_mapped_arc (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	non_mapped_area_id varchar(3) NOT NULL, -- Business key used to identify small NP areas that are not mapable.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	non_mapped_area numeric(7, 1) NOT NULL, -- Small non-productive area that will be subtracted from the Polygon gross area. This area is not mapped.
	stocking_status_code varchar(3) NOT NULL, -- A code to describe the previous stand type for the area under the prescription.
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_non_mapped_arc_pkey PRIMARY KEY (forest_cover_id, non_mapped_area_id, archive_date)
);
CREATE INDEX fcnma1_fca_fk_i ON silva.forest_cover_non_mapped_arc USING btree (forest_cover_id, archive_date);
CREATE INDEX fcnma1_ssc6_fk_i ON silva.forest_cover_non_mapped_arc USING btree (stocking_status_code);
CREATE INDEX fcnma1_stc3_fk_i ON silva.forest_cover_non_mapped_arc USING btree (stocking_type_code);
COMMENT ON TABLE silva.forest_cover_non_mapped_arc IS 'Small Forest Cover non-productive areas, such as roads, which do not require spatial mapping and are not part of the Net Area to be Reforested.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.non_mapped_area_id IS 'Business key used to identify small NP areas that are not mapable.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.non_mapped_area IS 'Small non-productive area that will be subtracted from the Polygon gross area. This area is not mapped.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_arc.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_cover_non_mapped_area definition

-- Drop table

-- DROP TABLE silva.forest_cover_non_mapped_area;

CREATE TABLE IF NOT EXISTS silva.forest_cover_non_mapped_area (
	forest_cover_id int8 NOT NULL, -- System generated value uniquely identifying the forest cover.
	non_mapped_area_id varchar(3) NOT NULL, -- Business key used to identify small NP areas that are not mapable.
	non_mapped_area numeric(7, 1) NOT NULL, -- Small non-productive area that will be subtracted from the Polygon gross area. This area is not mapped.
	stocking_status_code varchar(3) NOT NULL, -- A code to describe the previous stand type for the area under the prescription.
	stocking_type_code varchar(3) NULL, -- A further classification of the stocking status for the polygon.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT forest_cover_non_mapped_area_pkey PRIMARY KEY (forest_cover_id, non_mapped_area_id)
);
CREATE INDEX fcnma_fc_fk_i ON silva.forest_cover_non_mapped_area USING btree (forest_cover_id);
CREATE INDEX fcnma_ssc6_fk_i ON silva.forest_cover_non_mapped_area USING btree (stocking_status_code);
CREATE INDEX fcnma_stc3_fk_i ON silva.forest_cover_non_mapped_area USING btree (stocking_type_code);
COMMENT ON TABLE silva.forest_cover_non_mapped_area IS 'Small Forest Cover non-productive areas, such as roads, which do not require spatial mapping and are not part of the Net Area to be Reforested.';

-- Column comments

COMMENT ON COLUMN silva.forest_cover_non_mapped_area.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_area.non_mapped_area_id IS 'Business key used to identify small NP areas that are not mapable.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_area.non_mapped_area IS 'Small non-productive area that will be subtracted from the Polygon gross area. This area is not mapped.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_area.stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_area.stocking_type_code IS 'A further classification of the stocking status for the polygon.';
COMMENT ON COLUMN silva.forest_cover_non_mapped_area.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.forest_file_client definition

-- Drop table

-- DROP TABLE silva.forest_file_client;

CREATE TABLE IF NOT EXISTS silva.forest_file_client (
	forest_file_client_skey int8 NOT NULL, -- See FTA data model for more details.
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	client_number varchar(8) NOT NULL, -- Sequentially assigned number to identify a ministry client.
	client_locn_code varchar(2) NOT NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	forest_file_client_type_code varchar(1) NOT NULL, -- Identifes the client types codes applicable for assignment to forest files.
	licensee_start_date timestamp(0) NULL, -- The licensee start date for a given file.
	licensee_end_date timestamp(0) NULL, -- The licensee end date for a given file.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	CONSTRAINT forest_file_client_client_number_client_locn_code_forest_fi_key UNIQUE (client_number, client_locn_code, forest_file_id, forest_file_client_type_code, licensee_start_date),
	CONSTRAINT forest_file_client_forest_file_id_forest_file_client_type_c_key UNIQUE (forest_file_id, forest_file_client_type_code, client_number, client_locn_code, licensee_start_date),
	CONSTRAINT forest_file_client_pkey PRIMARY KEY (forest_file_client_skey)
);
CREATE INDEX ffc_i2 ON silva.forest_file_client USING btree (forest_file_id, forest_file_client_type_code);
COMMENT ON TABLE silva.forest_file_client IS 'Identifies the licencee assigned to a given file for a specified duration of time.';

-- Column comments

COMMENT ON COLUMN silva.forest_file_client.forest_file_client_skey IS 'See FTA data model for more details.';
COMMENT ON COLUMN silva.forest_file_client.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.forest_file_client.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.forest_file_client.client_locn_code IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.forest_file_client.forest_file_client_type_code IS 'Identifes the client types codes applicable for assignment to forest files.';
COMMENT ON COLUMN silva.forest_file_client.licensee_start_date IS 'The licensee start date for a given file.';
COMMENT ON COLUMN silva.forest_file_client.licensee_end_date IS 'The licensee end date for a given file.';
COMMENT ON COLUMN silva.forest_file_client.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.forest_file_client.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.forest_file_client.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.forest_file_client.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.forest_file_client.update_timestamp IS 'The date and time of the last update.';


-- silva.forest_mgmt_unit definition

-- Drop table

-- DROP TABLE silva.forest_mgmt_unit;

CREATE TABLE IF NOT EXISTS silva.forest_mgmt_unit (
	mgmt_skey int8 NOT NULL, -- System generated unique identifier
	mgmt_unit_type_code varchar(1) NOT NULL, -- The unique identifier for the management unit type.
	mgmt_unit_id varchar(4) NULL, -- The administrative management unit identifier defined by the ministry for managing the forest land, eg. 41C.
	description varchar(100) NULL, -- Description of the Managment Unit Identifer. E.g. Dawson Creek TSA Block C.
	entry_userid varchar(30) NOT NULL, -- The user that created the record.
	entry_timestamp timestamp(0) NOT NULL, -- The date the record was created.
	update_userid varchar(30) NOT NULL, -- The user that updated the record.
	update_timestamp timestamp(0) NOT NULL, -- The date the record was updated.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	CONSTRAINT forest_mgmt_unit_mgmt_unit_id_mgmt_unit_type_code_key UNIQUE (mgmt_unit_id, mgmt_unit_type_code),
	CONSTRAINT forest_mgmt_unit_pkey PRIMARY KEY (mgmt_skey)
);
COMMENT ON TABLE silva.forest_mgmt_unit IS 'The administrative management unit as defined by the ministry for managing the forest land, eg. Timber Supply Area.';

-- Column comments

COMMENT ON COLUMN silva.forest_mgmt_unit.mgmt_skey IS 'System generated unique identifier';
COMMENT ON COLUMN silva.forest_mgmt_unit.mgmt_unit_type_code IS 'The unique identifier for the management unit type.';
COMMENT ON COLUMN silva.forest_mgmt_unit.mgmt_unit_id IS 'The administrative management unit identifier defined by the ministry for managing the forest land, eg. 41C.';
COMMENT ON COLUMN silva.forest_mgmt_unit.description IS 'Description of the Managment Unit Identifer. E.g. Dawson Creek TSA Block C.';
COMMENT ON COLUMN silva.forest_mgmt_unit.entry_userid IS 'The user that created the record.';
COMMENT ON COLUMN silva.forest_mgmt_unit.entry_timestamp IS 'The date the record was created.';
COMMENT ON COLUMN silva.forest_mgmt_unit.update_userid IS 'The user that updated the record.';
COMMENT ON COLUMN silva.forest_mgmt_unit.update_timestamp IS 'The date the record was updated.';
COMMENT ON COLUMN silva.forest_mgmt_unit.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.forest_mgmt_unit.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.forest_mgmt_unit.expiry_date IS 'Date the code expires';


-- silva.forest_stewardship_plan definition

-- Drop table

-- DROP TABLE silva.forest_stewardship_plan;

CREATE TABLE IF NOT EXISTS silva.forest_stewardship_plan (
	fsp_id int8 NOT NULL, -- Unique identifier for Forest Stewardship Plan
	fsp_amendment_number int8 NOT NULL, -- Identifies amendment level of FSP
	fsp_status_code varchar(4) NOT NULL, -- A code indicating the status of the Forest Stewardship Plan. Examples include but are not limited to SUB (submitted), REJ (rejected) and APP (approved).
	fsp_amendment_code varchar(4) DEFAULT 'ORG'::character varying NOT NULL, -- A code indicating the type of amendment to an FSP. Examples include Original, Amendment and Replacement
	licensee_amendment_name varchar(30) NULL, -- The licensee-specified name given to the amendment.
	plan_name varchar(120) NOT NULL, -- Textual name of FSP, provided by plan holder
	plan_term_years int2 NULL, -- Length of FSP term in years
	plan_term_months int2 NULL, -- Length of FSP term in months beyond the indicated term in years (PLAN_TERM_YEARS).
	plan_start_date timestamp(0) NULL, -- The date the FSP comes into legal effect. Set by the FSP Decision Maker.
	plan_end_date timestamp(0) NULL, -- The date the FSP expires. The PLAN END DATE is calculated using either the Term Year/Months entered by the user or the End date.
	plan_submission_date timestamp(0) NOT NULL, -- Date FSP was submitted to MoFR
	plan_decision_date timestamp(0) NULL, -- The date on which the original FSP or amendment is approved or rejected.
	plan_decision_userid varchar(30) NULL, -- The user that approved or rejected the FSP or amendment.
	transition_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates when the FSP is transitional. A transitional FSP is an original submission created from an existing FDP that does not require approval.
	identified_areas_update_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates when Declared Areas data will be changed as part of the amendment.
	fdu_update_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates when FDU data will be changed as part of the amendment.
	stocking_standard_update_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates when Stocking Standard data will be changed as part of the amendment.
	frpa197_election_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether a licensee has elected (under the Forest and Range Practices Act section 197 legislation) to roll over existing cut blocks that were created under the authority of the Forest Practices Code Act. By rolling these cut blocks over, the licensee indicates that the blocks will be governed under the Stocking Standards identified within their approved Forest Stewardship Plan.
	contact_name varchar(120) NOT NULL, -- Name of a contact for the submitting licensee.
	telephone_number varchar(10) NOT NULL, -- The business telephone number including area code of the contact for submitting licensee
	email_address varchar(120) NOT NULL, -- Email of contact for submitting licensee
	amendment_approval_date timestamp(0) NULL, -- Date amendment to plan approved by MoFR
	amendment_authority varchar(100) NULL, -- Identifier of MoFR decision maker
	amendment_approval_requird_ind varchar(1) NULL, -- Indicates if the amendment needs MoFR approval
	amendment_submission_date timestamp(0) NULL, -- Date amendment to plan submitted by plan holder
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The userid responsible for inserting data into a table.
	entry_timestamp timestamp(0) NOT NULL, -- Contains the system timestamp when data in a table was inserted.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this road section record.
	update_timestamp timestamp(0) NOT NULL, -- The timestamp of the last update to the road section record.
	CONSTRAINT forest_stewardship_plan_pkey PRIMARY KEY (fsp_id, fsp_amendment_number)
);
CREATE INDEX fsp_fspsc_fk_i ON silva.forest_stewardship_plan USING btree (fsp_status_code);
COMMENT ON TABLE silva.forest_stewardship_plan IS 'A plan submitted by a forest industry licensee stating how the BC Government?s objectives for managing the province?s forest resources will be met. It identifies the plan-holder?s obligations for a five-year period.';

-- Column comments

COMMENT ON COLUMN silva.forest_stewardship_plan.fsp_id IS 'Unique identifier for Forest Stewardship Plan';
COMMENT ON COLUMN silva.forest_stewardship_plan.fsp_amendment_number IS 'Identifies amendment level of FSP';
COMMENT ON COLUMN silva.forest_stewardship_plan.fsp_status_code IS 'A code indicating the status of the Forest Stewardship Plan. Examples include but are not limited to SUB (submitted), REJ (rejected) and APP (approved).';
COMMENT ON COLUMN silva.forest_stewardship_plan.fsp_amendment_code IS 'A code indicating the type of amendment to an FSP. Examples include Original, Amendment and Replacement';
COMMENT ON COLUMN silva.forest_stewardship_plan.licensee_amendment_name IS 'The licensee-specified name given to the amendment.';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_name IS 'Textual name of FSP, provided by plan holder';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_term_years IS 'Length of FSP term in years';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_term_months IS 'Length of FSP term in months beyond the indicated term in years (PLAN_TERM_YEARS).';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_start_date IS 'The date the FSP comes into legal effect. Set by the FSP Decision Maker.';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_end_date IS 'The date the FSP expires. The PLAN END DATE is calculated using either the Term Year/Months entered by the user or the End date.';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_submission_date IS 'Date FSP was submitted to MoFR';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_decision_date IS 'The date on which the original FSP or amendment is approved or rejected.';
COMMENT ON COLUMN silva.forest_stewardship_plan.plan_decision_userid IS 'The user that approved or rejected the FSP or amendment.';
COMMENT ON COLUMN silva.forest_stewardship_plan.transition_ind IS 'Indicates when the FSP is transitional. A transitional FSP is an original submission created from an existing FDP that does not require approval.';
COMMENT ON COLUMN silva.forest_stewardship_plan.identified_areas_update_ind IS 'Indicates when Declared Areas data will be changed as part of the amendment.';
COMMENT ON COLUMN silva.forest_stewardship_plan.fdu_update_ind IS 'Indicates when FDU data will be changed as part of the amendment.';
COMMENT ON COLUMN silva.forest_stewardship_plan.stocking_standard_update_ind IS 'Indicates when Stocking Standard data will be changed as part of the amendment.';
COMMENT ON COLUMN silva.forest_stewardship_plan.frpa197_election_ind IS 'Indicates whether a licensee has elected (under the Forest and Range Practices Act section 197 legislation) to roll over existing cut blocks that were created under the authority of the Forest Practices Code Act. By rolling these cut blocks over, the licensee indicates that the blocks will be governed under the Stocking Standards identified within their approved Forest Stewardship Plan.';
COMMENT ON COLUMN silva.forest_stewardship_plan.contact_name IS 'Name of a contact for the submitting licensee.';
COMMENT ON COLUMN silva.forest_stewardship_plan.telephone_number IS 'The business telephone number including area code of the contact for submitting licensee';
COMMENT ON COLUMN silva.forest_stewardship_plan.email_address IS 'Email of contact for submitting licensee';
COMMENT ON COLUMN silva.forest_stewardship_plan.amendment_approval_date IS 'Date amendment to plan approved by MoFR';
COMMENT ON COLUMN silva.forest_stewardship_plan.amendment_authority IS 'Identifier of MoFR decision maker';
COMMENT ON COLUMN silva.forest_stewardship_plan.amendment_approval_requird_ind IS 'Indicates if the amendment needs MoFR approval';
COMMENT ON COLUMN silva.forest_stewardship_plan.amendment_submission_date IS 'Date amendment to plan submitted by plan holder';
COMMENT ON COLUMN silva.forest_stewardship_plan.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.forest_stewardship_plan.entry_userid IS 'The userid responsible for inserting data into a table.';
COMMENT ON COLUMN silva.forest_stewardship_plan.entry_timestamp IS 'Contains the system timestamp when data in a table was inserted.';
COMMENT ON COLUMN silva.forest_stewardship_plan.update_userid IS 'The userid of the individual who last updated this road section record.';
COMMENT ON COLUMN silva.forest_stewardship_plan.update_timestamp IS 'The timestamp of the last update to the road section record.';


-- silva.forhealth_rslt definition

-- Drop table

-- DROP TABLE silva.forhealth_rslt;

CREATE TABLE IF NOT EXISTS silva.forhealth_rslt (
	forhealth_rslt_id int8 NOT NULL, -- System generated value uniquely identifying the forest health damage agent occurrance.
	opening_id int8 NULL, -- System generated value uniquely identifying the opening.
	forest_cover_id int8 NULL, -- System generated value uniquely identifying the forest cover.
	forest_cover_layer_id int8 NULL, -- System generated value uniquely identifying the forest cover layer.
	silv_damage_agent_code varchar(3) NOT NULL, -- A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.
	incidence_pct int2 NULL, -- The percentage of the Standards Unit or Opening that the agent is affecting as defined by the survey strata.
	incidence_area numeric(6, 1) NULL, -- The area in hectares of the region involved.
	observation_date timestamp(0) NULL, -- The date this observation was made.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT forhealth_rslt_pkey PRIMARY KEY (forhealth_rslt_id)
);
CREATE INDEX fr_fclr_fk_i ON silva.forhealth_rslt USING btree (forest_cover_id, forest_cover_layer_id);
CREATE INDEX fr_o_fk_i ON silva.forhealth_rslt USING btree (opening_id);
COMMENT ON TABLE silva.forhealth_rslt IS 'A table which stores the forest health survey results.';

-- Column comments

COMMENT ON COLUMN silva.forhealth_rslt.forhealth_rslt_id IS 'System generated value uniquely identifying the forest health damage agent occurrance.';
COMMENT ON COLUMN silva.forhealth_rslt.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.forhealth_rslt.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forhealth_rslt.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forhealth_rslt.silv_damage_agent_code IS 'A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.';
COMMENT ON COLUMN silva.forhealth_rslt.incidence_pct IS 'The percentage of the Standards Unit or Opening that the agent is affecting as defined by the survey strata.';
COMMENT ON COLUMN silva.forhealth_rslt.incidence_area IS 'The area in hectares of the region involved.';
COMMENT ON COLUMN silva.forhealth_rslt.observation_date IS 'The date this observation was made.';
COMMENT ON COLUMN silva.forhealth_rslt.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forhealth_rslt.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forhealth_rslt.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forhealth_rslt.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forhealth_rslt.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.forhealth_rslt_archive definition

-- Drop table

-- DROP TABLE silva.forhealth_rslt_archive;

CREATE TABLE IF NOT EXISTS silva.forhealth_rslt_archive (
	forest_cover_id int8 NULL, -- System generated value uniquely identifying the forest cover.
	archive_date timestamp(0) NOT NULL, -- The date the forest cover entry was archived to this entity.
	forest_cover_layer_id int8 NULL, -- System generated value uniquely identifying the forest cover layer.
	forhealth_rslt_id int8 NOT NULL, -- System generated value uniquely identifying the forest health damage agent occurrance.
	opening_id int8 NULL, -- System generated value uniquely identifying the opening.
	silv_damage_agent_code varchar(3) NOT NULL, -- A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.
	incidence_pct int2 NULL, -- The percentage of the Standards Unit or Opening that the agent is affecting as defined by the survey strata.
	incidence_area numeric(6, 1) NULL, -- The area in hectares of the region involved.
	observation_date timestamp(0) NULL, -- The date this observation was made.
	forest_cover_archive_date timestamp(0) NULL, -- The date the forest cover entry was archived to this entity.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT forhealth_rslt_archive_pkey PRIMARY KEY (forhealth_rslt_id, archive_date)
);
CREATE INDEX fra_fcla_fk_i ON silva.forhealth_rslt_archive USING btree (forest_cover_id, forest_cover_archive_date, forest_cover_layer_id);
COMMENT ON TABLE silva.forhealth_rslt_archive IS 'A table which stores the archive of forest health survey results.';

-- Column comments

COMMENT ON COLUMN silva.forhealth_rslt_archive.forest_cover_id IS 'System generated value uniquely identifying the forest cover.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.forest_cover_layer_id IS 'System generated value uniquely identifying the forest cover layer.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.forhealth_rslt_id IS 'System generated value uniquely identifying the forest health damage agent occurrance.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.silv_damage_agent_code IS 'A code indicating the category of insect or disease that has caused a disturbance in the stands history. Stands with a disturbance caused by insects or disease can be further described by the ACTIVITY SUB CODE. The code for the damage agent as it is currently collected (94-3). The FIP based version of Damage Agent code, retained on an as is basis until migration to a stable Ministry version of Damage Agent is availible.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.incidence_pct IS 'The percentage of the Standards Unit or Opening that the agent is affecting as defined by the survey strata.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.incidence_area IS 'The area in hectares of the region involved.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.observation_date IS 'The date this observation was made.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.forest_cover_archive_date IS 'The date the forest cover entry was archived to this entity.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forhealth_rslt_archive.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.fsp_standards_regime_xref definition

-- Drop table

-- DROP TABLE silva.fsp_standards_regime_xref;

CREATE TABLE IF NOT EXISTS silva.fsp_standards_regime_xref (
	fsp_id int8 NOT NULL, -- Unique identifier for Forest Stewardship Plan
	fsp_amendment_number int8 NOT NULL, -- Identifies amendment level of FSP
	standards_regime_id int8 NOT NULL, -- Unique identifier for the STANDARD REGIME entry.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The userid responsible for inserting data into a table.
	entry_timestamp timestamp(0) NOT NULL, -- Contains the system timestamp when data in a table was inserted.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated the record.
	update_timestamp timestamp(0) NOT NULL, -- The timestamp of the last update to the record.
	CONSTRAINT fsp_standards_regime_xref_pkey PRIMARY KEY (fsp_id, fsp_amendment_number, standards_regime_id)
);
CREATE INDEX fspsrx_fsp_fk_i ON silva.fsp_standards_regime_xref USING btree (fsp_amendment_number, fsp_id);
CREATE INDEX fspsrx_sr_fk_i ON silva.fsp_standards_regime_xref USING btree (standards_regime_id);
COMMENT ON TABLE silva.fsp_standards_regime_xref IS 'A cross reference entity that allows for the relationship between a Forest Stewardship Plan and a Standards Regime. An FSP may refer to many Standards, and a Standard may be included in many FSPs.';

-- Column comments

COMMENT ON COLUMN silva.fsp_standards_regime_xref.fsp_id IS 'Unique identifier for Forest Stewardship Plan';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.fsp_amendment_number IS 'Identifies amendment level of FSP';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.entry_userid IS 'The userid responsible for inserting data into a table.';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.entry_timestamp IS 'Contains the system timestamp when data in a table was inserted.';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.update_userid IS 'The userid of the individual who last updated the record.';
COMMENT ON COLUMN silva.fsp_standards_regime_xref.update_timestamp IS 'The timestamp of the last update to the record.';

-- silva.harvesting_authority definition

-- Drop table

-- DROP TABLE silva.harvesting_authority;

CREATE TABLE IF NOT EXISTS silva.harvesting_authority (
	hva_skey int8 NOT NULL, -- Reference from FTA.
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	cutting_permit_id varchar(3) NULL, -- Identifier for a cutting permit associated with a quota type harvesting tenure.
	harvesting_authority_id varchar(30) NULL, -- The licensee provided identifier for non-cp related harvesting tenure. E.g. Fort St. John harvesting authority.
	forest_district int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".
	district_admn_zone varchar(4) NULL, -- District Admin Zone is a free format field used internally by the districts to group files within a district for reporting purposes. Examples of use are setting the field to a geographic area or to a persons initials. Reports can be pulled off by District Admin Zone to aid work management. This field is not used by all districts.
	geographic_district int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".
	mgmt_unit_id varchar(4) NULL, -- The administrative management unit identifier defined by the ministry for managing the forest tenure, eg. 41 - Dawson Creek TSA.
	mgmt_unit_type_code varchar(1) NULL, -- The unique identifier for the management unit type.
	licence_to_cut_code varchar(2) NULL, -- The purpose or reason that the licence to cut was granted, eg., right of way.
	harvest_type_code varchar(1) NOT NULL, -- Identifies the type of harvesting authority. Valid values include "Green", "Road", "Multi-Mark", and "Fort St. John".
	harvest_auth_status_code varchar(3) NULL, -- The after status image of the harvesting authority. The status indicates the point the harvesting authority is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.
	tenure_term int4 NULL, -- Term of the tenure (in months).
	status_date timestamp(0) NULL, -- The date that the harvest authorization status was last updated.
	issue_date timestamp(0) NULL, -- Date the harvest authorization was issued.
	expiry_date timestamp(0) NULL, -- Initial expiry date set upon activation of the harvest authorization.
	extend_date timestamp(0) NULL, -- Date to which the harvest authorization validity has been extended. Current expiry date.
	extend_count int8 NULL, -- Count of the number of extensions to the expiry date that the harvest authorization has had.
	harvest_auth_extend_reas_code varchar(1) NULL, -- Code to indicate the reason to extend the life of the timber mark.
	quota_type_code varchar(1) NULL, -- A Timber Supply Area will have volumes assigned to one or more partitions, which are in effect different timber type classifications, or geographic locations. Under each partition are the different apportionments. Examples of Partitions are Conventional
	crown_lands_region_code varchar(1) NULL, -- Describes the CROWN LANDS REGION
	salvage_type_code varchar(3) NULL, -- Code describing the salvage type.
	cascade_split_code varchar(1) NULL, -- Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information.
	catastrophic_ind varchar(1) NULL, -- Indicates if the damage to the stand of timber is catastrophic. Otherwise, it is endemic.
	crown_granted_ind varchar(1) NOT NULL, -- Indicator to signify whether the land that the timber mark pertains to has a verifiable crown grant document.
	cruise_based_ind varchar(1) NOT NULL, -- Indicates whether a cruise was used for billing purposes.
	deciduous_ind varchar(1) NOT NULL, -- Indicates whether the timber mark is primarily for harvesting deciduous species. Otherwise, coniferous.
	bcaa_folio_number varchar(23) NULL, -- BC Assessment Authority Folio Number for the land that the cutting permit/Timber Mark pertains to, if applicable, or the LTO number. BCAA Folio Numbers are in the format C15 733 06604.140-1-2; the format for LTO numbers is nnn-nnn-nnn.
	"location" varchar(100) NULL, -- Contains the land legal information for the harvest authority. Land legal information indicates where the land is legally located, such as "Lot 3 of DL 343, Plan 3422".
	higher_level_plan_reference varchar(30) NULL, -- Indicates the higher level plan reference for the given harvest authority. E.g. For Fort St. John, this could be "Sustainable Forest Management Plan".
	harvest_area numeric(11, 4) NULL, -- Identifes the harvest area in hectares for harvesting authorites without spatial.
	retirement_date timestamp(0) NULL, -- The date the harvesting authority was retired from the spatial conflict layer.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	is_waste_assessment_required varchar(1) DEFAULT 'U'::character varying NOT NULL, -- A value indicating whether a harvesting Authority record requires waste assessment or not.
	is_cp_extensn_appl_fee_waived varchar(1) DEFAULT 'U'::character varying NOT NULL, -- A value that is mandatory on extension to indicate if the Extension application fee has been waived.
	is_cp_extension_appl_fee_paid varchar(1) DEFAULT 'U'::character varying NOT NULL, -- A value to show if the extension application fee has been paid.N if IS_EXT_APP_FEE_WAIVED = "Y","U" otherwise. Not mandatory on extension
	is_within_fibre_recovery_zone varchar(1) DEFAULT 'U'::character varying NOT NULL, -- A value to show whether a Cutting Permit is in Fibre Recovery Zones or not. The valid values include: "Y" it"s within the Fibre Recovery Zones "N" It"s not within the Fibre Recovery Zones "U" Unknown.
	harvesting_authority_guid uuid DEFAULT uuid_generate_v4() NOT NULL, -- Contains Harvesting Authority Global Unique Identifier
	CONSTRAINT avcon_1148331360_catas_000 CHECK (((catastrophic_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1148331360_crown_000 CHECK (((crown_granted_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1148331360_cruis_000 CHECK (((cruise_based_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1148331360_decid_000 CHECK (((deciduous_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT harvesting_authority_forest_file_id_cutting_permit_id_harve_key UNIQUE (forest_file_id, cutting_permit_id, harvesting_authority_id),
	CONSTRAINT harvesting_authority_harvesting_authority_guid_key UNIQUE (harvesting_authority_guid),
	CONSTRAINT harvesting_authority_pkey PRIMARY KEY (hva_skey),
	CONSTRAINT hva_is_cp_ext_appl_fee_pa_ck CHECK (((is_cp_extension_appl_fee_paid)::text = ANY ((ARRAY['N'::character varying, 'U'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT hva_is_cp_ext_appl_fee_wa_ck CHECK (((is_cp_extensn_appl_fee_waived)::text = ANY ((ARRAY['N'::character varying, 'U'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT hva_is_waste_assess_req_ck CHECK (((is_waste_assessment_required)::text = ANY ((ARRAY['N'::character varying, 'U'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT hva_is_within_frz_ck CHECK (((is_within_fibre_recovery_zone)::text = ANY ((ARRAY['N'::character varying, 'U'::character varying, 'Y'::character varying])::text[])))
);
CREATE INDEX hva_ff_cp_i ON silva.harvesting_authority USING btree (forest_file_id, cutting_permit_id);
CREATE INDEX hva_i1 ON silva.harvesting_authority USING btree (forest_file_id, forest_district, harvest_auth_status_code);
CREATE INDEX hva_ltccd_fk_i ON silva.harvesting_authority USING btree (licence_to_cut_code);
CREATE INDEX hva_ou_fk_i ON silva.harvesting_authority USING btree (forest_district);
COMMENT ON TABLE silva.harvesting_authority IS 'Information about the timber cutting permission for a timber tenure.';

-- Column comments

COMMENT ON COLUMN silva.harvesting_authority.hva_skey IS 'Reference from FTA.';
COMMENT ON COLUMN silva.harvesting_authority.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.harvesting_authority.cutting_permit_id IS 'Identifier for a cutting permit associated with a quota type harvesting tenure.';
COMMENT ON COLUMN silva.harvesting_authority.harvesting_authority_id IS 'The licensee provided identifier for non-cp related harvesting tenure. E.g. Fort St. John harvesting authority.';
COMMENT ON COLUMN silva.harvesting_authority.forest_district IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.harvesting_authority.district_admn_zone IS 'District Admin Zone is a free format field used internally by the districts to group files within a district for reporting purposes. Examples of use are setting the field to a geographic area or to a persons initials. Reports can be pulled off by District Admin Zone to aid work management. This field is not used by all districts.';
COMMENT ON COLUMN silva.harvesting_authority.geographic_district IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.harvesting_authority.mgmt_unit_id IS 'The administrative management unit identifier defined by the ministry for managing the forest tenure, eg. 41 - Dawson Creek TSA.';
COMMENT ON COLUMN silva.harvesting_authority.mgmt_unit_type_code IS 'The unique identifier for the management unit type.';
COMMENT ON COLUMN silva.harvesting_authority.licence_to_cut_code IS 'The purpose or reason that the licence to cut was granted, eg., right of way.';
COMMENT ON COLUMN silva.harvesting_authority.harvest_type_code IS 'Identifies the type of harvesting authority. Valid values include "Green", "Road", "Multi-Mark", and "Fort St. John".';
COMMENT ON COLUMN silva.harvesting_authority.harvest_auth_status_code IS 'The after status image of the harvesting authority. The status indicates the point the harvesting authority is at in it"s lifecycle. Examples are: PP - Planning, HA - Harvesting, LC - Logging Complete, and S - Silviculture.';
COMMENT ON COLUMN silva.harvesting_authority.tenure_term IS 'Term of the tenure (in months).';
COMMENT ON COLUMN silva.harvesting_authority.status_date IS 'The date that the harvest authorization status was last updated.';
COMMENT ON COLUMN silva.harvesting_authority.issue_date IS 'Date the harvest authorization was issued.';
COMMENT ON COLUMN silva.harvesting_authority.expiry_date IS 'Initial expiry date set upon activation of the harvest authorization.';
COMMENT ON COLUMN silva.harvesting_authority.extend_date IS 'Date to which the harvest authorization validity has been extended. Current expiry date.';
COMMENT ON COLUMN silva.harvesting_authority.extend_count IS 'Count of the number of extensions to the expiry date that the harvest authorization has had.';
COMMENT ON COLUMN silva.harvesting_authority.harvest_auth_extend_reas_code IS 'Code to indicate the reason to extend the life of the timber mark.';
COMMENT ON COLUMN silva.harvesting_authority.quota_type_code IS 'A Timber Supply Area will have volumes assigned to one or more partitions, which are in effect different timber type classifications, or geographic locations. Under each partition are the different apportionments. Examples of Partitions are Conventional';
COMMENT ON COLUMN silva.harvesting_authority.crown_lands_region_code IS 'Describes the CROWN LANDS REGION';
COMMENT ON COLUMN silva.harvesting_authority.salvage_type_code IS 'Code describing the salvage type.';
COMMENT ON COLUMN silva.harvesting_authority.cascade_split_code IS 'Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information.';
COMMENT ON COLUMN silva.harvesting_authority.catastrophic_ind IS 'Indicates if the damage to the stand of timber is catastrophic. Otherwise, it is endemic.';
COMMENT ON COLUMN silva.harvesting_authority.crown_granted_ind IS 'Indicator to signify whether the land that the timber mark pertains to has a verifiable crown grant document.';
COMMENT ON COLUMN silva.harvesting_authority.cruise_based_ind IS 'Indicates whether a cruise was used for billing purposes.';
COMMENT ON COLUMN silva.harvesting_authority.deciduous_ind IS 'Indicates whether the timber mark is primarily for harvesting deciduous species. Otherwise, coniferous.';
COMMENT ON COLUMN silva.harvesting_authority.bcaa_folio_number IS 'BC Assessment Authority Folio Number for the land that the cutting permit/Timber Mark pertains to, if applicable, or the LTO number. BCAA Folio Numbers are in the format C15 733 06604.140-1-2; the format for LTO numbers is nnn-nnn-nnn.';
COMMENT ON COLUMN silva.harvesting_authority."location" IS 'Contains the land legal information for the harvest authority. Land legal information indicates where the land is legally located, such as "Lot 3 of DL 343, Plan 3422".';
COMMENT ON COLUMN silva.harvesting_authority.higher_level_plan_reference IS 'Indicates the higher level plan reference for the given harvest authority. E.g. For Fort St. John, this could be "Sustainable Forest Management Plan".';
COMMENT ON COLUMN silva.harvesting_authority.harvest_area IS 'Identifes the harvest area in hectares for harvesting authorites without spatial.';
COMMENT ON COLUMN silva.harvesting_authority.retirement_date IS 'The date the harvesting authority was retired from the spatial conflict layer.';
COMMENT ON COLUMN silva.harvesting_authority.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.harvesting_authority.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.harvesting_authority.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.harvesting_authority.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.harvesting_authority.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.harvesting_authority.is_waste_assessment_required IS 'A value indicating whether a harvesting Authority record requires waste assessment or not.';
COMMENT ON COLUMN silva.harvesting_authority.is_cp_extensn_appl_fee_waived IS 'A value that is mandatory on extension to indicate if the Extension application fee has been waived.';
COMMENT ON COLUMN silva.harvesting_authority.is_cp_extension_appl_fee_paid IS 'A value to show if the extension application fee has been paid.N if IS_EXT_APP_FEE_WAIVED = "Y","U" otherwise. Not mandatory on extension';
COMMENT ON COLUMN silva.harvesting_authority.is_within_fibre_recovery_zone IS 'A value to show whether a Cutting Permit is in Fibre Recovery Zones or not. The valid values include: "Y" it"s within the Fibre Recovery Zones "N" It"s not within the Fibre Recovery Zones "U" Unknown.';
COMMENT ON COLUMN silva.harvesting_authority.harvesting_authority_guid IS 'Contains Harvesting Authority Global Unique Identifier';


-- silva.hauling_authority definition

-- Drop table

-- DROP TABLE silva.hauling_authority;

CREATE TABLE IF NOT EXISTS silva.hauling_authority (
	timber_mark varchar(6) NOT NULL, -- See FTA data model for details.
	forest_file_id varchar(10) NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	marking_method_code varchar(1) NULL, -- Code indicating the method of marking the truckload of harvested timber.
	marking_instrument_code varchar(1) NULL, -- Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	hauling_authority_guid uuid DEFAULT uuid_generate_v4() NOT NULL, -- Contains Global Unique Identifier for Hauling Authority
	CONSTRAINT hauling_authority_hauling_authority_guid_key UNIQUE (hauling_authority_guid),
	CONSTRAINT hauling_authority_pkey PRIMARY KEY (timber_mark)
);
CREATE INDEX haa_ff_i ON silva.hauling_authority USING btree (forest_file_id);
CREATE INDEX haa_micd_fk_i ON silva.hauling_authority USING btree (marking_instrument_code);
CREATE INDEX haa_mmcd_fk_i ON silva.hauling_authority USING btree (marking_method_code);
COMMENT ON TABLE silva.hauling_authority IS 'Information about the moving permission for a timber tenure. For some Road Permits and Licences to Cut, no moving permission is granted.';

-- Column comments

COMMENT ON COLUMN silva.hauling_authority.timber_mark IS 'See FTA data model for details.';
COMMENT ON COLUMN silva.hauling_authority.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.hauling_authority.marking_method_code IS 'Code indicating the method of marking the truckload of harvested timber.';
COMMENT ON COLUMN silva.hauling_authority.marking_instrument_code IS 'Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.';
COMMENT ON COLUMN silva.hauling_authority.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.hauling_authority.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.hauling_authority.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.hauling_authority.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.hauling_authority.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.hauling_authority.hauling_authority_guid IS 'Contains Global Unique Identifier for Hauling Authority';


-- silva.open_viewable_category definition

-- Drop table

-- DROP TABLE silva.open_viewable_category;

CREATE TABLE IF NOT EXISTS silva.open_viewable_category (
	open_category_code varchar(7) NOT NULL, -- A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.
	opening_status_code varchar(3) NULL, -- A code indicating the status of the prescription. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
	viewable_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether the category is viewable for all users in RESULTS. Allowable values are "Y" and "N".
	CONSTRAINT avcon_1215562930_viewa_000 CHECK (((viewable_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT open_viewable_category_pkey PRIMARY KEY (open_category_code)
);
COMMENT ON TABLE silva.open_viewable_category IS 'OPEN_VIEWABLE_CATEGORY identifies the open categories that can be accessed by all licensees using the application. The system must use this stored information as a means to allow access for all users to openings that have an opening category with this designation enabled or openings in a Free Growing status. All other opening categories will continue to be restricted based on the current business rules.';

-- Column comments

COMMENT ON COLUMN silva.open_viewable_category.open_category_code IS 'A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.';
COMMENT ON COLUMN silva.open_viewable_category.opening_status_code IS 'A code indicating the status of the prescription. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.';
COMMENT ON COLUMN silva.open_viewable_category.viewable_ind IS 'Indicates whether the category is viewable for all users in RESULTS. Allowable values are "Y" and "N".';


-- silva.opening definition

-- Drop table

-- DROP TABLE silva.opening;

CREATE TABLE IF NOT EXISTS silva.opening (
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	geo_district_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	admin_district_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	mapsheet_grid varchar(3) NULL, -- NTG or BCGS grid. Values are 82, 83, 92, 93, 94, 95, 102, 103,104, 114.
	mapsheet_letter varchar(1) NULL, -- BCGS/NTG letter. Values are A - P, and W.
	mapsheet_square varchar(3) NULL, -- BCGS NUMBER or NTG NUMBER and LETTER. BCGS NUMBER values are 1-100, and NTG NUMBER values are 1-16 with NTG LETTER values A - H, and W.
	mapsheet_quad varchar(1) NULL, -- Quadrant. Blank if NTG number value entered; 0 - 4 if BCGS number entered.
	mapsheet_sub_quad varchar(1) NULL, -- Sub-Quadrant. Blank if NTG number value entered; 0 - 4 if BCGS number entered.
	opening_number varchar(4) NULL, -- An unique identifier up to four characters long that describes the opening on a specified mapsheet.
	opening_locn_name varchar(30) NULL, -- A descriptive name of the geographic location of the opening.
	open_category_code varchar(7) NOT NULL, -- A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.
	licensee_opening_id varchar(30) NULL, -- A unique identifier provided by the Licensee to identify the Opening Number.
	tsb_number_code varchar(3) NULL, -- The unique code identifying a Timber Supply Block.
	opening_status_code varchar(3) NOT NULL, -- A code indicating the status of the opening. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
	org_unit_no int8 NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	dist_admin_zone varchar(2) NULL, -- The code for the district zone where the opening is located. Codes are unique within each district.
	max_allow_permnt_access_pct numeric(3, 1) NULL, -- Maximum percentage of the Total Area Under the Prescription that may be occupied by permanent access structures, including roads, landings, gravel pits, borrow pits or other.
	prev_age_class_code varchar(1) NULL, -- The age classification of the primary tree species in the previous stand (for the area under the prescription).
	prev_site_index int4 NULL, -- The site index for the primary tree species in the previous stand (for the area under the prescription).
	prev_site_index_source_code varchar(1) NULL, -- The site index source code for the primary tree species in the previous stand (for the area under the opening).
	prev_height_class_code varchar(1) NULL, -- The height clan code (or the primary tree species in the previous stand (for the area under the prescription).
	prev_site_class_code varchar(1) NULL, -- This code indicates the quality of the land. Note: This column is not maintained by the system.
	prev_stocking_class_code varchar(1) NULL, -- The stocking class code for the primary tree species in the previous stand (for the area under the prescription).
	prev_stocking_status_code varchar(3) NULL, -- A code to describe the previous stand type for the area under the prescription.
	prev_tree_spp1_code varchar(8) NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	prev_tree_spp2_code varchar(8) NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	app_ent_by_userid varchar(30) NULL, -- The userid of the individual who entered the prescription information.
	approve_date timestamp(0) NULL, -- The date on which the prescription was approved.
	amendment_ind varchar(1) NULL, -- A Y/N indicator, indicating if the prescription has been amended.
	results_submission_id int8 NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT opening_pkey PRIMARY KEY (opening_id)
);
CREATE INDEX o_licensee_id_i ON silva.opening USING btree (licensee_opening_id);
CREATE INDEX o_open_category_code_i ON silva.opening USING btree (open_category_code);
CREATE INDEX o_opening_number_i ON silva.opening USING btree (mapsheet_grid, mapsheet_letter, mapsheet_quad, mapsheet_square, mapsheet_sub_quad, opening_number);
CREATE INDEX o_ou_fk_i ON silva.opening USING btree (admin_district_no);
CREATE INDEX o_status_i ON silva.opening USING btree (opening_status_code);
COMMENT ON TABLE silva.opening IS 'The administrative boundary of an area of land on which silviculture activities are planned and completed.';

-- Column comments

COMMENT ON COLUMN silva.opening.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening.geo_district_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.opening.admin_district_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.opening.mapsheet_grid IS 'NTG or BCGS grid. Values are 82, 83, 92, 93, 94, 95, 102, 103,104, 114.';
COMMENT ON COLUMN silva.opening.mapsheet_letter IS 'BCGS/NTG letter. Values are A - P, and W.';
COMMENT ON COLUMN silva.opening.mapsheet_square IS 'BCGS NUMBER or NTG NUMBER and LETTER. BCGS NUMBER values are 1-100, and NTG NUMBER values are 1-16 with NTG LETTER values A - H, and W.';
COMMENT ON COLUMN silva.opening.mapsheet_quad IS 'Quadrant. Blank if NTG number value entered; 0 - 4 if BCGS number entered.';
COMMENT ON COLUMN silva.opening.mapsheet_sub_quad IS 'Sub-Quadrant. Blank if NTG number value entered; 0 - 4 if BCGS number entered.';
COMMENT ON COLUMN silva.opening.opening_number IS 'An unique identifier up to four characters long that describes the opening on a specified mapsheet.';
COMMENT ON COLUMN silva.opening.opening_locn_name IS 'A descriptive name of the geographic location of the opening.';
COMMENT ON COLUMN silva.opening.open_category_code IS 'A code used to describe the category for the opening. The opening categories reference the governing applicable legislation and are determined by responsibility, opening origin, tenure type and prescription type.';
COMMENT ON COLUMN silva.opening.licensee_opening_id IS 'A unique identifier provided by the Licensee to identify the Opening Number.';
COMMENT ON COLUMN silva.opening.tsb_number_code IS 'The unique code identifying a Timber Supply Block.';
COMMENT ON COLUMN silva.opening.opening_status_code IS 'A code indicating the status of the opening. Examples include but are not limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.';
COMMENT ON COLUMN silva.opening.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.opening.dist_admin_zone IS 'The code for the district zone where the opening is located. Codes are unique within each district.';
COMMENT ON COLUMN silva.opening.max_allow_permnt_access_pct IS 'Maximum percentage of the Total Area Under the Prescription that may be occupied by permanent access structures, including roads, landings, gravel pits, borrow pits or other.';
COMMENT ON COLUMN silva.opening.prev_age_class_code IS 'The age classification of the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.opening.prev_site_index IS 'The site index for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.opening.prev_site_index_source_code IS 'The site index source code for the primary tree species in the previous stand (for the area under the opening).';
COMMENT ON COLUMN silva.opening.prev_height_class_code IS 'The height clan code (or the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.opening.prev_site_class_code IS 'This code indicates the quality of the land. Note: This column is not maintained by the system.';
COMMENT ON COLUMN silva.opening.prev_stocking_class_code IS 'The stocking class code for the primary tree species in the previous stand (for the area under the prescription).';
COMMENT ON COLUMN silva.opening.prev_stocking_status_code IS 'A code to describe the previous stand type for the area under the prescription.';
COMMENT ON COLUMN silva.opening.prev_tree_spp1_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.opening.prev_tree_spp2_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.opening.app_ent_by_userid IS 'The userid of the individual who entered the prescription information.';
COMMENT ON COLUMN silva.opening.approve_date IS 'The date on which the prescription was approved.';
COMMENT ON COLUMN silva.opening.amendment_ind IS 'A Y/N indicator, indicating if the prescription has been amended.';
COMMENT ON COLUMN silva.opening.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.opening.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.opening.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.opening.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.opening.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.opening.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.opening_amendment_history definition

-- Drop table

-- DROP TABLE silva.opening_amendment_history;

CREATE TABLE IF NOT EXISTS silva.opening_amendment_history (
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	opening_amendment_number int4 NOT NULL, -- A system generated value uniquely identifying an amendment.
	amendment_userid varchar(30) NOT NULL, -- The user ID of the person who initiates the amendment. Changes the status from APP to AMD.
	amendment_date timestamp(0) NOT NULL, -- The date the amendment was first initiated.
	submitted_by_userid varchar(30) NULL, -- The USERID of the person who submitted the amendment.
	submitted_date timestamp(0) NULL, -- The date and time the amendment was submitted.
	app_ent_by_userid varchar(30) NULL, -- The userid of the individual who entered the prescription information.
	approve_date timestamp(0) NULL, -- The date that SP or amendment has been approved (or put into effect (for MoF SP"s).
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT opening_amendment_history_pkey PRIMARY KEY (opening_id, opening_amendment_number)
);
COMMENT ON TABLE silva.opening_amendment_history IS 'Shows history of amendments made to an opening. Keeps track of dates, times, user identification and user name.';

-- Column comments

COMMENT ON COLUMN silva.opening_amendment_history.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening_amendment_history.opening_amendment_number IS 'A system generated value uniquely identifying an amendment.';
COMMENT ON COLUMN silva.opening_amendment_history.amendment_userid IS 'The user ID of the person who initiates the amendment. Changes the status from APP to AMD.';
COMMENT ON COLUMN silva.opening_amendment_history.amendment_date IS 'The date the amendment was first initiated.';
COMMENT ON COLUMN silva.opening_amendment_history.submitted_by_userid IS 'The USERID of the person who submitted the amendment.';
COMMENT ON COLUMN silva.opening_amendment_history.submitted_date IS 'The date and time the amendment was submitted.';
COMMENT ON COLUMN silva.opening_amendment_history.app_ent_by_userid IS 'The userid of the individual who entered the prescription information.';
COMMENT ON COLUMN silva.opening_amendment_history.approve_date IS 'The date that SP or amendment has been approved (or put into effect (for MoF SP"s).';
COMMENT ON COLUMN silva.opening_amendment_history.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.opening_amendment_history.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.opening_amendment_history.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.opening_amendment_history.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.opening_amendment_history.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.opening_attachment definition

-- Drop table

-- DROP TABLE silva.opening_attachment;

CREATE TABLE IF NOT EXISTS silva.opening_attachment (
	opening_attachment_file_id int8 NOT NULL, -- Uniquely identifies the attached file.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	attachment_name varchar(50) NOT NULL, -- File name, not including the path, of the attached file at the time it was uploaded.
	attachment_description varchar(120) NULL, -- Short description of the contents of the file.
	mime_type_code varchar(3) NULL, -- Multipurpose Internet Mail Extension (MIME) type is the method used by Web browsers to associate files of a certain type with helper applications that display files of that type.
	attachment_data bytea NOT NULL, -- The attached file.
	entry_userid varchar(30) NOT NULL, -- User who attached the file to the Opening.
	entry_timestamp timestamp(0) NOT NULL, -- Date and time that the file was attached to the Opening.
	update_userid varchar(30) NOT NULL, -- Userid of the user who last upated the attachment.
	update_timestamp timestamp(0) NOT NULL, -- Date and time that the attachment was last updated.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	opening_attachment_guid uuid DEFAULT uuid_generate_v4() NOT NULL, -- Unique identifier used to identify, save, retrieve or delete the Opening Attachment in the object storage. Saved as the ID within the metadata as well as the obfuscated file name
	CONSTRAINT opening_attachment_opening_attachment_guid_key UNIQUE (opening_attachment_guid),
	CONSTRAINT opening_attachment_pkey PRIMARY KEY (opening_attachment_file_id)
);
CREATE INDEX oa_mtc_fk_i ON silva.opening_attachment USING btree (mime_type_code);
CREATE INDEX oa_o_fk_i ON silva.opening_attachment USING btree (opening_id);
COMMENT ON TABLE silva.opening_attachment IS 'A file attached to an Opening for purpose of clarification or exhibit.';

-- Column comments

COMMENT ON COLUMN silva.opening_attachment.opening_attachment_file_id IS 'Uniquely identifies the attached file.';
COMMENT ON COLUMN silva.opening_attachment.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening_attachment.attachment_name IS 'File name, not including the path, of the attached file at the time it was uploaded.';
COMMENT ON COLUMN silva.opening_attachment.attachment_description IS 'Short description of the contents of the file.';
COMMENT ON COLUMN silva.opening_attachment.mime_type_code IS 'Multipurpose Internet Mail Extension (MIME) type is the method used by Web browsers to associate files of a certain type with helper applications that display files of that type.';
COMMENT ON COLUMN silva.opening_attachment.attachment_data IS 'The attached file.';
COMMENT ON COLUMN silva.opening_attachment.entry_userid IS 'User who attached the file to the Opening.';
COMMENT ON COLUMN silva.opening_attachment.entry_timestamp IS 'Date and time that the file was attached to the Opening.';
COMMENT ON COLUMN silva.opening_attachment.update_userid IS 'Userid of the user who last upated the attachment.';
COMMENT ON COLUMN silva.opening_attachment.update_timestamp IS 'Date and time that the attachment was last updated.';
COMMENT ON COLUMN silva.opening_attachment.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN silva.opening_attachment.opening_attachment_guid IS 'Unique identifier used to identify, save, retrieve or delete the Opening Attachment in the object storage. Saved as the ID within the metadata as well as the obfuscated file name';


-- silva.opening_comment_link definition

-- Drop table

-- DROP TABLE silva.opening_comment_link;

CREATE TABLE IF NOT EXISTS silva.opening_comment_link (
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	CONSTRAINT opening_comment_link_pkey PRIMARY KEY (opening_id, silviculture_comment_id)
);

-- Column comments

COMMENT ON COLUMN silva.opening_comment_link.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening_comment_link.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';


-- silva.opening_geometry definition

-- Drop table

-- DROP TABLE silva.opening_geometry;

CREATE TABLE IF NOT EXISTS silva.opening_geometry (
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	geometry geometry(geometry, 3005) NOT NULL, -- Spatial coordinate representation of the polygon related to the Opening.
	feature_area numeric(11, 4) NOT NULL, -- The area of the polygon pertaining to the Opening measured in square metres.
	feature_perimeter numeric(11, 4) NOT NULL, -- The perimeter of the polygon pertaining to the Opening measured in metres.
	feature_class_skey int8 NOT NULL, -- Unique identifier of the feature class.
	capture_method_code varchar(30) NULL,
	data_source_code varchar(10) NULL,
	observation_date timestamp(0) NULL, -- Captures the date of observation for the given feature image.
	data_quality_comment varchar(255) NULL, -- Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.
	entry_userid varchar(30) NOT NULL, -- The userid of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT opening_geometry_pkey PRIMARY KEY (opening_id)
);
CREATE INDEX og_geometry_i ON silva.opening_geometry USING gist (geometry);
COMMENT ON TABLE silva.opening_geometry IS 'The spatial representation for an opening, which is the administrative boundary of an area of land on which silviculture activities are planned and completed.';

-- Column comments

COMMENT ON COLUMN silva.opening_geometry.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening_geometry.geometry IS 'Spatial coordinate representation of the polygon related to the Opening.';
COMMENT ON COLUMN silva.opening_geometry.feature_area IS 'The area of the polygon pertaining to the Opening measured in square metres.';
COMMENT ON COLUMN silva.opening_geometry.feature_perimeter IS 'The perimeter of the polygon pertaining to the Opening measured in metres.';
COMMENT ON COLUMN silva.opening_geometry.feature_class_skey IS 'Unique identifier of the feature class.';
COMMENT ON COLUMN silva.opening_geometry.observation_date IS 'Captures the date of observation for the given feature image.';
COMMENT ON COLUMN silva.opening_geometry.data_quality_comment IS 'Allows users to specify comments regarding the accuracy and general quality of the given feature"s data.';
COMMENT ON COLUMN silva.opening_geometry.entry_userid IS 'The userid of the individual who entered the information.';
COMMENT ON COLUMN silva.opening_geometry.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.opening_geometry.update_userid IS 'The userid of the individual who last updated the information.';
COMMENT ON COLUMN silva.opening_geometry.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.opening_geometry.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.opening_land_status definition

-- Drop table

-- DROP TABLE silva.opening_land_status;

CREATE TABLE IF NOT EXISTS silva.opening_land_status (
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	opening_land_status_date timestamp(0) NOT NULL, -- The date of the last status update.
	np_unn_area numeric(7, 1) NOT NULL, -- The non-productive unnatural area, expressed in hectares.
	np_nat_area numeric(7, 1) NOT NULL, -- The non-productive natural area, expressed in hectares.
	np_for_area numeric(7, 1) NOT NULL, -- The non-productive forested area, expressed in hectares.
	nsr_npl_area numeric(7, 1) NOT NULL, -- The not sufficiently restocked nonplantable area, expressed in hectares.
	nsr_nat_area numeric(7, 1) NOT NULL, -- The not sufficiently restocked natural area, expressed in hectares.
	nsr_pl_area numeric(7, 1) NOT NULL, -- The not sufficiently restocked plantable area, expressed in hectares.
	sr_art_area numeric(7, 1) NOT NULL, -- The sufficiently restocked artificial area, expressed in hectares.
	sr_nat_area numeric(7, 1) NOT NULL, -- The sufficiently restocked natural area, expressed in hectares.
	mat_area numeric(7, 1) NOT NULL, -- The mature natural and artificial area , expressed in hectares. The Mature area field was added to the database in July 2000. Prior to July 2000 all Mature area was stored in the SR Artificial and SR Natural Area.
	fg_norm_area numeric(7, 1) NULL, -- The free growing area, expressed in hectares, of all polygons that have the "Free Growing" indicator set to "Y". is area is not double counted against the "SR" total.
	nc_br_area numeric(7, 1) NOT NULL, -- The non-commerical brush area, expressed in hectares. Area > 4 hectares and Ministry"s responsibility. Area has special significance in MLSIS and is not reflected in ISIS.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter used to ensure data integrity. This item should be incremented during each update.
	CONSTRAINT opening_land_status_pkey PRIMARY KEY (opening_id, opening_land_status_date)
);
COMMENT ON TABLE silva.opening_land_status IS 'Identifies the land status of the entire opening on a given date. These attributes are derived from the FOREST_COVER table.';

-- Column comments

COMMENT ON COLUMN silva.opening_land_status.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.opening_land_status.opening_land_status_date IS 'The date of the last status update.';
COMMENT ON COLUMN silva.opening_land_status.np_unn_area IS 'The non-productive unnatural area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.np_nat_area IS 'The non-productive natural area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.np_for_area IS 'The non-productive forested area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.nsr_npl_area IS 'The not sufficiently restocked nonplantable area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.nsr_nat_area IS 'The not sufficiently restocked natural area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.nsr_pl_area IS 'The not sufficiently restocked plantable area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.sr_art_area IS 'The sufficiently restocked artificial area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.sr_nat_area IS 'The sufficiently restocked natural area, expressed in hectares.';
COMMENT ON COLUMN silva.opening_land_status.mat_area IS 'The mature natural and artificial area , expressed in hectares. The Mature area field was added to the database in July 2000. Prior to July 2000 all Mature area was stored in the SR Artificial and SR Natural Area.';
COMMENT ON COLUMN silva.opening_land_status.fg_norm_area IS 'The free growing area, expressed in hectares, of all polygons that have the "Free Growing" indicator set to "Y". is area is not double counted against the "SR" total.';
COMMENT ON COLUMN silva.opening_land_status.nc_br_area IS 'The non-commerical brush area, expressed in hectares. Area > 4 hectares and Ministry"s responsibility. Area has special significance in MLSIS and is not reflected in ISIS.';
COMMENT ON COLUMN silva.opening_land_status.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.opening_land_status.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.opening_land_status.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.opening_land_status.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.opening_land_status.revision_count IS 'A counter used to ensure data integrity. This item should be incremented during each update.';


-- silva.planting_rslt definition

-- Drop table

-- DROP TABLE silva.planting_rslt;

CREATE TABLE IF NOT EXISTS silva.planting_rslt (
	activity_treatment_unit_id int8 NOT NULL, -- System generated value uniquely identifying the Activity Treatment Unit.
	results_ind varchar(1) NOT NULL, -- Indicates whether or not the planting information is planning information (N) or the result of actual planting performed.
	plant_rslt_seq_no int4 NOT NULL, -- Internal processing variable used to maintain order of the plant stock allotments for a specific ATU.
	silv_tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	number_planted int8 NULL, -- The number of stems planted for each species within the ATU.
	planted_no_beyond_xfer_limit int8 NULL, -- IN THE OVERFLOW TABLE.
	bid_price_per_tree numeric(5, 4) NULL, -- The anticipated (RESULTS IND = N) or actual (RESULTS IND = Y) bid price per tree for a planting contract.
	seedlot_number varchar(5) NULL, -- The unique number (key) assigned to a quantity of seed of a particular species and quality from a given location collected at a given time.
	veg_lot_id varchar(5) NULL, -- An identifier that is system generated by the system to uniquely identify this record.
	request_skey int8 NULL, -- A surrogate key to uniquely identify each Spar Request.
	item_id varchar(1) NULL, -- A unique identifier for each item within a request. It is alphabetically assigned (A, B,...Z).
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	climate_based_seed_xfer_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- CLIMATE BASED SEED TRANSFER - Indicates that the seedlings 'transferred beyond limit' are based on the Ministry's Climate Change Seed Policy.
	CONSTRAINT planting_rslt_pkey PRIMARY KEY (activity_treatment_unit_id, results_ind, plant_rslt_seq_no)
);
CREATE INDEX pr_sparri_fk_i ON silva.planting_rslt USING btree (request_skey, item_id);
CREATE INDEX pr_vl_fk_i ON silva.planting_rslt USING btree (veg_lot_id);
COMMENT ON TABLE silva.planting_rslt IS 'Collects planned and actual information about a planting activity performed on an Activity Treatment Unit.';

-- Column comments

COMMENT ON COLUMN silva.planting_rslt.activity_treatment_unit_id IS 'System generated value uniquely identifying the Activity Treatment Unit.';
COMMENT ON COLUMN silva.planting_rslt.results_ind IS 'Indicates whether or not the planting information is planning information (N) or the result of actual planting performed.';
COMMENT ON COLUMN silva.planting_rslt.plant_rslt_seq_no IS 'Internal processing variable used to maintain order of the plant stock allotments for a specific ATU.';
COMMENT ON COLUMN silva.planting_rslt.silv_tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.planting_rslt.number_planted IS 'The number of stems planted for each species within the ATU.';
COMMENT ON COLUMN silva.planting_rslt.planted_no_beyond_xfer_limit IS 'IN THE OVERFLOW TABLE.';
COMMENT ON COLUMN silva.planting_rslt.bid_price_per_tree IS 'The anticipated (RESULTS IND = N) or actual (RESULTS IND = Y) bid price per tree for a planting contract.';
COMMENT ON COLUMN silva.planting_rslt.seedlot_number IS 'The unique number (key) assigned to a quantity of seed of a particular species and quality from a given location collected at a given time.';
COMMENT ON COLUMN silva.planting_rslt.veg_lot_id IS 'An identifier that is system generated by the system to uniquely identify this record.';
COMMENT ON COLUMN silva.planting_rslt.request_skey IS 'A surrogate key to uniquely identify each Spar Request.';
COMMENT ON COLUMN silva.planting_rslt.item_id IS 'A unique identifier for each item within a request. It is alphabetically assigned (A, B,...Z).';
COMMENT ON COLUMN silva.planting_rslt.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.planting_rslt.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.planting_rslt.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.planting_rslt.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.planting_rslt.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';
COMMENT ON COLUMN silva.planting_rslt.climate_based_seed_xfer_ind IS 'CLIMATE BASED SEED TRANSFER - Indicates that the seedlings ''transferred beyond limit'' are based on the Ministry''s Climate Change Seed Policy.';


-- silva.prov_forest_use definition

-- Drop table

-- DROP TABLE silva.prov_forest_use;

CREATE TABLE IF NOT EXISTS silva.prov_forest_use (
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	file_status_st varchar(3) NULL, -- The current status of the forest land use, e.g. Pending - Planned, Harvesting - Suspended, Active. This is a subset of timber status code.
	file_status_date timestamp(0) NULL, -- The date on which the current forest land use status was set.
	file_type_code varchar(3) NULL, -- The FTAS code to indicate the type of file, and often synonymous with a tenure or a project.
	forest_region int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	bcts_org_unit int8 NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	sb_funded_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates if the harvest tenure is small business funded.
	district_admin_zone varchar(4) NULL, -- District Admin Zone is a free format field used internally by the districts to group files within a district for reporting purposes. Examples of use are setting the field to a geographic area or to a persons initials. Reports can be pulled off by District Admin Zone to aid work management. This field is not used by all districts.
	mgmt_unit_type varchar(1) NULL, -- The unique identifier for the management unit type.
	mgmt_unit_id varchar(4) NULL, -- The administrative management unit identifier defined by the ministry for managing the forest tenure, eg. 41 - Dawson Creek TSA.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	forest_tenure_guid uuid DEFAULT uuid_generate_v4() NOT NULL, -- A global unique identifier used to globally identified a Forest Tenure (aka, Forest File). It is used for exchanging data between external applications and services.
	CONSTRAINT avcon_1148331360_sb_fu_001 CHECK (((sb_funded_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT prov_forest_use_forest_tenure_guid_key UNIQUE (forest_tenure_guid),
	CONSTRAINT prov_forest_use_pkey PRIMARY KEY (forest_file_id)
);
CREATE INDEX pfu_fsc_fk_i ON silva.prov_forest_use USING btree (file_status_st);
CREATE INDEX pfu_ftcd_fk_i ON silva.prov_forest_use USING btree (file_type_code);
CREATE INDEX pfu_i2 ON silva.prov_forest_use USING btree (forest_file_id, file_type_code, file_status_st);
CREATE INDEX pfu_ou_fk_i ON silva.prov_forest_use USING btree (forest_region);
COMMENT ON TABLE silva.prov_forest_use IS 'General header information to identify the type of forest land use managed by the Ministry of Forests (eg., timber tenures, grazing tenures, recreation files, forest service roads, etc.). This is the main access point for examining land use information. Recommend that this entity be renamed to FOREST TENURE or FOREST FILE in the future (TF - this needs more analysis to determine what is indentified in this entity - licence, permit, etc and if all rights identified belong here).';

-- Column comments

COMMENT ON COLUMN silva.prov_forest_use.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.prov_forest_use.file_status_st IS 'The current status of the forest land use, e.g. Pending - Planned, Harvesting - Suspended, Active. This is a subset of timber status code.';
COMMENT ON COLUMN silva.prov_forest_use.file_status_date IS 'The date on which the current forest land use status was set.';
COMMENT ON COLUMN silva.prov_forest_use.file_type_code IS 'The FTAS code to indicate the type of file, and often synonymous with a tenure or a project.';
COMMENT ON COLUMN silva.prov_forest_use.forest_region IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.prov_forest_use.bcts_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.prov_forest_use.sb_funded_ind IS 'Indicates if the harvest tenure is small business funded.';
COMMENT ON COLUMN silva.prov_forest_use.district_admin_zone IS 'District Admin Zone is a free format field used internally by the districts to group files within a district for reporting purposes. Examples of use are setting the field to a geographic area or to a persons initials. Reports can be pulled off by District Admin Zone to aid work management. This field is not used by all districts.';
COMMENT ON COLUMN silva.prov_forest_use.mgmt_unit_type IS 'The unique identifier for the management unit type.';
COMMENT ON COLUMN silva.prov_forest_use.mgmt_unit_id IS 'The administrative management unit identifier defined by the ministry for managing the forest tenure, eg. 41 - Dawson Creek TSA.';
COMMENT ON COLUMN silva.prov_forest_use.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.prov_forest_use.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.prov_forest_use.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.prov_forest_use.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.prov_forest_use.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.prov_forest_use.forest_tenure_guid IS 'A global unique identifier used to globally identified a Forest Tenure (aka, Forest File). It is used for exchanging data between external applications and services.';


-- silva.results_audit_detail definition

-- Drop table

-- DROP TABLE silva.results_audit_detail;

CREATE TABLE IF NOT EXISTS silva.results_audit_detail (
	results_audit_detail_id int8 NOT NULL, -- The unique identifier for the audit detail entry. Created through a sequence.
	results_audit_event_id int8 NOT NULL, -- A unique system generated number to use as the key for the table.
	business_identifier varchar(30) NULL, -- Describes the business key for which data is being audited. Examples of business keys are Licensee ID, Standards Unit ID, Forest Cover Layer ID.
	table_name varchar(30) NOT NULL, -- The name of the data base table that was modified.
	column_name varchar(30) NOT NULL, -- The name of the column that the data was modified in.
	old_value varchar(250) NULL, -- The type of data that was modified. Would be one of, Character, Date or Number. This attribute will be used to determine which of the value fields contains the prior value of the element modified.
	new_value varchar(250) NULL, -- If the data type was Character, the value of the field before the change.
	entry_userid varchar(30) NOT NULL, -- The userid of the person who changed the data.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the data change occurred.
	CONSTRAINT results_audit_detail_pkey PRIMARY KEY (results_audit_detail_id)
);
CREATE INDEX rad_rae_fk_i ON silva.results_audit_detail USING btree (results_audit_event_id);
COMMENT ON TABLE silva.results_audit_detail IS 'Details each of the changes included during an EDT AUDIT EVENT. Shows the value of the modified column before and after the change.';

-- Column comments

COMMENT ON COLUMN silva.results_audit_detail.results_audit_detail_id IS 'The unique identifier for the audit detail entry. Created through a sequence.';
COMMENT ON COLUMN silva.results_audit_detail.results_audit_event_id IS 'A unique system generated number to use as the key for the table.';
COMMENT ON COLUMN silva.results_audit_detail.business_identifier IS 'Describes the business key for which data is being audited. Examples of business keys are Licensee ID, Standards Unit ID, Forest Cover Layer ID.';
COMMENT ON COLUMN silva.results_audit_detail.table_name IS 'The name of the data base table that was modified.';
COMMENT ON COLUMN silva.results_audit_detail.column_name IS 'The name of the column that the data was modified in.';
COMMENT ON COLUMN silva.results_audit_detail.old_value IS 'The type of data that was modified. Would be one of, Character, Date or Number. This attribute will be used to determine which of the value fields contains the prior value of the element modified.';
COMMENT ON COLUMN silva.results_audit_detail.new_value IS 'If the data type was Character, the value of the field before the change.';
COMMENT ON COLUMN silva.results_audit_detail.entry_userid IS 'The userid of the person who changed the data.';
COMMENT ON COLUMN silva.results_audit_detail.entry_timestamp IS 'The date and time the data change occurred.';


-- silva.results_audit_event definition

-- Drop table

-- DROP TABLE silva.results_audit_event;

CREATE TABLE IF NOT EXISTS silva.results_audit_event (
	results_audit_event_id int8 NOT NULL, -- A unique system generated number to use as the key for the table.
	opening_id int8 NULL, -- System generated value uniquely identifying the opening.
	standards_regime_id int8 NULL, -- Unique identifier for the STANDARD REGIME entry.
	silviculture_project_id int8 NULL, -- A system generated key which uniquely identifies an occurrence of a Silviculture Project.
	results_audit_action_code varchar(3) NOT NULL, -- The action code is a code associated with the action. I, -- Insert
	action_date timestamp(0) NOT NULL, -- The date the action occurred that is being logged.
	description varchar(2000) NULL, -- Description of the action that occurred. IE: Opening ID 8736636 submitted, or Opening status changed from SUB to APP.
	user_id varchar(30) NULL, -- The user id were the email was sent.
	email_sent_ind varchar(1) NULL, -- Indicator used to indicate if email was sent or not for this audit event.
	xml_submission_id int8 NULL, -- The submission file ID that was given when the data was loaded.
	opening_amendment_number int4 NULL, -- A system generated value uniquely identifying an amendment.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	CONSTRAINT results_audit_event_pkey PRIMARY KEY (results_audit_event_id)
);
CREATE INDEX rae_o_fk_i ON silva.results_audit_event USING btree (opening_id, action_date, results_audit_action_code);
CREATE INDEX rae_proj_i ON silva.results_audit_event USING btree (silviculture_project_id);
CREATE INDEX rae_sr_fk_i ON silva.results_audit_event USING btree (standards_regime_id, action_date, results_audit_action_code);
COMMENT ON TABLE silva.results_audit_event IS 'Stores Audit Information such as openings submitted, activities submitted, changes in statuses, etc.';

-- Column comments

COMMENT ON COLUMN silva.results_audit_event.results_audit_event_id IS 'A unique system generated number to use as the key for the table.';
COMMENT ON COLUMN silva.results_audit_event.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.results_audit_event.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.results_audit_event.silviculture_project_id IS 'A system generated key which uniquely identifies an occurrence of a Silviculture Project.';
COMMENT ON COLUMN silva.results_audit_event.results_audit_action_code IS 'The action code is a code associated with the action. I -- Insert';
COMMENT ON COLUMN silva.results_audit_event.action_date IS 'The date the action occurred that is being logged.';
COMMENT ON COLUMN silva.results_audit_event.description IS 'Description of the action that occurred. IE: Opening ID 8736636 submitted, or Opening status changed from SUB to APP.';
COMMENT ON COLUMN silva.results_audit_event.user_id IS 'The user id were the email was sent.';
COMMENT ON COLUMN silva.results_audit_event.email_sent_ind IS 'Indicator used to indicate if email was sent or not for this audit event.';
COMMENT ON COLUMN silva.results_audit_event.xml_submission_id IS 'The submission file ID that was given when the data was loaded.';
COMMENT ON COLUMN silva.results_audit_event.opening_amendment_number IS 'A system generated value uniquely identifying an amendment.';
COMMENT ON COLUMN silva.results_audit_event.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.results_audit_event.entry_timestamp IS 'The date and time the information was entered.';


-- silva.silv_comment_xref definition

-- Drop table

-- DROP TABLE silva.silv_comment_xref;

CREATE TABLE IF NOT EXISTS silva.silv_comment_xref (
	silv_comment_source_code varchar(4) NOT NULL, -- Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.
	silv_comment_type_code varchar(8) NOT NULL, -- Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.
	comment_order_no int4 NOT NULL, -- Used to order comment type codes when displaying all types for a comment source code.
	CONSTRAINT silv_comment_xref_pkey PRIMARY KEY (silv_comment_type_code, silv_comment_source_code)
);
COMMENT ON TABLE silva.silv_comment_xref IS 'A cross reference entity that allow for the relationship between SILV CMT SOURCE CODE and SILV CMT TYPE CODE.';

-- Column comments

COMMENT ON COLUMN silva.silv_comment_xref.silv_comment_source_code IS 'Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.';
COMMENT ON COLUMN silva.silv_comment_xref.silv_comment_type_code IS 'Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.';
COMMENT ON COLUMN silva.silv_comment_xref.comment_order_no IS 'Used to order comment type codes when displaying all types for a comment source code.';


-- silva.silv_project_comment_link definition

-- Drop table

-- DROP TABLE silva.silv_project_comment_link;

CREATE TABLE IF NOT EXISTS silva.silv_project_comment_link (
	silviculture_project_id int8 NOT NULL, -- A system generated key which uniquely identifies an occurrence of a Silviculture Project.
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	CONSTRAINT silv_project_comment_link_pkey PRIMARY KEY (silviculture_project_id, silviculture_comment_id)
);

-- Column comments

COMMENT ON COLUMN silva.silv_project_comment_link.silviculture_project_id IS 'A system generated key which uniquely identifies an occurrence of a Silviculture Project.';
COMMENT ON COLUMN silva.silv_project_comment_link.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';


-- silva.silv_relief_application definition

-- Drop table

-- DROP TABLE silva.silv_relief_application;

CREATE TABLE IF NOT EXISTS silva.silv_relief_application (
	silv_relief_application_id int8 NOT NULL, -- Uniquely identifies an Application for Relief from obligations.
	silv_relief_application_no int2 NOT NULL, -- The sequetial number given to the relief application baed on the number of application submitted for the opening only.
	activity_treatment_unit_id int8 NOT NULL, -- System generated value uniquely identifying the Activity Treatment Unit.
	silv_relief_appl_status_code varchar(3) NOT NULL, -- Code identifying the status of the relief application.
	pre_event_forest_cover_date timestamp(0) NOT NULL, -- Most recent observation date describing the forest condition prior to the event.
	post_event_forest_cover_date timestamp(0) NOT NULL, -- Most recent observation date after the damaging event demonstrating the current forest condition leading to the relief request.
	amendment_opening_id int8 NULL, -- System generated value uniquely identifying the opening.
	opening_amendment_number int4 NULL, -- A system generated value uniquely identifying an amendment.
	obligation_relief_ind varchar(1) NOT NULL, -- Denotes whether this application is applying for full relief from obligations.
	obligation_met_ind varchar(1) NOT NULL, -- Indicates if the Obligation cannot be met without significant extra expense due to the damaging event.
	cause_damage_ind varchar(1) NOT NULL, -- Indicates if the Obligation holder did not cause or contribute to the cause of the damage.
	officially_induced_error_ind varchar(1) NOT NULL, -- Indicates if the Obligation holder contributed to the cause of the damage but only as a result of an officially induced error.
	due_diligence_ind varchar(1) NOT NULL, -- Indicates if the Obligation holder exercised due diligence in relation to the cause of the damage.
	authorization_checked_ind varchar(1) NOT NULL, -- Indicates if the submitter is authorized by the holder of these silviculture obligations to make this submission and confirms their appropriate professional designation to make this submission.
	submitted_date timestamp(0) NULL, -- The date and time the application was submitted.
	submitted_by_userid varchar(30) NULL, -- The userid of the individual who submitted the application.
	approved_date timestamp(0) NULL, -- The date and time the application was approved.
	approved_by_userid varchar(30) NULL, -- The userid of the individual who approved the application.
	reject_comment varchar(2000) NULL, -- If the application is rejected, a reason must be provided.
	entry_userid varchar(30) NOT NULL, -- The userid of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT silv_relief_application_pkey PRIMARY KEY (silv_relief_application_id)
);
CREATE INDEX sra_atu_fk_i ON silva.silv_relief_application USING btree (activity_treatment_unit_id);
CREATE INDEX sra_oah_fk_i ON silva.silv_relief_application USING btree (amendment_opening_id, opening_amendment_number);
CREATE INDEX sra_srascd_fk_i ON silva.silv_relief_application USING btree (silv_relief_appl_status_code);
COMMENT ON TABLE silva.silv_relief_application IS 'Application for relief from obligation or montary reembursement for damage done by natural disasters etc.';

-- Column comments

COMMENT ON COLUMN silva.silv_relief_application.silv_relief_application_id IS 'Uniquely identifies an Application for Relief from obligations.';
COMMENT ON COLUMN silva.silv_relief_application.silv_relief_application_no IS 'The sequetial number given to the relief application baed on the number of application submitted for the opening only.';
COMMENT ON COLUMN silva.silv_relief_application.activity_treatment_unit_id IS 'System generated value uniquely identifying the Activity Treatment Unit.';
COMMENT ON COLUMN silva.silv_relief_application.silv_relief_appl_status_code IS 'Code identifying the status of the relief application.';
COMMENT ON COLUMN silva.silv_relief_application.pre_event_forest_cover_date IS 'Most recent observation date describing the forest condition prior to the event.';
COMMENT ON COLUMN silva.silv_relief_application.post_event_forest_cover_date IS 'Most recent observation date after the damaging event demonstrating the current forest condition leading to the relief request.';
COMMENT ON COLUMN silva.silv_relief_application.amendment_opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.silv_relief_application.opening_amendment_number IS 'A system generated value uniquely identifying an amendment.';
COMMENT ON COLUMN silva.silv_relief_application.obligation_relief_ind IS 'Denotes whether this application is applying for full relief from obligations.';
COMMENT ON COLUMN silva.silv_relief_application.obligation_met_ind IS 'Indicates if the Obligation cannot be met without significant extra expense due to the damaging event.';
COMMENT ON COLUMN silva.silv_relief_application.cause_damage_ind IS 'Indicates if the Obligation holder did not cause or contribute to the cause of the damage.';
COMMENT ON COLUMN silva.silv_relief_application.officially_induced_error_ind IS 'Indicates if the Obligation holder contributed to the cause of the damage but only as a result of an officially induced error.';
COMMENT ON COLUMN silva.silv_relief_application.due_diligence_ind IS 'Indicates if the Obligation holder exercised due diligence in relation to the cause of the damage.';
COMMENT ON COLUMN silva.silv_relief_application.authorization_checked_ind IS 'Indicates if the submitter is authorized by the holder of these silviculture obligations to make this submission and confirms their appropriate professional designation to make this submission.';
COMMENT ON COLUMN silva.silv_relief_application.submitted_date IS 'The date and time the application was submitted.';
COMMENT ON COLUMN silva.silv_relief_application.submitted_by_userid IS 'The userid of the individual who submitted the application.';
COMMENT ON COLUMN silva.silv_relief_application.approved_date IS 'The date and time the application was approved.';
COMMENT ON COLUMN silva.silv_relief_application.approved_by_userid IS 'The userid of the individual who approved the application.';
COMMENT ON COLUMN silva.silv_relief_application.reject_comment IS 'If the application is rejected, a reason must be provided.';
COMMENT ON COLUMN silva.silv_relief_application.entry_userid IS 'The userid of the individual who entered the information.';
COMMENT ON COLUMN silva.silv_relief_application.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.silv_relief_application.update_userid IS 'The userid of the individual who last updated the information.';
COMMENT ON COLUMN silva.silv_relief_application.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.silv_relief_application.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.silviculture_comment definition

-- Drop table

-- DROP TABLE silva.silviculture_comment;

CREATE TABLE IF NOT EXISTS silva.silviculture_comment (
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	comment_date timestamp(0) NOT NULL, -- Date which the comment is created.
	silv_comment_source_code varchar(4) NOT NULL, -- Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.
	silv_comment_type_code varchar(8) NOT NULL, -- Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.
	comment_text varchar(2000) NOT NULL, -- A long, variable length text field for entering comment text.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The USERID of the individual who entered the information.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last update this information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT silviculture_comment_pkey PRIMARY KEY (silviculture_comment_id)
);
COMMENT ON TABLE silva.silviculture_comment IS 'Stores general comments for the ISIS system, and is attached to one of: Opening, ATU, Project.';

-- Column comments

COMMENT ON COLUMN silva.silviculture_comment.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';
COMMENT ON COLUMN silva.silviculture_comment.comment_date IS 'Date which the comment is created.';
COMMENT ON COLUMN silva.silviculture_comment.silv_comment_source_code IS 'Code indicating source of comment. Examples include but are not limited to: OPEN, PHSP, RSLT, PLAN.';
COMMENT ON COLUMN silva.silviculture_comment.silv_comment_type_code IS 'Code indicating type of comment. Examples include but are not limited to: MGMNTOBJ, RECREATN, LNDSCAPE, WILDLIFE, FISHWATR, FIRE, SP, SURVEY.';
COMMENT ON COLUMN silva.silviculture_comment.comment_text IS 'A long, variable length text field for entering comment text.';
COMMENT ON COLUMN silva.silviculture_comment.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.silviculture_comment.entry_timestamp IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.silviculture_comment.update_userid IS 'The USERID of the individual who last update this information.';
COMMENT ON COLUMN silva.silviculture_comment.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.silviculture_comment.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.silviculture_project definition

-- Drop table

-- DROP TABLE silva.silviculture_project;

CREATE TABLE IF NOT EXISTS silva.silviculture_project (
	silviculture_project_id int8 NOT NULL, -- A system generated key which uniquely identifies an occurrence of a Silviculture Project.
	silv_base_code varchar(2) NOT NULL, -- Identifies a primary category of Silviculture Activity.
	fiscal_year int2 NOT NULL, -- The fiscal year in which the project commenced, or, in the case of a planned project, is expected to commence. A component of the concatenated business key which consists of: SILV BASE CODE FISCAL YEAR ORG UNIT CODE PROJECT SEQUENCE e.g. PL2006DCR035A
	org_unit_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	project_sequence varchar(4) NOT NULL, -- A one to four character identifier that is meaningful to the user and assists in distinguishing one Project from another within the same SILV BASE CODE, FISCAL YEAR, ORG UNIT CODE. A component of the concatenated business key which consists of: SILV BASE CODE FISCAL YEAR ORG UNIT CODE PROJECT SEQUENCE e.g. PL2006DCR035A
	project_location varchar(30) NULL, -- A comment regarding the general location of the project.
	silv_project_status_code varchar(3) NOT NULL, -- A code to indicate the current status of a SILVICULTURE PROJECT.
	client_number varchar(8) NULL, -- Sequentially assigned number to identify a ministry client.
	start_date timestamp(0) NOT NULL, -- The date that the project actually commenced, or, in the case of a planned project, the date it is currently scheduled to commence.
	view_date timestamp(0) NULL, -- The planned or actual date of viewing the contract particulars.
	coordinator_userid varchar(30) NULL, -- The userid of the contract coordinator for the project.
	overhead_cost numeric(9, 2) NULL, -- The planned or actual total overhead costs associated with the project.
	crew_contract_hire_code varchar(1) NULL, -- A code to indicate the tendering process used to select a contractor.
	dist_admin_zone varchar(2) NULL, -- The administrative zone for a district.
	unit_bid_code varchar(3) NOT NULL, -- A code to denote the unit basis on which work was tendered (e.g. days, hectares). Subset of UNIT OF MEASURE CODE.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record has been modified. Used in the web page access to determine if the record has been modified since the data was first retrieved.
	CONSTRAINT silviculture_project_pkey PRIMARY KEY (silviculture_project_id)
);
CREATE INDEX proj_cchcd_fk_i ON silva.silviculture_project USING btree (crew_contract_hire_code);
CREATE INDEX proj_fc2_fk_i ON silva.silviculture_project USING btree (client_number);
CREATE INDEX proj_pscd_fk_i ON silva.silviculture_project USING btree (silv_project_status_code);
CREATE INDEX proj_saz_fk_i ON silva.silviculture_project USING btree (org_unit_no, dist_admin_zone);
CREATE INDEX proj_sbc_fk_i ON silva.silviculture_project USING btree (silv_base_code);
CREATE INDEX proj_ubcd_fk_i ON silva.silviculture_project USING btree (unit_bid_code);
COMMENT ON TABLE silva.silviculture_project IS 'A administrative instrument that allows Ministry staff to plan and execute contracts for silviculture treatments. Collects the general project information recorded during Planting, Site Preparation, Pruning and Survey activities';

-- Column comments

COMMENT ON COLUMN silva.silviculture_project.silviculture_project_id IS 'A system generated key which uniquely identifies an occurrence of a Silviculture Project.';
COMMENT ON COLUMN silva.silviculture_project.silv_base_code IS 'Identifies a primary category of Silviculture Activity.';
COMMENT ON COLUMN silva.silviculture_project.fiscal_year IS 'The fiscal year in which the project commenced, or, in the case of a planned project, is expected to commence. A component of the concatenated business key which consists of: SILV BASE CODE FISCAL YEAR ORG UNIT CODE PROJECT SEQUENCE e.g. PL2006DCR035A';
COMMENT ON COLUMN silva.silviculture_project.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.silviculture_project.project_sequence IS 'A one to four character identifier that is meaningful to the user and assists in distinguishing one Project from another within the same SILV BASE CODE, FISCAL YEAR, ORG UNIT CODE. A component of the concatenated business key which consists of: SILV BASE CODE FISCAL YEAR ORG UNIT CODE PROJECT SEQUENCE e.g. PL2006DCR035A';
COMMENT ON COLUMN silva.silviculture_project.project_location IS 'A comment regarding the general location of the project.';
COMMENT ON COLUMN silva.silviculture_project.silv_project_status_code IS 'A code to indicate the current status of a SILVICULTURE PROJECT.';
COMMENT ON COLUMN silva.silviculture_project.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.silviculture_project.start_date IS 'The date that the project actually commenced, or, in the case of a planned project, the date it is currently scheduled to commence.';
COMMENT ON COLUMN silva.silviculture_project.view_date IS 'The planned or actual date of viewing the contract particulars.';
COMMENT ON COLUMN silva.silviculture_project.coordinator_userid IS 'The userid of the contract coordinator for the project.';
COMMENT ON COLUMN silva.silviculture_project.overhead_cost IS 'The planned or actual total overhead costs associated with the project.';
COMMENT ON COLUMN silva.silviculture_project.crew_contract_hire_code IS 'A code to indicate the tendering process used to select a contractor.';
COMMENT ON COLUMN silva.silviculture_project.dist_admin_zone IS 'The administrative zone for a district.';
COMMENT ON COLUMN silva.silviculture_project.unit_bid_code IS 'A code to denote the unit basis on which work was tendered (e.g. days, hectares). Subset of UNIT OF MEASURE CODE.';
COMMENT ON COLUMN silva.silviculture_project.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.silviculture_project.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.silviculture_project.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.silviculture_project.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.silviculture_project.revision_count IS 'A counter identifying the number of times this record has been modified. Used in the web page access to determine if the record has been modified since the data was first retrieved.';


-- silva.standards_regime definition

-- Drop table

-- DROP TABLE silva.standards_regime;

CREATE TABLE IF NOT EXISTS silva.standards_regime (
	standards_regime_id int8 NOT NULL, -- Unique identifier for the STANDARD REGIME entry.
	standards_regime_name varchar(50) NULL, -- A descriptive name of the type of STANDARD being tracked.
	standards_regime_status_code varchar(3) NOT NULL, -- The status of the STANDARD REGIME. Starts out as submitted and must be approved by an authorized resource prior to being used for a submission.
	silv_statute_code varchar(3) NULL, -- A legislative statute based on a current Act, Regulation or policy
	standards_objective varchar(50) NULL, -- Describes the objective of the standards.
	geographic_description varchar(50) NULL, -- A description of the georgraphic area for which standard are being set.
	mof_default_standard_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether the Stocking Standard was created by District, Region, or Headquarters staff.
	regen_delay_offset_yrs int2 NULL, -- The number of years allowed for the regeneration of tree growth.
	regen_obligation_ind varchar(1) NOT NULL, -- A yes/no indicator indicating if this standard is for a regeneration obligation..
	no_regen_early_offset_yrs int2 NULL, -- The minimum or early regen number of years.
	no_regen_late_offset_yrs int2 NULL, -- The maximum or late years of regen growth.
	free_growing_early_offset_yrs int2 NULL, -- The minimum or early growth number of years.
	free_growing_late_offset_yrs int2 NULL, -- The maximum or late years of tree growth.
	approved_by_userid varchar(30) NULL, -- The userid of the resource that approved the standard regime definition.
	approved_date timestamp(0) NULL, -- The date the standard regime definition was approved.
	submitted_by_userid varchar(30) NULL, -- The userid of the resource that submitted the standard regime definition.
	submitted_date timestamp(0) NULL, -- The date and time the standard regime definition was submitted.
	effective_date timestamp(0) NULL, -- The date upon which this standard goes into effect.
	expiry_date timestamp(0) NULL, -- The date this standard is no longer available for use by new openings.
	additional_standards varchar(4000) NULL, -- A free-format text field to allow for the definition of additional standards.
	reject_note varchar(2000) NULL, -- A free-format text field allowing for the documentation of why a submitted standard was rejected.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT standards_regime_pkey PRIMARY KEY (standards_regime_id)
);
CREATE INDEX sr_srsc_fk_i ON silva.standards_regime USING btree (standards_regime_status_code);
CREATE INDEX sr_sstcd_fk_i ON silva.standards_regime USING btree (silv_statute_code);

-- Column comments

COMMENT ON COLUMN silva.standards_regime.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.standards_regime.standards_regime_name IS 'A descriptive name of the type of STANDARD being tracked.';
COMMENT ON COLUMN silva.standards_regime.standards_regime_status_code IS 'The status of the STANDARD REGIME. Starts out as submitted and must be approved by an authorized resource prior to being used for a submission.';
COMMENT ON COLUMN silva.standards_regime.silv_statute_code IS 'A legislative statute based on a current Act, Regulation or policy';
COMMENT ON COLUMN silva.standards_regime.standards_objective IS 'Describes the objective of the standards.';
COMMENT ON COLUMN silva.standards_regime.geographic_description IS 'A description of the georgraphic area for which standard are being set.';
COMMENT ON COLUMN silva.standards_regime.mof_default_standard_ind IS 'Indicates whether the Stocking Standard was created by District, Region, or Headquarters staff.';
COMMENT ON COLUMN silva.standards_regime.regen_delay_offset_yrs IS 'The number of years allowed for the regeneration of tree growth.';
COMMENT ON COLUMN silva.standards_regime.regen_obligation_ind IS 'A yes/no indicator indicating if this standard is for a regeneration obligation..';
COMMENT ON COLUMN silva.standards_regime.no_regen_early_offset_yrs IS 'The minimum or early regen number of years.';
COMMENT ON COLUMN silva.standards_regime.no_regen_late_offset_yrs IS 'The maximum or late years of regen growth.';
COMMENT ON COLUMN silva.standards_regime.free_growing_early_offset_yrs IS 'The minimum or early growth number of years.';
COMMENT ON COLUMN silva.standards_regime.free_growing_late_offset_yrs IS 'The maximum or late years of tree growth.';
COMMENT ON COLUMN silva.standards_regime.approved_by_userid IS 'The userid of the resource that approved the standard regime definition.';
COMMENT ON COLUMN silva.standards_regime.approved_date IS 'The date the standard regime definition was approved.';
COMMENT ON COLUMN silva.standards_regime.submitted_by_userid IS 'The userid of the resource that submitted the standard regime definition.';
COMMENT ON COLUMN silva.standards_regime.submitted_date IS 'The date and time the standard regime definition was submitted.';
COMMENT ON COLUMN silva.standards_regime.effective_date IS 'The date upon which this standard goes into effect.';
COMMENT ON COLUMN silva.standards_regime.expiry_date IS 'The date this standard is no longer available for use by new openings.';
COMMENT ON COLUMN silva.standards_regime.additional_standards IS 'A free-format text field to allow for the definition of additional standards.';
COMMENT ON COLUMN silva.standards_regime.reject_note IS 'A free-format text field allowing for the documentation of why a submitted standard was rejected.';
COMMENT ON COLUMN silva.standards_regime.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.standards_regime.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.standards_regime.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.standards_regime.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.standards_regime.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.stocking_comment_link definition

-- Drop table

-- DROP TABLE silva.stocking_comment_link;

CREATE TABLE IF NOT EXISTS silva.stocking_comment_link (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	CONSTRAINT stocking_comment_link_pkey PRIMARY KEY (stocking_standard_unit_id, silviculture_comment_id)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_comment_link.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_comment_link.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';


-- silva.stocking_ecology definition

-- Drop table

-- DROP TABLE silva.stocking_ecology;

CREATE TABLE IF NOT EXISTS silva.stocking_ecology (
	stocking_ecology_id int8 NOT NULL, -- System generated value uniquely identifying the SU Ecology (Eco Strata).
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	bec_region_code varchar(3) NULL, -- A code identifying the biogeoclimatic region.
	bgc_zone_code varchar(4) NULL, -- A large geographic area with a broadly homogeneous macroclimate which influences the development of vegetation and soil.
	bgc_subzone_code varchar(3) NULL, -- A code for the bio-geoclimatic sub-zone for the area.
	bgc_variant varchar(1) NULL, -- A code for the biogeoclimatic variant. A division of the BGC Subzone on the basis of differences in floristic composition of the zonal ecosystem, but usually on the basis of differences in the cover and vigour of the plant species.
	bgc_phase varchar(1) NULL, -- A code for the biogeoclimatic phase. Accomodates the variation, resulting from local relief, in the regional climate of the subzones and variants.
	bec_site_series varchar(4) NULL, -- A code for the biogeoclimatic site series. Site series is the consideration of all ecosystems capable of producing vegetation belonging to the same plant association at climax. A code for a site series which is defined as all land areas capable of producing vegetation belonging to the same plant association (or sometimes, subassociation) within a biogeoclimatic subzone or variant. Two-digit numeric codes are usually forested site series; four-character alphnumeric codes are non-forested site series including wetlands, grasslands, talus, rock outcrops, etc. Any two-digit numeric codes for non-forested site series will be migrated to the alphanumeric codes whenever field guides are updated.
	bec_site_type varchar(3) NULL, -- A code for the biogeoclimatic site type. Site type is the partitionment of the site series according to one or more critical site factors thought to affect ecosystem response to management treatments.
	bec_seral varchar(4) NULL, -- The seral (often termed successional) classification in BEC is an integration of site and vegetation classifications with structural stage development.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT stocking_ecology_pkey PRIMARY KEY (stocking_ecology_id),
	CONSTRAINT stocking_ecology_stocking_standard_unit_id_key UNIQUE (stocking_standard_unit_id)
);
CREATE INDEX se_o_fk_i ON silva.stocking_ecology USING btree (opening_id);
COMMENT ON TABLE silva.stocking_ecology IS 'Primarily a classification of the land base in terms of biogeoclimatic and physical factors.';

-- Column comments

COMMENT ON COLUMN silva.stocking_ecology.stocking_ecology_id IS 'System generated value uniquely identifying the SU Ecology (Eco Strata).';
COMMENT ON COLUMN silva.stocking_ecology.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_ecology.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_ecology.bec_region_code IS 'A code identifying the biogeoclimatic region.';
COMMENT ON COLUMN silva.stocking_ecology.bgc_zone_code IS 'A large geographic area with a broadly homogeneous macroclimate which influences the development of vegetation and soil.';
COMMENT ON COLUMN silva.stocking_ecology.bgc_subzone_code IS 'A code for the bio-geoclimatic sub-zone for the area.';
COMMENT ON COLUMN silva.stocking_ecology.bgc_variant IS 'A code for the biogeoclimatic variant. A division of the BGC Subzone on the basis of differences in floristic composition of the zonal ecosystem, but usually on the basis of differences in the cover and vigour of the plant species.';
COMMENT ON COLUMN silva.stocking_ecology.bgc_phase IS 'A code for the biogeoclimatic phase. Accomodates the variation, resulting from local relief, in the regional climate of the subzones and variants.';
COMMENT ON COLUMN silva.stocking_ecology.bec_site_series IS 'A code for the biogeoclimatic site series. Site series is the consideration of all ecosystems capable of producing vegetation belonging to the same plant association at climax. A code for a site series which is defined as all land areas capable of producing vegetation belonging to the same plant association (or sometimes, subassociation) within a biogeoclimatic subzone or variant. Two-digit numeric codes are usually forested site series; four-character alphnumeric codes are non-forested site series including wetlands, grasslands, talus, rock outcrops, etc. Any two-digit numeric codes for non-forested site series will be migrated to the alphanumeric codes whenever field guides are updated.';
COMMENT ON COLUMN silva.stocking_ecology.bec_site_type IS 'A code for the biogeoclimatic site type. Site type is the partitionment of the site series according to one or more critical site factors thought to affect ecosystem response to management treatments.';
COMMENT ON COLUMN silva.stocking_ecology.bec_seral IS 'The seral (often termed successional) classification in BEC is an integration of site and vegetation classifications with structural stage development.';
COMMENT ON COLUMN silva.stocking_ecology.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_ecology.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_ecology.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_ecology.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_ecology.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.stocking_ecology_archive definition

-- Drop table

-- DROP TABLE silva.stocking_ecology_archive;

CREATE TABLE IF NOT EXISTS silva.stocking_ecology_archive (
	stocking_ecology_id int8 NOT NULL, -- System generated value uniquely identifying the SU Ecology (Eco Strata).
	stocking_event_history_id int8 NOT NULL, -- Unique identifier for a stocking standard amendment event
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	opening_id int8 NOT NULL, -- Opening id of opening this archive record was associated with.
	bec_region_code varchar(3) NULL, -- A code identifying the biogeoclimatic region.
	bgc_zone_code varchar(4) NULL, -- A large geographic area with a broadly homogeneous macroclimate which influences the development of vegetation and soil.
	bgc_subzone_code varchar(3) NULL, -- A code for the bio-geoclimatic sub-zone for the area.
	bgc_variant varchar(1) NULL, -- A code for the biogeoclimatic variant. A division of the BGC Subzone on the basis of differences in floristic composition of the zonal ecosystem, but usually on the basis of differences in the cover and vigour of the plant species.
	bgc_phase varchar(1) NULL, -- A code for the biogeoclimatic phase. Accomodates the variation, resulting from local relief, in the regional climate of the subzones and variants.
	bec_site_series varchar(4) NULL, -- A code for the biogeoclimatic site series. Site series is the consideration of all ecosystems capable of producing vegetation belonging to the same plant association at climax. A code for a site series which is defined as all land areas capable of producing vegetation belonging to the same plant association (or sometimes, subassociation) within a biogeoclimatic subzone or variant. Two-digit numeric codes are usually forested site series; four-character alphnumeric codes are non-forested site series including wetlands, grasslands, talus, rock outcrops, etc. Any two-digit numeric codes for non-forested site series will be migrated to the alphanumeric codes whenever field guides are updated.
	bec_site_type varchar(3) NULL, -- A code for the biogeoclimatic site type. Site type is the partitionment of the site series according to one or more critical site factors thought to affect ecosystem response to management treatments.
	bec_seral varchar(4) NULL, -- The seral (often termed successional) classification in BEC is an integration of site and vegetation classifications with structural stage development.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	CONSTRAINT stocking_ecology_archive_pkey PRIMARY KEY (stocking_ecology_id, stocking_event_history_id, stocking_standard_unit_id)
);
CREATE INDEX searc_ssuarc_fk_i ON silva.stocking_ecology_archive USING btree (stocking_event_history_id, stocking_standard_unit_id);
COMMENT ON TABLE silva.stocking_ecology_archive IS 'Contains archived geometry information for a Stocking Standard amendment.';

-- Column comments

COMMENT ON COLUMN silva.stocking_ecology_archive.stocking_ecology_id IS 'System generated value uniquely identifying the SU Ecology (Eco Strata).';
COMMENT ON COLUMN silva.stocking_ecology_archive.stocking_event_history_id IS 'Unique identifier for a stocking standard amendment event';
COMMENT ON COLUMN silva.stocking_ecology_archive.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_ecology_archive.opening_id IS 'Opening id of opening this archive record was associated with.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bec_region_code IS 'A code identifying the biogeoclimatic region.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bgc_zone_code IS 'A large geographic area with a broadly homogeneous macroclimate which influences the development of vegetation and soil.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bgc_subzone_code IS 'A code for the bio-geoclimatic sub-zone for the area.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bgc_variant IS 'A code for the biogeoclimatic variant. A division of the BGC Subzone on the basis of differences in floristic composition of the zonal ecosystem, but usually on the basis of differences in the cover and vigour of the plant species.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bgc_phase IS 'A code for the biogeoclimatic phase. Accomodates the variation, resulting from local relief, in the regional climate of the subzones and variants.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bec_site_series IS 'A code for the biogeoclimatic site series. Site series is the consideration of all ecosystems capable of producing vegetation belonging to the same plant association at climax. A code for a site series which is defined as all land areas capable of producing vegetation belonging to the same plant association (or sometimes, subassociation) within a biogeoclimatic subzone or variant. Two-digit numeric codes are usually forested site series; four-character alphnumeric codes are non-forested site series including wetlands, grasslands, talus, rock outcrops, etc. Any two-digit numeric codes for non-forested site series will be migrated to the alphanumeric codes whenever field guides are updated.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bec_site_type IS 'A code for the biogeoclimatic site type. Site type is the partitionment of the site series according to one or more critical site factors thought to affect ecosystem response to management treatments.';
COMMENT ON COLUMN silva.stocking_ecology_archive.bec_seral IS 'The seral (often termed successional) classification in BEC is an integration of site and vegetation classifications with structural stage development.';
COMMENT ON COLUMN silva.stocking_ecology_archive.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';
COMMENT ON COLUMN silva.stocking_ecology_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_ecology_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_ecology_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_ecology_archive.update_timestamp IS 'The date and time of the last update.';


-- silva.stocking_event_history definition

-- Drop table

-- DROP TABLE silva.stocking_event_history;

CREATE TABLE IF NOT EXISTS silva.stocking_event_history (
	stocking_event_history_id int8 NOT NULL, -- Unique identifier for a stocking standard amendment event
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	opening_amendment_id int8 NULL, -- System generated value uniquely identifying the opening.
	opening_amendment_number int4 NULL, -- A system generated value uniquely identifying an amendment.
	results_audit_action_code varchar(3) NOT NULL, -- The action code is a code associated with the action. I, -- Insert
	submitted_userid varchar(30) NULL, -- The USERID of the person who submitted the amendment.
	results_submission_id int8 NULL, -- ESF submission ID
	amend_event_timestamp timestamp(0) NOT NULL, -- The date and time the amendment was made
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT stocking_event_history_pkey PRIMARY KEY (stocking_event_history_id)
);
CREATE INDEX seh_o_fk_i ON silva.stocking_event_history USING btree (opening_id);
CREATE INDEX seh_oah_fk_i ON silva.stocking_event_history USING btree (opening_amendment_id, opening_amendment_number);
COMMENT ON TABLE silva.stocking_event_history IS 'Records amendments to stocking standards';

-- Column comments

COMMENT ON COLUMN silva.stocking_event_history.stocking_event_history_id IS 'Unique identifier for a stocking standard amendment event';
COMMENT ON COLUMN silva.stocking_event_history.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_event_history.opening_amendment_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_event_history.opening_amendment_number IS 'A system generated value uniquely identifying an amendment.';
COMMENT ON COLUMN silva.stocking_event_history.results_audit_action_code IS 'The action code is a code associated with the action. I -- Insert';
COMMENT ON COLUMN silva.stocking_event_history.submitted_userid IS 'The USERID of the person who submitted the amendment.';
COMMENT ON COLUMN silva.stocking_event_history.results_submission_id IS 'ESF submission ID';
COMMENT ON COLUMN silva.stocking_event_history.amend_event_timestamp IS 'The date and time the amendment was made';
COMMENT ON COLUMN silva.stocking_event_history.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_event_history.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_event_history.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.stocking_layer definition

-- Drop table

-- DROP TABLE silva.stocking_layer;

CREATE TABLE IF NOT EXISTS silva.stocking_layer (
	stocking_layer_id int8 NOT NULL, -- The unique identifier for the STOCKING LAYER
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	stocking_layer_code varchar(2) NOT NULL, -- A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.
	target_stocking int4 NULL, -- The number of well spaced (preferred or acceptable) trees per hectare that will, in normal circumstances, produce an optimal FG crop. Target stocking standards are the standards which silviculture activities are aimed at achieving.
	residual_basal_area int4 NULL, -- The residual basal area to be left after harvesting in m2/ha. Understood to refer to the mature layer only.
	min_horizontal_distance numeric(3, 1) NULL, -- Te minimum allowable horizontal distance in metres between trees of the preferred and acceptable species, which is required to be considered to be well-spaced.
	min_pref_stocking_standard int4 NULL, -- The minimum number of well spaced trees per hectare, of preferred species only, that must be present for the standards unit to be considered SR or FG.
	min_post_spacing int8 NULL, -- The minimum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.
	min_stocking_standard int4 NULL, -- The minimum number of well spaced trees per hectare (of preferred and acceptable species) that must be present for the standards unit to be considered SR or FG.
	max_post_spacing int8 NULL, -- The maximum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.
	max_conifer int8 NULL, -- The maximum allowable stand density of total countable conifers in order to achieve free growing status.
	hght_relative_to_comp int4 NULL, -- The height of the tree relative to competing vegetation, within a radius of one metre of the tree trunk, that must be met by a healthy well spaced tree of a preferred or acceptable species at FG.
	tree_size_unit_code varchar(3) NULL, -- The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT stocking_layer_pkey PRIMARY KEY (stocking_layer_id)
);
CREATE INDEX sl_o_fk_i ON silva.stocking_layer USING btree (opening_id);
CREATE INDEX sl_ssu_fk_i ON silva.stocking_layer USING btree (stocking_standard_unit_id);
COMMENT ON TABLE silva.stocking_layer IS 'Details the standards unit layer information.';

-- Column comments

COMMENT ON COLUMN silva.stocking_layer.stocking_layer_id IS 'The unique identifier for the STOCKING LAYER';
COMMENT ON COLUMN silva.stocking_layer.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_layer.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_layer.stocking_layer_code IS 'A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.';
COMMENT ON COLUMN silva.stocking_layer.target_stocking IS 'The number of well spaced (preferred or acceptable) trees per hectare that will, in normal circumstances, produce an optimal FG crop. Target stocking standards are the standards which silviculture activities are aimed at achieving.';
COMMENT ON COLUMN silva.stocking_layer.residual_basal_area IS 'The residual basal area to be left after harvesting in m2/ha. Understood to refer to the mature layer only.';
COMMENT ON COLUMN silva.stocking_layer.min_horizontal_distance IS 'Te minimum allowable horizontal distance in metres between trees of the preferred and acceptable species, which is required to be considered to be well-spaced.';
COMMENT ON COLUMN silva.stocking_layer.min_pref_stocking_standard IS 'The minimum number of well spaced trees per hectare, of preferred species only, that must be present for the standards unit to be considered SR or FG.';
COMMENT ON COLUMN silva.stocking_layer.min_post_spacing IS 'The minimum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.';
COMMENT ON COLUMN silva.stocking_layer.min_stocking_standard IS 'The minimum number of well spaced trees per hectare (of preferred and acceptable species) that must be present for the standards unit to be considered SR or FG.';
COMMENT ON COLUMN silva.stocking_layer.max_post_spacing IS 'The maximum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.';
COMMENT ON COLUMN silva.stocking_layer.max_conifer IS 'The maximum allowable stand density of total countable conifers in order to achieve free growing status.';
COMMENT ON COLUMN silva.stocking_layer.hght_relative_to_comp IS 'The height of the tree relative to competing vegetation, within a radius of one metre of the tree trunk, that must be met by a healthy well spaced tree of a preferred or acceptable species at FG.';
COMMENT ON COLUMN silva.stocking_layer.tree_size_unit_code IS 'The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).';
COMMENT ON COLUMN silva.stocking_layer.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_layer.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_layer.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_layer.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_layer.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.stocking_layer_archive definition

-- Drop table

-- DROP TABLE silva.stocking_layer_archive;

CREATE TABLE IF NOT EXISTS silva.stocking_layer_archive (
	stocking_layer_id int8 NOT NULL, -- The unique identifier for the STOCKING LAYER
	stocking_event_history_id int8 NOT NULL, -- Unique identifier for a stocking standard amendment event
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	opening_id int8 NOT NULL, -- Opening id of opening this archive record was associated with.
	stocking_layer_code varchar(2) NOT NULL, -- A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.
	min_horizontal_distance numeric(3, 1) NULL, -- Te minimum allowable horizontal distance in metres between trees of the preferred and acceptable species, which is required to be considered to be well-spaced.
	min_pref_stocking_standard int4 NULL, -- The minimum number of well spaced trees per hectare, of preferred species only, that must be present for the standards unit to be considered SR or FG.
	min_stocking_standard int4 NULL, -- The minimum number of well spaced trees per hectare (of preferred and acceptable species) that must be present for the standards unit to be considered SR or FG.
	min_post_spacing int8 NULL, -- The minimum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.
	residual_basal_area int4 NULL, -- The residual basal area to be left after harvesting in m2/ha. Understood to refer to the mature layer only.
	target_stocking int4 NULL, -- The number of well spaced (preferred or acceptable) trees per hectare that will, in normal circumstances, produce an optimal FG crop. Target stocking standards are the standards which silviculture activities are aimed at achieving.
	hght_relative_to_comp int4 NULL, -- The height of the tree relative to competing vegetation, within a radius of one metre of the tree trunk, that must be met by a healthy well spaced tree of a preferred or acceptable species at FG.
	tree_size_unit_code varchar(3) NULL, -- The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).
	max_conifer int8 NULL, -- The maximum allowable stand density of total countable conifers in order to achieve free growing status.
	max_post_spacing int8 NULL, -- The maximum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT stocking_layer_archive_pkey PRIMARY KEY (stocking_layer_id, stocking_event_history_id, stocking_standard_unit_id)
);
CREATE INDEX slarc_ssuarc_fk_i ON silva.stocking_layer_archive USING btree (stocking_event_history_id, stocking_standard_unit_id);
COMMENT ON TABLE silva.stocking_layer_archive IS 'Contains archived layer information for a Stocking Standard amendment.';

-- Column comments

COMMENT ON COLUMN silva.stocking_layer_archive.stocking_layer_id IS 'The unique identifier for the STOCKING LAYER';
COMMENT ON COLUMN silva.stocking_layer_archive.stocking_event_history_id IS 'Unique identifier for a stocking standard amendment event';
COMMENT ON COLUMN silva.stocking_layer_archive.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_layer_archive.opening_id IS 'Opening id of opening this archive record was associated with.';
COMMENT ON COLUMN silva.stocking_layer_archive.stocking_layer_code IS 'A code that identifies the forested layer within the Standards Unit to which the preferred/acceptable species information relates.';
COMMENT ON COLUMN silva.stocking_layer_archive.min_horizontal_distance IS 'Te minimum allowable horizontal distance in metres between trees of the preferred and acceptable species, which is required to be considered to be well-spaced.';
COMMENT ON COLUMN silva.stocking_layer_archive.min_pref_stocking_standard IS 'The minimum number of well spaced trees per hectare, of preferred species only, that must be present for the standards unit to be considered SR or FG.';
COMMENT ON COLUMN silva.stocking_layer_archive.min_stocking_standard IS 'The minimum number of well spaced trees per hectare (of preferred and acceptable species) that must be present for the standards unit to be considered SR or FG.';
COMMENT ON COLUMN silva.stocking_layer_archive.min_post_spacing IS 'The minimum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.';
COMMENT ON COLUMN silva.stocking_layer_archive.residual_basal_area IS 'The residual basal area to be left after harvesting in m2/ha. Understood to refer to the mature layer only.';
COMMENT ON COLUMN silva.stocking_layer_archive.target_stocking IS 'The number of well spaced (preferred or acceptable) trees per hectare that will, in normal circumstances, produce an optimal FG crop. Target stocking standards are the standards which silviculture activities are aimed at achieving.';
COMMENT ON COLUMN silva.stocking_layer_archive.hght_relative_to_comp IS 'The height of the tree relative to competing vegetation, within a radius of one metre of the tree trunk, that must be met by a healthy well spaced tree of a preferred or acceptable species at FG.';
COMMENT ON COLUMN silva.stocking_layer_archive.tree_size_unit_code IS 'The unit of measure used for height relative to competition. Expressed either in number of centimetres above vegetation (cm), or percentage above vegetation (%).';
COMMENT ON COLUMN silva.stocking_layer_archive.max_conifer IS 'The maximum allowable stand density of total countable conifers in order to achieve free growing status.';
COMMENT ON COLUMN silva.stocking_layer_archive.max_post_spacing IS 'The maximum number of well spaced coniferous trees per hectare to be retained following maximum density spacing.';
COMMENT ON COLUMN silva.stocking_layer_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_layer_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_layer_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_layer_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_layer_archive.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.stocking_layer_species definition

-- Drop table

-- DROP TABLE silva.stocking_layer_species;

CREATE TABLE IF NOT EXISTS silva.stocking_layer_species (
	stocking_layer_id int8 NOT NULL, -- The unique identifier for the STOCKING LAYER
	silv_tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	species_order int4 NOT NULL, -- The order in which the species should be stocked within the STOCKING LAYER.
	preferred_ind varchar(1) NOT NULL, -- A yes/no indication of whether this is the Preferred or Acceptable Tree Species.
	min_height numeric(3, 1) NULL, -- The minimum height that a healthy well spaced tree must be, to be considered free growing. Minimum heights vary by species and by site series.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT stocking_layer_species_pkey PRIMARY KEY (stocking_layer_id, silv_tree_species_code)
);
COMMENT ON TABLE silva.stocking_layer_species IS 'The species of trees and vegetation included in the STOCKING LAYER';

-- Column comments

COMMENT ON COLUMN silva.stocking_layer_species.stocking_layer_id IS 'The unique identifier for the STOCKING LAYER';
COMMENT ON COLUMN silva.stocking_layer_species.silv_tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.stocking_layer_species.species_order IS 'The order in which the species should be stocked within the STOCKING LAYER.';
COMMENT ON COLUMN silva.stocking_layer_species.preferred_ind IS 'A yes/no indication of whether this is the Preferred or Acceptable Tree Species.';
COMMENT ON COLUMN silva.stocking_layer_species.min_height IS 'The minimum height that a healthy well spaced tree must be, to be considered free growing. Minimum heights vary by species and by site series.';
COMMENT ON COLUMN silva.stocking_layer_species.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_layer_species.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_layer_species.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_layer_species.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_layer_species.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.stocking_layer_species_archive definition

-- Drop table

-- DROP TABLE silva.stocking_layer_species_archive;

CREATE TABLE IF NOT EXISTS silva.stocking_layer_species_archive (
	stocking_layer_id int8 NOT NULL, -- The unique identifier for the STOCKING LAYER
	stocking_event_history_id int8 NOT NULL, -- Unique identifier for a stocking standard amendment event
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	species_order int4 NOT NULL, -- The order in which the species should be stocked within the STOCKING LAYER.
	silv_tree_species_code varchar(8) NOT NULL, -- The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority
	preferred_ind varchar(1) NOT NULL, -- A yes/no indication of whether this is the Preferred or Acceptable Tree Species.
	min_height numeric(3, 1) NULL, -- The minimum height that a healthy well spaced tree must be, to be considered free growing. Minimum heights vary by species and by site series.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.
	CONSTRAINT stocking_layer_species_archive_pkey PRIMARY KEY (silv_tree_species_code, stocking_layer_id, stocking_event_history_id, stocking_standard_unit_id)
);
CREATE INDEX slsarc_slarc_fk_i ON silva.stocking_layer_species_archive USING btree (stocking_layer_id, stocking_event_history_id, stocking_standard_unit_id);
COMMENT ON TABLE silva.stocking_layer_species_archive IS 'Contains archived layer species information for a Stocking Standard amendment.';

-- Column comments

COMMENT ON COLUMN silva.stocking_layer_species_archive.stocking_layer_id IS 'The unique identifier for the STOCKING LAYER';
COMMENT ON COLUMN silva.stocking_layer_species_archive.stocking_event_history_id IS 'Unique identifier for a stocking standard amendment event';
COMMENT ON COLUMN silva.stocking_layer_species_archive.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.species_order IS 'The order in which the species should be stocked within the STOCKING LAYER.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.silv_tree_species_code IS 'The secondary tree species of Crown timber that was cut, damaged, destroyed or removed without authority';
COMMENT ON COLUMN silva.stocking_layer_species_archive.preferred_ind IS 'A yes/no indication of whether this is the Preferred or Acceptable Tree Species.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.min_height IS 'The minimum height that a healthy well spaced tree must be, to be considered free growing. Minimum heights vary by species and by site series.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_layer_species_archive.revision_count IS 'A counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';


-- silva.stocking_milestone definition

-- Drop table

-- DROP TABLE silva.stocking_milestone;

CREATE TABLE IF NOT EXISTS silva.stocking_milestone (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	silv_milestone_type_code varchar(3) NOT NULL, -- A means of identifying if the associated milestone is for post harvest, regeneration or free growing.
	results_submission_id int8 NULL, -- Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.
	declared_date timestamp(0) NULL, -- The date that the Stocking Standard achieved the Milestone, not necessarily when it was recorded electronically at the Ministry. Previous descriptions from IDD: - The date the post harvest information was declared. - The date on which the Standards Unit was declared regenerated. - An enterable date at which the Standards Unit was declared free growing. This date cannot be in the future. The last date of declaration for all SU"s will become the opening free growing date.
	declared_userid varchar(30) NULL, -- The userid of the resource who declared the post harvest information.
	declare_ind varchar(1) NULL, -- A system generated flag "Y/N" which is determined from the polygons which comprise the Standards Unit. If all polygons within the SU are declared free growing then "Y" is displayed. If all SU"s are declared FG then the opening can be declared FG. A Y/N flag indicating whether or not the given Standards unit within the prescription has been declared regenerated.
	declaration_submitted_date timestamp(0) NULL, -- The date that the Milestone declaration was recorded electronically at the Ministry.
	early_offset_years int2 NULL, -- The number of years, after the start of harvest, when free-growing may first be declared.
	late_offset_years int2 NULL, -- The number of years, after the start of harvest, by which free-growing must be achieved.
	due_early_date timestamp(0) NULL, -- The earliest year the milestone is to be declared.
	due_late_date timestamp(0) NULL, -- The latest year the milestone is to be declared.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	extent_feasible_declared_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- DECLARED TO THE EXTENT PRACTICABLE - Indicator for Milestones that do not meet applicable Stocking Standards.
	CONSTRAINT stocking_milestone_pkey PRIMARY KEY (stocking_standard_unit_id, silv_milestone_type_code)
);
CREATE INDEX sm_ded_i ON silva.stocking_milestone USING btree (due_early_date);
CREATE INDEX sm_dld_i ON silva.stocking_milestone USING btree (due_late_date);
COMMENT ON TABLE silva.stocking_milestone IS 'Silviculture milestones that have been achieved for a specific area (SU - Standards Unit). The milestones that are currently tracked are post harvest, regeneration and free growing.';

-- Column comments

COMMENT ON COLUMN silva.stocking_milestone.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_milestone.silv_milestone_type_code IS 'A means of identifying if the associated milestone is for post harvest, regeneration or free growing.';
COMMENT ON COLUMN silva.stocking_milestone.results_submission_id IS 'Used to link information about a data submission. A submission may have many rows of Silviculture (Form A), Activities (Form B), and Forest Cover/Milestones (Form C) information. This attribute will be receiving a system generated number from the Electronic Submission Framework.';
COMMENT ON COLUMN silva.stocking_milestone.declared_date IS 'The date that the Stocking Standard achieved the Milestone, not necessarily when it was recorded electronically at the Ministry. Previous descriptions from IDD: - The date the post harvest information was declared. - The date on which the Standards Unit was declared regenerated. - An enterable date at which the Standards Unit was declared free growing. This date cannot be in the future. The last date of declaration for all SU"s will become the opening free growing date.';
COMMENT ON COLUMN silva.stocking_milestone.declared_userid IS 'The userid of the resource who declared the post harvest information.';
COMMENT ON COLUMN silva.stocking_milestone.declare_ind IS 'A system generated flag "Y/N" which is determined from the polygons which comprise the Standards Unit. If all polygons within the SU are declared free growing then "Y" is displayed. If all SU"s are declared FG then the opening can be declared FG. A Y/N flag indicating whether or not the given Standards unit within the prescription has been declared regenerated.';
COMMENT ON COLUMN silva.stocking_milestone.declaration_submitted_date IS 'The date that the Milestone declaration was recorded electronically at the Ministry.';
COMMENT ON COLUMN silva.stocking_milestone.early_offset_years IS 'The number of years, after the start of harvest, when free-growing may first be declared.';
COMMENT ON COLUMN silva.stocking_milestone.late_offset_years IS 'The number of years, after the start of harvest, by which free-growing must be achieved.';
COMMENT ON COLUMN silva.stocking_milestone.due_early_date IS 'The earliest year the milestone is to be declared.';
COMMENT ON COLUMN silva.stocking_milestone.due_late_date IS 'The latest year the milestone is to be declared.';
COMMENT ON COLUMN silva.stocking_milestone.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_milestone.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_milestone.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_milestone.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_milestone.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';
COMMENT ON COLUMN silva.stocking_milestone.extent_feasible_declared_ind IS 'DECLARED TO THE EXTENT PRACTICABLE - Indicator for Milestones that do not meet applicable Stocking Standards.';


-- silva.stocking_milestone_cmt_link definition

-- Drop table

-- DROP TABLE silva.stocking_milestone_cmt_link;

CREATE TABLE IF NOT EXISTS silva.stocking_milestone_cmt_link (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	silv_milestone_type_code varchar(3) NOT NULL, -- A means of identifying if the associated milestone is for post harvest, regeneration or free growing.
	silviculture_comment_id int8 NOT NULL, -- System generated value uniquely identifying the ISIS comment.
	CONSTRAINT stocking_milestone_cmt_link_pkey PRIMARY KEY (stocking_standard_unit_id, silv_milestone_type_code, silviculture_comment_id)
);

-- Column comments

COMMENT ON COLUMN silva.stocking_milestone_cmt_link.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_milestone_cmt_link.silv_milestone_type_code IS 'A means of identifying if the associated milestone is for post harvest, regeneration or free growing.';
COMMENT ON COLUMN silva.stocking_milestone_cmt_link.silviculture_comment_id IS 'System generated value uniquely identifying the ISIS comment.';


-- silva.stocking_standard_unit definition

-- Drop table

-- DROP TABLE silva.stocking_standard_unit;

CREATE TABLE IF NOT EXISTS silva.stocking_standard_unit (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	opening_id int8 NOT NULL, -- System generated value uniquely identifying the opening.
	standards_unit_id varchar(4) NOT NULL, -- A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.
	standards_regime_id int8 NULL, -- Unique identifier for the STANDARD REGIME entry.
	net_area numeric(7, 1) NOT NULL, -- A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).
	max_allow_soil_disturbance_pct numeric(3, 1) NULL, -- This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.
	variance_ind varchar(1) NULL, -- A yes/no indicator indicating if this stocking standard is for a variance or not.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT stocking_standard_unit_pkey PRIMARY KEY (stocking_standard_unit_id)
);
CREATE INDEX ssu_o_fk_i ON silva.stocking_standard_unit USING btree (opening_id);
CREATE INDEX ssu_sr_fk_i ON silva.stocking_standard_unit USING btree (standards_regime_id);
CREATE INDEX ssu_su_i ON silva.stocking_standard_unit USING btree (opening_id, standards_unit_id);
COMMENT ON TABLE silva.stocking_standard_unit IS 'Basic Silviculture objective stated in quantifiable terms for a specific area. These are the acceptable standards for reforestation and soil conversation. Also known as SU - Standards Unit.';

-- Column comments

COMMENT ON COLUMN silva.stocking_standard_unit.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_standard_unit.opening_id IS 'System generated value uniquely identifying the opening.';
COMMENT ON COLUMN silva.stocking_standard_unit.standards_unit_id IS 'A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.';
COMMENT ON COLUMN silva.stocking_standard_unit.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.stocking_standard_unit.net_area IS 'A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).';
COMMENT ON COLUMN silva.stocking_standard_unit.max_allow_soil_disturbance_pct IS 'This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.';
COMMENT ON COLUMN silva.stocking_standard_unit.variance_ind IS 'A yes/no indicator indicating if this stocking standard is for a variance or not.';
COMMENT ON COLUMN silva.stocking_standard_unit.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_standard_unit.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_standard_unit.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_standard_unit.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_standard_unit.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.stocking_standard_unit_archive definition

-- Drop table

-- DROP TABLE silva.stocking_standard_unit_archive;

CREATE TABLE IF NOT EXISTS silva.stocking_standard_unit_archive (
	stocking_standard_unit_id int8 NOT NULL, -- System generated value uniquely identifing the stocking standards.
	stocking_event_history_id int8 NOT NULL, -- Unique identifier for a stocking standard amendment event
	opening_id int8 NOT NULL, -- Opening id of opening this archive record was associated with.
	standards_unit_id varchar(4) NOT NULL, -- A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.
	standards_regime_id int8 NULL, -- Unique identifier for the STANDARD REGIME entry.
	net_area numeric(7, 1) NOT NULL, -- A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).
	max_allow_soil_disturbance_pct numeric(3, 1) NULL, -- This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.
	variance_ind varchar(1) NULL, -- A yes/no indicator indicating if this stocking standard is for a variance or not.
	regen_delay_offset_yrs int2 NULL, -- The number of years allowed for the regeneration of tree growth.
	regen_obligation_ind varchar(1) NOT NULL, -- A yes/no indicator indicating if this stocking standard is for a regeneration obligation.
	no_regen_early_offset_yrs int2 NULL, -- The minimum or early regen number of years.
	no_regen_late_offset_yrs int2 NULL, -- The maximum or late years of regen growth.
	free_growing_early_offset_yrs int2 NULL, -- The minimum or early growth number of years.
	free_growing_late_offset_yrs int2 NULL, -- The maximum or late years of tree growth.
	amendment_rationale_comment varchar(2000) NULL, -- A comment describing the rationale for an Amendment to a Stocking Standard.
	entry_userid varchar(30) NOT NULL, -- The USERID of the individual who entered the information.
	entry_timestamp timestamp(0) NOT NULL, -- The date and time the information was entered.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated the information.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	revision_count int4 NOT NULL, -- Internal counter used by the system to avoid conflicting updates to the record.
	CONSTRAINT stocking_standard_unit_archive_pkey PRIMARY KEY (stocking_event_history_id, stocking_standard_unit_id)
);
CREATE INDEX ssuarc_seh_fk_i ON silva.stocking_standard_unit_archive USING btree (stocking_event_history_id);
COMMENT ON TABLE silva.stocking_standard_unit_archive IS 'Contains archived information about a Stocking Standard amendment.';

-- Column comments

COMMENT ON COLUMN silva.stocking_standard_unit_archive.stocking_standard_unit_id IS 'System generated value uniquely identifing the stocking standards.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.stocking_event_history_id IS 'Unique identifier for a stocking standard amendment event';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.opening_id IS 'Opening id of opening this archive record was associated with.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.standards_unit_id IS 'A unique identifier within the opening that identifies the standard unit. At least one standards unit per opening is required.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.standards_regime_id IS 'Unique identifier for the STANDARD REGIME entry.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.net_area IS 'A value in hectares for the net area to be reforested (NAR). The NAR is the total area under the prescription (TAUP) minus the area or no planned reforestation (NPR).';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.max_allow_soil_disturbance_pct IS 'This is the maximum soil disturbance. The maximum proportion of the net area to be reforested which will be occupied by soil disturbance, following harvest; or following rehab where required.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.variance_ind IS 'A yes/no indicator indicating if this stocking standard is for a variance or not.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.regen_delay_offset_yrs IS 'The number of years allowed for the regeneration of tree growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.regen_obligation_ind IS 'A yes/no indicator indicating if this stocking standard is for a regeneration obligation.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.no_regen_early_offset_yrs IS 'The minimum or early regen number of years.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.no_regen_late_offset_yrs IS 'The maximum or late years of regen growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.free_growing_early_offset_yrs IS 'The minimum or early growth number of years.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.free_growing_late_offset_yrs IS 'The maximum or late years of tree growth.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.amendment_rationale_comment IS 'A comment describing the rationale for an Amendment to a Stocking Standard.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.entry_userid IS 'The USERID of the individual who entered the information.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.entry_timestamp IS 'The date and time the information was entered.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.update_userid IS 'The USERID of the individual who last updated the information.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.stocking_standard_unit_archive.revision_count IS 'Internal counter used by the system to avoid conflicting updates to the record.';


-- silva.timber_mark definition

-- Drop table

-- DROP TABLE silva.timber_mark;

CREATE TABLE IF NOT EXISTS silva.timber_mark (
	timber_mark varchar(6) NOT NULL, -- Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	cutting_permit_id varchar(3) NOT NULL, -- Identifier for a cutting permit associated with a quota type harvesting tenure.
	forest_district int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".
	geographic_distrct int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".
	cascade_split_code varchar(1) NULL, -- Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information
	quota_type_code varchar(1) NULL, -- Identifies an allowable harvesting quota type.
	deciduous_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether the timber mark is primarily for harvesting deciduous species. Otherwise, coniferous.
	catastrophic_ind varchar(1) DEFAULT NULL::character varying NULL, -- Indicates if the damage to the stand of timber is catastrophic. Otherwise, it is endemic
	crown_granted_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicator to signify whether the land that the timber mark pertains to has a verifiable crown grant document.
	cruise_based_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether a cruise was used for billing purposes.
	certificate varchar(6) NULL, -- Timber mark certificate number for private marks
	hdbs_timber_mark varchar(7) NULL, -- This is the HDBS representation of the TIMBER MARK. This column is due to be deleted
	vm_timber_mark varchar(6) NULL, -- This is a reformatted definition of the TIMBER_MARK. It is used primarily in Silviculture Branch"s Major Licence Silviculture Information System. This column is due to be deleted.
	tenure_term int4 NULL, -- Term of the tenure (in months).
	bcaa_folio_number varchar(23) NULL, -- BC Assessment Authority Folio Number for the land that the cutting permit/Timber Mark pertains to, if applicable, or the LTO number. BCAA Folio Numbers are in the format C15 733 06604.140-1-2; the format for LTO numbers is nnn-nnn-nnn.
	activated_userid varchar(30) NULL, -- Userid of the person who activated the timber mark
	amended_userid varchar(30) NULL, -- Userid of the person who last amended the timber mark.
	district_admn_zone varchar(4) NULL, -- Identifies an administration area within a district for operational purposes.
	granted_acqrd_date timestamp(0) NULL, -- Date the property that the timber mark pertains to was crown granted; or, in lieu of a crown grant document, the date the land was thought to be acquired; or, a code for an historical land transfer.
	lands_region varchar(1) NULL, -- Region identifier for crown lands
	crown_granted_acq_desc varchar(240) NULL, -- The description of the crown granted acquisition.
	mark_status_st varchar(3) NULL, -- Current status of Timber Mark, eg., Pending - Planned, Harvesting - Suspended. This references Timber_Status_Code.
	mark_status_date timestamp(0) NULL, -- The date that the timber mark / cutting permit status was last updated
	mark_amend_date timestamp(0) NULL, -- If the timber mark has been amended, this date will reflect the time the most recent amendment was made
	mark_appl_date timestamp(0) NULL, -- DATE the application was received in the district.
	mark_cancel_date timestamp(0) NULL, -- Date the timber mark was cancelled.
	mark_extend_date timestamp(0) NULL, -- Date to which the timber mark validity has been extended. Current expiry date
	mark_extend_rsn_cd varchar(1) NULL, -- Code to indicate the reason to extend the life of the timber mark.
	mark_extend_count int8 NULL, -- Count of the number of extensions to the expiry date that the timber mark has had.
	mark_issue_date timestamp(0) NULL, -- Date the Timber Mark was issued
	mark_expiry_date timestamp(0) NULL, -- Initial expiry date set upon activation of the timber mark.
	markng_instrmnt_cd varchar(1) NULL, -- Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.
	marking_method_cd varchar(1) NULL, -- Code indicating the method of marking the truckload of harvested timber
	entry_userid varchar(30) NOT NULL, -- The userid of the individual performing the entry.
	entry_timestamp timestamp(0) NOT NULL, -- The data and time on which the entry was created.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	small_patch_salvage_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether or not Cutting Permit is for small patch salvage. Geometry does not have to be supplied for small patch salvage cut blocks that are under 1 hectare.
	salvage_type_code varchar(3) NULL, -- Code describing the salvage type.
	CONSTRAINT avcon_1054232647_catas_000 CHECK (((catastrophic_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_crown_001 CHECK (((crown_granted_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_cruis_000 CHECK (((cruise_based_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_decid_000 CHECK (((deciduous_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT timber_mark_forest_file_id_cutting_permit_id_key UNIQUE (forest_file_id, cutting_permit_id),
	CONSTRAINT timber_mark_pkey PRIMARY KEY (timber_mark),
	CONSTRAINT timber_mark_timber_mark_forest_file_id_key UNIQUE (timber_mark, forest_file_id)
);
CREATE INDEX tfalling_geographically_wit__i ON silva.timber_mark USING btree (geographic_distrct);
CREATE INDEX tm1_i ON silva.timber_mark USING btree (vm_timber_mark);
CREATE INDEX tm2_i ON silva.timber_mark USING btree (hdbs_timber_mark);
CREATE INDEX tm3_i ON silva.timber_mark USING btree (timber_mark, forest_file_id, forest_district);
CREATE INDEX tm_ou_fk_i ON silva.timber_mark USING btree (forest_district);
CREATE INDEX tm_pfu_fk_i ON silva.timber_mark USING btree (forest_file_id);
COMMENT ON TABLE silva.timber_mark IS 'Information about the timber cutting and moving permission for a timber tenure. For Private Timber Marks, the ministry does not grant cutting permission. For some Road Permits and Licences to Cut, no moving permission is granted. Otherwise, synonymous. This entity is not directly updated, but is rather done via a replication process using its base tables, of harvesting authority and hauling authority.';

-- Column comments

COMMENT ON COLUMN silva.timber_mark.timber_mark IS 'Unique identifying set of characters to be stamped or marked on the end of each log to associate the log with the specific authority to harvest and move timber.';
COMMENT ON COLUMN silva.timber_mark.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.timber_mark.cutting_permit_id IS 'Identifier for a cutting permit associated with a quota type harvesting tenure.';
COMMENT ON COLUMN silva.timber_mark.forest_district IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.timber_mark.geographic_distrct IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.timber_mark.cascade_split_code IS 'Code to indicate the administrative split for the timber mark, east or west of the Cascade Mountains, for tracking timber volume information';
COMMENT ON COLUMN silva.timber_mark.quota_type_code IS 'Identifies an allowable harvesting quota type.';
COMMENT ON COLUMN silva.timber_mark.deciduous_ind IS 'Indicates whether the timber mark is primarily for harvesting deciduous species. Otherwise, coniferous.';
COMMENT ON COLUMN silva.timber_mark.catastrophic_ind IS 'Indicates if the damage to the stand of timber is catastrophic. Otherwise, it is endemic';
COMMENT ON COLUMN silva.timber_mark.crown_granted_ind IS 'Indicator to signify whether the land that the timber mark pertains to has a verifiable crown grant document.';
COMMENT ON COLUMN silva.timber_mark.cruise_based_ind IS 'Indicates whether a cruise was used for billing purposes.';
COMMENT ON COLUMN silva.timber_mark.certificate IS 'Timber mark certificate number for private marks';
COMMENT ON COLUMN silva.timber_mark.hdbs_timber_mark IS 'This is the HDBS representation of the TIMBER MARK. This column is due to be deleted';
COMMENT ON COLUMN silva.timber_mark.vm_timber_mark IS 'This is a reformatted definition of the TIMBER_MARK. It is used primarily in Silviculture Branch"s Major Licence Silviculture Information System. This column is due to be deleted.';
COMMENT ON COLUMN silva.timber_mark.tenure_term IS 'Term of the tenure (in months).';
COMMENT ON COLUMN silva.timber_mark.bcaa_folio_number IS 'BC Assessment Authority Folio Number for the land that the cutting permit/Timber Mark pertains to, if applicable, or the LTO number. BCAA Folio Numbers are in the format C15 733 06604.140-1-2; the format for LTO numbers is nnn-nnn-nnn.';
COMMENT ON COLUMN silva.timber_mark.activated_userid IS 'Userid of the person who activated the timber mark';
COMMENT ON COLUMN silva.timber_mark.amended_userid IS 'Userid of the person who last amended the timber mark.';
COMMENT ON COLUMN silva.timber_mark.district_admn_zone IS 'Identifies an administration area within a district for operational purposes.';
COMMENT ON COLUMN silva.timber_mark.granted_acqrd_date IS 'Date the property that the timber mark pertains to was crown granted; or, in lieu of a crown grant document, the date the land was thought to be acquired; or, a code for an historical land transfer.';
COMMENT ON COLUMN silva.timber_mark.lands_region IS 'Region identifier for crown lands';
COMMENT ON COLUMN silva.timber_mark.crown_granted_acq_desc IS 'The description of the crown granted acquisition.';
COMMENT ON COLUMN silva.timber_mark.mark_status_st IS 'Current status of Timber Mark, eg., Pending - Planned, Harvesting - Suspended. This references Timber_Status_Code.';
COMMENT ON COLUMN silva.timber_mark.mark_status_date IS 'The date that the timber mark / cutting permit status was last updated';
COMMENT ON COLUMN silva.timber_mark.mark_amend_date IS 'If the timber mark has been amended, this date will reflect the time the most recent amendment was made';
COMMENT ON COLUMN silva.timber_mark.mark_appl_date IS 'DATE the application was received in the district.';
COMMENT ON COLUMN silva.timber_mark.mark_cancel_date IS 'Date the timber mark was cancelled.';
COMMENT ON COLUMN silva.timber_mark.mark_extend_date IS 'Date to which the timber mark validity has been extended. Current expiry date';
COMMENT ON COLUMN silva.timber_mark.mark_extend_rsn_cd IS 'Code to indicate the reason to extend the life of the timber mark.';
COMMENT ON COLUMN silva.timber_mark.mark_extend_count IS 'Count of the number of extensions to the expiry date that the timber mark has had.';
COMMENT ON COLUMN silva.timber_mark.mark_issue_date IS 'Date the Timber Mark was issued';
COMMENT ON COLUMN silva.timber_mark.mark_expiry_date IS 'Initial expiry date set upon activation of the timber mark.';
COMMENT ON COLUMN silva.timber_mark.markng_instrmnt_cd IS 'Code indicating the means of marking the timber for identification to the timber mark, eg., hammer, paint.';
COMMENT ON COLUMN silva.timber_mark.marking_method_cd IS 'Code indicating the method of marking the truckload of harvested timber';
COMMENT ON COLUMN silva.timber_mark.entry_userid IS 'The userid of the individual performing the entry.';
COMMENT ON COLUMN silva.timber_mark.entry_timestamp IS 'The data and time on which the entry was created.';
COMMENT ON COLUMN silva.timber_mark.update_userid IS 'The userid of the individual who last updated this information';
COMMENT ON COLUMN silva.timber_mark.update_timestamp IS 'The date and time of the last update';
COMMENT ON COLUMN silva.timber_mark.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.timber_mark.small_patch_salvage_ind IS 'Indicates whether or not Cutting Permit is for small patch salvage. Geometry does not have to be supplied for small patch salvage cut blocks that are under 1 hectare.';
COMMENT ON COLUMN silva.timber_mark.salvage_type_code IS 'Code describing the salvage type.';
