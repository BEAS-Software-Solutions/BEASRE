import React from "react";
import { Navigate } from "react-router-dom";
import authService from "./authService";

const ProtectedRoute = ({ user, children }) => {
    if (!user) {
        //authService.login();
        //return <Navigate to="/authentication/sign-in" />;
    }
    return children;
};

export default ProtectedRoute;
