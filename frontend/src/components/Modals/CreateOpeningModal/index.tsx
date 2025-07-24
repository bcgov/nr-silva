import React from "react";
import { Column, Grid, Modal } from "@carbon/react";
import { useModal } from "@/contexts/ModalContext";
import ModalHead from "../ModalHead";
import ModalTileButton from "../ModalTileButton";
import SeedBoxIcon from '@/assets/icon/SeedBox.svg?react';
import SeedingWithdrawalIcon from '@/assets/icon/SeedingWithdrawal.svg?react';
import './styles.scss';

import { GOV_FUNDED_OPENING, TENURED_OPENING } from "@/constants";

const CreateOpeningModal = () => {
  const { isOpen, closeModal } = useModal();

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
      <Grid className="tile-grid">
        <Column sm={4} md={4} lg={8}>
          <ModalTileButton
            id={TENURED_OPENING}
            icon={<SeedBoxIcon width={48} height={48} />}
            title="Harvest associated with tenure"
            subtitle="Register an opening to cover licensee or ministry responsibilities"
          />
        </Column>

        <Column sm={4} md={4} lg={8}>
          <ModalTileButton
            id={GOV_FUNDED_OPENING}
            icon={<SeedingWithdrawalIcon width={48} height={48} />}
            title="Government funded silviculture regime or investment"
            subtitle="Register an opening to cover ministry responsibilities"
          />
        </Column>
      </Grid>
    </Modal>
  );
};

export default CreateOpeningModal;
