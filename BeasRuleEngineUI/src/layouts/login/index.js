

import Card from "@mui/material/Card";
import MDBox from "components/MDBox";

import MDTypography from "components/MDTypography";

import { useEffect, useRef } from "react";
import authService from "services/authService";

function LoginUI() {
    const logoutCard = useRef();
    useEffect(() => {
        authService.login();
    }, [logoutCard]);

    return (
        <MDBox>
            <Card ref={logoutCard} style={{ textAlign: "center", padding: "12px" }}>
                <MDTypography color="dark" mt={1}>
                    Logging in...
                </MDTypography>
            </Card>
        </MDBox>
    );
}

export default LoginUI;
