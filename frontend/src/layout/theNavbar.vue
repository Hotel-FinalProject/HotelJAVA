<!-- html -->
<template>
  <nav class="navbar">
    <div class="navbar_logo">
      <!-- <img src="" alt="팀 Logo" class="logo" /> -->
      <span class="brand_name" @click="to_home()">호텔자바</span>
    </div>
    <div class="navbar_items">
      <ul class="navbar_item" v-if="!isLoggedIn">
        <li >
          <router-link to="/login">
            <span class="login_btn">로그인</span>
          </router-link>
        </li>
        <li>
          <router-link to="/register">
            <span class="signup_btn">회원가입</span>
          </router-link>
        </li>
      </ul>
      <ul class="navbar_item" v-else>
        <li>
          <router-link to="/">
            <button @click="logout" class="logout_btn">로그아웃</button>
          </router-link>
        </li>
        <li>
          <router-link to="/my_page">
            <span class="my_page_btn">마이페이지</span>
          </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<!-- javascript -->
<script>
import { useAuthStore } from '@/store/register_login';
import { computed } from 'vue';

export default {
  name: "theNavbar",
  setup() {
    const authStore = useAuthStore();

    // Pinia 스토어에서 로그인 상태를 반응형으로 가져오기 위해 computed 사용
    const isLoggedIn = computed(() => authStore.LoggedIn);
    
    // 로그아웃 함수
    const logout = () => {
      authStore.logout();
    };

    return {
      isLoggedIn,
      logout
    }
  },
  methods: {
    to_my_page() {
      this.$router.push("/my_page");
    },
    to_home() {
      this.$router.push("/");
    },
  },
};
</script>


<!-- css, scss -->
<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #00aef0;
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 20px;
}

.logo {
  height: 40px;
  margin-right: 10px;
}

.brand_name {
  font-size: 1.5em;
  font-weight: bold;
  color: #333;
}

.navbar_item {
  align-items: center;
  display: flex; 
  list-style: none; 
  padding: 5;
  margin: 5;
}

.navbar_item button{
  border: 0;
  background-color: transparent;
  font-size: 1.2em;
  margin-left: 20px;
}

.navbar_item li{
  margin-left: 20px;
  font-size: 1.2em;
}
.navbar_item a{
  text-decoration-line: none;
  color: #000;
  margin-left: 20px;
  font-size: 1.2em;
}

.login_btn .my_page_btn {
  color: white;
}

.navbar_btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2em;
  margin-left: 20px;
}
</style>
