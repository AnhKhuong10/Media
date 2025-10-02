import axios, { AxiosInstance } from "axios";

// Khai báo AxiosInstance để có type support
const apiClient: AxiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1", // endpoint BE RESTful
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
