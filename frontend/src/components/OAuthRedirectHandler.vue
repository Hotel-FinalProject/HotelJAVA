<template>
    <div>로그인 처리 중입니다...</div>
  </template>
  
  <script>
  import { useAuthStore } from "@/store/register_login";
  import { useRouter } from "vue-router";
  
  export default {
    name: "OAuthRedirectHandler",
    setup() {
      const router = useRouter();
      const authStore = useAuthStore();
  
      // URL에서 JWT 토큰을 추출합니다.
      const urlParams = new URLSearchParams(window.location.search);
      const token = urlParams.get("token");
  
      if (token) {
        // 받은 JWT 토큰을 핀아 스토어에 저장합니다.
        authStore.handleOAuth2Redirect(token);
        
        // 홈 화면으로 리다이렉트
        router.push({ path: "/" });
      } else {
        console.error("JWT 토큰이 URL에 존재하지 않습니다.");
        alert("로그인 처리 중 오류가 발생했습니다.");
      }
    },
  };
  </script>
  