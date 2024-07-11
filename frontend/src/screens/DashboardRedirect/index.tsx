import React from "react";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import LoginOrgSelection from "../../views/LoginOrgSelection";
import SideLayout from "../../layouts/SideLayout";
import Dashboard from "../Dashboard";

const DashboardRedirect: React.FC = () => {
  const userDetails = useSelector((state: any) => state.userDetails);
  const selectedClientRoles = useSelector((state:any)=>state.selectedClientRoles)
  const { user } = userDetails;

  const navigate = useNavigate();

  // Redirect logic based on selectedClientRoles existence
  useEffect(() => {
    if (user && selectedClientRoles) {
      navigate("/dashboard");
    }
  }, [user, selectedClientRoles]);

  return (
    <>
      {user && selectedClientRoles ? (
        <SideLayout pageContent={<Dashboard />} />
      ) : (
        <LoginOrgSelection />
      )}
    </>
  );
};

export default DashboardRedirect;
