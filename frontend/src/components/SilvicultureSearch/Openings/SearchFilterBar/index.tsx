import React, { useEffect, useState } from 'react';
import './SearchFilterBar.scss'
import { Tag, Link } from '@carbon/react';
import { OpeningSearchFilters } from "@/services/search/openings";

interface SearchFilterBarProps {
  filters: OpeningSearchFilters;
  clearFilter: (key: string, value: string|undefined) => void;
  clearFilters: () => void;
}

interface ActiveFilter {
  key: string;
  value: string | boolean | undefined;
  isArray: boolean;
  display: string;
}

// Utility functions
const formatKey = (key: string) => {
  return key
    .replace(/([a-z])([A-Z])/g, '$1 $2')
    .replace(/^\w/, (c) => c.toUpperCase());
};

const reverseFormatKey = (formattedKey: string) => {
  return formattedKey
    .toLowerCase()
    .replace(/ ([a-z])/g, (match) => match.trim().toUpperCase());
};

const getActiveFilters = (filters: OpeningSearchFilters) : ActiveFilter[] => {
  const activeFilters: ActiveFilter[] = [];

  for (const key in filters) {
    if (filters && filters[key as keyof typeof filters] && filters[key as keyof typeof filters].length !== 0) {
      if (Array.isArray(filters[key as keyof typeof filters])) {
        const values = filters[key as keyof typeof filters] as Array<string>;        
        values.forEach((value) => activeFilters.push({key: key, value, isArray: true, display: `${formatKey(key)}: ${value}`}));
        //activeFilters.push(`${formatKey(key)}: ${filters[key as keyof typeof filters].join(', ')}`);
      } else {
        activeFilters.push({ key: key, value: filters[key as keyof typeof filters], isArray: false, display: `${formatKey(key)}: ${filters[key as keyof typeof filters]}`});
      }
    }
  }

  return activeFilters;
};


const SearchFilterBar: React.FC<SearchFilterBarProps> = ({ filters, clearFilter, clearFilters}) => {
  const [activeFilters, setActiveFilters] = useState<ActiveFilter[]>([]);
  
  useEffect(() => {
    setActiveFilters(getActiveFilters(filters));
  }, [filters]);

  const handleClearFilter = (filter: ActiveFilter) => {    
    console.log('handleClearFilter', filter);
    clearFilter(filter.key, filter.isArray ? filter.value : undefined);
  };

  return (
    <div className="search-filter-bar">
      <div className="d-flex flex-row align-items-center justify-content-between">
        <div className="d-flex flex-row filters-container">
          <div className="row gx-0">
            {activeFilters.map((filter, index) => (
              <div className="col-auto" key={index}>
                <Tag
                  filter
                  className="mx-1"
                  type="outline"
                  title="Clear Filter"
                  onClose={() => handleClearFilter(filter)}
                >
                  {filter.display}
                </Tag>
              </div>
            ))}
          </div>
        </div>
        <div className="clear-button-container">
          <Link className="clear-filters-button" onClick={clearFilters}>Clear all filters</Link>
        </div>
      </div>
    </div>
  );
};
export default SearchFilterBar;
