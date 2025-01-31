import React, { useState, useEffect } from "react";
import { FlexGrid, Row, Column, Checkbox } from "@carbon/react";
import * as Icons from "@carbon/icons-react";

// Types
import { ITableHeader } from "@/types/TableHeader";

// Styles and others
import "./index.scss";

interface SearchTableColumnSelectorProps {
  headers: ITableHeader[];
  defaultSelection: string[];
  onChangeSelection: (selectedColumns: ITableHeader[]) => void;
}

const SearchTableColumnSelector: React.FC<SearchTableColumnSelectorProps> = ({
  headers,
  defaultSelection,
  onChangeSelection,
}) => {
  const [columnsSelected, setColumnsSelected] = useState<string>("");

  const handleCheckboxChange = (key: string) => {
    const newHeaders = headers.map((header) => {
      if (header.key === key) {
        return { ...header, selected: !header.selected };
      }
      return header;
    });
    onChangeSelection(newHeaders);
  };

  const displayCheckbox = (header: ITableHeader) => (
    <Checkbox
      className="edit-column-content--checkbox-item"
      labelText={header.header}
      id={`checkbox-label-${header.key}`}
      checked={header.selected === true}
      onChange={() => handleCheckboxChange(header.key)}
    />
  );

  const displayReadOnlyCheckbox = (header: ITableHeader) => (
    <div className="d-flex flex-row align-items-center edit-column-content--checkbox-item cursor-pointer">
      <Icons.CheckboxChecked size={21} />
      <p className="bx--checkbox-label-text">{header.header}</p>
    </div>
  );

  useEffect(() => {
    console.log("Headers:", headers, "Columns selected:", columnsSelected);

    if (columnsSelected === "select-all")
      onChangeSelection(
        headers.map((header) => ({ ...header, selected: true }))
      );
    if (columnsSelected === "select-default")
      onChangeSelection(
        headers.map((header) => ({
          ...header,
          selected: defaultSelection.includes(header.key),
        }))
      );
  }, [columnsSelected]);

  return (
    <>
      <div className="edit-column-content--dropdown-label">
        <p>Select Columns you want to see:</p>
      </div>
      <FlexGrid data-test-id="edit-column-content--edit-columns-container">
        {headers.map(
          (header) =>
            header &&
            header.key !== "actions" && (
              <Row key={`row-${header.key}`}>
                <Column sm={2} md={4} lg={8} key={header.key}>
                  {header.key === "openingId"
                    ? displayReadOnlyCheckbox(header)
                    : displayCheckbox(header)}
                </Column>
              </Row>
            )
        )}
      </FlexGrid>
      <div
        className="w-100 d-flex flex-row justify-content-between menu-item-container"
        id="select-all-column"
        data-testid="select-all-column"
        onClick={() => {
          handleCheckboxChange("select-all");
          setColumnsSelected("select-all");
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            handleCheckboxChange("select-all");
            setColumnsSelected("select-all");
          }
        }}
      >
        <p className="menu-item">Select all columns</p>
        {columnsSelected === "select-all" && <Icons.Checkmark size={13} />}
      </div>
      <div
        className="w-100 d-flex flex-row justify-content-between menu-item-container"
        id="select-default-column"
        data-testid="select-default-column"
        onClick={() => {
          handleCheckboxChange("select-default");
          setColumnsSelected("select-default");
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            handleCheckboxChange("select-default");
            setColumnsSelected("select-default");
          }
        }}
      >
        <p className="menu-item">Reset columns to default</p>
        {columnsSelected === "select-default" && <Icons.Checkmark size={13} />}
      </div>
    </>
  );
};

export default SearchTableColumnSelector;
