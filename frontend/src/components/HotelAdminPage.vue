<template>
  <SidebarLayout title="호텔 관리자">
    <!-- 사이드바 메뉴 -->
    <template #menu>
      <div class="menu-container">
        <a
          href="#"
          :class="{ active: currentView === 'RoomManagement' }"
          @click.prevent="currentView = 'RoomManagement'"
        >
          <span class="icon">🛏️</span>객실 관리
        </a>
        <a
          href="#"
          :class="{ active: currentView === 'ReservationManagement' }"
          @click.prevent="currentView = 'ReservationManagement'"
        >
          <span class="icon">📅</span>예약 관리
        </a>
        <a
          href="#"
          :class="{ active: currentView === 'Analytics' }"
          @click.prevent="currentView = 'Analytics'"
        >
          <span class="icon">📊</span>호텔 분석
        </a>
      </div>
    </template>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div v-if="currentView === 'RoomManagement'">
        <h2>객실 관리</h2>
        <p>객실 정보를 관리합니다.</p>
      </div>
      <div v-if="currentView === 'ReservationManagement'">
        <h2>예약 관리</h2>
        <p>예약 상태를 확인하고 수정할 수 있습니다.</p>
      </div>
      <div v-if="currentView === 'Analytics'">
        <h2>호텔 분석</h2>
        <p>호텔 관련 데이터를 분석합니다.</p>
      </div>
    </div>
  </SidebarLayout>
</template>

<script>
import SidebarLayout from "@/layout/SidebarLayout.vue";

export default {
  name: "HotelAdminPage",
  components: {
    SidebarLayout,
  },
  data() {
    return {
      currentView: "RoomManagement", // 초기 화면 설정
    };
  },
};
</script>

<style scoped>
/* 사이드바 메뉴 스타일 */
.menu-container {
  display: flex;
  flex-direction: column;
}

.menu-container a {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: #ffffff;
  font-size: 18px;
  text-decoration: none;
  transition: background-color 0.3s ease, color 0.3s ease;
  border-radius: 4px;
  margin-bottom: 5px;
}

.menu-container a:hover {
  background-color: #16518c;
}

.menu-container a.active {
  background-color: #004b8d;
  font-weight: bold;
}

/* 아이콘 스타일 */
.menu-container a .icon {
  margin-right: 10px;
  font-size: 20px;
}

/* 메인 콘텐츠 스타일 */
.main-content {
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-top: 20px;
}
</style>
