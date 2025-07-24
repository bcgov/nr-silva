import React from 'react';
import { useModal } from '@/contexts/ModalContext';
import CreateOpeningModal from './CreateOpeningModal';

const ModalRenderer = () => {
  const { modalKey } = useModal();

  if (!modalKey) return null;

  switch (modalKey) {
    case 'CREATE_OPENING':
      return (
        <CreateOpeningModal />
      )
    default:
      return null;
  }
};

export default ModalRenderer;
