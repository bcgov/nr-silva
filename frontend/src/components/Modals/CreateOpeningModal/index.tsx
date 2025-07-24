import React, { useState } from "react";
import { Button, Column, Grid, Modal } from "@carbon/react";
import { useModal } from "@/contexts/ModalContext";
import ModalHead from "../ModalHead";
import ModalTileButton from "../ModalTileButton";
import SeedBoxIcon from '@/assets/icon/SeedBox.svg?react';
import SeedingWithdrawalIcon from '@/assets/icon/SeedingWithdrawal.svg?react';
import { GOV_FUNDED_OPENING, TENURED_OPENING } from "@/constants";

import './styles.scss';

const CreateOpeningModal = () => {
  const { isOpen, closeModal } = useModal();
  const [selectedType, setSelectedType] = useState<typeof TENURED_OPENING | typeof GOV_FUNDED_OPENING | undefined>();

  const handleClick = (openingType: typeof TENURED_OPENING | typeof GOV_FUNDED_OPENING) => {
    setSelectedType(openingType);
  }


  return (
    <Modal
      passiveModal
      open={isOpen}
      modalHeading={
        <ModalHead
          helperTop="Create an opening"
          title="Define opening type"
          helperBottom="Select the type of opening you want to create to generate the opening ID (unique identifier)"
        />
      }
      onRequestClose={closeModal}
      className="create-opening-modal"
    >
      <Grid className="content-grid">
        <Column sm={4} md={4} lg={8}>
          <ModalTileButton
            id={TENURED_OPENING}
            icon={<SeedBoxIcon width={48} height={48} />}
            title="Harvest associated with tenure"
            subtitle="Register an opening to cover licensee or ministry responsibilities"
            selected={selectedType === TENURED_OPENING}
            onClick={() => handleClick(TENURED_OPENING)}
          />
        </Column>

        <Column sm={4} md={4} lg={8}>
          <ModalTileButton
            id={GOV_FUNDED_OPENING}
            icon={<SeedingWithdrawalIcon width={48} height={48} />}
            title="Government funded silviculture regime or investment"
            subtitle="Register an opening to cover ministry responsibilities"
            selected={selectedType === GOV_FUNDED_OPENING}
            onClick={() => handleClick(GOV_FUNDED_OPENING)}
          />
        </Column>

        <Column sm={4} md={4} lg={8}>
          <Button className="modal-button" kind="secondary">
            Cancel
          </Button>
        </Column>
        <Column sm={4} md={4} lg={8}>
          <Button className="modal-button" kind="primary">
            Create an opening
          </Button>
        </Column>
      </Grid>
    </Modal>
  );
};

export default CreateOpeningModal;
