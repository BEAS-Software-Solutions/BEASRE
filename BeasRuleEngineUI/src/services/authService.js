import { UserManager, WebStorageStateStore } from "oidc-client-ts";
import { oidcConfig } from "../config/oidcConfig";

const userManager = new UserManager({
    ...oidcConfig,
    userStore: new WebStorageStateStore({ store: window.localStorage }),
});

const authService = {
    login: () => userManager.signinRedirect(),
    logout: () => userManager.signoutRedirect(),
    getUser: () => userManager.getUser(),
    handleCallback: () => userManager.signinRedirectCallback(),
};

export default authService;
