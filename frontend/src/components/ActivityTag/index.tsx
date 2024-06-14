import React from 'react';
import { Tag } from '@carbon/react';
import * as Carbon from '@carbon/icons-react';
import { ActivityIconMap, FileIconMap } from './definitions';
import './styles.scss';

type ActivityTagProps = {
  type: string;
  fileFormat?: string;
};

// Type can be:
// IF 'Recent' tab: Update
// IF 'Files and Docs' tab: Opening retails, Opening report, ...

const ActivityTag: React.FC<ActivityTagProps> = (props) => {
  const activitiesKeys: string[] = Object.keys(ActivityIconMap);
  const activityKey: string = activitiesKeys.includes(props.type)? props.type : ActivityIconMap.Unknown;
  const tagType = ActivityIconMap[activityKey as keyof typeof ActivityIconMap];
  
  let iconName = null;

  // If it's not the 'Files and Docs' tab, then get's the icon given the activity value
  if (!props.fileFormat) {
    iconName = ActivityIconMap[tagType as keyof typeof ActivityIconMap];
  }

  if (!iconName) {
    const fileTypeKeys: string[] = Object.keys(FileIconMap);
    const fileTypeKey: string = fileTypeKeys.includes(props.type)? props.type : FileIconMap.Unknown;
    const fileIcon: string = FileIconMap[activityKey as keyof typeof FileIconMap];
    iconName = FileIconMap[fileTypeKey as keyof typeof FileIconMap];
  }

  const Icon = Carbon[iconName];

  return (
    <>
      <Icon size={18} />
      {props.type}
    </>
  );
};

export default ActivityTag;
