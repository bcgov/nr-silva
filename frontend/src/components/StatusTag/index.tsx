import React from 'react';
import { TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { Tag } from '@carbon/react';
import { StatusColourMap } from './definitions';
import './styles.scss';

interface IStatusTag {
  description: string;
}

const StatusTag: React.FC<IStatusTag> = (props) => {
  const colorKey: string = Object.keys(StatusColourMap).includes(props.description) ? props.description : StatusColourMap.Unknown;
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
