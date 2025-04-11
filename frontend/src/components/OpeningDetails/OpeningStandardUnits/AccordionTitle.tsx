import React from "react";
import { Location as LocationIcon } from "@carbon/icons-react";
import { DummyStandardUnitType } from "./constants";
import VerticalDivider from "../../VerticalDivider";

type AcoordionTitleProp = {
  standardUnit: DummyStandardUnitType;
};

/**
 * The accordion title component used specifically for standard units.
 */
const AcoordionTitle = ({ standardUnit }: AcoordionTitleProp) => {

  return (
    <div className="accordion-title-container">
      <div className="accordion-title-top">
        <LocationIcon size={20} />
        <h4>
          {standardUnit.name}
        </h4>
      </div>
      <div className="accordion-title-bottom">
        {/* No standard unit id or FSP id */}
        {
          (!standardUnit.ssid && !standardUnit.fspId)
            ? 'Manual stocking requirement'
            : null
        }
        {/* Has standard unit id but no FSP id */}
        {
          (standardUnit.ssid && !standardUnit.fspId)
            ? (
              <>
                {`SSID ${standardUnit.ssid}, Stocking objective`}
                <VerticalDivider />
                Ministry default
              </>
            )
            : null
        }
        {/* Has standard unit id AND FSP id */}
        {
          (standardUnit.ssid && standardUnit.fspId)
            ? (
              <>
                {`SSID ${standardUnit.ssid}, Stocking objective`}
                <VerticalDivider />
                {`FSP ID ${standardUnit.fspId}`}
              </>
            )
            : null
        }
      </div>
    </div>
  )
}

export default AcoordionTitle;
