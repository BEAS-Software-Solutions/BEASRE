

import Card from "@mui/material/Card";
import MDBox from "components/MDBox";

import DashboardLayout from "widgets/LayoutContainers/DashboardLayout";
import DashboardNavbar from "widgets/Navbars/DashboardNavbar";
import HelperUI from "./ui";

function HelperLayout() {

    return (

        <DashboardLayout>
            <DashboardNavbar />
            <MDBox>
                <Card style={{ textAlign: "center", padding: "12px" }}>
                    <HelperUI />
                </Card>
            </MDBox>
        </DashboardLayout>
    );
}

export default HelperLayout;
