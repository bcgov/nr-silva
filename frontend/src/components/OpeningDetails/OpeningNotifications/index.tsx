import { InlineNotification } from "@carbon/react";
import { OpeningDetailsNotificationDto } from "@/services/OpenApi";
import { statusOrder, statusToKind } from "./definitions";

import "./styles.scss";

type OpeningNotificationsProps = {
  notifications: OpeningDetailsNotificationDto[];
};

const OpeningNotifications = ({ notifications }: OpeningNotificationsProps) => {
  const sortedNotifications = [...notifications].sort(
    (a, b) => statusOrder[a.status] - statusOrder[b.status]
  );

  return (
    <div className="opening-notifications-container">
      {
        sortedNotifications.map((notification, idx) => (
          <InlineNotification
            key={`notification-${idx}`}
            data-testid={`notification-${idx}`}
            kind={statusToKind[notification.status]}
            title={notification.title!}
            subtitle={notification.description!}
            hideCloseButton={false}
            lowContrast={true}
          />
        ))
      }
    </div>

  );
}

export default OpeningNotifications;
