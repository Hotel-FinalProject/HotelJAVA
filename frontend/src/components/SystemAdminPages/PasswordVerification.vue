<template>
    <div v-if="isOpen" class="modal">
      <div class="modal-content">
        <h2>비밀번호 인증</h2>
        <form @submit.prevent="handleSubmit">
          <div>
            <label for="password" class = "text">비밀번호:</label>
            <input type="password" id="password" v-model="password" required />
          </div>
          <div class = "btn-container">
            <button type="submit" class="comfirm-btn">확인</button>
            <button type="button" @click="closeModal" class="cancel-btn" >취소</button>
          </div>
        </form>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { verifyPasswordAPI } from "@/api/api"; // 비밀번호 검증 API 사용
  
  export default {
    props: {
      isOpen: {
        type: Boolean,
        required: true,
      },
      onClose: {
        type: Function,
        required: true,
      },
      adminToken: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        password: "",
        errorMessage: "",
      };
    },
    methods: {
      async handleSubmit() {
        try {
          await verifyPasswordAPI(this.password, this.adminToken);
          this.$emit("verified"); // 인증 성공 시 이벤트 발생
          this.closeModal();
        } catch (error) {
          this.errorMessage = error.response?.data || "비밀번호가 틀렸습니다.";
        }
      },
      closeModal() {
        this.errorMessage = "";
        this.$emit("close");
      },
    },
  };
  </script>
  
  <style scoped>
  /* 모달 스타일 수정 */
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.3);
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 500; /* 모달보다 아래에 있도록 설정 */
}
  
/* 모달 컨텐츠를 모달 안에 담아 유지 */
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
}
  
  .error-message {
    color: red;
    margin-top: 10px;
  }
  .text{
    margin-right : 10px;
  }
  .btn-container{
    margin-top : 20px;
    display:flex;
    margin-left: 60px;
  }
  .comfirm-btn{
    margin-right: 20px;
  }
  .comfirm-btn, .cancel-btn{
    width: 70px;
  height: 30px;
  background-color: #00aef0;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  text-align: center;
  border: none;
  font-size: 15px;
  cursor: pointer;
  }
  </style>