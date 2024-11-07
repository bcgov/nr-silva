export interface NotificationContent {
  title: string;
  subTitle: string;
  buttonLabel?: string;
  dismissIn?: number;
  type: 'info' | 'error' | 'success' | 'warning' | 'info-square' | 'error-square' | 'warning-alt';
  onClose: () => void;
}