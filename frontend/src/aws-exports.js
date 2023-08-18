const awsconfig = {
    aws_cognito_region: process.env.REACT_APP_COGNITO_REGION,
    aws_user_pools_id: process.env.REACT_APP_USER_POOLS_ID,
    aws_user_pools_web_client_id: process.env.REACT_APP_USER_POOLS_WEB_CLIENT_ID,
    aws_mandatory_sign_in: 'enable',
    oauth: {
        domain: process.env.REACT_APP_AWS_DOMAIN,
        scope: ['openid'],
        redirectSignIn: "http://localhost:3000/dashboard",
        redirectSignOut: "http://localhost:3000/",
        responseType: 'code',
    },
    federationTarget: 'COGNITO_USER_POOLS',
};

export default awsconfig;