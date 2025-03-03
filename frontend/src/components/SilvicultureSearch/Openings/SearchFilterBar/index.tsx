import React, { useEffect, useState } from "react";
import { DismissibleTag, Button } from "@carbon/react";

// Custom types
import { OpeningSearchFilters } from "@/services/search/openings";
// Styles
import "./SearchFilterBar.scss";

const filterKeyValueMap = {
  mainSearchTerm: 'Search Term',
  orgUnit: 'Org Unit',
  category: 'Category',
  statusList: 'Status',
  myOpenings: 'My Openings',
  submittedToFrpa: 'FRPA Section 108',
  disturbanceDateStart: 'Disturbance date start',
  disturbanceDateEnd: 'Disturbance date end',
  regenDelayDateStart: 'Regen delay date start',
  regenDelayDateEnd: 'Regen delay date end',
  freeGrowingDateStart: 'Free growing date start',
  freeGrowingDateEnd: 'Free growing date end',
  updateDateStart: 'Update date start',
  updateDateEnd: 'Update date end',
  cuttingPermitId: 'Cutting permit id',
  cutBlockId: 'Cut block id',
  clientLocationCode: 'Client location code',
  clientNumber: 'Client number',
  timberMark: 'Timber mark'
};


interface SearchFilterBarProps {
  filters: OpeningSearchFilters;
  clearFilter: (key: string, value: string | undefined) => void;
  clearFilters: () => void;
}

interface ActiveFilter {
  key: string;
  value: string | boolean | string[] | undefined;
  isArray: boolean;
  display: string;
}

// Utility functions
const formatKey = (key: string) =>
  filterKeyValueMap[key as keyof typeof filterKeyValueMap] ||
  key
    .replace(/([a-z])([A-Z])/g, "$1 $2")
    .replace(/^\w/, (c) => c.toUpperCase());

const getActiveFilters = (filters: OpeningSearchFilters): ActiveFilter[] => {
  const activeFilters: ActiveFilter[] = [];

  for (const key in filters) {
    if (key === "mainSearchTerm") continue;

    if (filters && filters[key as keyof typeof filters]) {
      if (Array.isArray(filters[key as keyof typeof filters])) {
        const values = filters[key as keyof typeof filters] as Array<string>;
        values.forEach((value) =>
          activeFilters.push({
            key: key,
            value,
            isArray: true,
            display: `${formatKey(key)}: ${value}`,
          })
        );
      } else if (
        filters[key as keyof typeof filters] instanceof Boolean ||
        typeof filters[key as keyof typeof filters] === "boolean"
      ) {
        activeFilters.push({
          key: key,
          value: filters[key as keyof typeof filters],
          isArray: false,
          display: formatKey(key),
        });
      } else {
        activeFilters.push({
          key: key,
          value: filters[key as keyof typeof filters],
          isArray: false,
          display: `${formatKey(key)}: ${filters[key as keyof typeof filters]}`,
        });
      }
    }
  }

  return activeFilters;
};

const SearchFilterBar: React.FC<SearchFilterBarProps> = ({
  filters,
  clearFilter,
  clearFilters,
}) => {
  const [activeFilters, setActiveFilters] = useState<ActiveFilter[]>([]);

  useEffect(() => {
    setActiveFilters(getActiveFilters(filters));
  }, [filters]);

  const handleClearFilter = (filter: ActiveFilter) => {
    clearFilter(
      filter.key,
      filter.isArray ? filter.value?.toString() : undefined
    );
  };

  const filterHasValue = (key: string) => {
    const filterValue = filters[key as keyof typeof filters];
    if (filterValue && Array.isArray(filterValue)) {
      return filterValue.length > 0;
    }
    return filters[key as keyof typeof filters];
  };

  const hasFilters = () => {
    return Object.keys(filters).some(
      (key) => key !== "mainSearchTerm" && filterHasValue(key)
    );
  };

  if (!hasFilters()) {
    return <></>;
  }

  return (
    <div
      aria-label="Selected filters"
      role="group"
      className="filters-container"
    >
      {activeFilters.map((filter, index) => (
        <DismissibleTag
          key={`${filter.key}-${index}`}
          type="outline"
          text={filter.display}
          tagTitle={filter.display}
          title="Remove Filter"
          onClose={() => handleClearFilter(filter)}
        />
      ))}
      <Button kind="ghost" size="md" onClick={clearFilters}>
        Clear all filters
      </Button>
    </div>
  );
};
export default SearchFilterBar;
