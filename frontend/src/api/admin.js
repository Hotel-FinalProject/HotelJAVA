import axios from 'axios';

/** axios 기본 url 및 헤더 설정 */
const config = axios.create({
  baseURL: 'http://localhost:8081/api/admin/',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

function getUserListByAdmin(token) {
  return config.get('auth/user-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getHotelManagerListByAdmin(token){
  return config.get('auth/hotelAdmin-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getReportListByAdmin(token){
  return config.get('auth/review-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getAcountInfo(token) {
  return config.get('auth/userInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getReportInfo(token) {
  return config.get('auth/reviewReportInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

export {
  getUserListByAdmin,
  getHotelManagerListByAdmin,
  getReportListByAdmin,
  getAcountInfo,
  getReportInfo,
}