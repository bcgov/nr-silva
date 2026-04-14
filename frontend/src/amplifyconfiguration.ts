import { env } from './env';

const ZONE = env.VITE_ZONE ? env.VITE_ZONE.toLowerCase() : 'dev';
const retUrlEnv = ZONE !== 'prod' && ZONE !== 'test' ? 'dev' : ZONE;

const retUrlString = ZONE === 'prod'
  ? 'https://loginproxy.gov.bc.ca/auth/realms/standard/protocol/openid-connect/logout'
  : `https://${retUrlEnv}.loginproxy.gov.bc.ca/auth/realms/standard/protocol/openid-connect/logout`;

const logoutDomain = ZONE === 'prod' ? 'https://logon7.gov.bc.ca' : 'https://logontest7.gov.bc.ca';

const redirectSignOut = env.VITE_REDIRECT_SIGN_OUT?.trim()
  ? env.VITE_REDIRECT_SIGN_OUT
  : [
    `${logoutDomain}/clp-cgi/logoff.cgi`,
    '?retnow=1',
    `&returl=${retUrlString}`,
    `?redirect_uri=${window.location.origin}/`
  ].join('');

const redirectUri = window.location.origin;

const verificationMethods: 'code' | 'token' = 'code';

const amplifyconfig = {
  Auth: {
    Cognito: {
      userPoolId: env.VITE_USER_POOLS_ID,
      userPoolClientId: env.VITE_USER_POOLS_WEB_CLIENT_ID,
      signUpVerificationMethod: verificationMethods,
      loginWith: {
        oauth: {
          domain: 'lza-prod-fam-user-pool-domain.auth.ca-central-1.amazoncognito.com',
          scopes: ['openid'],
          redirectSignIn: [`${redirectUri}/dashboard`],
          redirectSignOut: [redirectSignOut],
          responseType: verificationMethods
        }
      }
    }
  }
};

export default amplifyconfig;
