import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

interface IProps {
  signed: boolean;
  children: JSX.Element;
}

const ProtectedRoute = ({ signed, children }: IProps): JSX.Element => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const { error, user } = userDetails
  const { pathname } = window.location;
  const encodedUrl = encodeURI(`/?page=${pathname}`);
  return (
    error? (
      <div className="h1">Error</div>
    ):(
      (() => {
        if(user?.isLoggedIn){
          console.log("User:", user);
          return children;  
        }
        else if(user?.isLoggedIn === false){
          return <Navigate to={encodedUrl} replace />;
        }

        return <>Sorry </>
      })()
    )
  );
};

export default ProtectedRoute ;