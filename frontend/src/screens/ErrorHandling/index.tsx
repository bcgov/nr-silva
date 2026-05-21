import React from "react";
import {
  isRouteErrorResponse,
  useRouteError,
  Navigate,
  useNavigate,
} from "react-router-dom";
import { Button, Stack, Heading } from "@carbon/react";
import { Information, Activity, WarningAlt } from "@carbon/icons-react";

import './styles.scss';

const CHUNK_RELOAD_KEY = 'silva_chunk_reload_attempted';

function isChunkLoadError(err: unknown): boolean {
  if (!(err instanceof Error)) return false;
  return (
    err.message.includes('Failed to fetch dynamically imported module') ||
    err.message.includes('Importing a module script failed') ||
    err.name === 'ChunkLoadError'
  );
}

const ErrorHandling: React.FC = () => {
  const error = useRouteError();
  const navigate = useNavigate();

  // When a lazy-loaded chunk 404s after a new deployment, auto-reload once to
  // pick up the fresh bundle. A sessionStorage flag prevents infinite loops if
  // the reload itself doesn't resolve the error.
  if (isChunkLoadError(error)) {
    if (!sessionStorage.getItem(CHUNK_RELOAD_KEY)) {
      sessionStorage.setItem(CHUNK_RELOAD_KEY, '1');
      window.location.reload();
      return null;
    }
  }

  const GoHomeBtn = () => (
    <Button onClick={() => navigate("/")}>
      Go to dashboard
    </Button>
  );

  if (isRouteErrorResponse(error)) {
    if (error.status === 401) {
      return <Navigate to="/" replace />;
    } else if (error.status === 403) {
      return (
        <div className="error-page-container">
          <Stack className="error-title" gap={4} orientation="horizontal">
            <Activity size={64} />
            <Heading>Access Denied</Heading>
          </Stack>
          <p>
            You don't have permission to access this resource. Please contact an administrator if you believe this is a mistake.
          </p>
          <GoHomeBtn />
        </div>
      );
    } else if (error.status === 404) {
      return (
        <div className="error-page-container">
          <Stack className="error-title" gap={4} orientation="horizontal">
            <Information size={64} />
            <Heading>Page Not Found</Heading>
          </Stack>
          <p>
            We couldn't find the page you're looking for. It may have been moved or no longer exists.
          </p>
          <GoHomeBtn />
        </div>
      );
    }
  }

  // Default error message for other types of errors
  return (
    <div className="error-page-container">
      <Stack className="error-title" gap={4} orientation="horizontal">
        <WarningAlt size={64} />
        <Heading>Oops! Something Went Wrong</Heading>
      </Stack>
      <p>
        An unexpected error occurred. Please try again or contact support if the problem persists.
      </p>
      <GoHomeBtn />
    </div>
  );
};

export default ErrorHandling;
