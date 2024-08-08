import React from 'react';

import {
  Column,
  IconButton
} from '@carbon/react';
import { Favorite, FavoriteFilled } from '@carbon/icons-react';

import Subtitle from '../Subtitle';

import './styles.scss';

interface SectionTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
  onClick?: () => void;
}

const SectionTitle = ({
  title,
  subtitle,
  onClick
}: SectionTitleProps) => {

  return (
    <Column className="section-title p-0">
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} onClick={onClick} />
    </Column>
  );
};

export default SectionTitle;
