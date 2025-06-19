import { Tag } from "@carbon/react"
import { SIZES } from "@carbon/react/lib/components/Tag/Tag";
import { codeColorMap, codeNameMap, CodeType, KNOWN_CODES } from "./constants";

import "./styles.scss";

type StockingStandardMilestoneStatusTagProps = {
  status: string;
  size?: keyof typeof SIZES;
}

const isKnownCode = (code: string): code is CodeType =>
  KNOWN_CODES.includes(code as CodeType);

const StockingStandardMilestoneStatusTag = ({ status }: StockingStandardMilestoneStatusTagProps) => {
  const name = isKnownCode(status) ? codeNameMap[status] : "Unknown Status";
  const tagType = isKnownCode(status) ? codeColorMap[status] : "gray";

  return (
    <Tag
      className="stocking-standard-milestone-status-tag"
      title={name}
      size="sm"
      type={tagType}
    >
      {name}
    </Tag>
  );
};

export default StockingStandardMilestoneStatusTag;
