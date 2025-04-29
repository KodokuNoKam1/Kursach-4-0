import React, { useState, useEffect } from 'react';
import { getUsers, updateUserRole } from '../api/api';

const Users = () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const fetchUsers = async () => {
            const response = await getUsers();
            setUsers(response.data);
        };
        fetchUsers();
    }, []);

    const handleUpdateRole = async (id, role) => {
        await updateUserRole(id, role);
        const response = await getUsers();
        setUsers(response.data);
    };

    return (
        <div>
            <h2>User Management</h2>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.username}</td>
                            <td>{user.role}</td>
                            <td>
                                <select
                                    onChange={(e) => handleUpdateRole(user.id, e.target.value)}
                                    defaultValue={user.role}
                                >
                                    <option value="USER">USER</option>
                                    <option value="ADMIN">ADMIN</option>
                                </select>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default Users;