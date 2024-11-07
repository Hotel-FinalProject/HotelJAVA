<template>
  <div class="container">
    <form @submit.prevent="submitForm">
      <h2>아이디 찾기</h2>
      <div class="form-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" />
      </div>
      <button type="submit" class="find-button">아이디 찾기</button>
    </form>

    <!-- 모달 -->
    <div v-if="isModalVisible" class="modal">
      <div class="modal-content">
        <span @click="closeModal" class="close">&times;</span>
        <p>{{ foundEmail }}</p>
        <button @click="goToLogin" class="login-button">
          로그인 페이지로 이동
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { useFindIdStore } from "@/store/findId"; // Pinia 스토어 import
import { ref } from "vue";
import { useRouter } from "vue-router";

export default {
  name: "FindId",
  setup() {
    const findIdStore = useFindIdStore();
    const router = useRouter();

    // 리액티브 변수 정의
    const name = ref("");
    const foundEmail = ref("");
    const isModalVisible = ref(false);

    // 아이디 찾기 폼 제출
    const submitForm = async () => {
      try {
        console.log("name : " + name.value);

        // Store의 findId 메서드 호출
        await findIdStore.findId(name.value);
        foundEmail.value = findIdStore.foundEmail; // Store에서 가져온 foundEmail 설정
        isModalVisible.value = true; // 모달 표시
      } catch (error) {
        foundEmail.value =
          "아이디를 찾을 수 없습니다. 입력한 정보를 다시 확인해주세요.";
        isModalVisible.value = true;
      }
    };

    // 모달 닫기
    const closeModal = () => {
      isModalVisible.value = false;
    };

    // 로그인 페이지로 이동
    const goToLogin = () => {
      router.push("/login");
    };

    return {
      name,
      foundEmail,
      isModalVisible,
      submitForm,
      closeModal,
      goToLogin,
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
  position: relative;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

/* 로그인 버튼 스타일 수정 */
.login-button {
  margin-top: 2rem; /* 버튼을 모달 하단으로 밀기 위한 여백 추가 */
  display: block; /* 블록 요소로 변경하여 가운데 정렬 가능하게 함 */
  margin-left: auto;
  margin-right: auto;
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.8rem;
  font-size: 1rem;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #218838;
}
</style>
