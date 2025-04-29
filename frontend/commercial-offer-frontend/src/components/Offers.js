import React, { useState, useEffect } from 'react';
import { getOffers, createOffer, deleteOffer, getCurrencies, searchOffers, exportOfferPdf, importOffers } from '../api/api';
import { Button, TextField, Select, MenuItem, FormControl, InputLabel } from '@mui/material';

const Offers = () => {
    const [offers, setOffers] = useState([]);
    const [currencies, setCurrencies] = useState([]);
    const [csvFile, setCsvFile] = useState(null);
    const [form, setForm] = useState({
        title: '',
        description: '',
        amount: 0,
        currency: { id: '' },
    });
    const [search, setSearch] = useState({ title: '', currencyId: '', minAmount: '', maxAmount: '' });

    useEffect(() => {
        const fetchData = async () => {
            const offersResponse = await getOffers();
            const currenciesResponse = await getCurrencies();
            setOffers(offersResponse.data);
            setCurrencies(currenciesResponse.data);
            if (currenciesResponse.data.length > 0) {
                setForm((prev) => ({ ...prev, currency: { id: currenciesResponse.data[0].id } }));
            }
        };
        fetchData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        await createOffer(form);
        const response = await getOffers();
        setOffers(response.data);
        setForm({ title: '', description: '', amount: 0, currency: { id: form.currency.id } });
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        const params = {};
        if (search.title) params.title = search.title;
        if (search.currencyId) params.currencyId = search.currencyId;
        if (search.minAmount) params.minAmount = search.minAmount;
        if (search.maxAmount) params.maxAmount = search.maxAmount;
        const response = await searchOffers(params);
        setOffers(response.data);
    };

    const handleDelete = async (id) => {
        await deleteOffer(id);
        const response = await getOffers();
        setOffers(response.data);
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

    const handleImport = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('file', csvFile);
        await importOffers(formData);
        const response = await getOffers();
        setOffers(response.data);
        setCsvFile(null);
    };

    return (
        <div>
            <h2>Commercial Offers</h2>

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

            <h3>Create Offer</h3>
            <form onSubmit={handleSubmit}>
                <TextField label="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} fullWidth />
                <TextField label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} fullWidth />
                <TextField label="Amount" type="number" value={form.amount} onChange={(e) => setForm({ ...form, amount: parseFloat(e.target.value) })} fullWidth />

                <FormControl fullWidth>
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

                <Button type="submit" variant="contained" color="primary">
                    Create Offer
                </Button>
            </form>

            <h3>Search Offers</h3>
            <form onSubmit={handleSearch}>
                <TextField label="Title" value={search.title} onChange={(e) => setSearch({ ...search, title: e.target.value })} fullWidth />
                
                <FormControl fullWidth>
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

                <TextField label="Min Amount" type="number" value={search.minAmount} onChange={(e) => setSearch({ ...search, minAmount: e.target.value })} fullWidth />
                <TextField label="Max Amount" type="number" value={search.maxAmount} onChange={(e) => setSearch({ ...search, maxAmount: e.target.value })} fullWidth />
                
                <Button type="submit" variant="contained" color="primary">
                    Search
                </Button>
            </form>

            <h3>Offers List</h3>
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Currency</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {offers.map((offer) => (
                        <tr key={offer.id}>
                            <td>{offer.title}</td>
                            <td>{offer.description}</td>
                            <td>{offer.amount}</td>
                            <td>{offer.currency.code}</td>
                            <td>
                                <Button onClick={() => handleDelete(offer.id)} variant="contained" color="secondary">
                                    Delete
                                </Button>
                                <Button onClick={() => handleExportPdf(offer.id)} variant="contained" color="primary">
                                    Export PDF
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default Offers;
