import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import authService from "services/authService";


const Callback = () => {
    const navigate = useNavigate();

    useEffect(() => {
        authService.handleCallback().then(() => {
            navigate("/rules");
        }).catch((error) => {
            console.error("Error during authentication callback:", error);
            navigate("/logout");
        });
    }, [navigate]);

    return <p>Signing in...</p>;
};

export default Callback;
