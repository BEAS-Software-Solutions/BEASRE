import { Card, Icon, IconButton, TextField, Tooltip, useTheme } from '@mui/material';
import MDBox from 'components/MDBox';
import React, { useEffect, useState, useImperativeHandle } from 'react';
import DataTable from 'react-data-table-component';
import { getData } from 'api/api';
import { showMessage } from 'context';
import theme from 'assets/theme';
import { useMaterialUIController } from 'context';
import themeDark from 'assets/theme-dark';
import MDTypography from 'components/MDTypography';
const DataTableWithPagination = React.forwardRef(({ cols, handleEdit, handleDelete, fetchUrl, handleView, handleDraw, searchKey }, ref) => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const theme = useTheme();
    const [controller] = useMaterialUIController();
    const { darkMode } = controller;
    useImperativeHandle(ref, () => ({
        refresh() {
            fetchData();
        }
    }));

    const handleRowsPerPageChange = (newRowsPerPage) => {
        setItemsPerPage(newRowsPerPage);
    };
    const deleteAction = async (item) => {
        await handleDelete(item);
        await fetchData();
    }

    const viewAction = async (item) => {
        await handleView(item);
    }

    const drawAction = async (item) => {
        await handleDraw(item);
    }

    const fetchData = async () => {
        setLoading(true);
        const result = await getData(fetchUrl + '?page=' + (currentPage - 1) + '&size=' + itemsPerPage + '&sort=createdDate,desc'
            + (searchValue && '&search=' + searchKey + '=like=' + searchValue + ',description=like=' + searchValue));
        setData(result);
        setLoading(false);
    };

    useEffect(() => {
        fetchData();
    }, [currentPage, itemsPerPage]);

    const currentItems = data.content;

    const handlePageChange = (page) => setCurrentPage(page);
    const handleCopy = (data) => {
        navigator.clipboard.writeText(data.id);
        showMessage("Copied");
    }
    const columns = [
        {
            name: 'Actions',
            cell: row => (
                <MDBox display="flex">
                    {handleEdit && (
                        <IconButton onClick={() => handleEdit(row)} sx={{ fontSize: "12pt" }}>
                            <Icon style={{color:darkMode?"white":"black"}}>{"edit"}</Icon>
                        </IconButton>
                    )}
                    {handleDelete && (
                        <IconButton onClick={() => deleteAction(row)} sx={{ fontSize: "12pt" }}>
                            <Icon style={{color:darkMode?"white":"black"}}>{"delete"}</Icon>
                        </IconButton>
                    )}
                    {handleView && (
                        <IconButton onClick={() => viewAction(row)} sx={{ fontSize: "12pt" }}>
                            <Icon style={{color:darkMode?"white":"black"}}>{"visibility"}</Icon>
                        </IconButton>
                    )}
                    {handleDraw && (
                        <IconButton onClick={() => drawAction(row)} sx={{ fontSize: "12pt" }}>
                            <Icon style={{color:darkMode?"white":"black"}}>{"draw"}</Icon>
                        </IconButton>
                    )}
                    <Tooltip title={row.id}>
                        <IconButton onClick={() => handleCopy(row)} sx={{ fontSize: "12pt" }}>
                            <Icon style={{color:darkMode?"white":"black"}}>copy</Icon>
                            <MDTypography>{row.id.substring(0, 8) + '...'}</MDTypography>
                        </IconButton>
                    </Tooltip>
                </MDBox >
            ),
            ignoreRowClick: true
        },
        ...cols,
    ];
    const [searchValue, setSearchValue] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const allowedPattern = /^[A-Za-z0-9*,-]*$/;

    const handleInputChange = (e) => {
        const value = e.target.value;
        if (allowedPattern.test(value)) {
            setSearchValue(value);
            setErrorMessage('');
        } else {
            setErrorMessage('Only characters A-Z, a-z, 0-9, and * are allowed.');
        }
    };
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            fetchData();
        }
    };

    return (
        <div>
            <MDBox display="flex" justifyContent="space-between" alignItems="center" p={3} >
                <MDBox>
                    <TextField
                        label={searchKey}
                        value={searchValue}
                        onChange={(e) => handleInputChange(e)}
                        onKeyDown={handleKeyDown}
                        variant='outlined'
                        error={!!errorMessage}
                        helperText={errorMessage}
                    />
                </MDBox>
                <MDBox color="text" px={2}>
                    <Icon sx={{ cursor: "pointer", fontWeight: "bold" }} fontSize="small" onClick={fetchData}>
                        refresh
                    </Icon>
                </MDBox>
            </MDBox>
            <DataTable
                columns={columns}
                data={currentItems}
                progressPending={loading}
                pagination={true}
                paginationServer={true}
                paginationTotalRows={data.totalElements}
                onChangePage={handlePageChange}
                onChangeRowsPerPage={handleRowsPerPageChange}
                paginationPerPage={itemsPerPage}
                paginationRowsPerPageOptions={[5, 10, 25, 50, 100]}
                highlightOnHover
                pointerOnHover
                theme={darkMode?"dark":theme.palette.mode}
            />

        </div>
    );
});

export default DataTableWithPagination;
