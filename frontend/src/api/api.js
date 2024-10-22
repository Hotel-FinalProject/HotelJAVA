import axios from 'axios';

const config = axios.create({
  baseURL: 'http://localhost:8081/api',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

function fetchTest(){
    return config.get('/users');
}

export default config;

export{
    fetchTest,
}
