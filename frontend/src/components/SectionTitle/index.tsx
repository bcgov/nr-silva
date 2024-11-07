import { Column } from '@carbon/react';
import Subtitle from '../Subtitle';
import './styles.scss';

interface SectionTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
}

const SectionTitle = ({
  title,
  subtitle
}: SectionTitleProps) => {

  return (
    <Column className="section-title p-0">
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default SectionTitle;
