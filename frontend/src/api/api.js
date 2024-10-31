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

function signupUser(payload) {
  return config.post('/users/signup', payload);
}

function loginUser(payload) {
  return config.post('/users/login', payload);
}

function checkEmail(email) {
  return config.get('/users/check-email', {
    params: {
      email: email
    }
  });
}

export{
    fetchTest,
    signupUser,
    loginUser,
    checkEmail
}
