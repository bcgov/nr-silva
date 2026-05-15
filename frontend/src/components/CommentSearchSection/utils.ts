import { CommentSearchParams } from '@/types/ApiType';

export const hasCommentSearchFilters = (params: CommentSearchParams | undefined): boolean => {
  if (!params) return false;

  const excludeKeys = new Set(['page', 'size']);

  return Object.entries(params).some(([key, value]) => {
    if (excludeKeys.has(key)) return false;
    if (Array.isArray(value)) return value.length > 0;
    return value !== undefined && value !== null && value !== '';
  });
};

export const readCommentSearchUrlParams = (): Partial<CommentSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<CommentSearchParams> = {};

  const searchTerm = searchParams.get('searchTerm');
  if (searchTerm) params.searchTerm = searchTerm;

  const commentLocation = searchParams.getAll('commentLocation');
  if (commentLocation.length > 0) params.commentLocation = commentLocation;

  const clientNumbers = searchParams.getAll('clientNumbers');
  if (clientNumbers.length > 0) params.clientNumbers = clientNumbers;

  const orgUnits = searchParams.getAll('orgUnits');
  if (orgUnits.length > 0) params.orgUnits = orgUnits;

  const updateDateStart = searchParams.get('updateDateStart');
  if (updateDateStart) params.updateDateStart = updateDateStart;

  const updateDateEnd = searchParams.get('updateDateEnd');
  if (updateDateEnd) params.updateDateEnd = updateDateEnd;

  const page = searchParams.get('page');
  if (page) {
    const pageNum = Number.parseInt(page, 10);
    if (Number.isFinite(pageNum)) params.page = pageNum;
  }

  const size = searchParams.get('size');
  if (size) {
    const sizeNum = Number.parseInt(size, 10);
    if (Number.isFinite(sizeNum)) params.size = sizeNum;
  }

  return params;
};

export const updateCommentSearchUrlParams = (params?: Partial<CommentSearchParams>): void => {
  const searchParams = new URLSearchParams();

  if (!params) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }

  if (params.searchTerm) {
    searchParams.append('searchTerm', params.searchTerm);
  }

  if (params.commentLocation && Array.isArray(params.commentLocation)) {
    params.commentLocation.forEach((v) => searchParams.append('commentLocation', v));
  }

  if (params.clientNumbers && Array.isArray(params.clientNumbers)) {
    params.clientNumbers.forEach((v) => searchParams.append('clientNumbers', v));
  }

  if (params.orgUnits && Array.isArray(params.orgUnits)) {
    params.orgUnits.forEach((v) => searchParams.append('orgUnits', v));
  }

  if (params.updateDateStart) {
    searchParams.append('updateDateStart', params.updateDateStart);
  }

  if (params.updateDateEnd) {
    searchParams.append('updateDateEnd', params.updateDateEnd);
  }

  if (params.page !== undefined) {
    searchParams.append('page', String(params.page));
  }

  if (params.size !== undefined) {
    searchParams.append('size', String(params.size));
  }

  const queryString = searchParams.toString();
  const newUrl = queryString ? `${window.location.pathname}?${queryString}` : window.location.pathname;
  window.history.replaceState({}, '', newUrl);
};
