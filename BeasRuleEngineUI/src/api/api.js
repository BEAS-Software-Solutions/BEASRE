// src/api.js
import axios from 'axios';
import authService from 'services/authService';

let errorHandler = null;

export const setGlobalErrorHandler = (handler) => {
    errorHandler = handler;
};

export const BASE_PATH = process.env.REACT_APP_BASE_PATH;


const api = axios.create({
    baseURL: BASE_PATH,
    headers: {
        'Content-Type': 'application/json',
    },
});

const getAccessToken = async () => {
    const user = await authService.getUser();
    if (user) {
        return user.access_token;
    }
    return null;
};

api.interceptors.request.use(
    async (config) => {
        const token = await getAccessToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        if (typeof window !== 'undefined') {
            window.dispatchEvent(new CustomEvent('loading', { detail: true }));
        }
        return config;
    },
    (error) => {
        if (typeof window !== 'undefined') {
            window.dispatchEvent(new CustomEvent('loading', { detail: false }));
        }
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    response => {
        if (typeof window !== 'undefined') {
            window.dispatchEvent(new CustomEvent('loading', { detail: false }));
        }
        return response;
    },
    error => {
        if (typeof window !== 'undefined') {
            window.dispatchEvent(new CustomEvent('loading', { detail: false }));
        }

        if (error.response && error.response.status === 401) {
            authService.login();
            return Promise.reject(error.response);
        }

        let message = 'Unexpected exception occured!';
        if (error.response) {
            if (errorHandler != null) {
                errorHandler(error.response ? error.response : message);
            }
            return Promise.reject(error.response);
        } else if (error.request) {

            message = 'No response received from server. Please check your network connection.';
        } else {
            message = 'Request setup error: ' + error.message;
        }

        if (errorHandler != null) {
            errorHandler(message);
        }

        return Promise.reject(error);
    }
);
export const getDataWithConfig = async (endpoint, config) => {
    try {
        const response = await api.get(endpoint,config);
        return response.data;
    } catch (error) {
        console.error('GET error: %s', error);
        throw error;
    }
};

export const getData = async (endpoint) => {
    try {
        const response = await api.get(endpoint);
        return response.data;
    } catch (error) {
        console.error('GET error: %s', error);
        throw error;
    }
};

export const postData = async (endpoint, data) => {
    try {
        const response = await api.post(endpoint, data);
        return response.data;
    } catch (error) {
        console.error('POST error: %s', error);
        throw error;
    }
};

export const putData = async (endpoint, data) => {
    try {
        const response = await api.put(endpoint, data);
        return response.data;
    } catch (error) {
        console.error('PUT error: %s', error);
        throw error;
    }
};

export const patchData = async (endpoint, data) => {
    try {
        const response = await api.patch(endpoint, data);
        return response.data;
    } catch (error) {
        console.error('PATCH error: %s', error);
        throw error;
    }
};

export const deleteData = async (endpoint) => {
    try {
        const response = await api.delete(endpoint);
        return response.data;
    } catch (error) {
        console.error('DELETE error: %s', error);
        throw error;
    }
};

export const fileUpload = async (endpoint, formData, headers) => {
    try {
        const response = await api.post(endpoint, formData, headers);
        return response.data;
    } catch (error) {
        console.error('POST error: %s', error);
        throw error;
    }
};


