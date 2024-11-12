import { useMutation, useQuery } from "@tanstack/react-query";
import axios from "axios";
import { getAuthIdToken } from "../../AuthService";

const backendUrl = import.meta.env.VITE_BACKEND_URL;

// Function to send the POST request
export const postViewedOpening = async (openingId: string): Promise<any> => {
    const authToken = getAuthIdToken();
    try {
      const response = await axios.put(`${backendUrl}/api/openings/recent/${openingId}`, null, {
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
