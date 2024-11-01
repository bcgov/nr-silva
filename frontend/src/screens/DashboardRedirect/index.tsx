import React from "react";
import { Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import { useAuth } from "../../contexts/AuthProvider";

const DashboardRedirect: React.FC = () => {
  const { user } = useAuth();
  return (
    <>
      {user ? <Navigate to={"/opening"} replace /> : <Loading className={'some-class'} withOverlay={true} />}
    </>
  );
};

export default DashboardRedirect;
