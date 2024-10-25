import { defineStore } from 'pinia';
import { signupUser, loginUser } from '@/api/api'; // 회원가입과 로그인 API 호출 함수

export const useAuthStore = defineStore('auth', {
  // 상태 정의
  state: () => ({
    users: [],
    currentUser: null,
    isLoggedIn: false
  }),

  // Getters
  getters: {
    getUsers(state) {
      return state.users;
    },
    isAuthenticated(state) {
      return state.isLoggedIn;
    }
  },

  // Actions
  actions: {
    // 회원가입 처리
    async signup(userData) {
      try {
        const response = await signupUser(userData); // API 호출
        this.users.push(response.data); // 성공 시 state 업데이트
        console.log('회원가입 성공:', response.data);
      } catch (error) {
        console.error('회원가입 실패:', error);
        throw error; // 오류를 호출한 곳으로 전달
      }
    },

    // 로그인 처리
    async login(userData) {
        try {
          const response = await loginUser(userData); // 로그인 API 호출
          this.currentUser = userData.email;
          this.isLoggedIn = true;
          this.token = response.data; // JWT 토큰 저장
          console.log('로그인 성공:', response.data);
        } catch (error) {
          console.error('로그인 실패:', error);
          throw new Error('로그인 실패'); // 예외 발생 시 Vue 컴포넌트에서 처리
        }
      },
      logout() {
        this.currentUser = null;
        this.isLoggedIn = false;
        this.token = null;
        console.log('로그아웃 성공');
      }
    }
});