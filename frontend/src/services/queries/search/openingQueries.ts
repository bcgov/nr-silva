import { useQuery } from "@tanstack/react-query";
import { fetchOpeningFilters, fetchOpenings, OpeningFilters, fetchUserRecentOpenings } from "../../search/openings";

export const useOpeningsQuery = (filters: OpeningFilters, enabled: boolean) => {
  return useQuery({
    queryKey: ["openings", filters],
    queryFn: () => fetchOpenings(filters),
    enabled // Only fetch when `enabled` is true
  });
};

export const useOpeningFiltersQuery = () => {
  return useQuery({
    queryKey: ["openingFilters"],
    queryFn: fetchOpeningFilters
  });
};

export const useUserRecentOpeningQuery = () => {
  return useQuery({
    queryKey: ["opening", "recent"],
    queryFn: () => fetchUserRecentOpenings(),
    refetchOnMount: "always"
  });
};
