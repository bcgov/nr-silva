import React, { ReactNode } from "react";

import './SideLayout.scss';
import BCHeaderwSide from "../../components/BCHeaderwSide";

interface SideLayoutProps {
  pageContent: ReactNode;
}

const SideLayout: React.FC<SideLayoutProps> = ({ pageContent }) => {
  return (
    <>
      <BCHeaderwSide />

      <div className="sidelayout-page-content">
        {pageContent}
      </div>
    </>
  );
};

export default SideLayout;
