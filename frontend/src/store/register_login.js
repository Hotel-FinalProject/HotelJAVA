import { defineStore } from 'pinia';
import { signupUser, loginUser } from '@/api/api'; // 회원가입과 로그인 API 호출 함수

/** 로그인/아웃, 회원가입 로직 */
export const useAuthStore = defineStore('auth', {
  // 상태 정의
  state: () => ({
    currentUser: null,
    accessToken: null,
    LoggedIn: false,
  }),

  // Getters
  getters: {
    getUsers(state) {
      return state.users;
    },
    isAuthenticated(state) {
      return state.LoggedIn;
    }
  },

  // Actions
  actions: {
    /** 회원가입 처리 */
    async signup(userData) {
      await signupUser(userData)
        .then(response => {
          console.log(response);
          this.users.push(response.data);
        })
        .catch(error => {
          console.error(error);
        })
    },

    /** 로그인 처리 */
    async login(userData) {
      try {
        const response = await loginUser(userData); // 로그인 API 호출
        this.currentUser = userData.email;
        this.LoggedIn = true;
        sessionStorage.setItem('token', response.data);
        console.log('로그인 성공:', response.data);
      } catch (error) {
        console.error('로그인 실패:', error);
        throw new Error('로그인 실패'); // 예외 발생 시 Vue 컴포넌트에서 처리
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
    },

    /** OAuth2 리다이렉션 처리 */
    handleOAuth2Redirect(token) {
      // 리다이렉션 이후 받은 토큰을 Pinia 상태에 저장
      this.setAccessToken(token);
      console.log('OAuth2 로그인 성공, 토큰 설정:', token);
    }
  }
});
