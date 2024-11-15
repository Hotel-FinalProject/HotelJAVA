<template>
    <div class="container">
      <div class="login-form">
        <h2 class="title">ADMIN</h2>
        <form @submit.prevent="submitForm">
          <div class="input-group">
            <label for="email">Email</label>
            <input type="text" id="email" v-model="email" />
          </div>
          <div class="input-group">
            <label for="password">비밀번호</label>
            <input type="password" id="passwd" v-model="passwd" />
          </div>
          <div class="top-actions">
            <button type="submit" class="login-button">로그인</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import router from "@/router";
  import { useAuthStore } from "@/store/register_login"; // Pinia 스토어 import
  
  export default {
    name: "LoginForm",
    data() {
      return {
        email: "",
        passwd: "",
      };
    },
    methods: {
      
      async submitForm() {
        const payload = {
          email: this.email,
          passwd: this.passwd,
        };
  
        try {
          const authStore = useAuthStore();
          await authStore.login(payload);
          alert("로그인 성공!");
          router.push({ path: "/" });
        } catch (error) {
          console.error("로그인 중 오류 발생: ", error);
          alert("로그인 실패. 이메일과 비밀번호를 확인하세요.");
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: calc(100vh - 120px); /* 상단 메뉴바 높이를 뺀 나머지 공간 사용 */
    margin-top: -50px; /* 약간 위로 당겨서 회원가입과 비슷한 높이로 조정 */
  }
  
  .login-form {
    background: #fff;
    padding: 40px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    max-width: 400px;
    width: 100%;
  }
  
  .title {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .input-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
  }
  
  .input-group label {
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  .input-group input {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
  }
  
  

  
  .login-button {
    background-color: #007bff;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
  }
  

  </style>