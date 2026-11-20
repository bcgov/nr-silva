-- silva.silv_activity_xref foreign keys

ALTER TABLE silva.silv_activity_xref ADD CONSTRAINT sax_sam_fk FOREIGN KEY (silv_activity_measurement_code) REFERENCES silva.silv_activity_measurement_code(silv_activity_measurement_code);
ALTER TABLE silva.silv_activity_xref ADD CONSTRAINT sax_sbc_fk FOREIGN KEY (silv_base_code) REFERENCES silva.silv_base_code(silv_base_code);
ALTER TABLE silva.silv_activity_xref ADD CONSTRAINT sax_smc_fk FOREIGN KEY (silv_method_code) REFERENCES silva.silv_method_code(silv_method_code);
ALTER TABLE silva.silv_activity_xref ADD CONSTRAINT sax_stc_fk FOREIGN KEY (silv_technique_code) REFERENCES silva.silv_technique_code(silv_technique_code);


-- silva.silv_base_objective_xref foreign keys

ALTER TABLE silva.silv_base_objective_xref ADD CONSTRAINT sbox_sbc_fk FOREIGN KEY (silv_base_code) REFERENCES silva.silv_base_code(silv_base_code);
ALTER TABLE silva.silv_base_objective_xref ADD CONSTRAINT sbox_soc_fk FOREIGN KEY (silv_objective_code) REFERENCES silva.silv_objective_code(silv_objective_code);


-- silva.silv_base_funding_xref foreign keys

ALTER TABLE silva.silv_base_funding_xref ADD CONSTRAINT sbfx_sbc_fk FOREIGN KEY (silv_base_code) REFERENCES silva.silv_base_code(silv_base_code);
ALTER TABLE silva.silv_base_funding_xref ADD CONSTRAINT sbfx_sfsc_fk FOREIGN KEY (silv_fund_srce_code) REFERENCES silva.silv_fund_srce_code(silv_fund_srce_code);
