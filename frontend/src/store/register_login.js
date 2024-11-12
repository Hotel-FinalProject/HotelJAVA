import { defineStore } from 'pinia';
import { signupUser, loginUser, checkEmail, sendVerificationEmailAPI, verifyEmailToken, updateUserInfoAPI, verifyPasswordAPI, changePasswordAPI, deactivateUserAPI, getUserInfo } from '@/api/api';

/** 로그인/아웃, 회원가입 로직 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: null,
    accessToken: null,
    LoggedIn: false,
    emailMessage: "", // 이메일 중복 확인 메시지
    isEmailAvailable: false, // 이메일 사용 가능 여부
    isVerified: false, // 이메일 인증 여부
    verificationToken: null, // 이메일 인증 토큰 저장
    userId: null,
    userName: null,
    phone: null,
  }),

  actions: {
    /** 회원가입 처리 */
    async signup(userData) {
      try {
        if (!this.verificationToken) {
          throw new Error("이메일 인증 토큰이 없습니다.");
        }

        const response = await signupUser(userData, this.verificationToken);
        return response;
      } catch (error) {
        console.error(error);
        throw error;
      }
    },

    /** 이메일 인증 요청 */
    async sendVerificationEmail(email, mode) {
      try {
        const response = await sendVerificationEmailAPI(email, mode);
        this.isVerified = true;
        this.verificationToken = response.data; // 이메일 인증 JWT 토큰을 상태로 저장

        // 토큰을 로컬 스토리지에 저장
        localStorage.setItem('verificationToken', response.data);
        return response;
      } catch (error) {
        console.error("이메일 인증 메일 발송 실패:", error);
        this.isVerified = false;
        throw error;
      }
    }
    ,

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

        // 로그인 성공 시 사용자 정보 저장
        this.currentUser = response.data.email;  // 서버 응답에서 가져오기
        this.LoggedIn = true;
        this.setAccessToken(response.data.token);
        this.phone = response.data.phone;
        this.userName = response.data.userName;
        this.userId = response.data.userId;

        console.log('로그인 성공:', response.data);

        // 사용자 정보를 LocalStorage에 저장 (필요시 계속 유지됨)
        localStorage.setItem('userInfo', JSON.stringify({
          userName: response.data.userName,
          userId: response.data.userId,
          email: response.data.email,
          phone: response.data.phone,
        }));

        // 토큰을 세션에 저장
        sessionStorage.setItem('token', response.data.token);

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
      this.userId = null;
      this.userName = null;
      this.phone = null;

      // 세션과 로컬 스토리지에서 데이터 제거
      sessionStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      console.log('로그아웃 성공');
    },

    /** 로그인 상태 확인 */
    checkLoginStatus() {
      const token = sessionStorage.getItem('token');
      const userInfo = localStorage.getItem('userInfo');

      if (token) {
        this.accessToken = token;
        this.LoggedIn = true;

        if (userInfo) {
          const parsedUserInfo = JSON.parse(userInfo);
          this.userName = parsedUserInfo.userName;
          this.userId = parsedUserInfo.userId;
          this.currentUser = parsedUserInfo.email;
          this.phone = parsedUserInfo.phone;
        }
      } else {
        this.LoggedIn = false;
      }
    },

    /** 회원 정보 수정 */
    async updateUserInfo(userInfo) {
      try {
        const response = await updateUserInfoAPI(userInfo, this.accessToken);
        if (response.status === 200) {
          // 사용자 정보를 LocalStorage에 업데이트
          localStorage.setItem('userInfo', JSON.stringify({
            ...JSON.parse(localStorage.getItem('userInfo')),
            ...userInfo
          }));
          // Pinia 상태 업데이트
          this.userName = userInfo.userName;
          this.phone = userInfo.phone;
          console.log('회원 정보 수정 성공:', response.data);
        }
        return response;
      } catch (error) {
        console.error('회원 정보 수정 오류:', error);
        throw error;
      }
    },

    /** 비밀번호 확인 */
    async verifyPassword(password) {
      try {
        const response = await verifyPasswordAPI(password, this.accessToken);
        return response;
      } catch (error) {
        console.error('비밀번호 확인 오류:', error);
        throw error;
      }
    },

    /** 비밀번호 변경 */
    async changePassword(newPassword) {
      try {
        const email = this.currentUser;
        const token = localStorage.getItem("verificationToken");
        const response = await changePasswordAPI(email, newPassword, token);
        return response;
      } catch (error) {
        console.error('비밀번호 변경 오류:', error);
        throw error;
      }
    },

    /** 계정 탈퇴 */
    async deactivateUser() {
      try {
        const response = await deactivateUserAPI(this.accessToken);
        return response;
      } catch (error) {
        console.error('계정 탈퇴 오류:', error);
        throw error;
      }
    },

    /** 사용자 정보 가져오기 */
    async getUserInfo() {
      try {
        const token = this.accessToken;
        if (!token) throw new Error("로그인이 필요합니다.");

        const response = await getUserInfo(token);
        return response;
      } catch (error) {
        console.error("사용자 정보 가져오기 오류:", error);
        throw error;
      }
    }
  }
});