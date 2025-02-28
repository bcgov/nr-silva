export type PagedResult<T> = {
  data: T[],
  pageIndex: number,
  perPage: number,
  totalPages: number,
  totalItems: number,
  hasNextPage: boolean,
}
