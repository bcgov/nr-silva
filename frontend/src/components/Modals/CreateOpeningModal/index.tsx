import React, { useState } from "react";
import { Button, Column, Grid, Modal } from "@carbon/react";
import { useNavigate } from "react-router-dom";
import { useModal } from "@/contexts/ModalContext";
import SeedBoxIcon from '@/assets/icon/SeedBox.svg?react';
import SeedingWithdrawalIcon from '@/assets/icon/SeedingWithdrawal.svg?react';
import { GOV_FUNDED_OPENING, TENURED_OPENING } from "@/constants";
import { CreateOpeningRoute } from "@/routes/config";
import { OpeningTypes } from "@/types/OpeningTypes";

import ModalHead from "../ModalHead";
import ModalTileButton from "../ModalTileButton";

import './styles.scss';


const CreateOpeningModal = () => {
  const { isOpen, closeModal } = useModal();
  const navigate = useNavigate();
  const [selectedType, setSelectedType] = useState<OpeningTypes | undefined>();

  const handleTileClick = (openingType: OpeningTypes) => {
    setSelectedType(openingType);
  }

  const handleCreateClick = () => {
    if (!selectedType) return;

    const searchParams = new URLSearchParams({ type: selectedType });

    navigate(`${CreateOpeningRoute.path}?${searchParams.toString()}`);

    closeModal();
  };

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
            onClick={() => handleTileClick(TENURED_OPENING)}
          />
        </Column>

        <Column sm={4} md={4} lg={8}>
          <ModalTileButton
            id={GOV_FUNDED_OPENING}
            icon={<SeedingWithdrawalIcon width={48} height={48} />}
            title="Government funded silviculture regime or investment"
            subtitle="Register an opening to cover ministry responsibilities"
            selected={selectedType === GOV_FUNDED_OPENING}
            onClick={() => handleTileClick(GOV_FUNDED_OPENING)}
          />
        </Column>

        <Column sm={4} md={4} lg={8}>
          <Button className="modal-button" kind="secondary" onClick={() => closeModal()}>
            Cancel
          </Button>
        </Column>
        <Column sm={4} md={4} lg={8}>
          <Button className="modal-button" kind="primary" onClick={handleCreateClick}>
            Create an opening
          </Button>
        </Column>
      </Grid>
    </Modal>
  );
};

export default CreateOpeningModal;
