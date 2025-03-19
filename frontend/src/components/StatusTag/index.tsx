import React from 'react';
import { TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { Tag } from '@carbon/react';
import { StatusColourMap, StatusKeyType } from './definitions';
import './styles.scss';

interface IStatusTag {
  description: StatusKeyType | string;
}

const StatusTag: React.FC<IStatusTag> = (props) => {
  const colorsKeys: string[] = Object.keys(StatusColourMap);
  const colorKey: string = colorsKeys.includes(props.description) ? props.description : StatusColourMap.Unknown;
  const typeColor: keyof typeof TYPES = StatusColourMap[colorKey as keyof typeof StatusColourMap];

  return (
    <Tag
      className="status-tag"
      type={typeColor}
      data-testid={`tag__status_colored_tag_${colorKey}`}
    >
      {props.description}
    </Tag>
  );
};

export default StatusTag;
