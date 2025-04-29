import React, { useState, useEffect } from 'react';
import { getCategories, createCategory } from '../api/api';
import { Button, TextField, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const Categories = () => {
    const [categories, setCategories] = useState([]);
    const [form, setForm] = useState({ name: '' });

    useEffect(() => {
        const fetchCategories = async () => {
            const response = await getCategories();
            setCategories(response.data);
        };
        fetchCategories();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        await createCategory(form);
        const response = await getCategories();
        setCategories(response.data);
        setForm({ name: '' });
    };

    return (
        <div>
            <h2>Categories</h2>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Category Name"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                    fullWidth
                    margin="normal"
                />
                <Button type="submit" variant="contained" color="primary">
                    Create Category
                </Button>
            </form>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {categories.map((category) => (
                        <TableRow key={category.id}>
                            <TableCell>{category.name}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    );
};

export default Categories;