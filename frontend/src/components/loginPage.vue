<template>
  <div class="container">
    <div class="login-form">
      <h2 class="title">로그인</h2>
      <form @submit.prevent="submitForm">
        <div class="input-group">
          <label for="email">Email</label>
          <input type="text" id="email" v-model="email" @keydown.enter="submitForm" />
        </div>
        <div class="input-group">
          <label for="password">비밀번호</label>
          <input type="password" id="passwd" v-model="passwd" @keydown.enter="submitForm" />
        </div>
      </form>
      <div class="top-actions">
        <router-link to="/register">
          <span class="register-btn">계정이 없으신가요? 가입하기 ➞</span>
        </router-link>
        <button type="submit" class="login-button" @click="submitForm">로그인</button>
      </div>
      <div class="divider">
        <span>SNS LOGIN</span>
      </div>
      <div class="social-login">
        <button @click="googleLogin" class="social-button google">G</button>
        <button @click="naverLogin" class="social-button naver">N</button>
        <button @click="kakaoLogin" class="social-button kakao">K</button>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 import

export default {
  name: "LoginForm",
  data() {
    return {
      email: "",
      passwd: "",
    };
  },
  methods: {
    async googleLogin() {
      window.location.href = "http://localhost:8081/oauth2/authorization/google";
    },
    async naverLogin() {
      // 네이버 로그인 기능 추가 필요 시 구현
    },
    async kakaoLogin() {
      // 카카오 로그인 기능 추가 필요 시 구현
    },
    async submitForm() {
      const payload = {
        email: this.email,
        passwd: this.passwd,
      };

      try {
        const authStore = useAuthStore();
        await authStore.login(payload);
        alert("로그인 성공!");
        router.push({ path: "/" });
      } catch (error) {
        console.error("로그인 중 오류 발생: ", error);
        alert("로그인 실패. 이메일과 비밀번호를 확인하세요.");
      }
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f9f9f9;
}

.login-form {
  background: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.input-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

.input-group input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}

.top-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.register-btn {
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
}

.login-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.divider {
  text-align: center;
  margin: 20px 0;
  position: relative;
}

.divider span {
  background-color: #fff;
  padding: 0 10px;
  font-weight: bold;
  color: #666;
}

.divider::before, .divider::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 40%;
  height: 1px;
  background-color: #ddd;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.social-button {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-weight: bold;
  font-size: 16px;
  color: #fff;
  border: none;
  cursor: pointer;
}

.google {
  background-color: #db4437;
}

.naver {
  background-color: #2db400;
}

.kakao {
  background-color: #ffeb00;
  color: #000;
}

.register-btn {
  display: block;
  text-align: center;
  margin-top: 20px;
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
}
</style>
