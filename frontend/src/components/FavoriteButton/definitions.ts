export interface FavoriteButtonProps {
  id?: string
  tooltipPosition: string;
  kind: "ghost" | "tertiary" | "primary" | "secondary" | "danger" | "danger--primary" | "danger--ghost" | "danger--tertiary" | undefined;
  size: "lg" | "md" | "sm" | "xl" | "2xl" | undefined;
  favorited: boolean;
  onFavoriteChange: (newStatus: boolean) => void;
  disabled?: boolean;
}
