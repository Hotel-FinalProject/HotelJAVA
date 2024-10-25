<template>
    <div class="container">
      <form @submit.prevent="submitForm">
        <div>
          <label for="email">Id</label>
          <input type="text" placeholder="example@test.com" id="email" v-model="email" />
        </div>
        <div>
          <label for="name">이름</label>
          <input type="text" id="name" v-model="name" />
        </div>
        <div>
          <label for="phone">전화번호</label>
          <input type="text" id="phone" v-model="phone" />
        </div>
        <div>
          <label for="passwd">비밀번호</label>
          <input type="password" id="passwd" v-model="passwd" />
        </div>
        <div>
          <label for="passwdConfirm">비밀번호 확인</label>
          <input type="password" id="passwdConfirm" v-model="passwdConfirm" />
        </div>
        <button type="submit">회원가입</button>
      </form>
    </div>
  </template>
  
  <script>
  import { useAuthStore } from '@/store/regist_login'; // Pinia 스토어 가져오기
  import { ref } from 'vue';
  
  export default {
    name: "SignupForm",
    setup() {
      const authStore = useAuthStore();
  
      // 상태 정의 (리액티브 변수 사용)
      const email = ref('');
      const name = ref('');
      const phone = ref('');
      const passwd = ref('');
      const passwdConfirm = ref('');
  
      // 폼 제출 처리
      const submitForm = async () => {
        if (passwd.value !== passwdConfirm.value) {
          alert('비밀번호가 일치하지 않습니다.');
          return;
        }
  
        const payload = {
          email: email.value,
          name: name.value,
          phone: phone.value,
          passwd: passwd.value
        };
  
        try {
          console.log(payload);
          await authStore.signup(payload); // Pinia의 signup 액션 호출
          alert('회원가입이 완료되었습니다!');
        } catch (error) {
          console.error(`회원가입 중 오류 발생: ${error.response.data}`);
          alert(`회원가입 중 오류 발생: ${error.response.data}`);
        }
      };
  
      return {
        email,
        name,
        phone,
        passwd,
        passwdConfirm,
        submitForm
      };
    }
  };
  </script>
  