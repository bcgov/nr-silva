import axios from 'axios';

const getHeaders = (useMultipart = false) => {
  let headers = {};
  if (useMultipart) {
    headers = Object.assign(headers, { 'content-type': 'multipart/form-data' });
  }
  return headers;
};

const api = {
  get: (url: string, params?: object) => axios.get(url, {
    headers: getHeaders(),
    ...params
  })
};

export default api;
