<template>
  <div>로그인 처리 중입니다...</div>
</template>

<script>
import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 가져오기
import { useRouter, useRoute } from "vue-router"; // Vue Router 가져오기

export default {
  name: "OAuthRedirectHandler",
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const route = useRoute();

    // 컴포넌트가 마운트될 때 실행
    const handleToken = () => {
      // 쿼리 파라미터에서 토큰 값을 추출
      const token = route.query.token;

      if (token) {
        // 토큰을 Pinia 스토어, 세션 스토리지에 저장하고 인증 상태를 업데이트
        authStore.setAccessToken(token);
        sessionStorage.setItem('token', token);

        // 메인 페이지로 리다이렉트
        router.push({ path: "/" });
      } else {
        alert("오류가 발생했습니다. 로그인 실패.");
        // 로그인 실패 시 로그인 페이지로 이동
        router.push({ path: "/login" });
      }
    };

    // 컴포넌트가 마운트된 후 handleToken 실행
    handleToken();

    return {};
  },
};
</script>
