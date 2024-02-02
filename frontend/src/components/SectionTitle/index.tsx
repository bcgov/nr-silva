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
}

const SectionTitle = ({
  title,
  subtitle
}: SectionTitleProps) => {
 

  return (
    <Column className="section-title">
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default SectionTitle;
