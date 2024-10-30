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
        <div class="action-buttons">
          <div class="social-login">
            <button @click="googleLogin" class="social-button google">G</button>
            <button class="social-button naver">N</button>
            <button class="social-button kakao">K</button>
          </div>
          <button type="submit" class="login-button">로그인</button>
        </div>
      </form>
      <router-link to="/register">
        <span class="register-btn">계정이 없으신가요? 가입하기 ➞</span>
      </router-link>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 import
// import axios from "axios"; // Axios import

export default {
  name: "LoginForm",
  data() {
    return {
      email: "",
      passwd: "", // 여기서 필드 이름을 'password'에서 'passwd'로 변경해야 일관성을 유지
    };
  },
  methods: {
    async googleLogin() {
      window.location.href = "http://localhost:8081/oauth2/authorization/google";
      // window.location.href = "http://localhost:8081/oauth2/authorization";
      // window.location.href = "http://localhost:8081/oauth2/authorize/google";
        // try {
        //     // 백엔드에 OAuth2 로그인 요청을 보냄
        //     const response = await axios.get("http://localhost:8081/oauth2/authorize/google");

        //     if (response && response.data && response.data.redirectUrl) {
        //         // 백엔드에서 받은 리다이렉트 URL로 이동
        //         window.location.href = response.data.redirectUrl;
        //     } else {
        //         console.error("리다이렉션 URL이 응답에 없습니다.");
        //         alert("로그인 처리 중 오류가 발생했습니다.");
        //     }
        // } catch (error) {
        //     console.error("구글 로그인 요청 실패:", error);
        //     alert("구글 로그인 요청 중 오류가 발생했습니다.");
        // }
    },
    async submitForm() {
      const payload = {
        email: this.email,
        passwd: this.passwd, // 로그인할 때 'passwd'를 사용하므로 데이터 필드 이름을 일치시킵니다.
      };

      try {
        const authStore = useAuthStore(); // Pinia 스토어 사용
        await authStore.login(payload); // 로그인 시도 (Pinia action 호출)
        alert("로그인 성공!");
        router.push({ path: "/" }); // 로그인 성공 시 홈 화면으로 리다이렉트
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

.action-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.social-login {
  display: flex;
  gap: 10px;
}

.social-button {
  width: 40px;
  height: 40px;
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