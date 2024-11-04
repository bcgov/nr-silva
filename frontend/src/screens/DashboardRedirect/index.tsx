import React from "react";
import { Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import { useGetAuth } from "../../contexts/AuthProvider";

const DashboardRedirect: React.FC = () => {
  const { user } = useGetAuth();
  return (
    <>
      {user ? <Navigate to={"/opening"} replace /> : <Loading className={'some-class'} withOverlay={true} />}
    </>
  );
};

export default DashboardRedirect;
