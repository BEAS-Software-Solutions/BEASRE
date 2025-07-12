import { format } from "date-fns";
const DataModel = {
 cols: [
        {
            name: 'Name',
            selector: row => row.name,
            sortable: true,
        },
        {
            name: 'Description',
            selector: row => row.description,
            sortable: true,
        },
        {
            name: 'Container Name',
            selector: row => row.containerName,
            sortable: true,
        },
        {
            name: 'Created By',
            selector: row => row.createdBy,
            sortable: true,
        },
        {
            name: 'Created Date',
            selector: row => format(new Date(row.createdDate), 'dd/MM/yyyy HH:mm'),
            sortable: true,
        },
    ],
    createDialogOpts: {
        submitButtonText: "Create",
        cancelButtonText: "Cancel",
        title: "Create Function",
        content: "Please enter the information you want",
    },
    editDialogOpts: {
        submitButtonText: "Edit",
        cancelButtonText: "Cancel",
        title: "Edit Function",
        content: "Please enter the information you want",
    },
    WARNING: {
        color: "warning",
        icon: "warning",
        title: "",
        content: "",
        info: "",
    },
    ERROR: {
        color: "error",
        icon: "error",
        title: "",
        content: "",
        info: "",
    },
    SUCCESS: {
        color: "success",
        icon: "success",
        title: "",
        content: "",
        info: "",
    },
    async message(type, title, content, info) {
        return new Promise((resolve) => {
            type.title = title;
            type.content = content;
            type.info = info;
            resolve(type);
        });
    },
}
export default DataModel;