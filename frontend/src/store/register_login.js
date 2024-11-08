import { defineStore } from 'pinia';
import { signupUser, loginUser, checkEmail } from '@/api/api'; // API 호출 함수 가져오기


/** 로그인/아웃, 회원가입 로직 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: null,
    accessToken: null,
    LoggedIn: false,
    emailMessage: "", // 이메일 중복 확인 메시지
    isEmailAvailable: false // 이메일 사용 가능 여부
  }),

  actions: {
    /** 회원가입 처리 */
    async signup(userData) {
      try {
        const response = await signupUser(userData);
        console.log(response);
        return response; // 성공적으로 처리된 경우 반환
      } catch (error) {
        console.error(error);
        throw error; // 오류 발생 시 Vue 컴포넌트에서 처리할 수 있도록 예외 throw
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
          this.emailMessage = error.response.data; // 이미 사용 중인 이메일
        } else {
          this.emailMessage = "이메일 중복 확인 중 오류가 발생했습니다.";
        }
        this.isEmailAvailable = false;
      }
    },

    /** 로그인 처리 */
    async login(userData) {
      try {
        const response = await loginUser(userData); // 로그인 API 호출
        this.currentUser = userData.email;
        this.LoggedIn = true;
        this.setAccessToken(response.data.token);
        this.userId = response.data.userId;
        this.phone = response.data.phone;
        console.log('로그인 성공:', response.data);
      } catch (error) {
        console.error('로그인 실패:', error);
        throw new Error('로그인 실패');
      }
    },

    /** 액세스 토큰 설정 */
    setAccessToken(token) {
      this.accessToken = token;
      this.LoggedIn = !!token;
      sessionStorage.setItem('token', token); // 토큰을 세션에 저장
    },

    /** 로그아웃 */
    logout() {
      this.currentUser = null;
      this.accessToken = null;
      this.LoggedIn = false;
      sessionStorage.removeItem('token');
      console.log('로그아웃 성공');
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
