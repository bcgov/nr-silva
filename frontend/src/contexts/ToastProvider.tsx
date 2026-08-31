import { useEffect, type ReactNode } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useTheme } from '@/utils/ThemeProvider';

interface ToastProviderProps {
  children: ReactNode;
}

const ToastProvider = ({ children }: ToastProviderProps) => {
  const { theme } = useTheme();

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
        className="default-toast-container"
        position="top-right"
        autoClose={5000}
        closeButton
        newestOnTop
        theme="colored"
      />
    </>
  );
};

export default ToastProvider;
