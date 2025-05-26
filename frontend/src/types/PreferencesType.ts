import { OpeningHeaderType } from "@/types/TableHeader";

export type ThemeType = "white" | "g10" | "g90" | "g100";

export interface UserPreference {
  theme: ThemeType;
  visibleColumns: Record<string, OpeningHeaderType[]>;
  [key: string]: any;
}