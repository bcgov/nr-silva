import { useEffect, useState } from "react";
import { Column, Grid, Button } from "@carbon/react";
import ActivitySearchInput from "@/components/ActivitySearchInput";
import { ActivitySearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { readActivitySearchUrlParams, updateActivitySearchUrlParams, hasActivitySearchFilters } from "./utils";


const ActivitiesSearchSection = () => {
  const [searchParams, setSearchParams] = useState<ActivitySearchParams>();
  const [queryParams, setQueryParams] = useState<ActivitySearchParams>();
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readActivitySearchUrlParams();
    if (hasActivitySearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
      const nextPage = urlParams.page ?? DEFAULT_PAGE_NUM;
      const nextSize = urlParams.size ?? PageSizesConfig[0]!;
      const paramsWithPagination = {
        ...urlParams,
        page: nextPage,
        size: nextSize,
      };

      setSearchParams(paramsWithPagination);
      setCurrPageNumber(nextPage);
      setCurrPageSize(nextSize);
      setQueryParams(paramsWithPagination);
    }
  }, []);

  const handleSearchFieldChange = (field: keyof ActivitySearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    }));
  };

  /**
   * Trigger search manually
   */
  const handleSearch = () => {
    if (!hasActivitySearchFilters(searchParams)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setQueryParams(searchParams);
    updateActivitySearchUrlParams(searchParams);
  };

  /**
   * Reset all search params
   */
  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSelectedOpeningIds([]);
    updateActivitySearchUrlParams(undefined);
  };

  return (
    <Grid className="default-grid activity-search-section-grid">
      <Column sm={4} md={8} lg={16}>
        <ActivitySearchInput searchParams={searchParams} handleSearchFieldChange={handleSearchFieldChange} />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-sub-grid">
          <Column sm={4} md={4} lg={6} max={4}>
            <Button className="default-button" onClick={handleReset} kind="secondary" renderIcon={CircleDash}>
              Clear all
            </Button>
          </Column>
          <Column sm={4} md={4} lg={6} max={4}>
            <Button className="default-button" onClick={handleSearch} renderIcon={Search}>
              Search
            </Button>
          </Column>
        </Grid>
      </Column>
    </Grid>
  );
};

export default ActivitiesSearchSection;
