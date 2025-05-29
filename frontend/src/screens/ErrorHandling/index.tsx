import React from "react";
import {
  isRouteErrorResponse,
  useRouteError,
  Navigate,
} from "react-router-dom";

const ErrorHandling: React.FC = () => {
  const error = useRouteError();

  if (isRouteErrorResponse(error)) {
    if (error.status === 401) {
      return <Navigate to={"/"} replace />;
    } else if (error.status === 403) {
      return <h1>Unauthorized</h1>;
    } else if (error.status === 404) {
      return <h1>Page Not Found</h1>;
    }
  }

  // Default error message for other types of errors
  return <h1>Oops! Something went wrong</h1>;
};

export default ErrorHandling;
