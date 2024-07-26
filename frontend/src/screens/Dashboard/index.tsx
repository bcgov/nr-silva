import React from "react";
import { useSelector } from "react-redux";
import { RootState } from "../../store";

const Dashboard: React.FC = () => {
  const userDetails = useSelector((state: RootState) => state.userDetails);
  const selectedClientRoles = useSelector((state: RootState) => state.selectedClientRoles);
  const { user } = userDetails;

  return (
    <>
      <div className="container-fluid">
        <h4 className='py-4'>
          Hello&nbsp;
          <span className='fw-bold'>{user.firstName+" "+user.lastName}</span>,
          welcome to the SILVA portal. You are logged in with the client&nbsp;
          <span className="fw-bold">{selectedClientRoles?.clientName}</span>!! </h4>
      </div>
    </>
  );
};

export default Dashboard;
