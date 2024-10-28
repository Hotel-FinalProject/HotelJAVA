<template>
    <div class="container">
      <form @submit.prevent="submitForm">
        <div>
          <label for="email">Email</label>
          <input type="text" id="email" v-model="email" />
        </div>
        <div>
          <label for="password">비밀번호</label>
          <input type="password" id="passwd" v-model="passwd" />
        </div>
        <button type="submit">로그인</button>
      </form>
       <router-link to ="/register">
            <span class="register-btn">회원가입</span>
       </router-link>
    </div>
  </template>

<script>
import router from '@/router';
import { useAuthStore } from '@/store/regist_login'; // Pinia 스토어 import

export default {
  name: "LoginForm",
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async submitForm() {
      const payload = {
        email: this.email,
        passwd: this.passwd,
      };

      try {
        const authStore = useAuthStore(); // Pinia 스토어 사용
        await authStore.login(payload);   // 로그인 시도 (Pinia action 호출)
        alert('로그인 성공!');
        router.push({path:'/'})
      } catch (error) {
        console.error('로그인 중 오류 발생: ', error);
        alert('로그인 실패. 이메일과 비밀번호를 확인하세요.');
      }
    },
  },
};
</script>