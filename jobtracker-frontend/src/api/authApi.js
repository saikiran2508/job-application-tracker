import axiosInstance from './axiosInstance';

export const registerUser = (data) => axiosInstance.post('/users/register', data);

export const loginUser = (data) => axiosInstance.post('/users/login', data);

export const getCurrentUser = () => axiosInstance.get('/users/me');