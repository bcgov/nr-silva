import React from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import LoginOrgSelection from "../../views/LoginOrgSelection";
import SideLayout from "../../layouts/SideLayout";
import Opening from "../Opening";
import { RootState } from "../../store";
import { useSelector } from "react-redux";

const DashboardRedirect: React.FC = () => {
  const userDetails = useSelector((state: RootState) => state.userDetails);
  const selectedClientRoles = useSelector((state:any)=>state.selectedClientRoles)
  const { user } = userDetails;

  const navigate = useNavigate();

  // Redirect logic based on selectedClientRoles existence
  useEffect(() => {
    if (user && selectedClientRoles) {
      navigate("/opening");
    }
  }, [user, selectedClientRoles]);

  return (
    <>
      {user && selectedClientRoles ? (
        <SideLayout pageContent={<Opening />} />
      ) : (
        <LoginOrgSelection />
      )}
    </>
  );
};

export default DashboardRedirect;
