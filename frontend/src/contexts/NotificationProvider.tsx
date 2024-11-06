import React, { createContext, useState, useContext, useEffect, useMemo, ReactNode } from 'react';
import {ActionableNotification} from "@carbon/react";
import {NotificationContent} from "../types/NotificationType";


// 1. Define an interface for the context value
interface NotificationContextType{
  displayNotification: (notification: NotificationContent) => void;
}

// 2. Define an interface for the provider's props
interface NotificationProviderProps {
  children: ReactNode;
}

// 3. Create the context with a default value of `undefined`
const NotificationContext = createContext<NotificationContextType|undefined>(undefined);

// 4. Create the NotificationProvider component with explicit typing
export const NotificationProvider: React.FC<NotificationProviderProps> = ({ children }) => {
  const [notification, setNotification] = useState<NotificationContent | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    if(notification){
      setIsOpen(true);

      if(notification.dismissIn){
        setTimeout(() => {
          hideNotification();
        }, notification.dismissIn);
      }
    }
  });

  const displayNotification = (value: NotificationContent) => {
    setNotification(value);
    setIsOpen(true);
  }

  const hideNotification = () => {
    setNotification(null);
    setIsOpen(false);
  }

  const notificationAction = () => {
    notification?.onClose();
    hideNotification();    
  }

  const contextValue = useMemo(() => ({
    displayNotification
  }),[displayNotification]);

  return (
    <NotificationContext.Provider value={contextValue}>
      {children}
      {isOpen && notification && (
        <ActionableNotification
          className="fav-toast"
          title={notification.title}
          subtitle={notification.subTitle}
          lowContrast={true}
          kind={notification.type}
          role="status"
          closeOnEscape
          onClose={hideNotification}
          actionButtonLabel={notification.buttonLabel}
          onActionButtonClick = {notificationAction}

        />
      )}
    </NotificationContext.Provider>
  )

};


// This is a helper hook to use the Notification context more easily
// 5. Create a custom hook to consume the context safely
export const useNotification = () => {
  const context = useContext(NotificationContext);
  if(!context){
    throw new Error('useNotification must be used within a NotificationProvider');
  }
  return context;
}