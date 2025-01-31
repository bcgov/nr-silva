import React, { useState, useEffect } from "react";
import {
  Search,
  Button,
  Dropdown,
  FilterableMultiSelect,
  FlexGrid,
  Row,
  Column,
  DismissibleTag,
  Popover,
  PopoverContent,
  TableToolbar,
  TableToolbarContent,
  TableContainer,
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";

// Custom components
import OpeningTableFilter from "@/components/OpeningTableFilter";
import SearchTable from "@/components/SearchTable";
import OpeningsMap from "@/components/OpeningsMap";
import SearchFilterBar from "@/components/SilvicultureSearch/Openings/SearchFilterBar";
import SearchTableColumnSelector from "@/components/SearchTableColumnSelector";

// API Requests
import {
  fetchCategories,
  fetchOrgUnits,
  fetchOpenings,
  CodeDescription,
  OrgUnit,
  PagedResult,
  OpeningItem,
  OpeningSearchFilters,
} from "@/services/search/openings";

//Types
import { sortItems } from "@/utils/multiSelectSortUtils";
import {
  searchScreenColumns,
  defaultsearchScreenColumnsSelection,
} from "@/constants/tableConstants";
import {
  SelectEvents,
  TextInputEvent,
  IdTextValueData,
} from "@/types/GeneralTypes";
import { ITableHeader } from "@/types/TableHeader";

// Styles and others
import "./index.scss";

const SearchTab: React.FC = () => {
  // Search Filters itself
  const [filters, setFilters] = useState<OpeningSearchFilters>({});
  // Data for multi-selects
  const [categories, setCategories] = useState<CodeDescription[]>([]);
  const [orgUnits, setOrgUnits] = useState<OrgUnit[]>([]);

  // Search results and state
  const [page, setPage] = useState<{ page: number; size: number }>({
    page: 1,
    size: 20,
  });
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isFetched, setIsFetched] = useState<boolean>(false);
  const [searchResults, setSearchResults] = useState<PagedResult<OpeningItem>>({
    data: [],
    hasNextPage: false,
    pageIndex: 0,
    perPage: 0,
    totalItems: 0,
    totalPages: 0,
  });

  // Suplemtary state for maping things
  const [selectedHeaders, setSelectedHeaders] =
    useState<ITableHeader[]>(searchScreenColumns);
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] =
    useState<boolean>(false);
  const [showMap, setShowMap] = useState<boolean>(false);
  const [showColumnEditor, setShowColumnEditor] = useState<boolean>(false);
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const cleanValues = (filterValues: OpeningSearchFilters) => {
    return Object.fromEntries(
      Object.entries(filterValues).filter(
        ([_, v]) =>
          v !== undefined &&
          v !== null &&
          (Array.isArray(v) ? v.length > 0 : true)
      )
    );
  };

  const setValue = (value: Partial<OpeningSearchFilters>) => {
    setTimeout(() => {
      setFilters(cleanValues({ ...filters, ...value }));
    }, 0);
  };

  const clearFilter = (key: string, value: string | undefined) => {
    if (value) {
      const values = (
        filters[key as keyof typeof filters] as Array<string>
      )?.filter((item) => item !== value);
      setValue({ [key]: values });
    } else {
      setValue({ [key]: undefined });
    }
  };

  const clearFilters = () => {
    setTimeout(() => {
      setFilters({});
    }, 0);
  };

  const onSearch = async () => {
    setIsLoading(true);
    setIsFetched(false);
    setIsOpen(false);

    if (hasFilters(true)) {
      const openings = await fetchOpenings({
        ...filters,
        page: page.page - 1,
        perPage: page.size,
      });
      setSearchResults(openings);
    }

    setIsLoading(false);
    setIsFetched(true);
  };

  const onSearchTermChange = (e: TextInputEvent) => {
    setValue({ mainSearchTerm: e.target.value });
  };

  const setMuliSelectValue = (
    field: string,
    value: IdTextValueData[] | CodeDescription[] | OrgUnit[]
  ) => {
    if (
      field === "orgUnit" &&
      value.every((item): item is OrgUnit => "orgUnitCode" in item)
    ) {
      setValue({ orgUnit: value.map((item: OrgUnit) => item.orgUnitCode) });
    } else if (
      field === "category" &&
      value.every((item): item is CodeDescription => "code" in item)
    ) {
      setValue({ category: value.map((item: CodeDescription) => item.code) });
    } else if (
      field === "status" &&
      value.every((item): item is IdTextValueData => "value" in item)
    ) {
      setValue({ statusList: value.map((item: IdTextValueData) => item.id) });
    }
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") onSearch();
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

  const countFilters = () => {
    return Object.keys(filters).filter(
      (key) => key !== "mainSearchTerm" && filterHasValue(key)
    ).length;
  };

  useEffect(() => {
    // Only trigger pagination when there are filters
    if (hasFilters(true)) {
      onSearch();
    }
  }, [page]);

  useEffect(() => {
    //When all filters are cleared, clear the search results
    if (!hasFilters(true)) {
      setSearchResults({
        data: [],
        hasNextPage: false,
        pageIndex: 0,
        perPage: 0,
        totalItems: 0,
        totalPages: 0,
      });
      setIsFetched(false);
      setIsLoading(false);
      setPage({ page: 1, size: 20 });
    }
  }, [filters]);

  useEffect(() => {
    // This is to circunvent the lack of label property for placeholder on a multi-select
    const categoryInput = document.querySelector(
      "div.category-multi-select div div input"
    );
    categoryInput?.setAttribute("placeholder", "Category");

    // This is to circunvent the lack of label property for placeholder on a multi-select
    const orgUnitInput = document.querySelector(
      "div.orgunit-multi-select div div input"
    );
    orgUnitInput?.setAttribute("placeholder", "Org unit");

    fetchCategories().then(setCategories);
    fetchOrgUnits().then(setOrgUnits);
  }, []);

  return (
    <>
      <FlexGrid className="openings-searchbar-container">
        <Row>
          <Column sm={4} md={8} lg={16} max={14} className="p-0">
            <FlexGrid className="opening-searchbar-inner">
              <Row>
                <Column sm={4} md={8} lg={16} max={7} className="p-0 mb-1">
                  <Search
                    size="md"
                    placeholder="Search by opening ID, opening number or file ID"
                    labelText="Search"
                    closeButtonLabelText="Clear search input"
                    id={`search-1`}
                    onChange={onSearchTermChange}
                    onKeyDown={onKeyDown}
                    value={filters.mainSearchTerm}
                  />
                </Column>

                <Column sm={4} md={8} lg={5} max={3} className="p-0 mb-1">
                  <FilterableMultiSelect
                    label="Enter or choose a category"
                    id="category-multiselect"
                    className="multi-select category-multi-select ms-1"
                    items={categories}
                    itemToString={(item: CodeDescription) =>
                      item ? `${item.code} - ${item.description}` : ""
                    }
                    selectionFeedback="top-after-reopen"
                    onChange={(e: SelectEvents) => {
                      setMuliSelectValue("category", e.selectedItems);
                    }}
                    selectedItems={
                      filters.category
                        ? categories.filter((item) =>
                            filters.category?.includes(item.code)
                          )
                        : []
                    }
                    sortItems={sortItems}
                  />
                </Column>

                <Column sm={4} md={8} lg={5} max={3} className="p-0 mb-1">
                  <FilterableMultiSelect
                    label="Enter or choose an org unit"
                    id="orgunit-multiselect"
                    className="multi-select orgunit-multi-select ms-1"
                    items={orgUnits}
                    itemToString={(item: OrgUnit) =>
                      item ? `${item.orgUnitCode} - ${item.orgUnitName}` : ""
                    }
                    selectionFeedback="top-after-reopen"
                    onChange={(e: SelectEvents) => {
                      setMuliSelectValue("orgUnit", e.selectedItems);
                    }}
                    selectedItems={
                      filters.orgUnit
                        ? orgUnits.filter((item) =>
                            filters.orgUnit?.includes(item.orgUnitCode)
                          )
                        : []
                    }
                    sortItems={sortItems}
                  />
                </Column>

                <Column sm={4} md={8} lg={6} max={3} className="p-0">
                  <div
                    className="advanced-search-field ms-1"
                    onClick={() => setIsOpen(!isOpen)}
                    onKeyUp={(e) => {
                      if (e.key === "Enter") setIsOpen(!isOpen);
                    }}
                    role="button"
                    tabIndex={0}
                  >
                    {hasFilters(false) && (
                      <DismissibleTag
                        className="mx-1"
                        type="high-contrast"
                        text={"+" + countFilters()}
                        onClose={() => clearFilters()}
                      ></DismissibleTag>
                    )}
                    <p className={hasFilters(false) ? "text-active" : ""}>
                      Advanced Search
                    </p>
                    {isOpen ? (
                      <Icons.ChevronSortUp />
                    ) : (
                      <Icons.ChevronSortDown />
                    )}
                  </div>
                  <Popover
                    isTabTip
                    open={isOpen}
                    onClose={() => setIsOpen(false)}
                    align={
                      document?.dir === "rtl" ? "bottom-right" : "bottom-left"
                    }
                    className="filter-popover"
                  >
                    <PopoverContent>
                      <OpeningTableFilter
                        filters={filters}
                        onFilterChange={setFilters}
                        categories={categories.map(({ code, description }) => ({
                          id: code,
                          text: description,
                        }))}
                        orgUnits={orgUnits.map(
                          ({ orgUnitCode, orgUnitName }) => ({
                            id: orgUnitCode,
                            text: orgUnitName,
                          })
                        )}
                      />
                    </PopoverContent>
                  </Popover>
                </Column>
              </Row>
            </FlexGrid>
          </Column>

          <Column sm={4} md={8} lg={16} max={2} className="p-0">
            <Button
              className="search-button ms-xl-2"
              renderIcon={Icons.Search}
              type="button"
              size="md"
              onClick={onSearch}
            >
              Search
            </Button>
          </Column>
        </Row>

        <Row>
          <Column lg={14} className="p-0">
            {hasFilters(false) && (
              <SearchFilterBar
                filters={filters}
                clearFilter={clearFilter}
                clearFilters={clearFilters}
              />
            )}
          </Column>
        </Row>
      </FlexGrid>

      {showMap && (
        <div
          className="search-spatial-container row p-0"
          data-testid="openings-map"
        >
          <div className="leaflet-container">
            <OpeningsMap
              openingId={null}
              openingIds={selectedOpeningIds}
              setOpeningPolygonNotFound={setOpeningPolygonNotFound}
            />
          </div>
        </div>
      )}
      <div className="row">
        {isFetched && searchResults.data.length > 0 && (
          <TableContainer className="search-data-table">
            <TableToolbar aria-label="data table toolbar">
              <TableToolbarContent>
                <Button
                  iconDescription="Show Map"
                  data-testid="toggle-spatial"
                  tooltipposition="bottom"
                  kind="ghost"
                  onClick={() => setShowMap(!showMap)}
                  renderIcon={Icons.Location}
                  size="md"
                >
                  {showMap ? "Hide map" : "Show map"}
                </Button>
                <Popover
                  open={showColumnEditor}
                  isTabTip
                  align={
                    document?.dir === "rtl" ? "bottom-left" : "bottom-right"
                  }
                  onRequestClose={() => setShowColumnEditor(false)}
                >
                  <Button
                    iconDescription="Edit Columns"
                    data-testid="edit-columns"
                    tooltipposition="bottom"
                    kind="ghost"
                    onClick={() => setShowColumnEditor(!showColumnEditor)}
                    renderIcon={Icons.Column}
                    size="md"
                  >
                    Edit columns
                  </Button>
                  <PopoverContent className="edit-column-content">
                    <SearchTableColumnSelector
                      headers={selectedHeaders}
                      defaultSelection={defaultsearchScreenColumnsSelection}
                      onChangeSelection={setSelectedHeaders}
                    />
                  </PopoverContent>
                </Popover>
              </TableToolbarContent>
            </TableToolbar>
          </TableContainer>
        )}
        <SearchTable
          headers={selectedHeaders}
          rows={searchResults.data}
          loading={isLoading}
          fetched={isFetched}
          total={searchResults.totalItems}
          entriesPerPage={searchResults.perPage}
          currentPage={searchResults.pageIndex + 1}
          onPageChange={(pageNumber: number) =>
            setPage({ page: pageNumber, size: page.size })
          }
          onSizeChange={(pageNumber: number, size: number) =>
            setPage({ page: pageNumber, size })
          }
        />
      </div>
    </>
  );
};

export default SearchTab;
