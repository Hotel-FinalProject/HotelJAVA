// api.js
import axios from 'axios';

/** axios 기본 url 및 헤더 설정 */
const config = axios.create({
  baseURL: 'http://localhost:8081/api',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

/** 회원가입 */
function signupUser(userData, verificationToken) {
  return config.post('/users/signup', userData, {
    headers: {
      'Authorization': `Bearer ${verificationToken}`,
    }, 
  });
}

/** 로그인 */
function loginUser(payload) {
  return config.post('/users/login', payload);
}

/** 이메일 중복 체크 */
function checkEmail(email) {
  return config.get('/users/check-email', {
    params: {
      email: email
    }
  });
}

/** 유저 ID찾기 */
function findIdUser(payload) {
  return config.post('/users/find-id', payload);
}

/** 이메일 인증 */
function sendVerificationEmailAPI(email) {
  return config.post('/users/send-verification-email', {
    email: email
  });
}

/** 이메일 인증 확인 */
function verifyEmailToken(token) {
  return config.get('/users/verify-email', {
    params: {
      token: token
    }
  });
}

/** 유저 정보 가져오기 */
export function getUserInfo(token) {
  return config.get('/users/me', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

export {
  signupUser,
  loginUser,
  checkEmail,
  findIdUser,
  sendVerificationEmailAPI,
  verifyEmailToken
};
