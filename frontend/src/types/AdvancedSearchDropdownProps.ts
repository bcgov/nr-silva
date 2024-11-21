import { OpeningSearchFilter } from '../types/OpeningSearchFilter';

export interface AdvancedSearchDropdownProps {
    toggleShowFilters: () => void; // Function to be passed as a prop
    handleFilterChange: (updatedFilters: Partial<OpeningSearchFilter>) => void;
    handleCheckboxChange : (value: string, group: string) => void;
  }
