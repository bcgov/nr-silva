import React, { useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import { getUserDetails } from '../actions/userAction';
import { Loading } from "@carbon/react";
import { RootState } from '../store';
import { useDispatch, useSelector } from 'react-redux';

interface IProps {
  children: JSX.Element;
}

const PostLoginRoute = ({ children }: IProps): JSX.Element => {
  const userDetails = useSelector((state: RootState) => state.userDetails)
  const { loading, error, user } = userDetails
  const { pathname } = window.location;
  const encodedUrl = encodeURI(`/?page=${pathname}`);

  const dispatch:any = useDispatch();
  useEffect(()=>{
    dispatch(getUserDetails())
  },[dispatch])

  return (
    loading ? (
      <Loading className={'some-class'} withOverlay={true} />
    ):
    error? (
      <div className="h1">Error</div>
    ):(
      (() => {
        if(user?.isLoggedIn === true){
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