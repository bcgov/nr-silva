import {
  Column
} from '@carbon/react';

import Subtitle from '../Subtitle';

import './styles.scss';

interface ChartTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
}

const ChartTitle = ({
  title,
  subtitle
}: ChartTitleProps) => {
 

  return (
    <Column className="section-title">
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default ChartTitle;
