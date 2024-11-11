import React from 'react';
import { Tooltip } from '@carbon/react';

const TruncatedText: React.FC<{text: string, maxLength?: number, parentWidth?: number}> = ({ text, maxLength, parentWidth }) => {

  // If parentWidth is provided, calculate the number of characters that can fit in the parent container
  // Otherwise, use maxLength if provided, otherwise default to 50
  // The number of characters that can fit in the parent container is calculated based on the parentWidth
  // If the parentWidth is less than 200px, we divide the parentWidth by 10, otherwise we divide by 5
  const charCount = parentWidth ? Math.floor(parentWidth / (parentWidth < 200 ? 10 : 5)) : maxLength ? maxLength : 50;
  const truncated = text.length > charCount ? text.slice(0, charCount) + "..." : text; 
  return <Tooltip  
  label={text}
  align="bottom-left"
  autoAlign
> 
<span>{truncated}</span></Tooltip>;
}

export default TruncatedText;