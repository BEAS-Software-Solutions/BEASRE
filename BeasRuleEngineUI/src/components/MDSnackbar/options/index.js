const MDSnackbarOptions = {
    createDialogOpts: {
        submitButtonText: "Create",
        cancelButtonText: "Cancel",
        title: "Create Project",
        content: "Please enter the information you want",
    },
    editDialogOpts: {
        submitButtonText: "Edit",
        cancelButtonText: "Cancel",
        title: "Edit Project",
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
};

export default MDSnackbarOptions;

