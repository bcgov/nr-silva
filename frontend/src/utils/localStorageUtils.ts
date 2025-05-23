import { UserPreference } from "@/types/PreferencesType";
import { OpeningHeaderType } from "@/types/TableHeader";

const defaultSearchTableHeaders: OpeningHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening Id', selected: true },
  { key: 'openingNumber', header: 'Opening number', selected: false },
  { key: 'forestFileId', header: 'File Id', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'orgUnitName', header: 'Org unit', selected: true },
  { key: 'status', header: 'Status', selected: true },
  { key: 'clientName', header: 'Client', selected: false },
  { key: 'timberMark', header: 'Timber mark', selected: false },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'openingGrossAreaHa', header: 'Gross Area (ha)', selected: true },
  { key: 'disturbanceStartDate', header: 'Disturbance date', selected: true },
  { key: 'regenDelayDate', header: 'Regen delay due date', selected: false },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];

const initialValue: UserPreference = {
  theme: "g100",
  visibleColumns: { silvicultureSearch: defaultSearchTableHeaders },
};

const loadUserPreference = (): UserPreference => {
  const storedPreference = localStorage.getItem("userPreference");
  return storedPreference ? JSON.parse(storedPreference) : initialValue;
};

const saveUserPreference = (preference: Partial<UserPreference>): UserPreference => {
  const updatedPreferences = { ...loadUserPreference(), ...preference, } as UserPreference;
  localStorage.setItem("userPreference", JSON.stringify(updatedPreferences));  
  return updatedPreferences;
};

export { defaultSearchTableHeaders, loadUserPreference, saveUserPreference };