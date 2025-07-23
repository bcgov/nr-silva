import React, { createContext, useContext, useState, ReactNode } from 'react';

type ModalKey = 'CREATE_OPENING';

interface ModalContextType {
  openModal: (key: ModalKey) => void;
  closeModal: () => void;
  modalKey: ModalKey | null;
  isOpen: boolean;
}

const ModalContext = createContext<ModalContextType | undefined>(undefined);

export const ModalProvider = ({ children }: { children: ReactNode }) => {
  const [modalKey, setModalKey] = useState<ModalKey | null>(null);
  const isOpen = modalKey !== null;

  const openModal = (key: ModalKey) => {
    setModalKey(key);
  };

  const closeModal = () => {
    setModalKey(null);
  };

  return (
    <ModalContext.Provider value={{ openModal, closeModal, modalKey, isOpen }}>
      {children}
    </ModalContext.Provider>
  );
};

export const useModal = (): ModalContextType => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error('useModal must be used within a ModalProvider');
  }
  return context;
};
