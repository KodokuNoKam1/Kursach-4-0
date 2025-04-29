import React, { useState, useEffect } from 'react';
import { getAuditLogs } from '../api/api';
import { Table, TableBody, TableCell, TableHead, TableRow, Pagination } from '@mui/material';

const AuditLogs = () => {
    const [auditLogs, setAuditLogs] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        const fetchAuditLogs = async () => {
            const response = await getAuditLogs(page - 1);
            setAuditLogs(response.data.content);
            setTotalPages(response.data.totalPages);
        };
        fetchAuditLogs();
    }, [page]);

    const handlePageChange = (event, value) => {
        setPage(value);
    };

    return (
        <div>
            <h2>Audit Logs</h2>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>User</TableCell>
                        <TableCell>Action</TableCell>
                        <TableCell>Details</TableCell>
                        <TableCell>Timestamp</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {auditLogs.map((log) => (
                        <TableRow key={log.id}>
                            <TableCell>{log.user.username}</TableCell>
                            <TableCell>{log.action}</TableCell>
                            <TableCell>{log.details}</TableCell>
                            <TableCell>{new Date(log.timestamp).toLocaleString()}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Pagination count={totalPages} page={page} onChange={handlePageChange} />
        </div>
    );
};

export default AuditLogs;