<template>
  <div class="verification-container">
    <h2>이메일 인증</h2>
    <div v-if="!verificationToken">
      <input
        v-model="email"
        placeholder="이메일을 입력하세요"
        @blur="mode === 'signup' ? checkEmailAvailability() : null"
      />
      <div
        v-if="mode === 'signup' && emailMessage"
        :class="{
          error: !isEmailAvailable,
          success: isEmailAvailable,
        }"
      >
        {{ emailMessage }}
      </div>
      <button
        @click="sendVerificationEmail"
        :disabled="mode === 'signup' && !isEmailAvailable"
      >
        인증 요청
      </button>
    </div>
    <div v-else-if="isVerified">
      <p>이메일 인증이 완료되었습니다.</p>
      <button v-if="mode === 'signup'" @click="proceedToSignup">
        회원가입 페이지로 이동
      </button>
      <button v-else-if="mode === 'resetPassword'" @click="proceedToResetPassword">
        비밀번호 재설정 페이지로 이동
      </button>
    </div>
    <div v-else>
      <p>이메일 인증 중입니다...</p>
    </div>
  </div>
</template>

<script>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/store/register_login";

export default {
  name: "EmailVerificationPage",
  setup() {
    const route = useRoute();
    const router = useRouter();
    const authStore = useAuthStore();

    // 모드 확인 (signup, resetPassword, editPassword)
    const mode = ref(route.query.mode);
    const verificationToken = ref(route.query.token);
    const email = ref("");
    const isVerified = ref(false);
    const emailMessage = ref("");
    const isEmailAvailable = ref(false);

    // URL에서 가져온 verificationToken 확인을 위해 콘솔 로그 추가
    console.log("초기 verificationToken:", verificationToken.value);
    console.log("초기 mode:", mode.value);

    /** 이메일 중복 확인 */
    const checkEmailAvailability = async () => {
      if (!email.value) {
        emailMessage.value = "이메일을 입력해주세요.";
        isEmailAvailable.value = false;
        return;
      }

      try {
        await authStore.checkEmailAvailability(email.value);
        emailMessage.value = authStore.emailMessage;
        isEmailAvailable.value = authStore.isEmailAvailable;
      } catch (error) {
        console.error("이메일 중복 확인 실패:", error);
        emailMessage.value = "이메일 중복 확인 중 오류가 발생했습니다.";
        isEmailAvailable.value = false;
      }
    };

    /** 이메일 인증 버튼 클릭 후 이메일 전송 요청 */
    const sendVerificationEmail = async () => {
      if (!email.value) {
        alert("이메일을 입력해주세요.");
        return;
      }

      // 회원가입 모드인 경우만 이메일 사용 가능 여부 확인
      if (mode.value === "signup" && !isEmailAvailable.value) {
        alert("사용할 수 없는 이메일입니다. 다른 이메일을 사용해주세요.");
        return;
      }

      try {
        await authStore.sendVerificationEmail(email.value, mode.value);
        alert("인증 이메일이 발송되었습니다. 메일을 확인해주세요.");
      } catch (error) {
        console.error("이메일 발송 실패:", error);
        alert("이메일 발송 중 오류가 발생했습니다.");
      }
    };

    /** verificationToken 값이 변경될 때마다 인증 시도 */
    watch(
      verificationToken,
      async (newToken) => {
        if (!newToken) {
          return;
        }

        try {
          console.log("인증 요청을 보낼 토큰:", newToken);
          const response = await authStore.verifyEmailToken(newToken);
          console.log("스프링에서 받은 이메일 인증 결과:", response.data);

          if (response.data && response.data.success) {
            isVerified.value = true;
            authStore.verificationToken = newToken;
            email.value = response.data.email;
            authStore.email = response.data.email;

            // 인증이 성공한 경우 모드에 따라 처리
            if (mode.value === "signup") {
              router.push({ path: "/register" });
            } else if (mode.value === "resetPassword") {
              router.push({ path: "/reset-password" });
            } else if (mode.value === "editPassword") {
              // editPassword 모드의 경우 이메일과 토큰을 로컬 스토리지에 저장
              localStorage.setItem("verifiedEmail", email.value);
              localStorage.setItem("verificationToken", newToken);
              alert("이메일 인증이 완료되었습니다. 비밀번호 변경을 진행해주세요.");
              window.close();
            }
          } else {
            console.error("응답 데이터에서 success 값을 찾을 수 없습니다.", response);
            alert("이메일 인증에 실패했습니다. 토큰이 만료되었거나 유효하지 않습니다. 다시 인증을 요청하세요.");

            // 이메일 인증 페이지로 다시 이동
            router.push({
              path: "/verify-email",
              query: {
                mode: mode.value,
              },
            });
          }
        } catch (error) {
          console.error("이메일 인증 중 오류 발생: ", error);
          alert("유효하지 않은 인증 토큰입니다.");
        }
      },
      { immediate: true }
    );

    // 회원가입 페이지로 이동
    const proceedToSignup = () => {
      router.push({ path: "/register" });
    };

    // 비밀번호 재설정 페이지로 이동
    const proceedToResetPassword = () => {
      router.push({ path: "/reset-password" });
    };

    return {
      email,
      emailMessage,
      isEmailAvailable,
      isVerified,
      mode, // mode 추가하여 템플릿에서 사용할 수 있도록 함
      checkEmailAvailability,
      sendVerificationEmail,
      proceedToSignup,
      proceedToResetPassword,
    };
  },
};
</script>

<style scoped>
.verification-container {
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

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

button:hover:enabled {
  background-color: #5948c5;
}

input {
  margin-bottom: 1rem;
  padding: 0.5rem;
  width: 80%;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.error {
  color: red;
  margin-top: 0.5rem;
}

.success {
  color: green;
  margin-top: 0.5rem;
}
</style>
