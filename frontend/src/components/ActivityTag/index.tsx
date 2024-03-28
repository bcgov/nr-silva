import React from 'react';
import { Tag } from '@carbon/react';
import * as Carbon from '@carbon/icons-react';

import { ActivityIconMap, FileIconMap } from './definitions';

import './styles.scss';

type ActivityTagProps = {
  type: keyof typeof ActivityIconMap;
  fileFormat: keyof typeof FileIconMap; // Optional fileFormat
};

const ActivityTag = ({ type, fileFormat }: ActivityTagProps) => {
  const tagType: keyof typeof ActivityIconMap = Object.keys(ActivityIconMap).includes(type) ? type : 'Unknown';
  let iconName;
  if (fileFormat && FileIconMap[fileFormat]) {
    iconName = FileIconMap[fileFormat];
  }
  else{
    iconName = ActivityIconMap[tagType]; // get the icon name by the type name
  }
  const Icon = Carbon[iconName]; // get the icon component by the icon name

  

  return (
    <>
      <Icon size={18} /> {type}
    </>
  );
};

export default ActivityTag;
