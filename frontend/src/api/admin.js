import axios from 'axios';

/** axios 기본 url 및 헤더 설정 */
const config = axios.create({
  baseURL: 'http://localhost:8081/api/admin/auth/',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

function getUserListByAdmin(token) {
  return config.get('user-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getHotelManagerListByAdmin(token){
  return config.get('hotelAdmin-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getUserSearch(token, name){
  return config.get('user-search', {
    params: {
      search : name
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getHotelAdminSearch(token, name){
  return config.get('user-search', {
    params: {
      search : name
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function requestActiveStatus(token, userId){
  return config.post('hotel', null, {
    params: {
      userId : userId
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function requestReportControl(token, reportId){
  return config.post('review-report', null, {
    params: {
      reportId : reportId
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getReportListByAdmin(token){
  return config.get('review-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getAcountInfo(token) {
  return config.get('userInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

function getReportInfo(token) {
  return config.get('reviewReportInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

export {
  getUserListByAdmin,
  getHotelManagerListByAdmin,
  getUserSearch,
  getHotelAdminSearch,
  requestActiveStatus,
  requestReportControl,
  getReportListByAdmin,
  getAcountInfo,
  getReportInfo,
}