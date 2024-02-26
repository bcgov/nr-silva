import { env } from './env';
import { ResourcesConfig } from '@aws-amplify/core';

const ZONE = env.VITE_ZONE.toLocaleLowerCase();
const redirectUri = window.location.origin;
const logoutDomain = `https://logon${ZONE === "prod"?'':'test'}7.gov.bc.ca`;
const retUrl = `https://${ZONE === "prod" ? "loginproxy" :ZONE === "test" ? "test.loginproxy": "dev.loginproxy"}.gov.bc.ca/auth/realms/standard/protocol/openid-connect/logout`;

const redirectSignOut =
  env.VITE_REDIRECT_SIGN_OUT && env.VITE_REDIRECT_SIGN_OUT.trim() !== ""
    ? env.VITE_REDIRECT_SIGN_OUT
    : `${logoutDomain}/clp-cgi/logoff.cgi?retnow=1&returl=${retUrl}?redirect_uri=${redirectUri}/`;

// https://docs.amplify.aws/javascript/build-a-backend/auth/set-up-auth/
const amplifyconfig: ResourcesConfig = {
  Auth: {
    Cognito: {
      userPoolId: env.VITE_USER_POOLS_ID ?? "ca-central-1_t2HSZBHur",
      userPoolClientId: env.VITE_USER_POOLS_WEB_CLIENT_ID ?? "70a2am185rie10r78b0ugcs1mm",
      signUpVerificationMethod: 'code', // 'code' | 'link'
      loginWith: {
        oauth: {
          domain: env.VITE_AWS_DOMAIN || "prod-fam-user-pool-domain.auth.ca-central-1.amazoncognito.com",
          scopes: ['openid',],
          redirectSignIn: [`${window.location.origin}/dashboard`],
          redirectSignOut: [redirectSignOut],
          responseType: 'code'
        }
      }
    }
  }
};

export default amplifyconfig;
