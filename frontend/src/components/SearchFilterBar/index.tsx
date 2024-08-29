import React, { useEffect, useState } from 'react';
import './SearchFilterBar.scss'
import { Tag, Link } from '@carbon/react';

interface SearchFilterBarProps {
  filters: any;
}

const SearchFilterBar: React.FC<SearchFilterBarProps> = ({filters}) => {
  const [activeFilters, setActiveFilters] = useState<string[]>([]);

  useEffect(() => {
    setActiveFilters(getActiveFilters(filters));
  }, [filters]);

  const handleClearFilter = (filter: string) => {
    console.log(`Clearing filter: ${filter}`);
    // Add your logic to clear the filter here
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
                  {filter}
                </Tag>
              </div>
            ))}
          </div>
        </div>
        <div className="clear-button-container">
          <Link className="clear-filters-button">Clear filters</Link>
        </div>
      </div>
    </div>
  );
};

export default SearchFilterBar;

// Utility functions
const formatKey = (key: string) => {
  return key
    .replace(/([a-z])([A-Z])/g, '$1 $2')
    .replace(/^\w/, (c) => c.toUpperCase());
};

const getActiveFilters = (filters: any) => {
  const activeFilters = [];

  for (const key in filters) {
    if (filters[key] && filters[key].length !== 0) {
      if (Array.isArray(filters[key])) {
        activeFilters.push(`${formatKey(key)}: ${filters[key].join(', ')}`);
      } else {
        activeFilters.push(`${formatKey(key)}: ${filters[key]}`);
      }
    }
  }

  return activeFilters;
};
