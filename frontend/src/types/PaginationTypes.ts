export type PaginatedResponseType<T> = {
  content: T[],
  page: PageType,
}

export type PaginationConfigType = {
  page: number,
  size: number
}

export type PageType = PaginationConfigType & {
  totalElements: number,
  totalPages: number,
}

export type SortDirectionType = 'ASC' | 'DESC' | 'NONE';
