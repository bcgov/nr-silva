import React, { createContext, useContext, useState, ReactNode } from 'react';

/**
 * Enum of available modal keys for identifying which modal is open.
 */
type ModalKey = 'CREATE_OPENING';

/**
 * The context shape for modal operations and state.
 */
interface ModalContextType {
  /**
   * Opens a modal by key.
   * @param key - The modal key to open.
   */
  openModal: (key: ModalKey) => void;

  /**
   * Closes the currently open modal.
   */
  closeModal: () => void;

  /**
   * The key of the currently open modal, or null if none is open.
   */
  modalKey: ModalKey | null;

  /**
   * Whether a modal is currently open.
   */
  isOpen: boolean;
}

/**
 * The modal context instance
 */
const ModalContext = createContext<ModalContextType | undefined>(undefined);

/**
 * Provider component for modal context.
 * Wrap your app or layout in this to enable modal state and controls.
 *
 * @param children - The child components to receive modal context access.
 */
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

/**
 * React hook for accessing modal context.
 * Must be used within a ModalProvider.
 *
 * @throws Error if used outside of ModalProvider.
 * @returns Modal context with open/close functions and state.
 */
export const useModal = (): ModalContextType => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error('useModal must be used within a ModalProvider');
  }
  return context;
};
