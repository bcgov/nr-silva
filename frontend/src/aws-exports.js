import { env } from './env';

const ZONE = env.VITE_ZONE.toLocaleLowerCase();
const retUrl = window.location.origin;
const logoutDomain = `https://logon${ZONE === "prod"?'':'test'}7.gov.bc.ca`;



const awsconfig = {
    aws_cognito_region: env.VITE_COGNITO_REGION || "ca-central-1",
    aws_user_pools_id: env.VITE_USER_POOLS_ID,
    aws_user_pools_web_client_id: env.VITE_USER_POOLS_WEB_CLIENT_ID,
    aws_mandatory_sign_in: 'enable',
    oauth: {
        domain: env.VITE_AWS_DOMAIN || "prod-fam-user-pool-domain.auth.ca-central-1.amazoncognito.com",
        scope: ['openid'],
        redirectSignIn: `${window.location.origin}/dashboard`,
        redirectSignOut: env.VITE_REDIRECT_SIGN_OUT || `${logoutDomain}/clp-cgi/logoff.cgi?retnow=1&returl=https://${ZONE}.loginproxy.gov.bc.ca/auth/realms/standard/protocol/openid-connect/logout?redirect_uri=${retUrl}/`,
        responseType: 'code',
    },
    federationTarget: 'COGNITO_USER_POOLS',
};

export default awsconfig;
