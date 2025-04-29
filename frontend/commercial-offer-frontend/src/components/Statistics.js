import React, { useState, useEffect } from 'react';
import { getOfferStatistics } from '../api/api';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const Statistics = () => {
    const [statistics, setStatistics] = useState([]);

    useEffect(() => {
        const fetchStatistics = async () => {
            const response = await getOfferStatistics();
            setStatistics(response.data);
        };
        fetchStatistics();
    }, []);

    const chartData = {
        labels: statistics.map((stat) => stat.currencyCode),
        datasets: [
            {
                label: 'Total Amount',
                data: statistics.map((stat) => stat.totalAmount),
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
            {
                label: 'Count',
                data: statistics.map((stat) => stat.count),
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
            },
        ],
    };

    const chartOptions = {
        scales: {
            y: {
                beginAtZero: true,
            },
        },
    };

    return (
        <div>
            <h2>Offer Statistics</h2>
            <Bar data={chartData} options={chartOptions} />
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