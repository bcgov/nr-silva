import React from "react";
import { Location as LocationIcon } from "@carbon/icons-react";

import { PLACE_HOLDER } from "@/constants";
import { OpeningDetailsStockingDetailsMilestoneDto, OpeningDetailsStockingDto } from "@/services/OpenApi";

import VerticalDivider from "../../VerticalDivider";
import StockingStandardMilestoneStatusTag from "../../Tags/StockingStandardMilestoneStatusTag";

type AcoordionTitleProp = {
  standardUnit: OpeningDetailsStockingDto;
};

const getCurrentMilestoneStatus = (milestones: OpeningDetailsStockingDetailsMilestoneDto) => {
  if (milestones.noRegenIndicated && milestones.noRegenDeclaredDate) return "NR";
  else if (milestones.freeGrowingDeclaredDate) return "FG";
  else if (milestones.regenDeclaredDate) return "RG";
  else if (milestones.postHarvestDeclaredDate) return "PH";
  else return "UN";
};

const renderMilestoneStatusTag = (milestones: OpeningDetailsStockingDetailsMilestoneDto) => {
  const status = getCurrentMilestoneStatus(milestones);

  return (
    status !== "UN"
      ? <StockingStandardMilestoneStatusTag status={status} />
      : null
  );
};

/**
 * The accordion title component used specifically for standard units.
 */
const AcoordionTitle = ({ standardUnit }: AcoordionTitleProp) => {

  return (
    <div
      className="default-accordion-title-container"
      data-testid={`standard-unit-accordion-${standardUnit.stocking.stockingStandardUnit ?? "unknown"}`}>
      <div className="accordion-title-top">
        <LocationIcon size={20} />
        <h4>
          {
            standardUnit.stocking.stockingStandardUnit ?? PLACE_HOLDER
          }
        </h4>
        {renderMilestoneStatusTag(standardUnit.stocking.milestones)}
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
