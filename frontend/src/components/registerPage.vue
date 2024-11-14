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
          <input type="text" id="email" v-model="authStore.email" disabled />
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
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router"; // Vue Router 사용
// import { decode as jwtDecode } from "jwt-decode";

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

    // 컴포넌트가 마운트될 때 JWT 토큰에서 이메일을 가져옴
    onMounted(() => {
      if (!authStore.verificationToken) {
        router.push("/verify-email?mode=signup");
      } else {
        email.value = authStore.email;
      }
    });

    /** 폼 제출 처리 */
    const submitForm = async () => {
      // 폼 유효성 검사
      if (password.value !== passwordConfirm.value) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
      }

      if (!authStore.verificationToken) {
        alert("이메일 인증 토큰이 없습니다. 이메일 인증을 다시 진행해주세요.");
        // return;
        router.push("/verify-email");
      }

      // 사용자 정보
      const payload = {
        email: email.value,
        name: name.value,
        phone: phone.value,
        password: password.value,
      };

      // 서버에 회원가입 요청
      try {
        // verificationToken을 URL 쿼리 파라미터로 추가하여 요청
        const response = await authStore.signup(payload);
        if (response && response.status === 201) {
          alert("회원가입이 완료되었습니다!");
          router.push("/login");
        } else {
          alert(`회원가입 중 오류 발생: ${response.data}`);
        }
      } catch (error) {
        if (error.response && error.response.data) {
          alert(`회원가입 중 오류 발생: ${error.response.data}`);
        } else {
          alert("회원가입 중 알 수 없는 오류가 발생했습니다.");
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
</style>
