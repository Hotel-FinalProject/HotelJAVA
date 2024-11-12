<template>
  <div class="user-edit-container">
    <h2>회원 정보 수정</h2>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="userName">이름</label>
        <input
          type="text"
          id="userName"
          v-model="userName"
          placeholder="이름을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          v-model="email"
          placeholder="이메일을 입력하세요"
          disabled
        />
        <p class="info-text">이메일은 수정할 수 없습니다.</p>
      </div>

      <div class="form-group">
        <label for="phone">전화번호</label>
        <input
          type="tel"
          id="phone"
          v-model="phone"
          placeholder="전화번호를 입력하세요"
        />
      </div>

      <!-- 비밀번호 확인 섹션 -->
      <div class="form-group">
        <label for="currentPassword">현재 비밀번호 확인</label>
        <input
          type="password"
          id="currentPassword"
          v-model="currentPassword"
          @input="checkPassword"
          placeholder="현재 비밀번호를 입력하세요"
        />
        <p v-if="!isPasswordValid && currentPassword" class="error-text">
          비밀번호가 올바르지 않습니다.
        </p>
      </div>

      <button type="submit" class="submit-button" :disabled="!isPasswordValid">
        수정 완료
      </button>
    </form>

    <hr />

    <h2>비밀번호 변경</h2>
    <form @submit.prevent="sendVerificationCode">
      <div class="form-group">
        <label for="newPassword">새 비밀번호</label>
        <input
          type="password"
          id="newPassword"
          v-model="newPassword"
          placeholder="새 비밀번호를 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="passwordConfirmation">비밀번호 확인</label>
        <input
          type="password"
          id="passwordConfirmation"
          v-model="passwordConfirmation"
          placeholder="비밀번호를 다시 입력하세요"
        />
      </div>

      <button
        type="submit"
        class="send-email-button"
        :disabled="newPassword !== passwordConfirmation"
      >
        이메일 인증번호 전송
      </button>
    </form>

    <!-- 인증번호 입력 섹션 (주석 처리됨) -->
    <!--
    <div v-if="isEmailSent" class="form-group">
      <label for="verificationCode">인증번호 입력</label>
      <input
        type="text"
        id="verificationCode"
        v-model="verificationCode"
        placeholder="이메일로 받은 인증번호를 입력하세요"
      />
      <button
        type="button"
        class="submit-button"
        @click="changePassword"
        :disabled="verificationCode.length === 0"
      >
        비밀번호 변경
      </button>
    </div>
    -->

    <!-- 비밀번호 변경 버튼 (토큰이 있으면 활성화) -->
    <div v-if="isEmailSent" class="form-group">
      <button
        type="button"
        class="submit-button"
        @click="changePassword"
        :disabled="!isEmailSent"
      >
        비밀번호 변경
      </button>
    </div>

    <hr />

    <h2>계정 탈퇴</h2>
    <button class="delete-account-button" @click="confirmDeactivateAccount">
      계정 탈퇴하기
    </button>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/register_login";

export default {
  name: "UserEditPage",
  setup() {
    const authStore = useAuthStore();
    const userInfo = JSON.parse(localStorage.getItem("userInfo")) || {};

    return {
      userName: userInfo.userName || "",
      email: userInfo.email || "",
      phone: userInfo.phone || "",
      authStore,
    };
  },
  data() {
    return {
      currentPassword: "",
      isPasswordValid: false,
      newPassword: "",
      passwordConfirmation: "",
      isEmailSent: false,
      verificationCode: "",
    };
  },
  methods: {
    async checkPassword() {
      try {
        const response = await this.authStore.verifyPassword(this.currentPassword);
        this.isPasswordValid = response.status === 200;
      } catch (error) {
        this.isPasswordValid = false;
        console.error("비밀번호 확인 오류:", error);
      }
    },
    async submitForm() {
      if (this.isPasswordValid) {
        try {
          await this.authStore.updateUserInfo({
            userName: this.userName,
            phone: this.phone,
          });
          alert("회원 정보가 성공적으로 수정되었습니다.");
        } catch (error) {
          console.error("회원 정보 수정 오류:", error);
          alert("회원 정보 수정 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
      }
    },
    async sendVerificationCode() {
      if (this.newPassword === this.passwordConfirmation) {
        try {
          await this.authStore.sendVerificationEmail(this.email, "editPassword");
          this.isEmailSent = true;
          alert("이메일로 인증번호가 발송되었습니다.");
        } catch (error) {
          console.error("인증 이메일 발송 오류:", error);
          alert("인증 이메일 발송 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
      } else {
        alert("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
      }
    },
    async changePassword() {
      try {
        if (this.isEmailSent) {
          await this.authStore.changePassword(this.newPassword);
          alert("비밀번호가 성공적으로 변경되었습니다.");
        } else {
          alert("인증번호가 필요합니다.");
        }
      } catch (error) {
        console.error("비밀번호 변경 오류:", error);
        alert("비밀번호 변경 중 오류가 발생했습니다. 다시 시도해 주세요.");
      }
    },
    confirmDeactivateAccount() {
      const isConfirmed = confirm("정말로 계정을 탈퇴하시겠습니까? 탈퇴 시 모든 데이터가 삭제됩니다.");
      if (isConfirmed) {
        this.deactivateAccount();
      }
    },
    async deactivateAccount() {
      try {
        await this.authStore.deactivateUser();
        alert("계정이 성공적으로 탈퇴되었습니다.");
        this.authStore.logout();
        window.location.href = "/";
      } catch (error) {
        console.error("계정 탈퇴 오류:", error);
        alert("계정 탈퇴 중 오류가 발생했습니다. 다시 시도해 주세요.");
      }
    },
  },
};
</script>
  
<style scoped>
.user-edit-container {
  max-width: 600px;
  margin: 0 auto;
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.info-text {
  color: #888;
  font-size: 0.9em;
}

.error-text {
  color: red;
  font-size: 0.9em;
}

.submit-button {
  background: #007bff;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.submit-button:disabled {
  background: #cccccc;
}

.submit-button:hover {
  background: #0056b3;
}

.send-email-button {
  margin-top: 10px;
  padding: 10px;
  background: #28a745;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.send-email-button:hover {
  background: #218838;
}

.delete-account-button {
  margin-top: 20px;
  padding: 10px;
  background: #dc3545;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.delete-account-button:hover {
  background: #c82333;
}
</style>
