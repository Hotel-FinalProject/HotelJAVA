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
            <h3>📅 예약 관리</h3>
            <div v-if="reservationSummary.length > 0">
              <table class="reservation-table">
                <thead>
                  <tr>
                    <th>객실 유형</th>
                    <th>예약자 이름</th>
                    <th>휴대폰 번호</th>
                    <th>요청 사항</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(reservation, index) in reservationSummary" :key="index">
                    <td>{{ reservation.roomName }}</td>
                    <td>{{ reservation.userName }}</td>
                    <td>{{ reservation.userPhone }}</td>
                    <td>{{ reservation.request || '없음' }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else>
              <p>오늘 예약 정보가 없습니다.</p>
            </div>
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
        <div v-if="rooms.length > 0">
          <table class="room-table">
            <thead>
              <tr>
                <th>객실 유형</th>
                <th>기준 인원</th>
                <th>객실 요금</th>
                <th>객실 설명</th>
                <th>편의 시설</th>
                <th>수정</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="room in rooms" :key="room.roomId">
                <!-- 객실 유형 -->
                <td>{{ room.name }}</td>
                <!-- 기준 인원 -->
                <td>{{ room.occupancy }}명</td>
                <!-- 객실 요금 -->
                <td>{{ room.price.toLocaleString() }}원</td>
                <!-- 객실 설명 -->
                <td class="description-cell">{{ room.description }}</td>
                <!-- 편의시설 -->
                <td>
                  <ul>
                    <li v-for="(amenity, index) in getLimitedAmenities(room)" :key="index">
                      {{ amenity }}
                    </li>
                  </ul>
                  <a class="toggle-button" @click="toggleAmenities(room)">
                    {{ room.amenitiesExpanded ? "접기" : "더보기" }}
                  </a>
                </td>
                <!-- 수정 버튼 -->
                <td>
                  <button @click="openEditModal(room)">수정</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>객실 정보가 없습니다.</p>
        </div>

        <!-- 수정 모달 -->
        <div v-if="editingRoom" class="modal">
          <div class="modal-content">
            <h3>객실 정보 수정</h3>
            <form @submit.prevent="saveEdit">
              <label>
                객실 유형:
                <input type="text" v-model="editingRoom.name" />
              </label>
              <label>
                기준 인원:
                <input type="number" v-model="editingRoom.occupancy" />
              </label>
              <label>
                객실 요금:
                <input type="number" v-model="editingRoom.price" />
              </label>
              <label>
                객실 설명:
                <textarea v-model="editingRoom.description"></textarea>
              </label>
              <button type="submit" class="save-button">저장</button>
              <button type="button" class="cancel-button" @click="cancelEdit('room')">취소</button>
            </form>
          </div>
        </div>
      </div>
      <div v-if="currentView === 'ReservationManagement'">
        <h2>예약 관리</h2>
        <p>예약 상태를 확인하고 수정할 수 있습니다.</p>
        <div v-if="reservations.length > 0">
          <table class="reservation-table">
            <thead>
              <tr>
                <th>객실 이름</th>
                <th>예약자 이름</th>
                <th>체크인</th>
                <th>체크아웃</th>
                <th>요청 사항</th>
                <th>상태</th>
                <th>수정</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="reservation in reservations" :key="reservation.reservationId">
                <td>{{ reservation.roomName }}</td>
                <td>{{ reservation.userName }}</td>
                <td>{{ reservation.checkIn }}</td>
                <td>{{ reservation.checkOut }}</td>
                <td>{{ reservation.request || "없음" }}</td>
                <td>{{ reservation.status }}</td>
                <td>
                  <button @click="openEditReservation(reservation)">수정</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>예약 정보가 없습니다.</p>
        </div>

        <!-- 수정 모달 -->
        <div v-if="editingReservation" class="modal">
          <div class="modal-content">
            <h3>예약 정보 수정</h3>
            <form @submit.prevent="updateReservation">
              <label>
                상태:
                <select v-model="editingReservation.status">
                  <option value="예약 완료">예약 완료</option>
                  <option value="취소됨">취소됨</option>
                  <option value="변경 요청">변경 요청</option>
                </select>
              </label>
              <label>
                체크인 날짜:
                <input type="date" v-model="editingReservation.checkIn" />
              </label>
              <label>
                체크아웃 날짜:
                <input type="date" v-model="editingReservation.checkOut" />
              </label>
              <label>
                요청 사항:
                <textarea v-model="editingReservation.request"></textarea>
              </label>
            <!-- 저장 버튼 -->
            <button type="submit" class="save-button">저장</button>
            <!-- 예약 수정 모달 닫기 -->
            <button type="button" class="cancel-button" @click="cancelEdit('reservation')">취소</button>
            </form>
          </div>
        </div>
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
import axios from "axios";

export default {
  name: "HotelAdminPage",
  components: {
    SidebarLayout,
  },
  data() {
    return {
      currentView: "Dashboard", // 초기 화면 설정
      roomSummary: null, // 객실 요약 정보를 저장할 상태
      reservationSummary: [], // 예약 요약 정보 상태 추가
      rooms: [], // 모든 객실 정보를 저장
      editingRoom: null, // 수정 중인 객실 정보
      reservations: [], // 예약 데이터를 저장할 배열
      editingReservation: null, // 수정 중인 예약 데이터
      hotelId: 17, // 임의로 지정한 호텔 ID (테스트용)

    };
  },
  methods: {
    async fetchRoomSummary() {
        try {
            const response = await axios.get(`http://localhost:8081/api/rooms/hotel/${this.hotelId}/room-summary`);
            this.roomSummary = response.data;
        } catch (error) {
            console.error("객실 요약 정보를 가져오는 중 오류 발생:", error);
            this.roomSummary = { totalRooms: 0, roomTypeCounts: {} }; // 기본값 설정
        }
    },
    async fetchReservationSummary() {
        try {
            const response = await axios.get(`http://localhost:8081/api/auth/reservation-summary`, {
                headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
            });
            this.reservationSummary = response.data; // 백엔드에서 필터링된 데이터 사용
        } catch (error) {
            console.error("예약 요약 정보를 가져오는 중 오류 발생:", error);
            this.reservationSummary = [];
        }
    },
    async fetchRooms() {
      try {
        const response = await axios.get(`http://localhost:8081/api/rooms/hotel/${this.hotelId}`);
        this.rooms = response.data;
      } catch (error) {
        console.error("객실 정보를 가져오는 중 오류 발생:", error);
        this.rooms = [];
      }
    },
     // 수정 모달 열기
    openEditModal(room) {
      this.editingRoom = { ...room }; // 선택한 객실 데이터를 수정 상태에 저장
    },
    // 수정 버튼 클릭 시 호출
    editRoom(room) {
      this.editingRoom = { ...room, amenities: room.amenities || "" }; // 기본값 추가
    },
    // 수정 저장
    async saveEdit() {
      if (!this.editingRoom.name || !this.editingRoom.occupancy || !this.editingRoom.price) {
        alert("모든 필드를 입력해주세요.");
        return;
      }
      try {
        // PUT 요청으로 서버에 수정 데이터 전송
        await axios.put(
          `http://localhost:8081/api/rooms/${this.editingRoom.roomId}`,
          this.editingRoom
        );
        alert("객실 정보가 성공적으로 수정되었습니다!");
        this.fetchRooms(); // 데이터 갱신
        this.editingRoom = null; // 수정 상태 종료
      } catch (error) {
        console.error("수정 중 오류 발생:", error);
        alert("수정에 실패했습니다. 다시 시도해주세요.");
      }
    },
    getAmenities(room) {
      const amenities = [];
      if (room.bathFacility) amenities.push("욕실");
      if (room.bath) amenities.push("욕조");
      if (room.airCondition) amenities.push("에어컨");
      if (room.tv) amenities.push("TV");
      if (room.cable) amenities.push("케이블 TV");
      if (room.internet) amenities.push("인터넷");
      if (room.refrigerator) amenities.push("냉장고");
      if (room.toiletries) amenities.push("세면도구");
      if (room.sofa) amenities.push("소파");
      if (room.tableYn) amenities.push("테이블");
      if (room.hairdryer) amenities.push("헤어드라이어");

      return amenities.length > 0 ? amenities : ["편의시설 없음"];
    },
    getLimitedAmenities(room) {
      if (!room.amenitiesExpanded) {
        return this.getAmenities(room).slice(0, 3); // 기본적으로 3개만 표시
      }
      return this.getAmenities(room); // 모든 편의시설 표시
    },
    toggleAmenities(room) {
      room.amenitiesExpanded = !room.amenitiesExpanded; // 접기/펼치기 상태 토글
    },
    async updateRoom() {
      try {
        const updatedRoom = {
          ...this.editingRoom,
          amenities: (this.editingRoom.amenities || "").split(",").map((item) => item.trim()),
        };
        await axios.put(`http://localhost:8081/api/rooms/${updatedRoom.roomId}`, updatedRoom);
        alert("객실 정보가 수정되었습니다.");
        this.editingRoom = null;
        this.fetchRooms(); // 수정 후 데이터 다시 로드
      } catch (error) {
        console.error("객실 정보를 수정하는 중 오류 발생:", error);
        alert("객실 정보를 수정하지 못했습니다.");
      }
    },
    // 예약 데이터 가져오기
  async fetchReservations() {
    try {
      const response = await axios.get(`http://localhost:8081/api/auth/reservationInfo-Date`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      this.reservations = response.data; // API에서 가져온 예약 데이터 저장
    } catch (error) {
      console.error("예약 정보를 가져오는 중 오류 발생:", error);
      this.reservations = [];
    }
  },
  // 예약 수정 요청
  async updateReservation() {
    if (!this.editingReservation) {
      alert("수정할 예약을 선택하세요.");
      return;
    }

    try {
      await axios.put(
        `http://localhost:8081/api/auth/reservation/${this.editingReservation.reservationId}`,
        this.editingReservation,
        {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      alert("예약 정보가 성공적으로 수정되었습니다!");
      this.editingReservation = null; // 수정 상태 해제
      this.fetchReservations(); // 수정 후 예약 데이터 다시 가져오기
    } catch (error) {
      console.error("예약 수정 중 오류 발생:", error);
      alert("예약 수정에 실패했습니다.");
    }
},
  // 예약 수정 모달 열기
  openEditReservation(reservation) {
    this.editingReservation = { ...reservation }; // 선택한 예약 데이터를 수정 상태에 저장
  },
  cancelEdit(type) {
    if (type === "room") {
      this.editingRoom = null; // 객실 수정 상태 해제
    } else if (type === "reservation") {
      this.editingReservation = null; // 예약 수정 상태 해제
    }
  },
  },
  mounted() {
      this.fetchRoomSummary(); // 대시보드 로드 시 API 호출
      this.fetchReservationSummary(); // 예약 요약 정보 API 호출
      this.fetchRooms(); // 컴포넌트가 로드될 때 API 호출
      this.fetchReservations(); // 예약 데이터
      // 모든 room에 기본 상태 추가
      this.rooms.forEach((room) => {
      this.$set(room, "amenitiesExpanded", false); // 접힌 상태 기본값
  });
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

/* 대시보드 요약 스타일 */
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

.reservation-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.reservation-table th,
.reservation-table td {
  width: 0.1px;
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.reservation-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

/* 열 크기 조정 - 첫 번째 열 (객실 유형) */
.reservation-table th:nth-child(1),
.reservation-table td:nth-child(1) {
  width: 70px; /* 원하는 고정된 너비 */
  /* max-width: 150px; 최대 너비 설정 */
  text-overflow: ellipsis; /* 내용이 넘칠 경우 말줄임표 */
  white-space: nowrap; /* 줄바꿈 방지 */
  overflow: hidden; /* 넘치는 내용 숨김 */
  text-align: center;
}

/* 두 번째 열 (예약자 이름) */
.reservation-table th:nth-child(2),
.reservation-table td:nth-child(2) {
  width: 40px; /* 원하는 고정된 너비 */
  text-align: center;
}

/* 세 번째 열 (휴대폰 번호) */
.reservation-table th:nth-child(3),
.reservation-table td:nth-child(3) {
  width: 50px; /* 원하는 고정된 너비 */
  text-align: center; /* 가운데 정렬 */
}

/* 네 번째 열 (요청 사항) */
.reservation-table th:nth-child(4),
.reservation-table td:nth-child(4) {
  max-width: 300px; /* 요청 사항의 최대 너비 설정 */
  text-overflow: ellipsis; /* 넘칠 경우 말줄임표 */
  white-space: nowrap; /* 줄바꿈 방지 */
  overflow: hidden; /* 넘치는 내용 숨김 */
  text-align: center;
}

.room-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.room-table th,
.room-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.room-table th {
  background-color: #f4f4f4;
}

.room-table input,
.room-table textarea {
  width: 100%;
  padding: 4px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* 수정 버튼 */
.room-table button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  border-radius: 4px;
}

.room-table button:hover {
  background-color: #0056b3;
}

.room-table button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

/* 객실유형 */
.room-table th:nth-child(1),
.room-table td:nth-child(1) {
  width: 200px;
  white-space: normal; /* 줄바꿈 허용 */
  word-wrap: break-word; /* 단어가 길면 줄바꿈 */
  overflow: visible; /* 넘치는 내용 표시 */
}

/* 기준 인원 열 */
.room-table th:nth-child(2),
.room-table td:nth-child(2) {
  width: 100px; /* 고정된 너비 */
  text-align: center; /* 가운데 정렬 */
}

/* 객실 요금 열 */
.room-table th:nth-child(3),
.room-table td:nth-child(3) {
  width: 150px; /* 고정된 너비 */
  text-align: center; /* 우측 정렬 */
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 20px 70px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-sizing: border-box; /* 여백 포함 */
}

.modal-content label {
  display: block;
  margin-bottom: 10px;
}

.modal-content input {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
}

.modal-content textarea {
  width: 100%; /* 가로 크기 */
  padding: 8px; /* 내부 여백 */
  margin-top: 5px; /* 위쪽 여백 */
  height: 150px; /* 높이를 키움 */
  border: 1px solid #ddd; /* 테두리 */
  border-radius: 4px; /* 모서리 둥글게 */
  font-size: 14px; /* 글자 크기 */
  line-height: 1.5; /* 줄 높이 */
  resize: vertical; /* 사용자가 위아래로만 크기 조정 가능 */
}

/* 저장 버튼 스타일 */
.save-button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  background-color: #28a745; /* 초록색 */
  color: white;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.save-button:hover {
  background-color: #218838; /* 어두운 초록색 */
}

/* 취소 버튼 스타일 */
.cancel-button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  background-color: #dc3545; /* 빨간색 */
  color: white;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.cancel-button:hover {
  background-color: #c82333; /* 어두운 빨간색 */
}

/* 더보기 */
.toggle-button {
  all: unset; /* 버튼 기본 스타일 모두 제거 */
  color: #007bff;
  cursor: pointer;
  font-size: 14px;
  text-decoration: underline;
}

.toggle-button:hover {
  color: #0056b3;
}

/* 객실설명 */
.description-cell {
  max-width: 300px; /* 원하는 최대 너비를 지정하세요 */
  max-height: 120px; /* 원하는 최대 높이를 지정하세요 */
  overflow: auto; /* 스크롤을 추가하여 넘치는 내용 표시 */
  text-overflow: ellipsis; /* 말줄임표 표시 (길어질 경우) */
  white-space: normal; /* 줄바꿈 허용 */
  word-wrap: break-word; /* 긴 단어도 줄바꿈 */
}

/* 예약 */
.reservation-table th,
.reservation-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.reservation-table th {
  background-color: #f4f4f4;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  max-width: 400px; /* 최대 너비 제한 */
  width: 100%; /* 화면 크기에 따라 조정 */
  overflow: hidden; /* 넘치는 내용 숨기기 */
  padding: 20px;
  box-sizing: border-box;
}


.modal-content label {
  display: block;
  margin-bottom: 10px;
}

.modal-content input,
.modal-content textarea {
  width: 100%; /* 부모 요소의 너비에 맞추기 */
  box-sizing: border-box; /* 테두리와 패딩을 너비 계산에 포함 */
  padding: 8px; /* 내부 여백 균등 설정 */
  margin-top: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

input, textarea {
  display: block; /* 인라인 요소가 아닌 블록 요소로 처리 */
  width: 100%; /* 부모 요소 너비에 맞추기 */
  max-width: 100%; /* 최대 너비 제한 */
  box-sizing: border-box; /* 패딩과 테두리를 포함 */
}

</style>
