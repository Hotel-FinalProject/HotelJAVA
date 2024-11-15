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
      'verificationToken': `Bearer ${verificationToken}`,
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
function findIdUser(name) {
  return config.post('/users/find-id', { name });
}

/** 이메일 인증 */
function sendVerificationEmailAPI(email, mode) {
  return config.post('/users/send-verification-email', {
    email: email,
    mode: mode,
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
function getUserInfo(token) {
  return config.get('/auth/users/me', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 유저 비밀번호 재설정 */
export const sendResetPasswordRequest = (email, token) => {
  return config.post('/users/reset-password', {
    email,
    token,
  });
};

/** 회원 정보 수정 */
function updateUserInfoAPI(userInfo, token) {
  return config.put('/auth/users/update', userInfo, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 비밀번호 확인 */
function verifyPasswordAPI(password, token) {
  return config.post('/auth/users/verify-password', { password }, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 비밀번호 변경 */
function changePasswordAPI(email, newPassword, token) {
  return config.post('/auth/users/reset-password', { email, newPassword, token });
}

/** 계정 탈퇴 */
function deactivateUserAPI(token) {
  return config.delete('/auth/users/delete', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 예약 정보 가져오기 */
function getReservationInfo(token) {
  return config.get('/auth/reservationInfo', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 결제 취소 */
function cancelReservationPay(imp_uid, roomId, token){
  return config.post(`/auth/paymentCancel/${imp_uid}`, {roomId}, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
}

/** 리뷰 작성 */
function createReview(reviewData, token) {
  return config.post('/auth/reviews', reviewData, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'multipart/form-data' // 이미지 업로드를 위한 설정
    }
  });
}

/** 단일 리뷰 조회 */
function getReview(reviewId, token) {
  return config.get(`/auth/reviews/user-review/${reviewId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 호텔별 리뷰 목록 조회 */
function getReviewsByHotel(hotelId) {
  return config.get(`/reviews/hotel/${hotelId}`);
}

/** 유저별 리뷰 목록 조회 */
function getReviewsByUser(userId, token) {
  return config.get(`/auth/reviews/user/${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 리뷰 수정 */
function updateReview(reviewId, reviewData, token) {
  return config.put(`/auth/reviews/${reviewId}`, reviewData, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 리뷰 삭제 */
function deleteReview(reviewId, token) {
  return config.delete(`/auth/reviews/${reviewId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}

/** 리뷰 신고 */
function reportReview(reportData, token) {
  return config.post('/reports', reportData, {
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
  getUserInfo,
  verifyEmailToken,
  updateUserInfoAPI,
  verifyPasswordAPI,
  changePasswordAPI,
  deactivateUserAPI,
  getReservationInfo,
  cancelReservationPay,
  createReview,
  getReview,
  getReviewsByHotel,
  getReviewsByUser,
  updateReview,
  deleteReview,
  reportReview
};