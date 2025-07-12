

import Card from "@mui/material/Card";
import MDBox from "components/MDBox";

import MDTypography from "components/MDTypography";
import DashboardLayout from "widgets/LayoutContainers/DashboardLayout";
import DashboardNavbar from "widgets/Navbars/DashboardNavbar";
import RulesUI from "./ui";

function RulesLayout() {

    return (

        <DashboardLayout>
            <DashboardNavbar />
            <MDBox>
                <Card style={{ textAlign: "center", padding: "12px" }}>
                    <RulesUI />
                </Card>
            </MDBox>
        </DashboardLayout>
    );
}

export default RulesLayout;
