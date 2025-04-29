import React, { useState } from 'react';
import axios from 'axios';

const ClientForm = () => {
    const [name, setName] = useState('');
    const [company, setCompany] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');

    const handleSubmit = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/clients', {
                name,
                company,
                email,
                phone
            });
            console.log('Client created:', response.data);
        } catch (error) {
            console.error('Error creating client:', error);
        }
    };

    return (
        <div>
            <h2>Добавить клиента</h2>
            <input
                type="text"
                placeholder="Имя"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <input
                type="text"
                placeholder="Компания"
                value={company}
                onChange={(e) => setCompany(e.target.value)}
            />
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input
                type="text"
                placeholder="Телефон"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            <button onClick={handleSubmit}>Сохранить</button>
        </div>
    );
};

export default ClientForm;