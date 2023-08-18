import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import { USER_DETAILS_REQUEST } from '../constants/userConstants';
import { getUserDetails } from '../actions/userAction';
import { Loading } from "@carbon/react";

interface IProps {
  signed: boolean;
  children: JSX.Element;
}

const PostLoginRoute = ({ signed, children }: IProps): JSX.Element => {
  const userDetails = useSelector((state:any) => state.userDetails)
  const { loading, error, user } = userDetails
  const { pathname } = window.location;
  const encodedUrl = encodeURI(`/?page=${pathname}`);

  const dispatch:any = useDispatch();
  useEffect(()=>{
    dispatch(getUserDetails())
  },[dispatch])

  // if (!signed) {
    // const { pathname } = window.location;
    // const encodedUrl = encodeURI(`/?page=${pathname}`);
  //   return <Navigate to={encodedUrl} replace />;
  // }

  return (
    loading ? (
      <Loading className={'some-class'} withOverlay={true} />
    ):
    error? (
      <div className="h1">Error</div>
    ):(
      (() => {
        if(user?.isLoggedIn === true){
          console.log("User:", user);
          return children;  
        }
        else if(user?.isLoggedIn === false){
          return <Navigate to={encodedUrl} replace />;
        }

        return <></>
      })()
    )
  );
};

export default PostLoginRoute ;