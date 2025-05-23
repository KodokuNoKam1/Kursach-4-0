import React, { useState, useEffect } from 'react';
import { getOffersPaginated, createOffer, updateOffer, deleteOffer, getCurrencies, searchOffers, exportOfferPdf, importOffers, getCategories, createCategory, exportOffersToExcel } from '../api/api';
import { Button, TextField, Select, MenuItem, FormControl, InputLabel, Table, TableBody, TableCell, TableHead, TableRow, Pagination, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { CircularProgress, Box } from '@mui/material';

const Offers = () => {
    const [offers, setOffers] = useState([]);
    const [currencies, setCurrencies] = useState([]);
    const [categories, setCategories] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [loading, setLoading] = useState(false);
    const [form, setForm] = useState({
        id: null,
        title: '',
        description: '',
        amount: 0,
        currency: { id: '' },
        category: { id: '' },
    });
    const [search, setSearch] = useState({ title: '', currencyId: '', minAmount: '', maxAmount: '', categoryId: '' });
    const [csvFile, setCsvFile] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const offersResponse = await getOffersPaginated(page - 1);
                const currenciesResponse = await getCurrencies();
                const categoriesResponse = await getCategories();
                setOffers(offersResponse.data.content);
                setTotalPages(offersResponse.data.totalPages);
                setCurrencies(currenciesResponse.data);
                setCategories(categoriesResponse.data);
                if (currenciesResponse.data.length > 0) {
                    setForm((prev) => ({ ...prev, currency: { id: currenciesResponse.data[0].id } }));
                }
                if (categoriesResponse.data.length > 0) {
                    setForm((prev) => ({ ...prev, category: { id: categoriesResponse.data[0].id } }));
                }
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [page]);

    const handleOpenDialog = (offer = null) => {
        if (offer) {
            setForm({
                id: offer.id,
                title: offer.title,
                description: offer.description || '',
                amount: offer.amount,
                currency: { id: offer.currency.id },
                category: { id: offer.category ? offer.category.id : '' },
            });
        } else {
            setForm({
                id: null,
                title: '',
                description: '',
                amount: 0,
                currency: { id: currencies[0]?.id || '' },
                category: { id: categories[0]?.id || '' },
            });
        }
        setOpenDialog(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (form.id) {
                await updateOffer(form.id, form);
            } else {
                await createOffer(form);
            }
            const response = await getOffersPaginated(page - 1);
            setOffers(response.data.content);
            handleCloseDialog();
        } finally {
            setLoading(false);
        }
    };
    
    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const params = {};
            if (search.title) params.title = search.title;
            if (search.currencyId) params.currencyId = search.currencyId;
            if (search.minAmount) params.minAmount = search.minAmount;
            if (search.maxAmount) params.maxAmount = search.maxAmount;
            if (search.categoryId) params.categoryId = search.categoryId;
            const response = await searchOffers(params);
            setOffers(response.data);
        } finally {
            setLoading(false);
        }
    };
    
    const handleImport = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const formData = new FormData();
            formData.append('file', csvFile);
            await importOffers(formData);
            const response = await getOffersPaginated(page - 1);
            setOffers(response.data.content);
            setCsvFile(null);
        } finally {
            setLoading(false);
        }
    };
    
    const handleExportExcel = async () => {
        setLoading(true);
        try {
            const response = await exportOffersToExcel();
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'offers.xlsx');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } finally {
            setLoading(false);
        }
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (form.id) {
            await updateOffer(form.id, form);
        } else {
            await createOffer(form);
        }
        const response = await getOffersPaginated(page - 1);
        setOffers(response.data.content);
        handleCloseDialog();
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        const params = {};
        if (search.title) params.title = search.title;
        if (search.currencyId) params.currencyId = search.currencyId;
        if (search.minAmount) params.minAmount = search.minAmount;
        if (search.maxAmount) params.maxAmount = search.maxAmount;
        if (search.categoryId) params.categoryId = search.categoryId;
        const response = await searchOffers(params);
        setOffers(response.data);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this offer?')) {
            await deleteOffer(id);
            const response = await getOffersPaginated(page - 1);
            setOffers(response.data.content);
        }
    };

    const handleExportPdf = async (id) => {
        const response = await exportOfferPdf(id);
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `offer_${id}.pdf`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    const handleExportExcel = async () => {
        const response = await exportOffersToExcel();
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'offers.xlsx');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    const handleImport = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('file', csvFile);
        await importOffers(formData);
        const response = await getOffersPaginated(page - 1);
        setOffers(response.data.content);
        setCsvFile(null);
    };

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    return (
        <Box sx={{ padding: { xs: 1, sm: 2, md: 3 } }}>
        {loading && (
            <Box display="flex" justifyContent="center" my={2}>
                <CircularProgress />
            </Box>
        )}
        <div>
            {/* ... остальной код ... */}
        </div>
    </Box>);
        <div>
            <h2>Commercial Offers</h2>
            <Button onClick={() => handleOpenDialog()} variant="contained" color="primary" style={{ margin: '10px 0' }}>
                Create Offer
            </Button>
            <Button onClick={handleExportExcel} variant="contained" color="primary" style={{ margin: '10px' }}>
                Export to Excel
            </Button>
            <h3>Import Offers from CSV</h3>
            <form onSubmit={handleImport}>
                <input
                    type="file"
                    accept=".csv"
                    onChange={(e) => setCsvFile(e.target.files[0])}
                />
                <Button type="submit" variant="contained" color="primary" disabled={!csvFile}>
                    Import
                </Button>
            </form>
            <h3>Search Offers</h3>
            <form onSubmit={handleSearch}>
                <TextField
                    label="Title"
                    value={search.title}
                    onChange={(e) => setSearch({ ...search, title: e.target.value })}
                    margin="normal"
                />
                <FormControl margin="normal">
                    <InputLabel>Currency</InputLabel>
                    <Select
                        value={search.currencyId}
                        onChange={(e) => setSearch({ ...search, currencyId: e.target.value })}
                    >
                        <MenuItem value="">All</MenuItem>
                        {currencies.map((currency) => (
                            <MenuItem key={currency.id} value={currency.id}>
                                {currency.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <FormControl margin="normal">
                    <InputLabel>Category</InputLabel>
                    <Select
                        value={search.categoryId}
                        onChange={(e) => setSearch({ ...search, categoryId: e.target.value })}
                    >
                        <MenuItem value="">All</MenuItem>
                        {categories.map((category) => (
                            <MenuItem key={category.id} value={category.id}>
                                {category.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <TextField
                    label="Min Amount"
                    type="number"
                    value={search.minAmount}
                    onChange={(e) => setSearch({ ...search, minAmount: e.target.value })}
                    margin="normal"
                />
                <TextField
                    label="Max Amount"
                    type="number"
                    value={search.maxAmount}
                    onChange={(e) => setSearch({ ...search, maxAmount: e.target.value })}
                    margin="normal"
                />
                <Button type="submit" variant="contained" color="primary">
                    Search
                </Button>
            </form>
            <h3>Offers List</h3>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Title</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell>Amount</TableCell>
                        <TableCell>Currency</TableCell>
                        <TableCell>Category</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {offers.map((offer) => (
                        <TableRow key={offer.id}>
                            <TableCell>{offer.title}</TableCell>
                            <TableCell>{offer.description}</TableCell>
                            <TableCell>{offer.amount}</TableCell>
                            <TableCell>{offer.currency.code}</TableCell>
                            <TableCell>{offer.category ? offer.category.name : 'None'}</TableCell>
                            <TableCell>
                                <Button onClick={() => handleOpenDialog(offer)} color="primary">
                                    Edit
                                </Button>
                                <Button onClick={() => handleDelete(offer.id)} color="secondary">
                                    Delete
                                </Button>
                                <Button onClick={() => handleExportPdf(offer.id)} color="primary">
                                    Export PDF
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Pagination count={totalPages} page={page} onChange={handlePageChange} />
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>{form.id ? 'Edit Offer' : 'Create Offer'}</DialogTitle>
                <DialogContent>
                    <TextField
                        label="Title"
                        value={form.title}
                        onChange={(e) => setForm({ ...form, title: e.target.value })}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Description"
                        value={form.description}
                        onChange={(e) => setForm({ ...form, description: e.target.value })}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Amount"
                        type="number"
                        value={form.amount}
                        onChange={(e) => setForm({ ...form, amount: parseFloat(e.target.value) })}
                        fullWidth
                        margin="normal"
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Currency</InputLabel>
                        <Select
                            value={form.currency.id}
                            onChange={(e) => setForm({ ...form, currency: { id: parseInt(e.target.value) } })}
                        >
                            {currencies.map((currency) => (
                                <MenuItem key={currency.id} value={currency.id}>
                                    {currency.name} ({currency.code})
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Category</InputLabel>
                        <Select
                            value={form.category.id}
                            onChange={(e) => setForm({ ...form, category: { id: parseInt(e.target.value) } })}
                        >
                            {categories.map((category) => (
                                <MenuItem key={category.id} value={category.id}>
                                    {category.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} color="secondary">
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default Offers;