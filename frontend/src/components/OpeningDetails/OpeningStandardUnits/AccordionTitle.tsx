import React from "react";
import { Location as LocationIcon, Launch as LaunchIcon } from "@carbon/icons-react";
import { DummyStandardUnitType } from "./constants";
import VerticalDivider from "../../VerticalDivider";
import { Link } from "react-router-dom";

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
                {
                  <Link to="">
                    {`SSID ${standardUnit.ssid}, Stocking objective`}
                  </Link>
                }
                <VerticalDivider />
                <span>Ministry default</span>
              </>
            )
            : null
        }
        {/* Has standard unit id AND FSP id */}
        {
          (standardUnit.ssid && standardUnit.fspId)
            ? (
              <>
                {
                  <Link to="">
                    {`SSID ${standardUnit.ssid}, Stocking objective`}
                  </Link>
                }
                <VerticalDivider />
                {
                  <Link
                    className="fsp-link"
                    to={`https://apps.nrs.gov.bc.ca/ext/fsp/indexAction.do?fsp_id=${standardUnit.fspId}`}
                    target="_blank"
                  >
                    {`FSP ID ${standardUnit.fspId}`} <LaunchIcon />
                  </Link>
                }
              </>
            )
            : null
        }
      </div>
    </div>
  )
}

export default AcoordionTitle;
