import { Grid, TextareaAutosize, TextField } from "@mui/material";
import CodeMirror from "@uiw/react-codemirror";
import { java } from "@codemirror/lang-java";

const EditDialogBody = (item) => {
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
                <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="packageUrl"
                    name="packageUrl"
                    label="Package URL"
                    placeholder="Package URL is mandatory, enter functions here"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.packageUrl || ""}
                    onChange={(e)=>{item.packageUrl=e.target.value;}}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="packagePath"
                    name="packagePath"
                    label="Package Path"
                    placeholder="Package Path is mandatory, enter functions here"
                    type="text"
                    fullWidth
                    variant="standard"
                    defaultValue={item?.packagePath || ""}
                    onChange={(e)=>{item.packagePath=e.target.value;}}
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

