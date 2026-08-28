import { useEffect, type ReactNode } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface ToastProviderProps {
  children: ReactNode;
}

const ToastProvider = ({ children }: ToastProviderProps) => {
  useEffect(() => {
    const dismissToastsOnEscape = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        toast.dismiss();
      }
    };

    document.addEventListener('keydown', dismissToastsOnEscape);

    return () => {
      document.removeEventListener('keydown', dismissToastsOnEscape);
    };
  }, []);

  return (
    <>
      {children}
      <ToastContainer
        position="top-right"
        autoClose={5000}
        closeButton
        newestOnTop
      />
    </>
  );
};

export default ToastProvider;
