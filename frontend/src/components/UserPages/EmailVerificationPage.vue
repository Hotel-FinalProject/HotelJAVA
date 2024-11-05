<template>
  <div class="verification-container">
    <h2>이메일 인증</h2>
    <div v-if="!verificationToken">
      <input
        v-model="email"
        placeholder="이메일을 입력하세요"
        @blur="checkEmailAvailability"
      />
      <div
        v-if="emailMessage"
        :class="{
          error: !isEmailAvailable,
          success: isEmailAvailable,
        }"
      >
        {{ emailMessage }}
      </div>
      <button @click="sendVerificationEmail" :disabled="!isEmailAvailable">
        인증 요청
      </button>
    </div>
    <div v-else-if="isVerified">
      <p>
        이메일 인증이 완료되었습니다. 아래 버튼을 클릭하여 회원가입을
        완료하세요.
      </p>
      <button @click="proceedToSignup">회원가입 페이지로 이동</button>
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

    const verificationToken = ref(route.query.token);
    const email = ref("");
    const isVerified = ref(false);
    const emailMessage = ref("");
    const isEmailAvailable = ref(false);

    // URL에서 가져온 verificationToken 확인을 위해 콘솔 로그 추가
    console.log("초기 verificationToken:", verificationToken.value);

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

      if (!isEmailAvailable.value) {
        alert("사용할 수 없는 이메일입니다. 다른 이메일을 사용해주세요.");
        return;
      }

      try {
        await authStore.sendVerificationEmail(email.value);
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
          console.log("인증 요청을 보낼 토큰:", newToken); // 토큰 값 확인
          const response = await authStore.verifyEmailToken(newToken);
          console.log("스프링에서 받은 이메일 인증 결과:", response.data); // 응답 결과 확인

          // 응답 객체의 success 여부 확인
          if (response.data && response.data.success) {
            // 인증 성공 시, 인증 상태와 이메일을 저장하고 회원가입 페이지로 이동
            isVerified.value = true;
            authStore.verificationToken = newToken;
            email.value = response.data.email; // 이메일도 상태에 저장 가능
            authStore.email = response.data.email; // state에 이메일 저장
            router.push({
              path: "/register", // 경로 이름을 'register'로 수정
            });
          } else {
            console.error(
              "응답 데이터에서 success 값을 찾을 수 없습니다.",
              response
            );
            alert("이메일 인증에 실패했습니다. 다시 시도해 주세요.");
          }
        } catch (error) {
          console.error("이메일 인증 중 오류 발생: ", error);
          alert("유효하지 않은 인증 토큰입니다.");
        }
      },
      { immediate: true } // 컴포넌트가 처음 로드될 때도 실행
    );

    return {
      email,
      emailMessage,
      isEmailAvailable,
      isVerified,
      checkEmailAvailability,
      sendVerificationEmail,
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
