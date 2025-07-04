// Utility to convert rows and headers to a CSV format
export const downloadXLSX = (headers: any[], rows: any[], filename = "data.csv"): void => {
  const csvData = convertToCSV(headers, rows);
  return downloadCSV(csvData, filename);
};

// Utility to convert rows and headers to a CSV format
export const convertToCSV = (headers: any[], rows: any[]): string => {
  // Map headers to a CSV-compatible format
  const headerRow = headers
    .filter(h => h.selected && h.key !== "actions")
    .map(h => h.header)
    .join(",");

  // Map rows to a CSV-compatible format
  const csvRows = rows.map(row => {
    return headers
      .filter(h => h.selected && h.key !== "actions")
      .map(header => {
        const cell = row[header.key as keyof any];
        // Handle escaping quotes and commas
        const escaped = typeof cell === 'string' ? `"${cell.replace(/"/g, '""')}"` : cell;
        return escaped;
      })
      .join(",");
  });

  // Combine the header and the row data
  return [headerRow, ...csvRows].join("\n");
};

export const downloadCSV = (csvData: string, filename = "data.csv"): void => {
  // Create a Blob object from the CSV data
  const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');

  // Create a download link
  const url = URL.createObjectURL(blob);
  link.setAttribute('href', url);
  link.setAttribute('download', filename);
  link.style.visibility = 'hidden';

  // Append the link and simulate click to start download
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

/**
 * Converts a number of bytes into a human-readable string using decimal (SI) units.
 *
 * @param {number} bytes - The number of bytes to format.
 * @param {number} [decimals=1] - Number of decimal places to include.
 * @returns {string} Human-readable formatted string (e.g., "1.2 MB").
 */
export const formatBytesDecimal = (bytes: number, decimals = 1): string => {
  if (bytes === 0) return '0 B';

  const k = 1000;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  const value = bytes / Math.pow(k, i);

  return `${parseFloat(value.toFixed(decimals))} ${sizes[i]}`;
};
