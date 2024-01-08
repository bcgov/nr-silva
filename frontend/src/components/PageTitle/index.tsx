import React from 'react';

import {
  Column,
  IconButton
} from '@carbon/react';
import { Favorite, FavoriteFilled } from '@carbon/icons-react';

import Subtitle from '../Subtitle';

import './styles.scss';

interface PageTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
}

const PageTitle = ({
  title,
  subtitle
}: PageTitleProps) => {
 

  return (
    <Column className="title-section">
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default PageTitle;
