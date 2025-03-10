import React from "react";
import { Button, Column, DismissibleTag } from "@carbon/react";

import { OpeningSearchFilterType } from "./definitions";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { filterDisplayNameMap } from "./constants";

type OpeningFilterBarProps = {
  filters: OpeningSearchFilterType,
  setFilters: React.Dispatch<React.SetStateAction<OpeningSearchFilterType>>,
  handleClearFilters: () => void
}

const OpeningFilterBar = ({ filters, setFilters, handleClearFilters }: OpeningFilterBarProps) => {
  const getFilterDisplayName = (key: keyof OpeningSearchFilterType): string => {
    return filterDisplayNameMap[key] || '';
  };


  const handleClose = (key: keyof OpeningSearchFilterType, val: string | number | boolean | undefined) => {
    setFilters((prevFilters) => {
      const currentValue = prevFilters[key];

      if (Array.isArray(currentValue)) {
        // Remove the specific value from the array
        const updatedArray = currentValue.filter((item) =>
          (item as CodeDescriptionDto).code !== val
        );

        return {
          ...prevFilters,
          [key]: updatedArray.length > 0 ? updatedArray : [],
        };
      }

      return {
        ...prevFilters,
        [key]: '',
      };
    });
  };

  return (
    <Column className="opening-filter-bar-col" sm={4} md={8} lg={16}>
      {
        (Object.keys(filters) as (keyof OpeningSearchFilterType)[])
          .filter((filterKey) => {
            if (filterKey === 'mainSearchTerm' || filterKey === 'dateType') {
              return false;
            }

            const value = filters[filterKey];
            // Filter out:
            // - `null` or `undefined`
            // - Empty strings `""`
            // - Empty arrays `[]`
            if (value === undefined || value === null || value === "" || (Array.isArray(value) && value.length === 0)) {
              return false;
            }

            return true;
          })
          .map((filterKey) => {
            const filterVal = filters[filterKey];

            if (Array.isArray(filterVal)) {
              return filterVal.map((subVal: CodeDescriptionDto) => (
                <DismissibleTag
                  key={subVal.code}
                  className="silviculture-search-dismissible-tag"
                  size="md"
                  type="outline"
                  text={`${getFilterDisplayName(filterKey)}: ${(subVal as CodeDescriptionDto).code}`}
                  onClose={() => handleClose(filterKey, subVal.code)}
                />
              ))
            }
            return (
              <DismissibleTag
                className="silviculture-search-dismissible-tag"
                key={filterKey}
                size="md"
                type="outline"
                text={`${getFilterDisplayName(filterKey)}: ${filters[filterKey]}`}
                onClose={() => handleClose(filterKey, (filters[filterKey] as string | number | boolean | undefined))}
              />
            )
          })
      }
      <Button
        type="button"
        kind="ghost"
        size="sm"
        onClick={handleClearFilters}
      >
        Clear filters
      </Button>
    </Column>
  )
}

export default OpeningFilterBar;
