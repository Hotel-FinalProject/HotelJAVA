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

    // URL에서 인가 코드를 추출합니다.
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    console.log(code);

    if (code) {
      // 백엔드로 인가 코드를 보내 JWT 토큰을 받아옵니다.
      fetch(`http://localhost:8081/api/users/oauth2/token?code=${code}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.token) {
            // 받은 JWT 토큰을 핀아 스토어에 저장합니다.
            authStore.handleOAuth2Redirect(data.token);
            router.push({ path: "/" }); // 홈 화면으로 리다이렉트
          } else {
            console.error("토큰 발급 실패:", data);
            alert("로그인 처리 중 오류가 발생했습니다.");
          }
        })
        .catch((error) => {
          console.error("OAuth2 토큰 요청 실패:", error);
          alert("로그인 처리 중 오류가 발생했습니다.");
        });
    } else {
      console.error("인가 코드가 URL에 존재하지 않습니다.");
      alert("로그인 처리 중 오류가 발생했습니다.");
    }
  },
};
</script>
