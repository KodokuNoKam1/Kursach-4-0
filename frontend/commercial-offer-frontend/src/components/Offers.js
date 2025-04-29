import React, { useState, useEffect } from 'react';
import { getOffers, createOffer, getCurrencies } from '../api/api';

const Offers = () => {
    const [offers, setOffers] = useState([]);
    const [currencies, setCurrencies] = useState([]);
    const [form, setForm] = useState({
        title: '',
        description: '',
        amount: 0,
        currency: { id: '' },
    });

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

    return (
        <div>
            <h2>Commercial Offers</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Title:</label>
                    <input
                        type="text"
                        value={form.title}
                        onChange={(e) => setForm({ ...form, title: e.target.value })}
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        value={form.description}
                        onChange={(e) => setForm({ ...form, description: e.target.value })}
                    />
                </div>
                <div>
                    <label>Amount:</label>
                    <input
                        type="number"
                        value={form.amount}
                        onChange={(e) => setForm({ ...form, amount: parseFloat(e.target.value) })}
                    />
                </div>
                <div>
                    <label>Currency:</label>
                    <select
                        value={form.currency.id}
                        onChange={(e) =>
                            setForm({ ...form, currency: { id: parseInt(e.target.value) } })
                        }
                    >
                        {currencies.map((currency) => (
                            <option key={currency.id} value={currency.id}>
                                {currency.name} ({currency.code})
                            </option>
                        ))}
                    </select>
                </div>
                <button type="submit">Create Offer</button>
            </form>
            <h3>Offers List</h3>
            <ul>
                {offers.map((offer) => (
                    <li key={offer.id}>
                        {offer.title} - {offer.amount} {offer.currency.code}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Offers;