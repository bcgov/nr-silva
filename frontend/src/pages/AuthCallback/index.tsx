import { Button, Column, Grid, InlineNotification, Loading } from '@carbon/react';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { REDIRECT_KEY } from '@/constants';
import { useAuth } from '@/contexts/AuthProvider';
import { AUTH_CALLBACK_PATH, DASHBOARD_PATH } from '@/routes/paths';

const resolveRedirectTarget = (rawPath: string | null): string => {
  if (
    !rawPath ||
    !rawPath.startsWith('/') ||
    rawPath.startsWith('//') ||
    rawPath.startsWith(AUTH_CALLBACK_PATH)
  ) {
    return DASHBOARD_PATH;
  }
  return rawPath;
};

/**
 * OIDC authorization-code PKCE callback handler for `/authCallback`.
 */
const AuthCallback = () => {
  const { completeLogin } = useAuth();
  const navigate = useNavigate();
  const hasStartedRef = useRef(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    if (hasStartedRef.current) {
      return;
    }
    hasStartedRef.current = true;

    const targetPath = resolveRedirectTarget(localStorage.getItem(REDIRECT_KEY));
    localStorage.removeItem(REDIRECT_KEY);

    void (async () => {
      try {
        await completeLogin(async () => {
          await navigate(targetPath, { replace: true });
        });
      } catch (err) {
        console.error('OIDC callback processing failed:', err);
        setErrorMessage('We could not complete your sign-in. Please return to the home page and try again.');
      }
    })();
  }, [completeLogin, navigate]);

  if (errorMessage) {
    return (
      <Grid className="default-grid">
        <Column sm={4} md={8} lg={16}>
          <InlineNotification
            kind="error"
            title="Sign-in failed"
            subtitle={errorMessage}
            hideCloseButton
          />
          <Button
            kind="primary"
            onClick={() => {
              void navigate('/', { replace: true });
            }}
          >
            Return to Home
          </Button>
        </Column>
      </Grid>
    );
  }

  return <Loading withOverlay description="Completing sign-in..." />;
};

export default AuthCallback;
