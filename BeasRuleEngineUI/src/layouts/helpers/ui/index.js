import { postData, deleteData, patchData } from "api/api";
import GenericDialog from "components/GenericDialog";
import CreateDialogBody from "./dialogs/CreateDialogBody";
import EditDialogBody from "./dialogs/EditDialogBody";
import { useRef } from "react";
import { useConfirm } from "material-ui-confirm";
import MDButton from "components/MDButton";
import { Card, Icon } from "@mui/material";
import DataTableWithPagination from "components/DataTable";
import Notification from "components/Notification";

const { default: MDBox } = require("components/MDBox");
const { default: DataModel } = require("../model");

function HelperUI({ children }) {

    const datatable = useRef();
    const createDialogRef = useRef();
    const editDialogRef = useRef();

    const notifiRef = useRef();
    const confirm = useConfirm();

    const createNew = () => {
        if (createDialogRef.current) {
            createDialogRef.current.showDialog(DataModel.createDialogOpts);
        }
    };

    const createSubmitAction = (formJson) => {
        postData("/rule-helper/create", {
            "name": formJson.name,
            "description": formJson.description,
            "packageUrl": formJson.packageUrl,
            "packagePath": formJson.packagePath,
            "containerName": formJson.containerName,
        }).then((obj) => {
            DataModel
                .message(DataModel.SUCCESS, "Create Request", "Create request is completed.", Date.now)
                .then((o) => {
                    showSnack(o).then(() => datatable.current.refresh());
                });
        }).catch((e) => {
            console.error(e);
        });
    };

    const editSubmitAction = (formJson) => {
        patchData("/rule-helper/update", {
            "id": formJson.id,
            "name": formJson.name,
            "description": formJson.description,
            "packageUrl": formJson.packageUrl,
            "packagePath": formJson.packagePath,
            "containerName": formJson.containerName,
        }).then((obj) => {
            DataModel
                .message(DataModel.SUCCESS, "Edit Request", "Edit request is completed.", Date.now)
                .then((o) => {
                    showSnack(o).then(() => datatable.current.refresh());;
                });
        }).catch((e) => {
            console.error(e);
        });
    };

    const handleDelete = async (item) => {
        return new Promise((resolve) => {
            confirm({ description: `This will permanently delete ${item.id}.` })
                .then(() => {
                    deleteData("/rule-helper/delete/" + item.id).then(() => resolve());
                })
                .then(() => {
                    DataModel
                        .message(DataModel.SUCCESS, "Delete Request", "Delete request is completed!", Date.now)
                        .then((opts) => showSnack(opts).then(() => datatable.current.refresh()));
                })
                .catch((e) => {
                    console.error(e);
                });
        });
    };

    const handleEdit = async (item) => {
        if (editDialogRef.current) {
            editDialogRef.current.showDialog(DataModel.editDialogOpts, item);
        }
    };

    const showSnack = async (opts) => {
        new Promise((resolve) => {
            if (notifiRef.current) {
                notifiRef.current.show(opts);
                resolve(opts);
            }
        });
    };

    return (
        <MDBox>
            <Notification ref={notifiRef} />
            <GenericDialog ref={createDialogRef} prop submitAction={createSubmitAction} body={CreateDialogBody} />
            <GenericDialog ref={editDialogRef} prop submitAction={editSubmitAction} body={EditDialogBody} />
            <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3}>
                <MDButton variant="contained" color="secondary" onClick={createNew}>
                    <Icon sx={{ fontWeight: "bold" }}>{"add"}</Icon>
                    <span>Add New</span>
                </MDButton>
            </MDBox>
            <MDBox>
                <Card>
                    <DataTableWithPagination
                        ref={datatable}
                        cols={DataModel.cols}
                        handleEdit={handleEdit}
                        handleDelete={handleDelete}
                        fetchUrl={"/rule-helper/read"}
                        searchKey={"name"}
                    />
                </Card>
            </MDBox>
        </MDBox>
    );
}
export default HelperUI;