export type PaginatedResponseType<T> = {
  content: T[],
  page: PageType,
}

export type PageType = {
  size: number,
  number: number,
  totalElements: number,
  totalPages: number,
}
