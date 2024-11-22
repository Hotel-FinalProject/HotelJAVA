import axios from 'axios';

/** axios 기본 url 및 헤더 설정 */
const config = axios.create({
  baseURL: '/api/admin/auth/',  // Spring Boot API 경로
  headers: {
    'Content-Type': 'application/json'
  }
});

/** 유저 목록 */
function getUserListByAdmin(token) {
  return config.get('user-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 호텔 관리자 목록 */
function getHotelManagerListByAdmin(token){
  return config.get('hotelAdmin-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 유저 검색 */
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

/** 호텔 관리자 검색 */
function getHotelAdminSearch(token, name){
  return config.get('hotelAdmin-search', {
    params: {
      search : name
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 유저 비활성화/활성화 */
function requestActiveStatus(token, userId){
  return config.post('hotel', null, {
    params: {
      userId: userId
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 호텔 관리자 비활성화/활성화 */
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

/** 신고 목록 */
function getReportListByAdmin(token){
  return config.get('review-read', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 대시보드 유저 정보 */
function getAcountInfo(token) {
  return config.get('userInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 대시보드 신고 수 */
function getReportInfo(token) {
  return config.get('reviewReportInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 호텔 관리자 계정 생성 */
function requestCreateHotelAdmin(token, userInfo, hotelId){
  return config.post(`hotelAdmin-create/${hotelId}`, userInfo, {
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
  requestCreateHotelAdmin
}