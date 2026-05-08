import React from "react";
import { Location as LocationIcon } from "@carbon/icons-react";

import { PLACE_HOLDER } from "@/constants";
import { OpeningDetailsStockingDetailsMilestoneDto, OpeningDetailsStockingDto, OpeningStockingHistoryDto } from "@/services/OpenApi";

import VerticalDivider from "../../VerticalDivider";
import StockingStandardMilestoneStatusTag from "../../Tags/StockingStandardMilestoneStatusTag";

type AcoordionTitleProp = {
  standardUnit: OpeningDetailsStockingDto | OpeningStockingHistoryDto;
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
 * The accordion title component used specifically for standards units.
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
        {
          "milestones" in standardUnit.stocking && standardUnit.stocking.milestones
            ? renderMilestoneStatusTag(standardUnit.stocking.milestones)
            : null
        }
      </div>
      <div className="accordion-title-bottom">
        <p>SSID {standardUnit.stocking.srid}</p>
        <VerticalDivider />
        <p>
          {
            standardUnit.stocking.standardsObjective ?? PLACE_HOLDER
          }
        </p>
      </div>
    </div>
  )
}

export default AcoordionTitle;
