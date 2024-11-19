<template>
  <SidebarLayout title="호텔 관리자">
    <!-- 사이드바 메뉴 -->
    <template #menu>
      <div class="menu-container">
        <a
          href="#"
          :class="{ active: currentView === 'Dashboard' }"
          @click.prevent="currentView = 'Dashboard'"
        >
          <span class="icon">📋</span>대시보드
        </a>
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
          <span class="icon">📊</span>매출 분석
        </a>
      </div>
    </template>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div v-if="currentView === 'Dashboard'">
        <h2>대시보드</h2>
        <p>호텔의 전체 상태를 한눈에 확인하세요.</p>
        <p>{{ roomSummary }}</p>

        <ul class="dashboard-summary">
          <li class="dashboard-item">
  <h3>🛏️ 객실 관리</h3>
  <p>전체 객실 수: {{ roomSummary?.totalRooms || 0 }}개</p>
<div v-if="roomSummary?.roomTypeCounts && Object.keys(roomSummary.roomTypeCounts).length > 0">
  <p>유형별 객실 수:</p>
  <ul>
    <li v-for="(count, type) in roomSummary.roomTypeCounts" :key="type">
      {{ type }}: {{ count }}개
    </li>
  </ul>
</div>
<div v-else>
  <p>객실 정보가 없습니다.</p>
</div>

</li>
          <li class="dashboard-item">
            <h3>📅 오늘 예약 정보</h3>
            <div v-if="todayReservations.length > 0">
              <ul>
                <li v-for="(reservation, index) in todayReservations" :key="index">
                  객실 유형: {{ reservation.roomName }}<br />
                  예약자 이름: {{ reservation.userName }}<br />
                  휴대폰 번호: {{ reservation.phone }}<br />
                  요청 사항: {{ reservation.request || '없음' }}
                </li>
              </ul>
            </div>
            <p v-else>오늘 예약 정보가 없습니다.</p>
          </li>
          <li class="dashboard-item">
            <h3>📊 매출 분석</h3>
            <p>이번 달 매출: 1,200,000원</p>
            <p>예약률: 75%</p>
          </li>
        </ul>
      </div>
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
import axios from 'axios';
import SidebarLayout from "@/layout/SidebarLayout.vue";

export default {
  name: "HotelAdminPage",
  components: {
    SidebarLayout, // SidebarLayout 등록
  },
  data() {
    return {
      currentView: "Dashboard", // 초기 화면
      roomSummary: {
        totalRooms: 0,
        roomTypeCounts: {},
      },
      todayReservations: [], // 오늘 예약 정보를 저장할 상태
    };
  },
  methods: {
    async fetchRoomSummary() {
  try {
    const hotelId = 17; // 고정된 호텔 ID
    const date = new Date().toISOString().split("T")[0]; // 오늘 날짜

    const response = await axios.get(`http://localhost:8081/api/rooms/hotel/${hotelId}/room-summary`, {
        params: { date },
    });

    console.log("응답 데이터:", response.data); // 반환 데이터 확인
    this.roomSummary = response.data || { totalRooms: 0, roomTypeCounts: {} };
  } catch (error) {
    console.error("객실 요약 데이터를 가져오는 중 오류 발생:", error);
    this.roomSummary = { totalRooms: 0, roomTypeCounts: {} }; // 기본값 설정
  }
}

,
    async fetchTodayReservations() {
      try {
        const hotelId = 17; // 실제 호텔 ID

        const response = await axios.get(`http://localhost:8081/api/auth/reservations/today`, {
          params: { hotelId },
        });

        console.log("오늘 예약 정보:", response.data); // 응답 데이터 확인
        this.todayReservations = response.data;
      } catch (error) {
        console.error("오늘 예약 정보를 가져오는 중 오류 발생:", error);
      }
    },
  },
  mounted() {
  this.fetchRoomSummary();
  this.fetchTodayReservations(); // 오늘 예약 정보 가져오기
}
,
};
</script>

<style scoped>
/* 기존 스타일 유지 */
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

.menu-container a .icon {
  margin-right: 10px;
  font-size: 20px;
}

.main-content {
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-top: 20px;
}

.dashboard-summary {
  list-style: none;
  padding: 0;
}

.dashboard-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.dashboard-item h3 {
  margin-bottom: 10px;
  font-size: 20px;
}

.dashboard-item p {
  margin: 5px 0;
  color: #555;
}

.dashboard-item ul {
  list-style: none;
  padding: 0;
}

.dashboard-item ul li {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}
</style>
