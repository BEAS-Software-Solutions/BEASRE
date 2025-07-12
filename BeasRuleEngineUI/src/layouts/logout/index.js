

import Card from "@mui/material/Card";
import MDBox from "components/MDBox";


import MDTypography from "components/MDTypography";

import { useEffect, useRef } from "react";
import authService from "services/authService";

function LogoutUI() {
    const logoutCard = useRef();
    useEffect(() => {
        authService.logout();
    }, [logoutCard]);

    return (
        <MDBox>
            <Card ref={logoutCard} style={{ textAlign: "center", padding: "12px" }}>
                <MDTypography color="dark" mt={1}>
                    Logging out...
                </MDTypography>
            </Card>
        </MDBox>
    );
}

export default LogoutUI;
