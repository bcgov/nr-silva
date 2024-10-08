export const countActiveFilters = (filters: any): number => {
    let count = 0;

    Object.keys(filters).forEach((key) => {
      const value = filters[key];

      // Check if the value is an array (e.g., for checkboxes)
      if (Array.isArray(value)) {
        count += value.filter((item) => item !== "").length; // Count non-empty values in the array
      } else if (value !== null && value !== "" && value !== "Option 1") {
        // Increment the count for non-default, non-null, and non-empty values
        count += 1;
      }
    });

    return count;
  };

// Helper function to create date filter parameters based on dateType
export const createDateParams = (filters: any) => {
  let dateStartKey = '';
  let dateEndKey = '';

  // Map dateType to appropriate backend keys
  switch (filters.dateType) {
    case 'Disturbance':
      dateStartKey = 'disturbanceDateStart';
      dateEndKey = 'disturbanceDateEnd';
      break;
    case 'Regen Delay':
      dateStartKey = 'regenDelayDateStart';
      dateEndKey = 'regenDelayDateEnd';
      break;
    case 'Free Growing':
      dateStartKey = 'freeGrowingDateStart';
      dateEndKey = 'freeGrowingDateEnd';
      break;
    case 'Update':
      dateStartKey = 'updateDateStart';
      dateEndKey = 'updateDateEnd';
      break;
    default:
      // If dateType is null or undefined, return empty keys
      dateStartKey = '';
      dateEndKey = '';
      break;
  }

  return { dateStartKey, dateEndKey };
};

// Helper function to slice out the date part (YYYY-MM-DD) from an ISO date string
export const formatDateToYYYYMMDD = (isoString: string | undefined): string | null => {
  if (!isoString) return null; // Handle null or undefined values
  return isoString.slice(0, 10); // Slicing first 10 characters gives us the date part (YYYY-MM-DD)
};