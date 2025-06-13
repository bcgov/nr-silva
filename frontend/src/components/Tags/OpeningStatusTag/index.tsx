import React from 'react';
import { SIZES, TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { Tag } from '@carbon/react';
import { CodeDescriptionDto } from '@/types/OpenApiTypes';
import { codeColorMap, CodeType, KNOWN_CODES } from './constants';

type OpeningStatusTagProps = {
  status: CodeDescriptionDto,
  size?: keyof typeof SIZES,
};

const isKnownCode = (code: string): code is CodeType =>
  KNOWN_CODES.includes(code as CodeType);

const OpeningStatusTag = ({ status, size }: OpeningStatusTagProps) => {
  const { code, description } = status;
  let tagType: keyof typeof TYPES = 'outline';

  if (isKnownCode(code!)) {
    tagType = codeColorMap[code];
  }

  return (
    <Tag
      title={isKnownCode(code!) ? description! : "Unknown"}
      size={size ?? 'md'}
      type={tagType}
      data-testid={`tag__status_colored_tag_${tagType}`}
    >
      {isKnownCode(code!) ? description : "Unknown"}
    </Tag>
  )

}

export default OpeningStatusTag;
