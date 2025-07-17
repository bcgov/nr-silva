import React, { ReactNode } from "react";
import BCHeader from "@/components/BCHeader";

import './styles.scss';

interface SideLayoutProps {
  pageContent: ReactNode;
}

const SideLayout: React.FC<SideLayoutProps> = ({ pageContent }) => {
  return (
    <>
      <BCHeader />

      <div className="sidelayout-page-content">
        {pageContent}
      </div>
    </>
  );
};

export default SideLayout;
