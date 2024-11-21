<template>
    <div v-if="isOpen" class="modal">
      <div class="modal-content">
        <h2>호텔 관리자 계정 생성</h2>
        <form @submit.prevent="handleCreateAdmin" class="form-container">
          <div class="form-group">
            <label for="hotelId">호텔 Index:</label>
            <input v-model="newAdmin.hotelId" type="number" id="hotelId" class="form-input" required />
          </div>
          <div class="form-group">
            <label for="name">이름:</label>
            <input v-model="newAdmin.name" type="text" id="name" class="form-input" required />
          </div>
          <div class="form-group">
            <label for="email">이메일:</label>
            <input v-model="newAdmin.email" type="email" id="email" class="form-input" required />
          </div>
          <div class="button-group">
            <button type="submit" class="btn btn-primary">계정 생성</button>
            <button type="button" @click="closeModal" class="btn btn-secondary">취소</button>
          </div>
        </form>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { requestCreateHotelAdmin } from "@/api/admin";
  
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
        newAdmin: {
          email: "",
          name: "",
          hotelId: null,
        },
        errorMessage: "",
      };
    },
    methods: {
      async handleCreateAdmin() {
        try {
          // 계정 생성 API 요청
          await requestCreateHotelAdmin(
            this.adminToken,
            {
              email: this.newAdmin.email,
              name: this.newAdmin.name,
              phone: this.newAdmin.phone,
            },
            this.newAdmin.hotelId
          );
          alert("호텔 관리자 계정이 성공적으로 생성되었습니다!");
          this.closeModal();
        } catch (error) {
          this.errorMessage = error.response?.data || "오류가 발생했습니다.";
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
  /* 모달 스타일 */
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: #fff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    max-width: 500px;
    width: 100%;
    text-align: center;
  }
  
  .form-container {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  
  .form-input {
    width: 100%;
    padding: 10px;
    margin-top: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    transition: border-color 0.3s;
  }
  
  .form-input:focus {
    border-color: #007bff;
    outline: none;
  }
  
  .button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
  
  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s, box-shadow 0.3s;
  }
  
  .btn-primary {
    background-color: #007bff;
    color: #fff;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    box-shadow: 0 4px 10px rgba(0, 91, 187, 0.3);
  }
  
  .btn-secondary {
    background-color: #6c757d;
    color: #fff;
  }
  
  .btn-secondary:hover {
    background-color: #5a6268;
    box-shadow: 0 4px 10px rgba(90, 98, 104, 0.3);
  }
  
  .error-message {
    color: red;
    margin-top: 20px;
    font-weight: bold;
  }
  </style>