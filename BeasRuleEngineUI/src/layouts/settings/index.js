

import { Grid } from "@mui/material";
import Card from "@mui/material/Card";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";

import DashboardLayout from "widgets/LayoutContainers/DashboardLayout";
import DashboardNavbar from "widgets/Navbars/DashboardNavbar";

import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";
import { getData } from "api/api";
import { useRef } from "react";
import { useConfirm } from "material-ui-confirm";
import Notification from "components/Notification";

function SettingsLayout() {
    const notifiRef = useRef();
    const confirm = useConfirm();

    const message = async (type, title, content, info) => {
        return new Promise((resolve) => {
            type.title = title;
            type.content = content;
            type.info = info;
            resolve(type);
        });
    }

    const WARNING = {
        color: "warning",
        icon: "warning",
        title: "",
        content: "",
        info: "",
    };
    const ERROR = {
        color: "error",
        icon: "error",
        title: "",
        content: "",
        info: "",
    };
    const SUCCESS = {
        color: "success",
        icon: "success",
        title: "",
        content: "",
        info: "",
    };

    const showSnack = async (opts) => {
        new Promise((resolve) => {
            if (notifiRef.current) {
                notifiRef.current.show(opts);
                resolve(opts);
            }
        });
    };

    const synchronization = async (item) => {
        return new Promise((resolve) => {
            confirm({ description: `All rules, functions and helpers will be deleted!` })
                .then(() => {
                    getData("/rule-engine/sync").then(() => resolve());
                })
                .then(() => {
                    message(SUCCESS, "Sync Request", "Sync request is completed!", Date.now)
                        .then((opts) => showSnack(opts));
                })
                .catch((e) => {
                    console.error(e);
                });
        });
    };
    return (

        <DashboardLayout>
            <DashboardNavbar />
            <MDBox>
                <Notification ref={notifiRef} />
                <Card style={{ textAlign: "left", padding: "12px" }}>
                    <MDButton style={{ width: "200px" }} 
                    variant="gradient" color="info" size="small"
                    onClick={() => synchronization()}>
                        Synch Engine
                    </MDButton>
                    <hr style={{ margin: "12px", color: "gray" }} />
                    <Grid container spacing={2} justifyContent="left">
                        <Grid item xs={12} sm={6} md={9} style={{ textAlign: "left" }}>
                            <SwaggerUI url="http://localhost:8070/beasre/v1/v3/api-docs/public" />
                        </Grid>
                    </Grid>
                </Card>
            </MDBox>
        </DashboardLayout>
    );
}

export default SettingsLayout;
