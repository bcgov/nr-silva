-- silva.forest_client definition

-- Drop table

-- DROP TABLE silva.forest_client;

CREATE TABLE silva.forest_client (
	client_number varchar(8) NOT NULL, -- Sequentially assigned number to identify a ministry client.
	client_name varchar(60) NOT NULL, -- The name of the Ministry Client - Company or Individual. Entered as: the full corporate name if a Corporation; the full registered name if a Partnership; the legal surname if an Individual.
	legal_first_name varchar(30) NULL, -- The client"s legal first name
	legal_middle_name varchar(30) NULL, -- The client"s legal middle name of names if an individual.
	client_status_code varchar(3) NOT NULL, -- A code indicating the status of ministry client.
	client_type_code varchar(1) NOT NULL, -- A code indicating a type of ministry client.
	birthdate timestamp(0) NULL, -- Client"s date of birth, if the client is an individual.
	client_id_type_code varchar(4) NULL, -- A code indicating a type of identification used to identify a Client. Eg. BC Drivers Licence, Military Number, SIN
	client_identification varchar(40) NULL, -- The reference number for sources of identification for an Individual client.
	registry_company_type_code varchar(4) NULL, -- A code indicating a type of Corporate Registry company.
	corp_regn_nmbr varchar(9) NULL, -- Client"s corporate registration number.
	client_acronym varchar(8) NULL, -- A familiar aphabetic acronym to be used as an alternate to the ministry"s client number for data entry and display.
	wcb_firm_number varchar(6) NULL, -- Workers Compensation Board registration number.
	ocg_supplier_nmbr varchar(10) NULL, -- Office of the Controller General"s reference number for identification of Government Suppliers.
	client_comment varchar(4000) NULL, -- Text for the comment on the client.
	add_timestamp timestamp(0) NOT NULL, -- The date and time on which the entry was created.
	add_userid varchar(30) NOT NULL, -- The USERID of the individual performing the entry.
	add_org_unit int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	update_userid varchar(30) NOT NULL, -- The USERID of the individual who last updated this information.
	update_org_unit int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	revision_count int4 NOT NULL, -- Number of times updated
	CONSTRAINT forest_client_pkey PRIMARY KEY (client_number)
);
CREATE UNIQUE INDEX fc_acronym_i ON silva.forest_client USING btree (client_acronym);
CREATE INDEX fc_citc_fk_i ON silva.forest_client USING btree (client_id_type_code);
CREATE INDEX fc_csc_fk_i ON silva.forest_client USING btree (client_status_code);
CREATE INDEX fc_ctcx_fk_i ON silva.forest_client USING btree (client_type_code, registry_company_type_code);
CREATE INDEX fc_ou_fk_i ON silva.forest_client USING btree (add_org_unit);
CREATE INDEX fc_ou_is_updated_by_fk_i ON silva.forest_client USING btree (update_org_unit);
CREATE UNIQUE INDEX "i2$_forest_client" ON silva.forest_client USING btree (client_name, legal_first_name, legal_middle_name, client_number);
CREATE INDEX "i4$_forest_client" ON silva.forest_client USING btree (ocg_supplier_nmbr);
CREATE INDEX "i5$_forest_client" ON silva.forest_client USING btree (corp_regn_nmbr);
COMMENT ON TABLE silva.forest_client IS 'Any individual who is dealing, has dealt or plans to deal with the Ministry.';

-- Column comments

COMMENT ON COLUMN silva.forest_client.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.forest_client.client_name IS 'The name of the Ministry Client - Company or Individual. Entered as: the full corporate name if a Corporation; the full registered name if a Partnership; the legal surname if an Individual.';
COMMENT ON COLUMN silva.forest_client.legal_first_name IS 'The client"s legal first name';
COMMENT ON COLUMN silva.forest_client.legal_middle_name IS 'The client"s legal middle name of names if an individual.';
COMMENT ON COLUMN silva.forest_client.client_status_code IS 'A code indicating the status of ministry client.';
COMMENT ON COLUMN silva.forest_client.client_type_code IS 'A code indicating a type of ministry client.';
COMMENT ON COLUMN silva.forest_client.birthdate IS 'Client"s date of birth, if the client is an individual.';
COMMENT ON COLUMN silva.forest_client.client_id_type_code IS 'A code indicating a type of identification used to identify a Client. Eg. BC Drivers Licence, Military Number, SIN';
COMMENT ON COLUMN silva.forest_client.client_identification IS 'The reference number for sources of identification for an Individual client.';
COMMENT ON COLUMN silva.forest_client.registry_company_type_code IS 'A code indicating a type of Corporate Registry company.';
COMMENT ON COLUMN silva.forest_client.corp_regn_nmbr IS 'Client"s corporate registration number.';
COMMENT ON COLUMN silva.forest_client.client_acronym IS 'A familiar aphabetic acronym to be used as an alternate to the ministry"s client number for data entry and display.';
COMMENT ON COLUMN silva.forest_client.wcb_firm_number IS 'Workers Compensation Board registration number.';
COMMENT ON COLUMN silva.forest_client.ocg_supplier_nmbr IS 'Office of the Controller General"s reference number for identification of Government Suppliers.';
COMMENT ON COLUMN silva.forest_client.client_comment IS 'Text for the comment on the client.';
COMMENT ON COLUMN silva.forest_client.add_timestamp IS 'The date and time on which the entry was created.';
COMMENT ON COLUMN silva.forest_client.add_userid IS 'The USERID of the individual performing the entry.';
COMMENT ON COLUMN silva.forest_client.add_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.forest_client.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.forest_client.update_userid IS 'The USERID of the individual who last updated this information.';
COMMENT ON COLUMN silva.forest_client.update_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.forest_client.revision_count IS 'Number of times updated';


-- silva.client_location definition

-- Drop table

-- DROP TABLE silva.client_location;

CREATE TABLE silva.client_location (
	client_number varchar(8) NOT NULL, -- Sequentially assigned number to identify a ministry client.
	client_locn_code varchar(2) NOT NULL, -- A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.
	client_locn_name varchar(40) NULL, -- Restricted for identifying corporate division names and joint venture names. Other comments are INVALID.
	hdbs_company_code varchar(5) NULL, -- The identifier used as the key to the name and address table in the Harvest Database System. It is maintained in this system as a cross-reference to the name and address file.
	address_1 varchar(40) NOT NULL, -- The first line of the client"s address.
	address_2 varchar(40) NULL, -- The second line of the client"s address.
	address_3 varchar(40) NULL, -- The third line of the client"s address.
	city varchar(30) NOT NULL, -- Town or City name of the client"s address.
	province varchar(50) NULL, -- Province or State of the client"s address.
	postal_code varchar(10) NULL, -- Code used by Canadian Federal Government Postal System; may also contain an American Zip Code or another country"s postal code.
	country varchar(50) NOT NULL, -- The country in which the client is located.
	business_phone varchar(10) NULL, -- Client"s business telephone number and area code.
	home_phone varchar(10) NULL, -- Client"s Home telephone number and area code.
	cell_phone varchar(10) NULL, -- Client"s business cell phone number and area code.
	fax_number varchar(10) NULL, -- Area code and Fax number for the client location.
	email_address varchar(128) NULL, -- Client"s email address for this location.
	locn_expired_ind varchar(1) NOT NULL, -- Identifies a previous address for a client which should not be used for any future purposes.
	returned_mail_date timestamp(0) NULL, -- The date when the Ministry receives returned mail for this client location address.
	trust_location_ind varchar(1) NOT NULL, -- Indicates a location to which funds held in trust will be returned to.
	cli_locn_comment varchar(4000) NULL, -- Text for the comment on the client location.
	update_timestamp timestamp(0) NOT NULL, -- The date and time of the last update.
	update_userid varchar(30) NOT NULL, -- The userid of the individual who last updated this information.
	update_org_unit int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	add_timestamp timestamp(0) NOT NULL, -- The date and time on which the entry was created.
	add_userid varchar(30) NOT NULL, -- The userid of the individual performing the entry.
	add_org_unit int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	revision_count int4 NOT NULL, -- Number of times updated
	CONSTRAINT client_location_pkey PRIMARY KEY (client_number, client_locn_code)
);
CREATE INDEX cl_fc_fk_i ON silva.client_location USING btree (client_number);
CREATE INDEX cl_ou_fk_i ON silva.client_location USING btree (update_org_unit);
CREATE INDEX cl_ou_is_created_by_fk_i ON silva.client_location USING btree (add_org_unit);
CREATE INDEX "i2$_client_location" ON silva.client_location USING btree (hdbs_company_code);
COMMENT ON TABLE silva.client_location IS 'The description and the details of the address(es) at which a client operates. Its main purpose is to provide alternate mailing addresses. Maximum number of locations per client is 99. The average is 2 per client. Location 00 is always the client.';

-- Column comments

COMMENT ON COLUMN silva.client_location.client_number IS 'Sequentially assigned number to identify a ministry client.';
COMMENT ON COLUMN silva.client_location.client_locn_code IS 'A code to uniquely identify, within each client, the addresses of different divisions or locations at which the client operates. The location code is sequentially assigned starting with "00" for the client"s permanent address.';
COMMENT ON COLUMN silva.client_location.client_locn_name IS 'Restricted for identifying corporate division names and joint venture names. Other comments are INVALID.';
COMMENT ON COLUMN silva.client_location.hdbs_company_code IS 'The identifier used as the key to the name and address table in the Harvest Database System. It is maintained in this system as a cross-reference to the name and address file.';
COMMENT ON COLUMN silva.client_location.address_1 IS 'The first line of the client"s address.';
COMMENT ON COLUMN silva.client_location.address_2 IS 'The second line of the client"s address.';
COMMENT ON COLUMN silva.client_location.address_3 IS 'The third line of the client"s address.';
COMMENT ON COLUMN silva.client_location.city IS 'Town or City name of the client"s address.';
COMMENT ON COLUMN silva.client_location.province IS 'Province or State of the client"s address.';
COMMENT ON COLUMN silva.client_location.postal_code IS 'Code used by Canadian Federal Government Postal System; may also contain an American Zip Code or another country"s postal code.';
COMMENT ON COLUMN silva.client_location.country IS 'The country in which the client is located.';
COMMENT ON COLUMN silva.client_location.business_phone IS 'Client"s business telephone number and area code.';
COMMENT ON COLUMN silva.client_location.home_phone IS 'Client"s Home telephone number and area code.';
COMMENT ON COLUMN silva.client_location.cell_phone IS 'Client"s business cell phone number and area code.';
COMMENT ON COLUMN silva.client_location.fax_number IS 'Area code and Fax number for the client location.';
COMMENT ON COLUMN silva.client_location.email_address IS 'Client"s email address for this location.';
COMMENT ON COLUMN silva.client_location.locn_expired_ind IS 'Identifies a previous address for a client which should not be used for any future purposes.';
COMMENT ON COLUMN silva.client_location.returned_mail_date IS 'The date when the Ministry receives returned mail for this client location address.';
COMMENT ON COLUMN silva.client_location.trust_location_ind IS 'Indicates a location to which funds held in trust will be returned to.';
COMMENT ON COLUMN silva.client_location.cli_locn_comment IS 'Text for the comment on the client location.';
COMMENT ON COLUMN silva.client_location.update_timestamp IS 'The date and time of the last update.';
COMMENT ON COLUMN silva.client_location.update_userid IS 'The userid of the individual who last updated this information.';
COMMENT ON COLUMN silva.client_location.update_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.client_location.add_timestamp IS 'The date and time on which the entry was created.';
COMMENT ON COLUMN silva.client_location.add_userid IS 'The userid of the individual performing the entry.';
COMMENT ON COLUMN silva.client_location.add_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.client_location.revision_count IS 'Number of times updated';

-- silva.harvest_sale definition

-- Drop table

-- DROP TABLE silva.harvest_sale;

CREATE TABLE IF NOT EXISTS silva.harvest_sale (
	forest_file_id varchar(10) NOT NULL, -- File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.
	sb_fund_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- See FTA data model for more details.
	sale_method_code varchar(1) NULL, -- Method for type of sale of timber under a tenure. Default: Sealed Tender.
	sale_type_cd varchar(2) NULL, -- Describes the type of timber sale.
	planned_sale_date timestamp(0) NULL, -- Estimated sale date of tenure.
	tender_opening_dt timestamp(0) NULL, -- The advertised date that sealed tenders will be opened or the date the auction was held.
	plnd_sb_cat_code varchar(1) NULL, -- Describes the type of timber sale.
	sold_sb_cat_code varchar(1) NULL, -- Describes the type of timber sale.
	total_bidders int4 NULL, -- Total number of applicants that bid on this sale. To be eventually replaced by a calculated field.
	lumpsum_bonus_amt numeric(11, 2) NULL, -- Lump sum bonus amount for securing harvesting rights on ministry controlled land.
	cash_sale_est_vol numeric(13, 2) NULL, -- The estimated volume in cubic meters sold under a Cash Sale type Timber Mark. This is mandatory for Cash Sales, and the default is zero for non-cash sales.
	cash_sale_tot_dol numeric(11, 2) NULL, -- The total dollars received for a sale sold under a Cash Sale type Timber Mark. This is mandatory for cash sales, and defaults to zero for non-cash sales.
	payment_method_cd varchar(1) NULL, -- The method of collecting the payment for the licence, such as "Cash" or "Automatic Invoice".
	salvage_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates if the timber sale is for salvage wood.
	sale_volume int8 NULL, -- The volume (m3) advertised for the sale; or, if not advertised, the volume offered for sale.
	admin_area_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Admin Area Indicator indicates whether cash sales are within an administrative area.
	minor_facility_ind varchar(1) DEFAULT 'N'::character varying NOT NULL, -- Indicates whether a minor processing facility is on site with an active permit.
	bcts_org_unit int8 NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".
	fta_bonus_bid numeric(11, 2) NULL, -- The FTA BONUS BID is the amount paid out as a bonus for the award of the tenure.
	fta_bonus_offer numeric(11, 2) NULL, -- The FTA BONUS OFFER is the amount offered as a bonus for the award of the tenure.
	revision_count int4 NOT NULL, -- A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.
	entry_userid varchar(30) NOT NULL, -- The unique user id of the resource who initially added the entry.
	entry_timestamp timestamp(0) NOT NULL, -- Timestamp when the event information was entered.
	update_userid varchar(30) NOT NULL, -- Id of user making last change to data.
	update_timestamp timestamp(0) NOT NULL, -- Timestamp of this data"s last update.
	CONSTRAINT avcon_1054232647_admin_000 CHECK (((admin_area_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_minor_000 CHECK (((minor_facility_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_salva_000 CHECK (((salvage_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT avcon_1054232647_sb_fu_000 CHECK (((sb_fund_ind)::text = ANY ((ARRAY['N'::character varying, 'Y'::character varying])::text[]))),
	CONSTRAINT harvest_sale_pkey PRIMARY KEY (forest_file_id)
);
CREATE INDEX hs_pmcd_fk_i ON silva.harvest_sale USING btree (payment_method_cd);
CREATE INDEX hs_psccd_fk_i ON silva.harvest_sale USING btree (plnd_sb_cat_code);
CREATE INDEX hs_smcd_fk_i ON silva.harvest_sale USING btree (sale_method_code);
CREATE INDEX hs_ssccd_fk_i ON silva.harvest_sale USING btree (sold_sb_cat_code);
CREATE INDEX hs_stcd_fk_i ON silva.harvest_sale USING btree (sale_type_cd);
COMMENT ON TABLE silva.harvest_sale IS 'Sale information relating to specific tenures, usually let via the competitive bid process. Information on planned sale and actual sale details are required for Small Business tenures.';

-- Column comments

COMMENT ON COLUMN silva.harvest_sale.forest_file_id IS 'File identification assigned to Provincial Forest Use files. Assigned file number. Usually the Licence, Tenure or Private Mark number.';
COMMENT ON COLUMN silva.harvest_sale.sb_fund_ind IS 'See FTA data model for more details.';
COMMENT ON COLUMN silva.harvest_sale.sale_method_code IS 'Method for type of sale of timber under a tenure. Default: Sealed Tender.';
COMMENT ON COLUMN silva.harvest_sale.sale_type_cd IS 'Describes the type of timber sale.';
COMMENT ON COLUMN silva.harvest_sale.planned_sale_date IS 'Estimated sale date of tenure.';
COMMENT ON COLUMN silva.harvest_sale.tender_opening_dt IS 'The advertised date that sealed tenders will be opened or the date the auction was held.';
COMMENT ON COLUMN silva.harvest_sale.plnd_sb_cat_code IS 'Describes the type of timber sale.';
COMMENT ON COLUMN silva.harvest_sale.sold_sb_cat_code IS 'Describes the type of timber sale.';
COMMENT ON COLUMN silva.harvest_sale.total_bidders IS 'Total number of applicants that bid on this sale. To be eventually replaced by a calculated field.';
COMMENT ON COLUMN silva.harvest_sale.lumpsum_bonus_amt IS 'Lump sum bonus amount for securing harvesting rights on ministry controlled land.';
COMMENT ON COLUMN silva.harvest_sale.cash_sale_est_vol IS 'The estimated volume in cubic meters sold under a Cash Sale type Timber Mark. This is mandatory for Cash Sales, and the default is zero for non-cash sales.';
COMMENT ON COLUMN silva.harvest_sale.cash_sale_tot_dol IS 'The total dollars received for a sale sold under a Cash Sale type Timber Mark. This is mandatory for cash sales, and defaults to zero for non-cash sales.';
COMMENT ON COLUMN silva.harvest_sale.payment_method_cd IS 'The method of collecting the payment for the licence, such as "Cash" or "Automatic Invoice".';
COMMENT ON COLUMN silva.harvest_sale.salvage_ind IS 'Indicates if the timber sale is for salvage wood.';
COMMENT ON COLUMN silva.harvest_sale.sale_volume IS 'The volume (m3) advertised for the sale; or, if not advertised, the volume offered for sale.';
COMMENT ON COLUMN silva.harvest_sale.admin_area_ind IS 'Admin Area Indicator indicates whether cash sales are within an administrative area.';
COMMENT ON COLUMN silva.harvest_sale.minor_facility_ind IS 'Indicates whether a minor processing facility is on site with an active permit.';
COMMENT ON COLUMN silva.harvest_sale.bcts_org_unit IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests offices. Values stored here are for the computers use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.harvest_sale.fta_bonus_bid IS 'The FTA BONUS BID is the amount paid out as a bonus for the award of the tenure.';
COMMENT ON COLUMN silva.harvest_sale.fta_bonus_offer IS 'The FTA BONUS OFFER is the amount offered as a bonus for the award of the tenure.';
COMMENT ON COLUMN silva.harvest_sale.revision_count IS 'A count of the number of times an entry in the entity has been modified. Used to validate if the current information displayed on a user"s web browser is the most current.';
COMMENT ON COLUMN silva.harvest_sale.entry_userid IS 'The unique user id of the resource who initially added the entry.';
COMMENT ON COLUMN silva.harvest_sale.entry_timestamp IS 'Timestamp when the event information was entered.';
COMMENT ON COLUMN silva.harvest_sale.update_userid IS 'Id of user making last change to data.';
COMMENT ON COLUMN silva.harvest_sale.update_timestamp IS 'Timestamp of this data"s last update.';

-- silva.payment_method_code definition

-- Drop table

-- DROP TABLE silva.payment_method_code;

CREATE TABLE IF NOT EXISTS silva.payment_method_code (
	payment_method_code varchar(1) NOT NULL, -- The method of collecting the payment for the licence, such as "Cash" or "Automatic Invoice".
	description varchar(120) NOT NULL, -- Description of the code
	effective_date timestamp(0) NOT NULL, -- Date the code is effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT payment_method_code_pkey PRIMARY KEY (payment_method_code)
);

-- Column comments

COMMENT ON COLUMN silva.payment_method_code.payment_method_code IS 'The method of collecting the payment for the licence, such as "Cash" or "Automatic Invoice".';
COMMENT ON COLUMN silva.payment_method_code.description IS 'Description of the code';
COMMENT ON COLUMN silva.payment_method_code.effective_date IS 'Date the code is effective';
COMMENT ON COLUMN silva.payment_method_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.payment_method_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.sb_category_code definition

-- Drop table

-- DROP TABLE silva.sb_category_code;

CREATE TABLE IF NOT EXISTS silva.sb_category_code (
	sb_category_code varchar(1) NOT NULL, -- Describes the type of timber sale.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT sb_category_code_pkey PRIMARY KEY (sb_category_code)
);

-- Column comments

COMMENT ON COLUMN silva.sb_category_code.sb_category_code IS 'Describes the type of timber sale.';
COMMENT ON COLUMN silva.sb_category_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.sb_category_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.sb_category_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.sb_category_code.update_timestamp IS 'The date and time the value was last modified.';



-- silva.sale_method_code definition

-- Drop table

-- DROP TABLE silva.sale_method_code;

CREATE TABLE IF NOT EXISTS silva.sale_method_code (
	sale_method_code varchar(1) NOT NULL, -- Method for type of sale of timber under a tenure. Default: Sealed Tender.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT sale_method_code_pkey PRIMARY KEY (sale_method_code)
);

-- Column comments

COMMENT ON COLUMN silva.sale_method_code.sale_method_code IS 'Method for type of sale of timber under a tenure. Default: Sealed Tender.';
COMMENT ON COLUMN silva.sale_method_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.sale_method_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.sale_method_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.sale_method_code.update_timestamp IS 'The date and time the value was last modified.';

-- silva.sale_type_code definition

-- Drop table

-- DROP TABLE silva.sale_type_code;

CREATE TABLE IF NOT EXISTS silva.sale_type_code (
	sale_type_code varchar(2) NOT NULL, -- Describes the type of timber sale.
	description varchar(120) NOT NULL, -- Description of the code value
	effective_date timestamp(0) NOT NULL, -- Date the code becomes effective
	expiry_date timestamp(0) NOT NULL, -- Date the code expires
	update_timestamp timestamp(0) NOT NULL, -- The date and time the value was last modified.
	CONSTRAINT sale_type_code_pkey PRIMARY KEY (sale_type_code)
);

-- Column comments

COMMENT ON COLUMN silva.sale_type_code.sale_type_code IS 'Describes the type of timber sale.';
COMMENT ON COLUMN silva.sale_type_code.description IS 'Description of the code value';
COMMENT ON COLUMN silva.sale_type_code.effective_date IS 'Date the code becomes effective';
COMMENT ON COLUMN silva.sale_type_code.expiry_date IS 'Date the code expires';
COMMENT ON COLUMN silva.sale_type_code.update_timestamp IS 'The date and time the value was last modified.';


-- silva.org_unit definition

-- Drop table

-- DROP TABLE silva.org_unit;

CREATE TABLE IF NOT EXISTS silva.org_unit (
	org_unit_no int8 NOT NULL, -- Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".
	org_unit_code varchar(6) NOT NULL, -- Identifies any office within the ministry. First character identifies Exec, HQ branch, Region, or District. Next two chars identify the office name; next two the section (HQ Branch) or program (Region or District); last char identifies the subsection.
	org_unit_name varchar(100) NOT NULL, -- The name or title of a ministry office or section; for example Kamloops Forest Region; Silviculture Branch; Kispiox Forest District Protection program.
	location_code varchar(3) NOT NULL, -- Unique identifier for one of the office locations within the Ministry of Forests.
	area_code varchar(3) NOT NULL, -- The 3 digit numer that identifies each telephone service area in a country.
	telephone_no varchar(7) NOT NULL, -- The general office telephone number (excluding the area code) for a Ministry of Forests office.
	org_level_code varchar(1) NOT NULL, -- Represents the organizational level of the Ministry, and is one of: D - District Forestry Office, E - Executive, H - Headquarters Branch, R - Regional Forestry Office.
	office_name_code varchar(2) NOT NULL, -- Represents an office within a specific organizational level.
	rollup_region_no int8 NOT NULL, -- Identifier designating the region to which the org unit is rolled into for reporting purposes. Values stored here are for the computers use only, and are not to be used by people as "Ministry Codes".
	rollup_region_code varchar(6) NOT NULL, -- Identifier designating the region to which the org unit is rolled into for reporting purposes.
	rollup_dist_no int8 NOT NULL, -- Identifier designating the district to which the org unit is rolled into for reporting purposes. Values stored here are for the computers use only and are not to be used by people as "Ministry Codes".
	rollup_dist_code varchar(6) NOT NULL, -- Identifier designating the district to which the org unit is rolled into for reporting purposes.
	effective_date timestamp(0) NOT NULL, -- The date the Organizational Unit became effective as a valid Org Unit within the ministry"s organizational structure.
	expiry_date timestamp(0) NOT NULL, -- The date the Organizational Unit is Obsoleted and is no longer an active Org Unit within the ministrys organizational structure. At the determination of Human Resources Branch, an expired Org Unit may be retained for historical purposed, or may be deleted.
	update_timestamp timestamp(0) NULL, -- The date and time of the last modification.
	CONSTRAINT org_unit_pkey PRIMARY KEY (org_unit_no)
);
CREATE UNIQUE INDEX "i2$_org_unit" ON silva.org_unit USING btree (org_unit_code, org_unit_no);
CREATE UNIQUE INDEX "i3$_org_unit" ON silva.org_unit USING btree (org_unit_code);
CREATE UNIQUE INDEX "i4$_org_unit" ON silva.org_unit USING btree (org_unit_no, org_unit_code, org_unit_name, location_code, telephone_no, org_level_code, office_name_code);
CREATE INDEX "i5$_org_unit" ON silva.org_unit USING btree (rollup_dist_code, rollup_region_code);
CREATE INDEX "i6$_org_unit" ON silva.org_unit USING btree (rollup_region_no, rollup_dist_no);
CREATE INDEX "i7$_org_unit" ON silva.org_unit USING btree (rollup_region_code, rollup_dist_code);
CREATE UNIQUE INDEX "i8$_org_unit" ON silva.org_unit USING btree (org_unit_no, org_unit_code);
CREATE UNIQUE INDEX "i9$_org_unit" ON silva.org_unit USING btree (org_unit_code, org_level_code, org_unit_no, org_unit_name);

-- Column comments

COMMENT ON COLUMN silva.org_unit.org_unit_no IS 'Unique physical identifier for the storage of organizational unit codes for the Ministry of Forests" offices. Values stored here are for the computer"s use only, and are not to be used by people as "ministry codes".';
COMMENT ON COLUMN silva.org_unit.org_unit_code IS 'Identifies any office within the ministry. First character identifies Exec, HQ branch, Region, or District. Next two chars identify the office name; next two the section (HQ Branch) or program (Region or District); last char identifies the subsection.';
COMMENT ON COLUMN silva.org_unit.org_unit_name IS 'The name or title of a ministry office or section; for example Kamloops Forest Region; Silviculture Branch; Kispiox Forest District Protection program.';
COMMENT ON COLUMN silva.org_unit.location_code IS 'Unique identifier for one of the office locations within the Ministry of Forests.';
COMMENT ON COLUMN silva.org_unit.area_code IS 'The 3 digit numer that identifies each telephone service area in a country.';
COMMENT ON COLUMN silva.org_unit.telephone_no IS 'The general office telephone number (excluding the area code) for a Ministry of Forests office.';
COMMENT ON COLUMN silva.org_unit.org_level_code IS 'Represents the organizational level of the Ministry, and is one of: D - District Forestry Office, E - Executive, H - Headquarters Branch, R - Regional Forestry Office.';
COMMENT ON COLUMN silva.org_unit.office_name_code IS 'Represents an office within a specific organizational level.';
COMMENT ON COLUMN silva.org_unit.rollup_region_no IS 'Identifier designating the region to which the org unit is rolled into for reporting purposes. Values stored here are for the computers use only, and are not to be used by people as "Ministry Codes".';
COMMENT ON COLUMN silva.org_unit.rollup_region_code IS 'Identifier designating the region to which the org unit is rolled into for reporting purposes.';
COMMENT ON COLUMN silva.org_unit.rollup_dist_no IS 'Identifier designating the district to which the org unit is rolled into for reporting purposes. Values stored here are for the computers use only and are not to be used by people as "Ministry Codes".';
COMMENT ON COLUMN silva.org_unit.rollup_dist_code IS 'Identifier designating the district to which the org unit is rolled into for reporting purposes.';
COMMENT ON COLUMN silva.org_unit.effective_date IS 'The date the Organizational Unit became effective as a valid Org Unit within the ministry"s organizational structure.';
COMMENT ON COLUMN silva.org_unit.expiry_date IS 'The date the Organizational Unit is Obsoleted and is no longer an active Org Unit within the ministrys organizational structure. At the determination of Human Resources Branch, an expired Org Unit may be retained for historical purposed, or may be deleted.';
COMMENT ON COLUMN silva.org_unit.update_timestamp IS 'The date and time of the last modification.';
