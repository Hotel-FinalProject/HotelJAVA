<template>
  <div class="password-reset-container">
    <h2>비밀번호 재설정</h2>
    <div v-if="!isEmailSent">
      <p>임시 비밀번호를 발급하기 위해 이메일 인증을 완료하세요.</p>
      <button @click="sendResetPasswordEmail">비밀번호 재설정 요청</button>
    </div>
    <div v-else>
      <p>임시 비밀번호가 이메일로 발송되었습니다. 이메일을 확인하세요.</p>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useResetPasswordStore } from "@/store/reset_password";
import { useAuthStore } from "@/store/register_login"; // useAuthStore 추가
import { useRouter } from "vue-router";
import { sendResetPasswordRequest } from "@/api/api";

export default {
  name: "PasswordResetPage",
  setup() {
    const resetPasswordStore = useResetPasswordStore();
    const authStore = useAuthStore(); // 이메일 인증 토큰을 가져오기 위해 authStore 사용
    const router = useRouter();

    const isEmailSent = ref(false);

    // 페이지가 마운트될 때 토큰과 이메일이 있는지 확인하고, 없으면 이메일 인증 페이지로 리다이렉트
    onMounted(() => {
      if (!authStore.verificationToken || !authStore.email) {
        alert("이메일 인증 정보가 없습니다. 이메일 인증 페이지로 이동합니다.");
        router.push({
          path: "/verify-email",
          query: {
            mode: "resetPassword",
          },
        });
      } else {
        // 이메일과 토큰을 resetPasswordStore에 저장
        resetPasswordStore.email = authStore.email;
        resetPasswordStore.token = authStore.verificationToken;
      }
    });

    const sendResetPasswordEmail = async () => {
      if (!resetPasswordStore.email || !resetPasswordStore.token) {
        alert("이메일 인증 정보가 없습니다. 이메일 인증을 먼저 해주세요.");
        return;
      }

      try {
        const response = await sendResetPasswordRequest(
          resetPasswordStore.email,
          resetPasswordStore.token
        );

        if (response.status === 200) {
          isEmailSent.value = true;
          alert("임시 비밀번호가 이메일로 발송되었습니다.");
        } else {
          alert("비밀번호 재설정 요청 중 오류가 발생했습니다.");
        }
      } catch (error) {
        console.error("비밀번호 재설정 요청 실패:", error);
        alert("비밀번호 재설정 중 오류가 발생했습니다. 다시 시도해주세요.");
      }
    };

    return {
      isEmailSent,
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
</style>
