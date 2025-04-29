import React, { useState, useEffect } from 'react';
import { getOfferStatistics } from '../api/api';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const Statistics = () => {
    const [statistics, setStatistics] = useState([]);

    useEffect(() => {
        const fetchStatistics = async () => {
            const response = await getOfferStatistics();
            setStatistics(response.data);
        };
        fetchStatistics();
    }, []);

    return (
        <div>
            <h2>Offer Statistics</h2>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Currency</TableCell>
                        <TableCell>Count</TableCell>
                        <TableCell>Total Amount</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {statistics.map((stat, index) => (
                        <TableRow key={index}>
                            <TableCell>{stat.currencyCode}</TableCell>
                            <TableCell>{stat.count}</TableCell>
                            <TableCell>{stat.totalAmount.toFixed(2)}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    );
};

export default Statistics;