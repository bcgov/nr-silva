import { CommentSearchParams } from '@/types/ApiType';
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from '@/utils/SearchUtils';

const CONFIG: ParamConfig<CommentSearchParams> = {
  strings: ['searchTerm', 'updateDateStart', 'updateDateEnd'],
  numbers: ['page', 'size'],
  arrays: ['commentLocation', 'clientNumbers', 'orgUnits'],
};

export const hasCommentSearchFilters = (params: CommentSearchParams | undefined): boolean =>
  hasActiveSearchFilters(params, ['page', 'size']);

export const readCommentSearchUrlParams = (
  search: string = window.location.search
): Partial<CommentSearchParams> => readUrlParamsWithConfig<CommentSearchParams>(search, CONFIG);

export const updateCommentSearchUrlParams = (params?: Partial<CommentSearchParams>): void =>
  updateUrlParamsWithConfig<CommentSearchParams>(params, CONFIG);
