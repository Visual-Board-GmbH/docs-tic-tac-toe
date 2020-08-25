import axios from "axios";

const instance = axios.create({
  baseURL: "http://" + location.hostname + ":8081",
  timeout: 10000,
  withCredentials: true
})

export default instance;
