import { OpeningSearchResponseDto } from "./OpeningTypes";

export interface ITableHeader {
  key: string;
  header: string;
  selected: boolean;
  elipsis?: boolean;
}

export type TableHeaderType<T> = {
  key: T;
  header: string;
  selected?: boolean;
}

export type OpendingHeaderKeyType = keyof OpeningSearchResponseDto | 'actions';

export type OpeningHeaderType = TableHeaderType<OpendingHeaderKeyType>;
