import { env } from './env';

const awsconfig = {
    aws_cognito_region: env.REACT_APP_COGNITO_REGION || "ca-central-1",
    aws_user_pools_id: env.REACT_APP_USER_POOLS_ID,
    aws_user_pools_web_client_id: env.REACT_APP_USER_POOLS_WEB_CLIENT_ID,
    aws_mandatory_sign_in: 'enable',
    oauth: {
        domain: env.REACT_APP_AWS_DOMAIN || "prod-fam-user-pool-domain.auth.ca-central-1.amazoncognito.com",
        scope: ['openid'],
        redirectSignIn: `${window.location.origin}/dashboard`,
        redirectSignOut: env.REACT_APP_REDIRECT_SIGN_OUT || "https://logontest7.gov.bc.ca/clp-cgi/logoff.cgi?retnow=1&returl=https://dev.loginproxy.gov.bc.ca/auth/realms/standard/protocol/openid-connect/logout?redirect_uri=http://localhost:3000/",
        responseType: 'code',
    },
    federationTarget: 'COGNITO_USER_POOLS',
};

export default awsconfig;
