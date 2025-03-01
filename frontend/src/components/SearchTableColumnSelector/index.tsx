import React, { useState, useEffect } from "react";
import { FlexGrid, Row, Column, Checkbox } from "@carbon/react";
import * as Icons from "@carbon/icons-react";

// Types
import { OpeningHeaderType, TableHeaderType } from "@/types/TableHeader";

// Styles and others
import "./styles.scss";

interface SearchTableColumnSelectorProps<T> {
  headers: TableHeaderType<T>[];
  setHeaders: React.Dispatch<React.SetStateAction<TableHeaderType<T>[]>>;
}

const SearchTableColumnSelector = <T,>({
  headers,
  setHeaders
}: SearchTableColumnSelectorProps<T>) => {
  const handleCheckboxChange = (key: string) => {
    setHeaders(
      () => headers.map((header) => {
        if (header.key === key) {
          return { ...header, selected: !header.selected };
        }
        return header;
      })
    );
  };

  const displayCheckbox = (header: OpeningHeaderType) => (
    <Checkbox
      className="edit-column-content--checkbox-item"
      labelText={header.header}
      id={`checkbox-label-${header.key}`}
      checked={header.selected === true}
      onChange={() => handleCheckboxChange(header.key)}
    />
  );



  return (
    <>
      <div>gasas</div>
    </>
  )
};

export default SearchTableColumnSelector;
