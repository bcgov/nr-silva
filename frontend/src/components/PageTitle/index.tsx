import React from 'react';
import { Column, Tag } from "@carbon/react";
import { Chemistry } from '@carbon/icons-react';
import Subtitle from '../Subtitle';

import './styles.scss';

interface PageTitleProps {
  title: string;
  subtitle?: string;
  experimental?: boolean;
  children?: React.ReactNode
}

const PageTitle: React.FC<PageTitleProps> = ({
  title,
  subtitle,
  experimental,
  children
}: PageTitleProps) => {
  return (
    <Column className="page-title-col" sm={4} md={8} lg={16}>
      <div className="page-title-container">
        <div className="title-container">
          <h1>{title}</h1>
          {children}
          {
            experimental
              ? (
                <Tag
                  className="experimental-tag"
                  type="cyan"
                  size="md"
                  renderIcon={Chemistry}
                >
                  Experimental
                </Tag>
              )
              : null
          }
        </div>
        {
          subtitle ? <Subtitle text={subtitle} /> : null
        }
      </div>
    </Column>
  );
};

export default PageTitle;
