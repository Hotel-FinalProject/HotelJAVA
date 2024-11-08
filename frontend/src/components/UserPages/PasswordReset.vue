<template>
  <div class="password-reset-container">
    <h2>비밀번호 찾기</h2>
    <div v-if="!isVerified">
      <input v-model="email" placeholder="이메일을 입력하세요" />
      <input v-model="name" placeholder="이름을 입력하세요" />
      <button @click="sendResetPasswordEmail">이메일 인증 요청</button>
    </div>
    <div v-else>
      <p>인증이 완료되었습니다. 임시 비밀번호를 이메일로 발송합니다.</p>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/register_login";
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";

export default {
  name: "PasswordResetPage",
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();

    const email = ref("");
    const name = ref("");
    const isVerified = ref(false);

    // 컴포넌트 마운트 시 동작
    onMounted(() => {
      if (!authStore.verificationToken) {
        // 인증 토큰이 없을 경우 이메일 인증 페이지로 리다이렉트
        router.push({
          path: "/verify-email",
          query: {
            mode: "resetPassword",
          },
        });
      } else {
        // 인증 토큰이 있는 경우, 이메일 값을 설정
        email.value = authStore.email;
        isVerified.value = true; // 인증이 완료된 상태로 설정
      }
    });

    const sendResetPasswordEmail = async () => {
      try {
        // 이메일 인증 요청을 보낼 때 'resetPassword' 모드로 전송
        await authStore.sendVerificationEmail(email.value, "resetPassword");
        alert("이메일로 인증 요청이 전송되었습니다. 인증을 완료하세요.");
        router.push({
          path: "/verify-email",
          query: {
            mode: "resetPassword",
          },
        });
      } catch (error) {
        console.error("비밀번호 찾기 이메일 발송 실패:", error);
        alert("이메일 발송 중 오류가 발생했습니다.");
      }
    };

    return {
      email,
      name,
      isVerified,
      sendResetPasswordEmail,
    };
  },
};
</script>

<style scoped>
.password-reset-container {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h2 {
  margin-bottom: 1rem;
}

button {
  background-color: #6c5ce7;
  color: white;
  border: none;
  padding: 0.8rem;
  font-size: 1rem;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #5948c5;
}

input {
  margin-bottom: 1rem;
  padding: 0.5rem;
  width: 80%;
  border-radius: 4px;
  border: 1px solid #ddd;
}
</style>
