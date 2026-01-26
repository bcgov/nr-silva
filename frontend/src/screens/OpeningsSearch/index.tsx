import { useEffect, useState, useRef } from "react";
import PageTitle from "@/components/PageTitle";
import {
  Grid,
  Column,
  Button,
} from "@carbon/react";

import FavOpeningSection from "@/components/FavouriteOpenings/FavOpeningSection";
import { useQuery } from "@tanstack/react-query";
import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { openingSearch } from "@/services/OpeningSearchService";
import {
  readOpeningSearchUrlParams,
  updateOpeningSearchUrlParams,
  hasActiveFilters,
} from "@/utils/OpeningSearchParamsUtils";
import OpeningsSearchInput from "@/components/OpeningsSearchInput";

import './styles.scss';

const OpeningsSearch = () => {

  const [searchParams, setSearchParams] = useState<OpeningSearchParamsType>();
  const hasAutoSearchedFromUrl = useRef(false);

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readOpeningSearchUrlParams();
    if (hasActiveFilters(urlParams)) {
      setSearchParams(urlParams);
      hasAutoSearchedFromUrl.current = true;
    }
  }, []);

  useEffect(() => {
    document.title = `Openings Search - Silva`;
    return () => {
      document.title = "Silva";
    };
  }, []);

  const openingSearchQuery = useQuery({
    queryKey: ['search', 'openings', searchParams],
    queryFn: () => openingSearch(searchParams),
    enabled: false,
    refetchOnMount: true
  });

  // If the page was loaded with URL params, perform a one-time auto-search
  useEffect(() => {
    if (hasAutoSearchedFromUrl.current && hasActiveFilters(searchParams)) {
      hasAutoSearchedFromUrl.current = false; // reset to prevent re-triggering
      openingSearchQuery.refetch();
    }
  }, [searchParams, openingSearchQuery]);

  /**
   * Handler to update a single field in searchParams
   */
  const handleSearchFieldChange = (field: keyof OpeningSearchParamsType, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: (value === '' || value === null) ? undefined : value,
    }));
  };

  /**
   * Trigger search manually
   */
  const handleSearch = () => {
    // Only search if params have active filters
    if (!hasActiveFilters(searchParams)) {
      return;
    }
    // Update URL when search is triggered
    updateOpeningSearchUrlParams(searchParams);
    // Revalidate query - if params are already set, this re-runs the query
    openingSearchQuery.refetch();
  };

  /**
   * Reset all search params
   */
  const handleReset = () => {
    setSearchParams(undefined);
  };

  return (
    <Grid className="default-grid openings-search-grid">
      <Column sm={4} md={8} lg={16}>
        <PageTitle title="Openings Search" />
      </Column>

      <Column sm={4} md={8} lg={16} className="bookmark-col">
        <FavOpeningSection />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <OpeningsSearchInput searchParams={searchParams} onSearchParamsChange={handleSearchFieldChange} />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
          <Button onClick={handleSearch} disabled={!hasActiveFilters(searchParams)}>
            Search
          </Button>
          <Button onClick={handleReset} kind="secondary">
            Reset
          </Button>
        </div>
      </Column>

      {/* Search form will go here */}
      {/* Use handleSearchFieldChange(field, value) to update individual fields */}
      {/* Use handleSearchParamsChange({ field1: value1, field2: value2 }) to update multiple at once */}

      <Column sm={4} md={8} lg={16}>
        {openingSearchQuery.isLoading && <div>Loading...</div>}
        {openingSearchQuery.isError && <div>Error loading results</div>}
        {openingSearchQuery.data && <div>Results: {openingSearchQuery.data.page?.totalElements} items</div>}
      </Column>
    </Grid >
  );
};

export default OpeningsSearch;
