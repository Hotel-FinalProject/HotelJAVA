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
    <router-link to="/register">
      <span class="register-btn">회원가입</span>
    </router-link>
  </div>
  <div class="social-login">
    <button @click="googleLogin" class="google-login-btn">
      Google로 로그인
    </button>
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
