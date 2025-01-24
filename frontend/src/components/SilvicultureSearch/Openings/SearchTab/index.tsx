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
  InlineNotification,
  DataTable,
  TableContainer,
  TableToolbar,
  TableToolbarContent,
  TableToolbarSearch,
  TableToolbarMenu,
  TableToolbarAction,
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
  Popover,
  PopoverContent,
  Layer,
} from "@carbon/react";
import * as Icons from "@carbon/icons-react";

// Custom components
import TableToolbarFilter from "@/components/TableToolbarFilter";
import SearchTable from "@/components/SilvicultureSearch/Openings/SearchTable";
import SearchBar from "@/components/SilvicultureSearch/Openings/SearchBar";
import OpeningsMap from "@/components/OpeningsMap";

// API Requests
import {
  fetchCategories,
  fetchOrgUnits,
  fetchOpenings,
  CodeDescription,
  OrgUnit,
  PagedResult,
  OpeningItem
} from "@/services/search/openings";

//Types
import { OpeningSearchFilters } from "@/services/search/openings";
import { TextValueData, sortItems } from "@/utils/multiSelectSortUtils";
import { searchScreenColumns } from "@/constants/tableConstants";

// Styles and others
import "./index.scss";

interface SelectEvent {
  selectedItems: TextValueData[];
}
interface TextInputEvent extends SyntheticEvent {
  target: HTMLInputElement;
}

const SearchTab: React.FC = () => {
  const [filters, setFilters] = useState<OpeningSearchFilters>({});
  const [categories, setCategories] = useState<CodeDescription[]>([]);
  const [orgUnits, setOrgUnits] = useState<OrgUnit[]>([]);

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isFetched, setIsFetched] = useState<boolean>(false);
  const [searchResults, setSearchResults] = useState<PagedResult<OpeningItem>>({data:[], hasNextPage: false, pageIndex:0, perPage:0, totalItems:0,totalPages:0 });


  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openingPolygonNotFound, setOpeningPolygonNotFound] = useState<boolean>(false);
  const [showMap, setShowMap] = useState<boolean>(false);
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const debugEvent = (name: string, e: never) => {
    console.log(`DEBUG :: ${name}`, e);
  };

  const setValue = (value: Partial<OpeningSearchFilters>) => {
    debugEvent("setValue", value as never);
    setTimeout(() => { setFilters({...filters, ...value}) },0);
  }

  const onSearchTermChange = (e: TextInputEvent) => {
    setValue({ mainSearchTerm: e.target.value });
  };

  const setMuliSelectValue = (field: string, value: TextValueData[] | CodeDescription[] | OrgUnit[]) => {    
    debugEvent("setMuliSelectValue", field as never);
    debugEvent("setMuliSelectValue", value as never);
    
    if (field === "orgUnit" && value.every((item): item is OrgUnit => 'orgUnitCode' in item)) {
      setValue({ orgUnit: value.map((item: OrgUnit) => item.orgUnitCode) });
    } else if (field === "category" && value.every((item): item is CodeDescription => 'code' in item)) {
      setValue({ category: value.map((item: CodeDescription) => item.code) });
    } else if (field === "status" && value.every((item): item is TextValueData => 'value' in item)) {
      setValue({ statusList: value.map((item: TextValueData) => item.value) }); 
    }
  }

  const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    debugEvent("onKeyDown", e as never);
    if(e.key === "Enter") onSearch();
  }

  const onSearch = async () => {
    debugEvent("onSearch", filters as never);
    console.log("Search button clicked",filters);

    setIsLoading(true);
    setIsFetched(false);

    const openings = await fetchOpenings(filters);
    setSearchResults(openings);

    setIsLoading(false);
    setIsFetched(true);
  }

  useEffect(() => {
    debugEvent("filters", filters as never);
  }, [filters]);

  useEffect(() => {

    // This is to circunvent the lack of label property for placeholder on a multi-select
    const categoryInput = document.querySelector("div.category-multi-select div div input");
    categoryInput?.setAttribute("placeholder", "Category");

    // This is to circunvent the lack of label property for placeholder on a multi-select
    const orgUnitInput = document.querySelector("div.orgunit-multi-select div div input");
    orgUnitInput?.setAttribute("placeholder", "Org unit");

    fetchCategories().then(setCategories);
    fetchOrgUnits().then(setOrgUnits);
  }, []);

  return (
    <div className="container-fluid p-0 pb-5 align-content-center">
      <FlexGrid className="openings-searchbar-container">
        
        <Row>
          <Column lg={8} max={8} className="p-0 mb-2 mb-lg-0">
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

          <Column lg={2} max={2} className="p-0 ml-2">
            <FilterableMultiSelect
              label="Enter or choose a category"
              id="category-multiselect"
              className="multi-select category-multi-select"                                
              items={categories}
              itemToString={(item: CodeDescription) => (item ? `${item.code} - ${item.description}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvent) => { setMuliSelectValue('category',e.selectedItems) }}
              selectedItems={ filters.category ? categories.filter(item => filters.category?.includes(item.code)) : []}
              sortItems={sortItems}
            />
          </Column>

          <Column lg={2} max={2} className="p-0 ml-2">
            <FilterableMultiSelect
              label="Enter or choose an org unit"
              id="orgunit-multiselect"
              className="multi-select orgunit-multi-select"              
              items={orgUnits}
              itemToString={(item: OrgUnit) => (item ? `${item.orgUnitCode} - ${item.orgUnitName}` : "")}
              selectionFeedback="top-after-reopen"
              onChange={(e: SelectEvent) => { setMuliSelectValue('orgUnit',e.selectedItems) }}
              selectedItems={ filters.orgUnit ? orgUnits.filter(item => filters.orgUnit?.includes(item.orgUnitCode)) : []}
              sortItems={sortItems}
            />
          </Column>

          <Column lg={2} max={2} className="p-0 ml-2">
            <Dropdown
              titleText=""
              id="advanced-search-dropdown"                
              items={[]}                
              onChange={(e: SelectEvent) => { debugEvent('Drop is down',e as never) }}
              onClick={(e: React.MouseEvent<HTMLDivElement>) => { debugEvent('Drop is clicked',e as never) }}
              label="Advanced search"
              selectedItem={null}
            />
          </Column>

          <Column sm={0} lg={1} max={1} className="p-0">
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
        <SearchTable
          headers={searchScreenColumns}
          rows={searchResults.data}
          loading={isLoading}
          fetched={isFetched}

          total={searchResults.totalItems}
          entriesPerPage={searchResults.perPage}
          currentPage={searchResults.pageIndex + 1}
          onPageChange={(page: number) => {}}
          onSizeChange={(page: number, size: number) => {}}
          />
      </div>
    </div>
  );
};

export default SearchTab;
