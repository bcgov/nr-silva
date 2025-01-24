import React from "react";
import {
  Button,
  FlexGrid,
  Row,
  Column,
  Tag,
  Link
} from "@carbon/react";

import { OpeningSearchFilters } from "@/services/search/openings";

interface FilterProps {
  filters: OpeningSearchFilters;
  onFilterChange: (updatedFilter: OpeningSearchFilters) => void;
}

interface FilterTagContent {
  key: string;
  value: string;
}

const SearchBar: React.FC<FilterProps> = ({filters,onFilterChange}) => {

  const hasFilters = () =>  Object.keys(filters).length > 0;
  
  const activeFilters = (currentFilters: OpeningSearchFilters) : FilterTagContent[] => {
    return Object
    .keys(currentFilters)
    .filter(key => {
      const value = currentFilters[key as keyof OpeningSearchFilters] as undefined | string | boolean | string[];
      if (typeof value === 'boolean') {
        return value;
      } else if (typeof value === 'string') {
        return value.length > 0;
      } else if (Array.isArray(value)) {
        return value.length > 0;
      }
      return false;
    })
    .map(key => {
      const value = currentFilters[key as keyof OpeningSearchFilters] as undefined | string | boolean | string[];
      if (typeof value === 'boolean') {
        return {key, value: key};
      } else if (Array.isArray(value)) {
        return  {key, value: `${key}: ${value.join(', ')}`};
      }
      return {key,value: `${key}: ${value}`};
    });    
  }

  const removeFilter = (filter: string) => {
    const {[filter as keyof OpeningSearchFilters]: _, ...rest} = filters;    
    onFilterChange(rest as OpeningSearchFilters);
  }
  
  return (    
      <FlexGrid className="openings-searchbar-container">
        <Row>
          <Column>          
            {activeFilters(filters).map((filter, index) => (              
              <Tag
                key={index}
                filter
                className="mx-1"
                type="outline"
                title="Clear Filter"
                onClose={() => removeFilter(filter.key)}
              >
                {filter.value}
              </Tag>
              ))
            }
          </Column>
          <Column>
          {hasFilters() && <Link className="clear-filters-button" onClick={() => onFilterChange({})}>Clear all filters</Link>}
          </Column>
        </Row>
      </FlexGrid>
  );
};

export default SearchBar;
