export const oidcConfig = {
    authority: process.env.REACT_APP_OIDC_AUTHORITY,
    client_id: process.env.REACT_APP_OIDC_CLIENT_ID,
    redirect_uri: process.env.REACT_APP_OIDC_REDIRECT_URI,
    post_logout_redirect_uri: process.env.REACT_APP_OIDC_POST_LOGOUT_REDIRECT_URI,
    response_type: process.env.REACT_APP_OIDC_RESPONSE_TYPE,
    scope: process.env.REACT_APP_OIDC_SCOPE,

    authorization_endpoint: process.env.REACT_APP_OIDC_AUTHORIZATION_ENDPOINT,
    token_endpoint: process.env.REACT_APP_OIDC_TOKEN_ENDPOINT,
    userinfo_endpoint: process.env.REACT_APP_OIDC_USERINFO_ENDPOINT,
    end_session_endpoint: process.env.REACT_APP_OIDC_END_SESSION_ENDPOINT,
    introspection_endpoint: process.env.REACT_APP_OIDC_INTROSPECTION_ENDPOINT,
    jwks_uri: process.env.REACT_APP_OIDC_JWKS_URI
};

export default oidcConfig;
