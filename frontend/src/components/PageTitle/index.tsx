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
    // Get the current location to use as lookup
    const currentLocation = useLocation().pathname;
    // Recursive function to find the full path to the current item, retaining full objects
    for (const item of leftMenu) {    
      // Check for match at subItem level
      if (item.items) {
        for (const subItem of item.items) {
          //Create as an array for the sake of the return type
          const subPath = [subItem];

          // Check for match at subItem level, ideally it should never match as this is a high level item
          // but it can happen
          if (subItem.link === currentLocation) {
            return subPath; // Direct match, return the path
          }

          // Check for match at subSubItem level, and this is usually the case for most of the
          // items. This is the most common scenario, and for that, we will return both the item
          // and the parent
          if (subItem.subItems) {
            for (const subSubItem of subItem.subItems) {
              // Create an array, so we can generate the breadcrumb with the deeper levels              
              if (subSubItem.link === currentLocation) {                
                // We're returning the subPath here because we want to stop at the first level
                // This is because so far we have just two levels, and we don't want to go deeper in the breadcrumb
                // In case we wanted, we could just do this before this if and return the fullPath instead
                //const fullPath = [...subPath, subSubItem];
                return subPath; 

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
