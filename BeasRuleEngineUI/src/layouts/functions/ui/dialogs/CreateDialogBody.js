import { Grid, TextareaAutosize, TextField } from "@mui/material";
import CodeMirror from "@uiw/react-codemirror";
import { java } from "@codemirror/lang-java";
import { useState } from "react";

const CreateDialogBody = (item) => {
    const [code, setCode] = useState("");
    return (
        <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
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
                    value={code || ""}
                    height="300px"
                    extensions={[java()]}
                    onChange={(value) => setCode(value)}
                    theme="light"
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
                    placeholder="Category are optional, enter functions here with comma separated"
                    type="text"
                    fullWidth
                    variant="standard"
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
                />
            </Grid>
        </Grid>
    );
}

export default CreateDialogBody;

