import React from 'react';
import { Breadcrumb, BreadcrumbItem, Column } from "@carbon/react";
import { useNavigate } from 'react-router-dom';

import { BreadCrumbType } from '@/types/BreadCrumbTypes';
import Subtitle from '../Subtitle';
import UnderConstructionTag from '../Tags/UnderConstructionTag';

import './styles.scss';

interface PageTitleProps {
  title: string;
  subtitle?: string;
  experimental?: boolean;
  children?: React.ReactNode;
  breadCrumbs?: BreadCrumbType[];
}

const PageTitle: React.FC<PageTitleProps> = ({
  title,
  subtitle,
  experimental,
  children,
  breadCrumbs
}: PageTitleProps) => {
  const navigate = useNavigate();

  return (
    <Column className="page-title-col" sm={4} md={8} lg={16}>
      {
        breadCrumbs?.length
          ? (
            <Breadcrumb className='page-title-breadcrumb'>
              {
                breadCrumbs.map((crumb) => (
                  <BreadcrumbItem
                    key={crumb.name}
                    onClick={() => navigate(crumb.path)}
                  >
                    {crumb.name}
                  </BreadcrumbItem>
                ))
              }
            </Breadcrumb>
          )
          : null
      }
      <div className="page-title-container">
        <div className="title-container">
          <h1>{title}</h1>
          {children}
          {
            experimental
              ? <UnderConstructionTag />
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
