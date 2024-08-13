import React, { useEffect, useState } from "react";
import "./OpeningsSearchBar.scss";
import { Search, MultiSelect, Button } from "@carbon/react";
import * as Icons from '@carbon/icons-react';
import Icon from "../Icon";

const OpeningsSearchBar = () => {
  const items:any[] = [
    {
      id: "downshift-1-item-0",
      text: "Option 1",
    },
    {
      id: "downshift-1-item-1",
      text: "Option 2",
    },
    {
      id: "downshift-1-item-2",
      text: "Option 3 - a disabled item",
      disabled: true,
    },
    {
      id: "downshift-1-item-3",
      text: "Option 4",
    },
    {
      id: "downshift-1-item-4",
      text: "An example option that is really long to show what should be done to handle long text",
    },
    {
      id: "downshift-1-item-5",
      text: "Option 5",
    },
  ];

  return (
    <div>
      <div className="openings-searchbar-container d-flex flex-row align-content-center">
        <Search
          size="md"
          placeholder="Search by opening ID, opening number, timber mark or file ID"
          labelText="Search"
          closeButtonLabelText="Clear search input"
          id={`search-1`}
          onChange={() => {}}
          onKeyDown={() => {}}
        />
        <MultiSelect
          label="Advanced Search"
          showLabel = {false}
          showHelper = {false}
          id="carbon-multiselect-example"
          size='md'
          items={items}
          itemToString={(item:any) => (item ? item.text : "")}
          selectionFeedback="top-after-reopen"
          className='multi-dropdown w-50 ms-1'
        />
        <Button
          className="search-button ms-2"
          renderIcon={Icons.Search}
          type="button"
          size='md'
          onClick={() => console.log("search clicked")}
        >
          Search
        </Button>
      </div>
    </div>
  );
};

export default OpeningsSearchBar;
