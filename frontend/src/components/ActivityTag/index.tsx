import DynamicIcon from '../DynamicIcon';
import { ActivityIconMap, FileIconMap } from './definitions';
import './styles.scss';
import { ActivityTagFileFormatEnum, ActivityTagTypeEnum } from '../../types/ActivityTagType';

type ActivityTagProps = {
  type: ActivityTagTypeEnum;
  fileFormat?: ActivityTagFileFormatEnum; // Optional fileFormat
};

const ActivityTag = ({ type, fileFormat }: ActivityTagProps) => {
  const iconName = (fileFormat && FileIconMap[fileFormat]) ? FileIconMap[fileFormat] : ActivityIconMap[type];

  return (
    <>
      <DynamicIcon iconName={iconName!} size={18} />&nbsp;{type}
    </>
  );
};

export default ActivityTag;
