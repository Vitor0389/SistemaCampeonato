import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1';

export const register = async (data) => {
  const response = await axios.post(`${API_URL}/register`, data);
  return response.data;
};

export const authenticate = async (data) => {
  const response = await axios.post(`${API_URL}/authenticate`, data);
  return response.data;
};
