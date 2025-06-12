import React from "react";
import { Tag } from "@carbon/react";
import { CodeDescriptionDto } from "@/services/OpenApi";
import { SIZES, TYPES } from "@carbon/react/lib/components/Tag/Tag";
import { CodeType, codeColorMap, KNOWN_CODES } from "./constants";

type StockingStatusTagProps = {
  status: CodeDescriptionDto,
  size?: keyof typeof SIZES;
}

const isKnownCode = (code: string): code is CodeType =>
  KNOWN_CODES.includes(code as CodeType);

const StockingStatusTag = ({ status, size }: StockingStatusTagProps) => {
  const { code, description } = status;
  let tagType: keyof typeof TYPES = 'gray';

  if (isKnownCode(code!)) {
    tagType = codeColorMap[code];
  }

  return (
    <Tag
      title={description!}
      size={size ?? 'md'}
      type={tagType}
    >
      {description}
    </Tag>
  )

}

export default StockingStatusTag;
