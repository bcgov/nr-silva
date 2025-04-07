import React from "react";
import { Location as LocationIcon } from "@carbon/icons-react";
import { DummyStandardUnitType } from "./constants";
import VerticalDivider from "../../VerticalDivider";
import { Button } from "@carbon/react";

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
        {/* No standard unit id or fsp id */}
        {
          (!standardUnit.ssid && !standardUnit.fspId)
            ? 'Manual stocking requirement'
            : null
        }
        {/* Has standard unit id but no fsp id */}
        {
          (standardUnit.ssid && !standardUnit.fspId)
            ? (
              <>
                {
                  <Button kind="ghost">{standardUnit.ssid}</Button>
                }
                <VerticalDivider />
                <span>Ministry default</span>
              </>
            )
            : null
        }
      </div>
    </div>
  )
}

export default AcoordionTitle;
