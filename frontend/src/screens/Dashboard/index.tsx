import React from "react";
import { useSelector } from "react-redux";


const Dashboard: React.FC = () => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const { user } = userDetails
    return (
      <>
      <div className="container">
        <h4 className='py-4'>Hello <span className='fw-bold'>{user.firstName+" "+user.lastName}</span>, welcome to the SILVA portal. You are logged in with the client <span className="fw-bold">{userDetails.selectedClientRoles.clientName}</span>!! </h4>
      </div>
      </>
    );
  };

export default Dashboard;