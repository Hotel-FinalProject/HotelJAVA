<template>
  <div class="container">
    <form @submit.prevent="submitForm">
      <h2>회원가입</h2>
      <div class="form-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" />
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" />
      </div>
      <div class="form-group">
        <label for="passwordConfirm">비밀번호 확인</label>
        <input type="password" id="passwordConfirm" v-model="passwordConfirm" />
      </div>
      <div class="form-group email-group">
        <div>
          <label for="email">이메일</label>
          <input type="text" id="email" v-model="email" @blur="checkEmail" />
          <div
            v-if="authStore.emailMessage"
            :class="{
              error: !authStore.isEmailAvailable,
              success: authStore.isEmailAvailable,
            }"
          >
            {{ authStore.emailMessage }}
          </div>
        </div>
      </div>
      <div class="form-group">
        <label for="phone">전화번호</label>
        <input type="text" id="phone" v-model="phone" />
      </div>
      <button type="submit" class="signup-button">회원가입</button>
    </form>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 가져오기
import { ref } from "vue";
import { useRouter } from "vue-router"; // Vue Router 사용

export default {
  name: "SignupForm",
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();

    // 상태 정의 (리액티브 변수 사용)
    const email = ref("");
    const name = ref("");
    const phone = ref("");
    const password = ref("");
    const passwordConfirm = ref("");

    // 이메일 정규식 패턴
    const emailPattern = /^[a-zA-Z0-9+\-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

    // 이메일 중복 확인
    const checkEmail = async () => {
      if (email.value === "") {
        authStore.emailMessage = "이메일을 입력해주세요.";
        authStore.isEmailAvailable = false;
        return;
      }

      // 이메일 형식 검증
      if (!emailPattern.test(email.value)) {
        authStore.emailMessage = "올바른 이메일 형식이 아닙니다.";
        authStore.isEmailAvailable = false;
        return;
      }

      try {
        await authStore.checkEmailAvailability(email.value);
      } catch (error) {
        if (error.response && error.response.status === 409) {
          authStore.emailMessage = "이미 사용 중인 이메일입니다.";
          alert("이미 사용 중인 이메일입니다.");
        } else {
          alert("이메일 확인 중 오류가 발생했습니다.");
        }
      }
    };

    // 폼 제출 처리
    const submitForm = async () => {
      if (password.value !== passwordConfirm.value) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
      }

      if (!authStore.isEmailAvailable) {
        alert("이메일 중복 확인을 해주세요.");
        return;
      }

      const payload = {
        email: email.value,
        name: name.value,
        phone: phone.value,
        password: password.value,
      };

      try {
        console.log(payload);
        const response = await authStore.signup(payload); // Pinia의 signup 액션 호출

        // 서버로부터 성공 상태 코드 확인 후 로그인 페이지로 이동
        if (response && response.status === 201) {
          alert("회원가입이 완료되었습니다!");
          router.push("/login"); // 회원가입 성공 시 로그인 페이지로 이동
        } else {
          // 성공 상태가 아니면 에러 처리
          alert(`회원가입 중 오류 발생: ${response.data}`);
        }
      } catch (error) {
        if (error.response && error.response.data) {
          alert(`회원가입 중 오류 발생: ${error.response.data}`); // 서버에서 보내온 오류 메시지 출력
        } else {
          alert("회원가입 중 알 수 없는 오류가 발생했습니다."); // 알 수 없는 오류 처리
        }
        console.error(`회원가입 중 오류 발생: ${error}`);
      }
    };

    return {
      email,
      name,
      phone,
      password,
      passwordConfirm,
      submitForm,
      checkEmail,
      authStore,
    };
  },
};
</script>



<style scoped>
.container {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input[type="text"],
input[type="password"] {
  width: calc(100% - 10px);
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
  font-size: 1rem;
}

input[type="text"]:focus,
input[type="password"]:focus {
  border-color: #888;
}

.email-group {
  display: flex;
  align-items: center;
}

.email-group div {
  flex: 1;
}

.check-button {
  margin-left: 0.5rem;
  height: 2.4rem;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0 1rem;
  cursor: pointer;
  border-radius: 4px;
}

.check-button:hover {
  background-color: #0056b3;
}

.signup-button {
  width: 100%;
  background-color: #6c5ce7;
  color: white;
  border: none;
  padding: 0.8rem;
  font-size: 1rem;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.signup-button:hover {
  background-color: #5948c5;
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
