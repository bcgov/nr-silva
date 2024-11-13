import React from 'react';
import { useLocation } from 'react-router-dom';
import { Column, Breadcrumb, BreadcrumbItem } from "@carbon/react";
import { leftMenu, LeftMenuItem } from '../../components/BCHeaderwSide/constants';
import Subtitle from '../Subtitle';

import './styles.scss';

interface PageTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
}

const PageTitle: React.FC<PageTitleProps> = ({
  title,
  subtitle
}: PageTitleProps) => {

  // This will return up to the second level, even if we use just the first one
  const extractCurrentItems = (): LeftMenuItem[] => {
    const currentLocation = useLocation().pathname;

    for (const item of leftMenu) {
      if (item.items) {
        for (const subItem of item.items) {
          if (subItem.link === currentLocation) {
            return [subItem];
          }

          if (subItem.subItems) {
            for (const subSubItem of subItem.subItems) {
              if (subSubItem.link === currentLocation) {
                return [subItem];
              }
            }
          }
        }
      }
    }
    return [];
  }


  return (
    <Column className="title-section">
      <Breadcrumb>
          {extractCurrentItems().map(item => (
            <BreadcrumbItem key={item.name} href={item.link}>{item.name}</BreadcrumbItem>
          ))}
          </Breadcrumb>
      <div className="title-favourite">
        <h1>{title}</h1>
      </div>
      <Subtitle text={subtitle} />
    </Column>
  );
};

export default PageTitle;
