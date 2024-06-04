import * as Carbon from '@carbon/icons-react';

const Icon = (props: string) => {
  const iconName = props;
  const Base = Carbon[iconName]
  return <Base />;
} 

export default Icon;
