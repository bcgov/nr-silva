import { useQuery } from "@tanstack/react-query";
import { fetchOpeningFilters, fetchOpenings, OpeningFilters } from "../../search/openings";

export const useOpeningsQuery = (filters: OpeningFilters) => {
  return useQuery({
    queryKey: ["openings", filters],
    queryFn: () => fetchOpenings(filters)
  });
};

export const useOpeningFiltersQuery = () => {
  return useQuery({
    queryKey: ["openingFilters"],
    queryFn: fetchOpeningFilters,
  });
};