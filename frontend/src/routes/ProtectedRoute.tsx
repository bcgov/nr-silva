import React from 'react';
import { Navigate } from 'react-router-dom';
import { RootState } from '../store';
import { useSelector } from 'react-redux';

interface IProps {
  children: JSX.Element;
}

const ProtectedRoute = ({ children }: IProps): JSX.Element => {
  const userDetails = useSelector((state: RootState) => state.userDetails);
  const { error, user } = userDetails;
  const { pathname } = window.location;
  const encodedUrl = encodeURI(`/?page=${pathname}`);
  return (
    error? (
      <div className="h1">Error</div>
    ):(
      (() => {
        if(user?.isLoggedIn){
          return children;  
        }
        else if(user?.isLoggedIn === false){
          return <Navigate to={encodedUrl} replace />;
        }

        return <>Sorry </>;
      })()
    )
  );
};

export default ProtectedRoute;
