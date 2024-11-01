<template>
  <div class="container">
    <form @submit.prevent="submitForm">
      <h2>아이디 찾기</h2>
      <div class="form-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" />
      </div>
      <div class="form-group">
        <label for="phone">전화번호</label>
        <input type="text" id="phone" v-model="phone" @input="formatPhone" />
      </div>
      <button type="submit" class="find-button">아이디 찾기</button>
    </form>

    <!-- 모달 -->
    <div v-if="isModalVisible" class="modal">
      <div class="modal-content">
        <span @click="closeModal" class="close">&times;</span>
        <p>조회된 아이디: {{ foundEmail }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { useFindIdStore } from "@/store/findId"; // Pinia 스토어 import
import { ref } from "vue";

export default {
  name: "FindId",
  setup() {
    const findIdStore = useFindIdStore();

    // 리액티브 변수 정의
    const name = ref("");
    const phone = ref("");
    const foundEmail = ref("");
    const isModalVisible = ref(false);

    // 전화번호 입력 포맷 설정 함수
    const formatPhone = () => {
      let cleaned = phone.value.replace(/\D/g, ""); // 숫자 이외의 문자 제거
      if (cleaned.length > 3 && cleaned.length <= 7) {
        phone.value = `${cleaned.slice(0, 3)}-${cleaned.slice(3)}`;
      } else if (cleaned.length > 7) {
        phone.value = `${cleaned.slice(0, 3)}-${cleaned.slice(3, 7)}-${cleaned.slice(7, 11)}`;
      } else {
        phone.value = cleaned;
      }
    };

    // 아이디 찾기 폼 제출
    const submitForm = async () => {
      try {
        console.log("name : " + name.value + " phone : " + phone.value);

        // Store의 findId 메서드 호출
        await findIdStore.findId({ name: name.value, phone: phone.value });
        foundEmail.value = findIdStore.foundEmail; // Store에서 가져온 foundEmail 설정
        isModalVisible.value = true; // 모달 표시
      } catch (error) {
        alert("아이디를 찾을 수 없습니다. 입력한 정보를 다시 확인해주세요.");
      }
    };

    // 모달 닫기
    const closeModal = () => {
      isModalVisible.value = false;
    };

    return {
      name,
      phone,
      foundEmail,
      isModalVisible,
      submitForm,
      closeModal,
      formatPhone,
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
  
  input[type="text"] {
    width: calc(100% - 10px);
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    outline: none;
    font-size: 1rem;
  }
  
  input[type="text"]:focus {
    border-color: #888;
  }
  
  .find-button {
    width: 100%;
    background-color: #007bff;
    color: white;
    border: none;
    padding: 0.8rem;
    font-size: 1rem;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s;
  }
  
  .find-button:hover {
    background-color: #0056b3;
  }
  
  .modal {
    display: block;
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
  }
  
  .modal-content {
    background-color: #fff;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
    max-width: 400px;
  }
  
  .close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
    cursor: pointer;
  }
  </style>
  