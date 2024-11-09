<template>
  <div class="container">
    <div class="login-form">
      <h2 class="title">로그인</h2>
      <form @submit.prevent="submitForm">
        <div class="input-group">
          <label for="email">Email</label>
          <input type="text" id="email" v-model="email" />
        </div>
        <div class="input-group">
          <label for="password">비밀번호</label>
          <input type="password" id="passwd" v-model="passwd" />
        </div>
        <div class="top-actions">
          <router-link to="/find-my-id">
            <span class="find-id-btn">아이디 찾기</span>
          </router-link>
          <button type="submit" class="login-button">로그인</button>
        </div>
      </form>
      <div class="divider">
        <span>SNS LOGIN</span>
      </div>
      <div class="social-login">
        <button @click="googleLogin" class="social-button google">G</button>
        <button @click="naverLogin" class="social-button naver">N</button>
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
      window.location.href = "http://localhost:8081/oauth2/authorization/naver"
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
  height: calc(100vh - 120px); /* 상단 메뉴바 높이를 뺀 나머지 공간 사용 */
  margin-top: -50px; /* 약간 위로 당겨서 회원가입과 비슷한 높이로 조정 */
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

.top-actions a{
  text-decoration-line: none;
}

.find-id-btn {
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
</style>
