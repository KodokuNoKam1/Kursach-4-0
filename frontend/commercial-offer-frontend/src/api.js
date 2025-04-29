import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// Добавление токена в заголовки
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const register = (username, password, role) =>
    api.post('/auth/register', { username, password, role });
export const login = (username, password) =>
    api.post('/auth/login', { username, password });
export const getOffers = () => api.get('/offers');
export const createOffer = (offer) => api.post('/offers', offer);
export const getCurrencies = () => api.get('/currencies');
export const importOffers = (formData) => api.post('/offers/import', formData);
export const getNotifications = () => api.get('/notifications');
export const markNotificationAsRead = (id) => api.put(`/notifications/${id}/read`);
export const getOfferStatistics = () => api.get('/offers/statistics');
export const getUsers = () => api.get('/users');
export const updateUserRole = (id, role) => api.put(`/users/${id}/role`, role, {
    headers: { 'Content-Type': 'text/plain' },
});
export const searchOffers = (params) => api.get('/offers/search', { params });
export const getOfferHistory = (id) => api.get(`/offers/${id}/history`);
export const deleteOffer = (id) => api.delete(`/offers/${id}`);
export const exportOfferPdf = (id) => api.get(`/offers/${id}/pdf`, { responseType: 'blob' });