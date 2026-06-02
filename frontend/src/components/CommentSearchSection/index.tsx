import { useEffect, useRef, useState } from "react";
import { Column, Grid, Button, Stack, InlineLoading, Pagination } from "@carbon/react";
import CommentSearchInput from "@/components/CommentSearchSection/CommentSearchInput";
import { CommentSearchParams } from "@/types/ApiType";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";
import { CircleDash, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { isAuthRefreshInProgress } from "@/constants/tanstackConfig";
import EmptySection from "@/components/EmptySection";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import CommentSearchCard from "./CommentSearchCard";
import { readCommentSearchUrlParams, updateCommentSearchUrlParams, hasCommentSearchFilters } from "./utils";
import { COMMENT_KEYWORD_MIN_LENGTH, COMMENT_KEYWORD_MAX_LENGTH } from "./constants";
import useScrollToSearchResults from "@/hooks/useScrollToSearchResults";

import "./styles.scss";

const CommentSearchSection = () => {
  const [searchParams, setSearchParams] = useState<CommentSearchParams>();
  const [queryParams, setQueryParams] = useState<CommentSearchParams>();
  const [showValidation, setShowValidation] = useState(false);
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => PageSizesConfig[0]!);
  const resultsRef = useRef<HTMLDivElement>(null);
  const shouldScrollRef = useRef(false);
  const emptyResultsRef = useRef<HTMLDivElement>(null);

  const isSearchTermValid = (params?: CommentSearchParams) =>
    !!params?.searchTerm &&
    params.searchTerm.length >= COMMENT_KEYWORD_MIN_LENGTH &&
    params.searchTerm.length <= COMMENT_KEYWORD_MAX_LENGTH;

  const commentSearchQuery = useQuery({
    queryKey: ['search', 'comments', queryParams],
    queryFn: () =>
      API.SearchEndpointService.searchComments(
        queryParams!.searchTerm!,
        queryParams?.commentLocation as Parameters<typeof API.SearchEndpointService.searchComments>[1] ?? undefined,
        queryParams?.clientNumbers,
        queryParams?.orgUnits,
        queryParams?.updateDateStart,
        queryParams?.updateDateEnd,
        queryParams?.page,
        queryParams?.size ?? PageSizesConfig[0]!,
        queryParams?.sort,
      ),
    enabled: !!queryParams && isSearchTermValid(queryParams),
  });

  // On page load, read URL params and prefill search (one time)
  useEffect(() => {
    const urlParams = readCommentSearchUrlParams();
    if (hasCommentSearchFilters(urlParams) || urlParams.page !== undefined || urlParams.size !== undefined) {
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

  useScrollToSearchResults(
    resultsRef,
    emptyResultsRef,
    shouldScrollRef,
    commentSearchQuery.isLoading,
    commentSearchQuery.data,
    commentSearchQuery.data?.page?.totalElements,
  );

  const handleSearchFieldChange = (field: keyof CommentSearchParams, value: unknown) => {
    setSearchParams((prev) => ({
      ...prev,
      [field]: value === '' || value === null ? undefined : value,
    }));
  };

  const handleSearch = () => {
    setShowValidation(true);
    const paramsWithPagination: CommentSearchParams = {
      ...searchParams,
      page: DEFAULT_PAGE_NUM,
      size: PageSizesConfig[0]!,
    };

    if (!isSearchTermValid(paramsWithPagination)) {
      return;
    }

    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    setSearchParams(paramsWithPagination);
    setQueryParams(paramsWithPagination);
    updateCommentSearchUrlParams(paramsWithPagination);
    shouldScrollRef.current = true;
  };

  const handleReset = () => {
    setSearchParams(undefined);
    setQueryParams(undefined);
    setShowValidation(false);
    setCurrPageNumber(DEFAULT_PAGE_NUM);
    setCurrPageSize(PageSizesConfig[0]!);
    updateCommentSearchUrlParams(undefined);
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;
    if (!queryParams) return;

    const paramsWithPagination: CommentSearchParams = {
      ...queryParams,
      page: nextPageNum,
      size: nextPageSize,
    };

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setQueryParams(paramsWithPagination);
    updateCommentSearchUrlParams(paramsWithPagination);
  };

  return (
    <Grid className="default-grid comment-search-section">
      <Column sm={4} md={8} lg={16}>
        <CommentSearchInput
          searchParams={searchParams}
          handleSearchFieldChange={handleSearchFieldChange}
          showValidation={showValidation}
          onSearch={handleSearch}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Grid className="default-sub-grid">
          <Column sm={4} md={4} lg={6} max={4}>
            <Button
              className="default-button"
              onClick={handleReset}
              kind="secondary"
              renderIcon={CircleDash}
            >
              Clear all
            </Button>
          </Column>
          <Column sm={4} md={4} lg={6} max={4}>
            <Button
              className="default-button"
              onClick={handleSearch}
              renderIcon={Search}
            >
              Search
            </Button>
          </Column>
        </Grid>
      </Column>

      {isSearchTermValid(queryParams) ? (
        <Column className="full-width-col" sm={4} md={8} lg={16}>
          <div className="search-table-banner" ref={resultsRef}>
            <Stack className="search-result-title-section" orientation="vertical">
              <h5 className="search-result-title">Search results</h5>
              <Stack
                className="search-result-sub-title-section"
                orientation="horizontal"
                gap={commentSearchQuery.isLoading || isAuthRefreshInProgress() ? 4 : 2}
              >
                <p className="search-result-subtitle">Total search results:</p>
                {commentSearchQuery.isLoading || isAuthRefreshInProgress() ? (
                  <InlineLoading />
                ) : (
                  <p className="search-result-subtitle">
                    {commentSearchQuery.data?.page?.totalElements ?? 0}
                  </p>
                )}
              </Stack>
            </Stack>
          </div>

          {!commentSearchQuery.isLoading && !isAuthRefreshInProgress() ? (
            commentSearchQuery.data?.page?.totalElements &&
              commentSearchQuery.data.page.totalElements > 0 ? (
              <>
                {commentSearchQuery.data?.content?.map((comment, idx) => (
                  <CommentSearchCard key={`${comment.openingId}-${idx}`} commentDto={comment} keyword={queryParams?.searchTerm ?? ''} index={idx} />
                ))}
                <Pagination
                  className="default-pagination-white"
                  page={currPageNumber + 1}
                  pageSize={currPageSize}
                  pageSizes={PageSizesConfig}
                  totalItems={commentSearchQuery.data?.page?.totalElements ?? 0}
                  onChange={handlePagination}
                />
              </>
            ) : (
              <div ref={emptyResultsRef}>
                <EmptySection
                  title="No results found"
                  description="Try adjusting your search criteria."
                  icon="Search"
                />
              </div>
            )
          ) : null}
        </Column>
      ) : null}
    </Grid>
  );
};

export default CommentSearchSection;

