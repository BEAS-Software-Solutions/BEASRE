

import Card from "@mui/material/Card";
import MDBox from "components/MDBox";

import DashboardLayout from "widgets/LayoutContainers/DashboardLayout";
import DashboardNavbar from "widgets/Navbars/DashboardNavbar";
import FunctionUI from "./ui";

function FunctionLayout() {

    return (

        <DashboardLayout>
            <DashboardNavbar />
            <MDBox>
                <Card style={{ textAlign: "center", padding: "12px" }}>
                    <FunctionUI />
                </Card>
            </MDBox>
        </DashboardLayout>
    );
}

export default FunctionLayout;
