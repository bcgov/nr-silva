import React, { useState } from 'react';
import './SearchFilterBar.scss'
import { Tag, Link } from '@carbon/react';

interface SearchFilterBarProps {
  
}

const SearchFilterBar: React.FC<SearchFilterBarProps> = ({ }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [filters, setFilters] = useState({});

  return (
    <div className="search-filter-bar">
      <div className="d-flex flex-row align-items-center justify-content-between">
        <div className="d-flex flex-row filters-container">
          {/* here the list of all the applied filters will be shown */}
          <Tag
            filter
            className="mx-1"
            type="outline"
            title="Clear Filter"
            onClose={() => console.log("Status: Free Growing")}
          >
            {"Status: Free Growing"}
          </Tag>

          <Tag
            filter
            className="mx-1"
            type="outline"
            title="Clear Filter"
            onClose={() =>
              console.log("Opening filters: Openings created by me")
            }
          >
            {"Opening filters: Openings created by me"}
          </Tag>
        </div>
        <div className="clear-button-container">
          <Link className="clear-filters-button">Clear filters</Link>
        </div>
      </div>
    </div>
  );
};

export default SearchFilterBar;