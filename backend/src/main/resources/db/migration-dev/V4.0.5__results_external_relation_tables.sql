-- silva.forest_client foreign keys and references

ALTER TABLE silva.forest_client ADD CONSTRAINT fc_ou_fk FOREIGN KEY (add_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.forest_client ADD CONSTRAINT fc_ou_is_updated_by_fk FOREIGN KEY (update_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_fc2_fk FOREIGN KEY (client_number) REFERENCES silva.forest_client(client_number);
ALTER TABLE silva.client_location ADD CONSTRAINT cl_fc_fk FOREIGN KEY (client_number) REFERENCES silva.forest_client(client_number);

-- silva.client_location foreign keys and references

ALTER TABLE silva.client_location ADD CONSTRAINT cl_ou_fk FOREIGN KEY (update_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.client_location ADD CONSTRAINT cl_ou_is_created_by_fk FOREIGN KEY (add_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.results_electronic_submission ADD CONSTRAINT res_cn_fk FOREIGN KEY (client_number,client_locn_code) REFERENCES silva.client_location(client_number,client_locn_code);
ALTER TABLE silva.cut_block_client ADD CONSTRAINT cbc_cl_fk FOREIGN KEY (client_number,client_locn_code) REFERENCES silva.client_location(client_number,client_locn_code);
ALTER TABLE silva.for_client_link ADD CONSTRAINT fcl_cl_fk FOREIGN KEY (client_number,client_locn_code) REFERENCES silva.client_location(client_number,client_locn_code);
ALTER TABLE silva.forest_file_client ADD CONSTRAINT ffc_cl_fk FOREIGN KEY (client_number,client_locn_code) REFERENCES silva.client_location(client_number,client_locn_code);

-- silva.harvest_sale foreign keys

ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_ou_fk FOREIGN KEY (bcts_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_pmcd_fk FOREIGN KEY (payment_method_cd) REFERENCES silva.payment_method_code(payment_method_code);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_psccd FOREIGN KEY (plnd_sb_cat_code) REFERENCES silva.sb_category_code(sb_category_code);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_smcd_fk FOREIGN KEY (sale_method_code) REFERENCES silva.sale_method_code(sale_method_code);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_ssccd FOREIGN KEY (sold_sb_cat_code) REFERENCES silva.sb_category_code(sb_category_code);
ALTER TABLE silva.harvest_sale ADD CONSTRAINT hs_stcd_fk FOREIGN KEY (sale_type_cd) REFERENCES silva.sale_type_code(sale_type_code);

-- silva.org_unit foreign key references
ALTER TABLE silva.silv_admin_zone ADD CONSTRAINT saz_ou_fk FOREIGN KEY (org_unit_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.results_electronic_submission ADD CONSTRAINT res_ou_fk FOREIGN KEY (org_unit_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_ou_fk FOREIGN KEY (org_unit_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_geo_ou_fk FOREIGN KEY (geographic_district) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_ou_fk FOREIGN KEY (forest_district) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.opening ADD CONSTRAINT o_ou_fk FOREIGN KEY (admin_district_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.opening ADD CONSTRAINT o_ou_resides_within_fk FOREIGN KEY (geo_district_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.prov_forest_use ADD CONSTRAINT pfu_bcts_ou_fk FOREIGN KEY (bcts_org_unit) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.prov_forest_use ADD CONSTRAINT pfu_ou_fk FOREIGN KEY (forest_region) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_ou_fk FOREIGN KEY (org_unit_no) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tfalling_geographically_wit_fk FOREIGN KEY (geographic_distrct) REFERENCES silva.org_unit(org_unit_no);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_ou_fk FOREIGN KEY (forest_district) REFERENCES silva.org_unit(org_unit_no);
