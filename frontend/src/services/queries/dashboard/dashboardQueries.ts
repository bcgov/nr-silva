import { useMutation, useQuery } from "@tanstack/react-query";
import axios from "axios";
import { getAuthIdToken } from "../../AuthService";
import { fetchOpeningsPerYearAPI } from "../../OpeningService";
import { IOpeningPerYear } from "../../../types/IOpeningPerYear";
import { fetchOrgUnits } from "../../search/openings";

const backendUrl = import.meta.env.VITE_BACKEND_URL;

// Function to send the POST request
export const postViewedOpening = async (openingId: string): Promise<any> => {
    const authToken = getAuthIdToken();
    try {
      const response = await axios.post(`${backendUrl}/viewed/${openingId}`, null, {
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      return response.data;
    } catch (error:any) {
      if (error.response?.status === 403) {
        throw new Error("Forbidden: You don't have permission to view this opening.");
      } else {
        throw new Error(error.response.data.message);
      }
    }
  };
  
  // Hook for using the mutation
  export const usePostViewedOpening = () => {
    return useMutation({
      mutationFn: (openingId: string) => postViewedOpening(openingId),
    });
  };

// Custom hook to use in your component
export const useFetchOpeningsPerYear = (props: IOpeningPerYear) => {
  return useQuery({
    queryKey: ['openingsPerYear', props],  // Cache key including props
    queryFn: () => fetchOpeningsPerYearAPI(props),  // Fetch function
    enabled: true, // For Conditional fetch we can use !!props.orgUnitCode || !!props.statusCode || !!props.entryDateStart || !!props.entryDateEnd
    staleTime: 5 * 60 * 1000,  // Cache duration (optional)
  });
};

export const useDistrictListQuery = () => {
  return useQuery({
    queryKey: ["districtList"],
    queryFn: fetchOrgUnits
  });
};
  
