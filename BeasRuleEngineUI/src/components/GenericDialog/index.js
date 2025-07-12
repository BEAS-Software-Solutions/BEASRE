import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Paper } from "@mui/material";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import { useMaterialUIController } from "context";
import React, { useState } from "react";
import styled from "styled-components";

const GenericDialog = React.forwardRef((props, ref) => {
  const { submitAction, body } = props;
  const [item, setItem] = useState(null);
  const [dialog, show] = useState(false);
  const close = () => show(false);
  const [controller] = useMaterialUIController();
  const { darkMode } = controller;
  const [dialogOptions, setDialogOptions] = useState({
    submitButtonText: "Submit",
    cancelButtonText: "Cancel",
    title: "Generic Dialog",
    content: "Generic Dialog Content",
  });

  React.useImperativeHandle(ref, () => ({
    showDialog(opts, item) {
      options(opts, item).then(show(true));
    },
    closeDialog: close,
  }));

  const options = async (opts, item) => {
    return new Promise((resolve) => {
      setItem(item);
      setDialogOptions(opts);
      resolve();
    });
  };


  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: darkMode ? 'dark' : 'white',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: darkMode ? 'white' : 'black',
  }));

  return (
    <React.Fragment>
      <Dialog
        maxWidth="lg"
        maxHeight="lg"
        open={dialog}
        onClose={close}
        PaperProps={{
          component: "form",
          onSubmit: (event) => {
            event.preventDefault();
            if (submitAction) {
              const formData = new FormData(event.currentTarget);
              const formJson = Object.fromEntries(formData.entries());
              submitAction(formJson);
            }
            close();
          },
        }}
      >
        <DialogTitle style={{backgroundColor:darkMode?"#424242":"white"}}><MDTypography>{dialogOptions.title}</MDTypography> </DialogTitle>
        <DialogContent style={{backgroundColor:darkMode?"#424242":"white"}}>
          <DialogContentText style={{color:darkMode?"white":"black"}}>
            {dialogOptions.content}
          </DialogContentText>
          {body(item)}
        </DialogContent>
        <DialogActions style={{backgroundColor:darkMode?"#424242":"white"}}>
          <MDButton variant="contained" color="dark" onClick={close}>{dialogOptions.cancelButtonText}</MDButton>
          <MDButton variant="contained" color="success" type="submit">{dialogOptions.submitButtonText}</MDButton>
        </DialogActions>
      </Dialog>
    </React.Fragment>
  );
});

export default GenericDialog;

