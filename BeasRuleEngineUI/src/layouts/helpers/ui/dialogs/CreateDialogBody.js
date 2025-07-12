import { Grid, TextField } from "@mui/material";

const CreateDialogBody = (item) => (
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

export default CreateDialogBody;

