import { ActivitySearchResponseDto, DisturbanceSearchResponseDto, OpeningSearchResponseDto } from "@/services/OpenApi";

export type TableHeaderType<T> = {
  key: T;
  header: string;
  selected?: boolean;
  sortable?: boolean;
}

export type OpendingHeaderKeyType = keyof OpeningSearchResponseDto | 'actions';

export type OpeningHeaderType = TableHeaderType<OpendingHeaderKeyType>;

export type ActivityHeaderKeyType = keyof ActivitySearchResponseDto | 'actions';

export type ActivityHeaderType = TableHeaderType<ActivityHeaderKeyType>;

export type DisturbanceHeaderKeyType = keyof DisturbanceSearchResponseDto | 'actions';

export type DisturbanceHeaderType = TableHeaderType<DisturbanceHeaderKeyType>;
