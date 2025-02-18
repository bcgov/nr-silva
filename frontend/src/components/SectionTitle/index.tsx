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
    <Column className="section-title-container">
      <h6 className="section-title">{title}</h6>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default SectionTitle;
