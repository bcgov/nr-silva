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