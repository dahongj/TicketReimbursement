import axiosInstance from './axiosConfig';

export interface User {
    username: string;
    password: string;
    role?: string; 
}

export interface LoginResponse {
    accountId: number;
    role: string;
}

export const register = async (user: User): Promise<void> => {
    await axiosInstance.post('/register', user);
};

export const login = async (user: User): Promise<LoginResponse> => {
    const response = await axiosInstance.post<LoginResponse>('/login', user);
    return response.data; 
};