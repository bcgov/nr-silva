export const PageSizesConfig = [
  20, 40, 60, 80, 100
];

export const DEFAULT_PAGE_NUM = 0 as const;

/** Cap fed to Carbon's Pagination to avoid rendering thousands of <option> elements */
export const MAX_PAGINATION_PAGES = 500 as const;

export const OddPageSizesConfig = [
  10, 30, 50, 70, 100
];

export const MAX_SEARCH_LENGTH = 50 as const;
