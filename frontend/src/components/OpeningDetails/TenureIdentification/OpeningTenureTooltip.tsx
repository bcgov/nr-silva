import React from "react";
import { OpeningTenureDto } from "@/types/OpeningTypes";


const OpeningTenureTooltip: React.FC<{ primary: OpeningTenureDto }> = ({ primary }) => {
  const fields = [
    { label: "File ID:", value: primary.fileId },
    { label: "Cut Block:", value: primary.cutBlock },
    { label: "Cutting Permit:", value: primary.cuttingPermit },
    { label: "Timber Mark:", value: primary.timberMark },
  ];

  return (
    <dl className="primary-tenure-dl">
      {
        fields
          .filter((field) => field.value !== null && field.value !== undefined && field.value !== "")
          .map((field) => (
            <div className="label-val-container" key={field.label}>
              <dt>{field.label}</dt>
              <dd>{field.value}</dd>
            </div>
          ))
      }
    </dl>
  );
};

export default OpeningTenureTooltip;
