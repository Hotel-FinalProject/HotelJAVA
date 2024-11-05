import jwt_decode from "jwt-decode";
import { defineStore } from 'pinia';
import { signupUser, loginUser, checkEmail, sendVerificationEmailAPI, verifyEmailToken } from '@/api/api'; // API 호출 함수 가져오기

export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: null,
    accessToken: null,
    LoggedIn: false,
    emailMessage: "", // 이메일 중복 확인 메시지
    isEmailAvailable: false, // 이메일 사용 가능 여부
    isVerified: false, // 이메일 인증 여부
    verificationToken: null, // 이메일 인증 토큰 저장
    email: null,
  }),

  actions: {
    /** 회원가입 처리 */
    async signup(userData) {
      try {
        if (!this.verificationToken) {
          throw new Error("이메일 인증 토큰이 없습니다.");
        }

        // const payload = {
        //   ...userData,
        //   verificationToken: this.verificationToken
        // };

        // const response = await signupUser(payload);
        // return response;
        const response = await signupUser(userData, this.verificationToken);
        return response;
      } catch (error) {
        console.error(error);
        throw error;
      }
    },

    /** 이메일 인증 요청 */
    async sendVerificationEmail(email) {
      try {
        const response = await sendVerificationEmailAPI(email);
        this.isVerified = true;
        this.verificationToken = response.data; // 이메일 인증 JWT 토큰을 상태로 저장
        return response;
      } catch (error) {
        console.error("이메일 인증 메일 발송 실패:", error);
        this.isVerified = false;
        throw error;
      }
    },

    /** 이메일 인증 토큰 확인 및 저장 */
    async verifyEmailToken(token) {
      try {
        const response = await verifyEmailToken(token);
        if (response && response.status === 200) {
          this.isVerified = true;
          this.verificationToken = token; // 인증된 JWT 토큰을 상태로 저장
        }
        return response;
      } catch (error) {
        console.error("이메일 인증 실패:", error);
        this.isVerified = false;
        throw error;
      }
    },

    /** JWT 디코딩 */
    decodeJwt(token) {
      try {
        return jwt_decode(token);
      } catch (error) {
        console.error("토큰 디코딩 중 오류:", error);
        throw error;
      }
    },

    /** 이메일 중복 확인 */
    async checkEmailAvailability(email) {
      try {
        const response = await checkEmail(email);
        this.emailMessage = response.data;
        this.isEmailAvailable = true;
      } catch (error) {
        if (error.response && error.response.status === 409) {
          this.emailMessage = error.response.data;
        } else {
          this.emailMessage = "이메일 중복 확인 중 오류가 발생했습니다.";
        }
        this.isEmailAvailable = false;
      }
    },

    /** 로그인 처리 */
    async login(userData) {
      try {
        const response = await loginUser(userData);
        this.currentUser = userData.email;
        this.LoggedIn = true;
        this.setAccessToken(response.data);
      } catch (error) {
        console.error('로그인 실패:', error);
        throw new Error('로그인 실패');
      }
    },

    /** 액세스 토큰 설정 */
    setAccessToken(token) {
      this.accessToken = token;
      this.LoggedIn = !!token;
      sessionStorage.setItem('token', token);
    },

    /** 로그아웃 */
    logout() {
      this.currentUser = null;
      this.accessToken = null;
      this.LoggedIn = false;
      sessionStorage.removeItem('token');
    },

    /** 로그인 상태 확인 */
    checkLoginStatus() {
      const token = sessionStorage.getItem('token');
      if (token) {
        this.accessToken = token;
        this.LoggedIn = true;
      } else {
        this.LoggedIn = false;
      }
    }
  }
});