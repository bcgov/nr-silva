import React from "react";
import { Location as LocationIcon } from "@carbon/icons-react";
import VerticalDivider from "../../VerticalDivider";
import { OpeningDetailsStockingDto } from "@/types/OpeningTypes";

type AcoordionTitleProp = {
  standardUnit: OpeningDetailsStockingDto;
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
          {standardUnit.stocking.stockingStandardUnit}
        </h4>
      </div>
      <div className="accordion-title-bottom">
        {/* No standard unit id or FSP id */}
        {
          (!standardUnit.stocking.ssid && !standardUnit.stocking.fspId)
            ? 'Manual stocking requirement'
            : null
        }
        {/* Has standard unit id but no FSP id */}
        {
          (standardUnit.stocking.ssid && !standardUnit.stocking.fspId)
            ? (
              <>
                {`SSID ${standardUnit.stocking.ssid}, Stocking objective`}
                <VerticalDivider />
                Ministry default
              </>
            )
            : null
        }
        {/* Has standard unit id AND FSP id */}
        {
          (standardUnit.stocking.ssid && standardUnit.stocking.fspId)
            ? (
              <>
                {`SSID ${standardUnit.stocking.ssid}, Stocking objective`}
                <VerticalDivider />
                {`FSP ID ${standardUnit.stocking.fspId}`}
              </>
            )
            : null
        }
      </div>
    </div>
  )
}

export default AcoordionTitle;
