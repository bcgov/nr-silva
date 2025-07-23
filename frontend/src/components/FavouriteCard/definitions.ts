
import * as Icons from '@carbon/icons-react';
export interface FavouriteCardProps {
  index: number,
  title: string,
  link: string,
  icon: keyof typeof Icons;
  opensModal?: boolean;
}
