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

// Utility to generate and download PDF from table data
export const downloadPDF = (headers: any[], rows: any[], filename = "data.pdf"): void => {

};
