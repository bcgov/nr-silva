import React from "react";
import { useSelector } from "react-redux";
import StandardCard from "../../components/StandardCard";


const Opening: React.FC = () => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const { user } = userDetails
    return (
      <>
      <div className="container">
        <h4 className='py-4'>Hello <span className='fw-bold'>{user.firstName+" "+user.lastName}</span>, Opening</h4>
      </div>
      </>
    );
  };

export default Opening;