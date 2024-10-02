import React, { useEffect, useState } from 'react';
import './ExpandingSearch.scss';
import { ExpandableSearch } from '@carbon/react';

/**
 * Renders an Expanding Search component.
 *
 * @returns {JSX.Element} The ExpandingSearch to be rendered.
 */
function ExpandingSearch (): JSX.Element {
  return (
    <ExpandableSearch
      size="lg"
      labelText="Search"
      closeButtonLabelText="Clear search input"
      id="search-expandable-1"
      onChange={() => {}}
      onKeyDown={() => {}}
    />
  );
};

export default ExpandingSearch;
