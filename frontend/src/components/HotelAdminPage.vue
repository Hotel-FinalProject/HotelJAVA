<!-- HotelAdminPage.vue -->
<template>
  <div class="hotel-admin-page">
    <nav class="navbar">
      <div class="navbar_logo">
        <span class="brand_name" @click="to_home()">호텔 관리자</span>
      </div>
      <div class="navbar_items">
        <ul class="navbar_item">
          <li>
            <router-link to="/">
              <button @click="logout" class="logout_btn">로그아웃</button>
            </router-link>
          </li>
        </ul>
      </div>
    </nav>

    <main class="content">
      <h2 class="page-title">마이페이지</h2>
      <div class="dashboard">
        <section class="section reservation-status">
          <h3>예약 현황</h3>
          <!-- 예약 현황 내용을 추가 -->
        </section>

        <section class="section room-status">
          <h3>객실 현황</h3>
          <!-- 객실 현황 내용을 추가 -->
        </section>

        <section class="section memo">
          <h3>메모</h3>
          <!-- 메모 내용을 추가 -->
        </section>

        <section class="section user-info">
          <h3>내 정보</h3>
          <p>직급: 관리자</p>
          <p>부서: 호텔 관리</p>
          <p>연락처: 010-1234-5678</p>
        </section>
      </div>
    </main>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/register_login';
import { computed } from 'vue';

export default {
  name: "HotelAdminPage",
  setup() {
    const authStore = useAuthStore();
    const isLoggedIn = computed(() => authStore.LoggedIn);

    const logout = () => {
      authStore.logout();
    };

    return {
      isLoggedIn,
      logout
    };
  },
  methods: {
    to_home() {
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.hotel-admin-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f8f9fa;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #00aef0;
  padding: 10px 20px;
  color: white;
}

.brand_name {
  font-size: 1.5em;
  font-weight: bold;
  cursor: pointer;
}

.navbar_items {
  display: flex;
}

.logout_btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.2em;
  cursor: pointer;
}

.content {
  flex: 1;
  padding: 20px;
}

.page-title {
  font-size: 1.8em;
  font-weight: bold;
  margin-bottom: 20px;
}

.dashboard {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.section {
  background-color: #e0e0e0;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.section h3 {
  font-size: 1.4em;
  margin-bottom: 10px;
}

.user-info p {
  margin: 5px 0;
}
</style>
