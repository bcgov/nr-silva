import React from "react";
import { useSelector } from "react-redux";


const Dashboard: React.FC = () => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const { user } = userDetails
    return (
      <>
      <div className="container">
        <h4 className='py-4'>Hello <span className='fw-bold'>{user.firstName+" "+user.lastName}</span>, welcome to the SILVA portal. Please take a moment to review the available options and select the test that best matches your access requirements:</h4>
      </div>
      </>
    );
  };

export default Dashboard;