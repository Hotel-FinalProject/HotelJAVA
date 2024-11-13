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
};