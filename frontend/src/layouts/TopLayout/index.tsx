import React ,{ ReactNode } from "react";
import BCHeader from "../../components/BCHeader";

import './TopLayout.scss';

interface TopLayoutProps {
    pageContent: ReactNode;
}

const TopLayout: React.FC<TopLayoutProps> = ({pageContent}) => {
    return (
      <>
         <BCHeader/>
         <div className="toplayout-page-content">
            {pageContent}
         </div>
      </>
    );
  };

export default TopLayout;