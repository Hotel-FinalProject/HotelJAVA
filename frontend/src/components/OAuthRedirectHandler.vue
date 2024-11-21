<template>
  <div>로그인 처리 중입니다...</div>
</template>

<script>
import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 가져오기
import { useRouter, useRoute } from "vue-router"; // Vue Router 가져오기
import axios from 'axios'; // axios 가져오기

export default {
  name: "OAuthRedirectHandler",
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const route = useRoute();

    // 컴포넌트가 마운트될 때 실행
    const handleToken = async () => {
      // 쿼리 파라미터에서 토큰 값을 추출
      const token = route.query.token;

      if (token) {
        // 토큰을 Pinia 스토어에 저장하고 인증 상태를 업데이트
        authStore.setAccessToken(token);

        try {
          // 토큰을 이용하여 사용자 정보 가져오기
          const response = await axios.get("http://localhost:8081/api/auth/users/me", {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });

          if (response && response.status === 200) {
            // 사용자 정보 저장
            const userInfo = response.data;
            authStore.currentUser = userInfo.email;
            authStore.userId = userInfo.userId;
            authStore.userName = userInfo.name;
            authStore.phone = userInfo.phone;
            authStore.LoggedIn = true;

            // 사용자 정보를 로컬 스토리지나 세션 스토리지에 저장 (로그인 상태 유지)
            localStorage.setItem('userInfo', JSON.stringify({
              userName: userInfo.name,
              userId: userInfo.userId,
              email: userInfo.email,
              phone: userInfo.phone,
            }));
            sessionStorage.setItem('token', token);

            // 메인 페이지로 리다이렉트
            router.push({ path: "/" });
          } else {
            console.error("사용자 정보를 가져오는 데 실패했습니다.");
            alert("사용자 정보를 가져오는 데 실패했습니다. 다시 시도해 주세요.");
            router.push({ path: "/login" });
          }
        } catch (error) {
          console.error("사용자 정보 요청 실패:", error);
          alert("사용자 정보 요청에 실패했습니다. 다시 시도해 주세요.");
          router.push({ path: "/login" });
        }
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
