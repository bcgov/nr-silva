-- THE.FOREST_CLIENT foreign key
ALTER TABLE THE.FOREST_CLIENT ADD CONSTRAINT FC_OU_FK FOREIGN KEY (ADD_ORG_UNIT) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.FOREST_CLIENT ADD CONSTRAINT FC_OU_IS_UPDATED_BY_FK FOREIGN KEY (UPDATE_ORG_UNIT) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);

-- THE.CLIENT_LOCATION foreign key
ALTER TABLE THE.CLIENT_LOCATION ADD CONSTRAINT CL_FC_FK FOREIGN KEY (CLIENT_NUMBER) REFERENCES THE.FOREST_CLIENT(CLIENT_NUMBER);
ALTER TABLE THE.CLIENT_LOCATION ADD CONSTRAINT CL_OU_FK FOREIGN KEY (UPDATE_ORG_UNIT) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.CLIENT_LOCATION ADD CONSTRAINT CL_OU_IS_CREATED_BY_FK FOREIGN KEY (ADD_ORG_UNIT) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);

-- THE.FOREST_FILE_CLIENT foreign key
ALTER TABLE THE.FOREST_FILE_CLIENT ADD CONSTRAINT FFC_CL_FK FOREIGN KEY (CLIENT_NUMBER,CLIENT_LOCN_CODE) REFERENCES THE.CLIENT_LOCATION(CLIENT_NUMBER,CLIENT_LOCN_CODE);

-- THE.CUT_BLOCK_CLIENT foreign key
ALTER TABLE THE.CUT_BLOCK_CLIENT ADD CONSTRAINT CBC_CL_FK FOREIGN KEY (CLIENT_NUMBER,CLIENT_LOCN_CODE) REFERENCES THE.CLIENT_LOCATION(CLIENT_NUMBER,CLIENT_LOCN_CODE);

-- THE.FOR_CLIENT_LINK foreign key
ALTER TABLE THE.FOR_CLIENT_LINK ADD CONSTRAINT FCL_CL_FK FOREIGN KEY (CLIENT_NUMBER,CLIENT_LOCN_CODE) REFERENCES THE.CLIENT_LOCATION(CLIENT_NUMBER,CLIENT_LOCN_CODE);

-- THE.RESULTS_ELECTRONIC_SUBMISSION foreign key
ALTER TABLE THE.RESULTS_ELECTRONIC_SUBMISSION ADD CONSTRAINT RES_CN_FK FOREIGN KEY (CLIENT_NUMBER,CLIENT_LOCN_CODE) REFERENCES THE.CLIENT_LOCATION(CLIENT_NUMBER,CLIENT_LOCN_CODE);

-- THE.SILVICULTURE_PROJECT foreign key
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_FC2_FK FOREIGN KEY (CLIENT_NUMBER) REFERENCES THE.FOREST_CLIENT (CLIENT_NUMBER);