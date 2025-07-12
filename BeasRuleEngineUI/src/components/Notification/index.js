import MDSnackbar from "components/MDSnackbar";
import React, { useImperativeHandle, useState } from "react";

const Notification = React.forwardRef((props, ref) => {
    const Types = {
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
    };
    useImperativeHandle(ref, () => ({
        show(opts) {
            setOptions(opts).then(show());
        },
    }));

    const [infoSB, setInfoSB] = useState(false);
    const openInfoSB = () => setInfoSB(true);
    const closeInfoSB = () => setInfoSB(false);
    const [snackOptions, setSnackOptions] = useState({
        icon: "notifications",
        title: "Title",
        content: "Content",
        dateTime: "1 sec ago",
        reason: "Page Execution"
    });
    const setOptions = async (opts) => {
        return new Promise((resolve) => {
            setSnackOptions(opts);
            resolve()
        })
    };
    const show = () => {
        openInfoSB();
    }
    return (
        <MDSnackbar
            color={snackOptions.color}
            icon={snackOptions.icon}
            title={snackOptions.title}
            content={snackOptions.content}
            dateTime={snackOptions.dateTime}
            reason={snackOptions.reason}
            open={infoSB}
            onClose={closeInfoSB}
            close={closeInfoSB}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        />
    );
});

export default Notification;