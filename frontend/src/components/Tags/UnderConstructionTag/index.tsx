import React from "react";
import { Construction } from "@carbon/icons-react";
import { Tag, Tooltip } from "@carbon/react";

import './styles.scss';

const UnderConstructionTag = () => (
  <Tooltip
    label="This page is under development. Features may be incomplete or display incorrect data."
    align="bottom"
  >
    <Tag
      className="under-construction-tag "
      type="cyan"
      size="md"
      renderIcon={Construction}
    >
      Under construction
    </Tag>
  </Tooltip>
);

export default UnderConstructionTag;
