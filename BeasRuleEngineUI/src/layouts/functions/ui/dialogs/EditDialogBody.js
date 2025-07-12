import { Grid, TextareaAutosize, TextField } from "@mui/material";
import CodeMirror from "@uiw/react-codemirror";
import { java } from "@codemirror/lang-java";
import { useState } from "react";

const EditDialogBody = (item) => {
    const [code, setCode] = useState("");
    return (
        <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="id"
                    name="id"
                    label="Id"
                    readOnly
                    placeholder="Id is read only"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.id || ""}
                    InputProps={{
                        readOnly: true,
                    }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="name"
                    name="name"
                    label="Name"
                    placeholder="Enter name here"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.name || ""}
                    InputProps={{
                        readOnly: true,
                    }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="description"
                    name="description"
                    label="Description"
                    placeholder="Enter description here"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.description || ""}
                    onChange={(e)=>{item.description=e.target.value;}}
                />
            </Grid>
            <Grid item xs={12}>
                <CodeMirror
                    required
                    margin="dense"
                    type="text"
                    label="MVL Code"
                    placeholder="Enter MVL code here"
                    fullWidth
                    variant="standard"
                    value={item?.mvlCode || ""}
                    height="300px"
                    extensions={[java()]}
                    onChange={(value) => setCode(value)}
                    theme="light"
                    defaultValue={item?.mvlCode || ""}
                />
                <input type="hidden" value={code} id="mvlCode" name="mvlCode" />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required={false}
                    margin="dense"
                    id="category"
                    name="category"
                    label="Category"
                    placeholder="Category are optional, enter helpers here with comma separated"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.category || ""}
                    onChange={(e)=>{item.category=e.target.value;}}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required={false}
                    margin="dense"
                    id="containerName"
                    name="containerName"
                    label="Container Name"
                    placeholder="Container name is optional, enter category name here"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.containerName || ""}
                    onChange={(e)=>{item.containerName=e.target.value;}}
                />
            </Grid>
        </Grid>
    );
};

export default EditDialogBody;

