import React, { useEffect, useState } from 'react';
import './ExpandingSearch.scss';
import { ExpandableSearch } from '@carbon/react';

const ExpandingSearch = () => {

  return (
    <div >
      <ExpandableSearch size="lg" labelText="Search" closeButtonLabelText="Clear search input" id="search-expandable-1" onChange={() => {}} onKeyDown={() => {}} />
    </div>
  );
};

export default ExpandingSearch;
