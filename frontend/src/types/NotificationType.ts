import { KindProps } from "@carbon/react/lib/components/Notification/Notification";

export interface NotificationContent {
  title: string;
  subTitle?: string;
  buttonLabel?: string;
  dismissIn?: number;
  type?: KindProps;
  onClose: () => void;
}
