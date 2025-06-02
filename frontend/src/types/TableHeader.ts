import { OpeningSearchResponseDto } from "./OpenApiTypes";

export type TableHeaderType<T> = {
  key: T;
  header: string;
  selected?: boolean;
  sortable?: boolean;
}

export type OpendingHeaderKeyType = keyof OpeningSearchResponseDto | 'actions';

export type OpeningHeaderType = TableHeaderType<OpendingHeaderKeyType>;
