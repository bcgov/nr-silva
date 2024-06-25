import React from 'react';
import { Tag } from '@carbon/react';
import { StatusColourMap } from './definitions';
import './styles.scss';

interface IStatusTag {
  code: string;
}

const StatusTag: React.FC<IStatusTag> = (props) => {
  const colorsKeys: string[] = Object.keys(StatusColourMap);
  const colorKey: string = colorsKeys.includes(props.code)? props.code : StatusColourMap.Unknown;
  const typeColor: string = StatusColourMap[colorKey as keyof typeof StatusColourMap];

  return (
    <Tag
      className="status-tag"
      type={typeColor}
      data-testid={`tag__status_colored_tag_${colorKey}`}
    >
      { props.code }
    </Tag>
  );
};

export default StatusTag;
