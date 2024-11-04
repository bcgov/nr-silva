import React from "react";
import { Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import { useGetAuth } from "../../contexts/AuthProvider";

const DashboardRedirect: React.FC = () => {
  const { isLoggedIn } = useGetAuth();
  return (
    <>
      {isLoggedIn ? <Navigate to={"/opening"} replace /> : <Loading className={'some-class'} withOverlay={true} />}
    </>
  );
};

export default DashboardRedirect;
