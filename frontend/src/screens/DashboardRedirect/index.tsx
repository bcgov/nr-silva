import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import OrganizationSelection from "../../components/OrganizationSelection";
import LoginOrgSelection from "../../views/LoginOrgSelection";
import SideLayout from "../../layouts/SideLayout";
import Dashboard from "../Dashboard";

const DashboardRedirect: React.FC = () => {
  const userDetails = useSelector((state: any) => state.userDetails);
  const { user } = userDetails;
  const navigate = useNavigate();

  useEffect(() => {
    if (user && user.selectedClientRoles) {
      navigate("/dashboard");
    }
  }, [user, navigate]);

  return (
    <>
      {user && user.selectedClientRoles ? (
        <SideLayout pageContent={<Dashboard />} />
      ) : (
        <LoginOrgSelection />
      )}
    </>
  );
};

export default DashboardRedirect;
