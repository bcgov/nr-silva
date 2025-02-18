import React, { useEffect, useState } from "react";
import "./SearchFilterBar.scss";
import { DismissibleTag, Button, Grid, Column } from "@carbon/react";
import { OpeningSearchFilters } from "@/services/search/openings";

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
const formatKey = (key: string) => {
  return key
    .replace(/([a-z])([A-Z])/g, "$1 $2")
    .replace(/^\w/, (c) => c.toUpperCase());
};

const getActiveFilters = (filters: OpeningSearchFilters): ActiveFilter[] => {
  const activeFilters: ActiveFilter[] = [];

  for (const key in filters) {
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

  const hasFilters = (ignoreSearchBar: boolean) => {
    return Object.keys(filters).some(
      (key) =>
        (ignoreSearchBar || key !== "mainSearchTerm") && filterHasValue(key)
    );
  };

  if (!hasFilters(true)) {
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
