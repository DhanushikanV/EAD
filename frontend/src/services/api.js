import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'https://ead-backend-hya6gkbfgrawecae.canadacentral-01.azurewebsites.net/api', 
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
