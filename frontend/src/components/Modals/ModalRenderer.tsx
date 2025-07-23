import React from 'react';
import { Modal } from '@carbon/react';
import { useModal } from '@/contexts/ModalContext';

const ModalRenderer = () => {
  const { modalKey, isOpen, closeModal } = useModal();

  if (!modalKey) return null;

  switch (modalKey) {
    case 'CREATE_OPENING':
      return (
        <Modal
          open={isOpen}
          modalHeading="Are you sure?"
          primaryButtonText="Delete"
          secondaryButtonText="Cancel"
          onRequestClose={closeModal}
        >
        </Modal>
      );
    default:
      return null;
  }
};

export default ModalRenderer;
