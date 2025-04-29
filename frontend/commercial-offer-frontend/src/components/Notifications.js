import React, { useState, useEffect } from 'react';
import { getNotifications, markNotificationAsRead } from '../api/api';
import { Table, TableBody, TableCell, TableHead, TableRow, Button } from '@mui/material';

const Notifications = () => {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        const fetchNotifications = async () => {
            const response = await getNotifications();
            setNotifications(response.data);
        };
        fetchNotifications();
    }, []);

    const handleMarkAsRead = async (id) => {
        await markNotificationAsRead(id);
        const response = await getNotifications();
        setNotifications(response.data);
    };

    return (
        <div>
            <h2>Notifications</h2>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Message</TableCell>
                        <TableCell>Timestamp</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {notifications.map((notification) => (
                        <TableRow key={notification.id}>
                            <TableCell>{notification.message}</TableCell>
                            <TableCell>{new Date(notification.timestamp).toLocaleString()}</TableCell>
                            <TableCell>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={() => handleMarkAsRead(notification.id)}
                                >
                                    Mark as Read
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    );
};

export default Notifications;