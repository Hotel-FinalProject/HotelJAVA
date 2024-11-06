<template>
  <div class="details-container">
    <!-- 객실 이미지 -->
    <div class="room-image-container">
      <template v-if="room.primaryImageUrl">
        <img :src="room.primaryImageUrl" class="img-container" alt="Room Image" />
      </template>
      <template v-else>
        <p class="no-image-text">업체측에서 제공된 이미지가 없습니다</p>
      </template>
    </div>

    <!-- 객실 기본 정보 -->
    <div class="hotel-name">{{ room.name }}</div>
    <p class="room-description" v-html="formattedDescription"></p>

    <div class="hotel-info2">
      <p class="hotel-tel">
        전화번호: {{ room.hotelPhone ? room.hotelPhone.replace('<br />', ', ') : "정보없음" }}
      </p>
      <p class="hotel-addr">주소: {{ room.hotelAddress || "정보없음" }}</p>
      <div class="avg-person">
        <img class="person-icon" src="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png">
        <span class="avg-person-text">기준인원 {{ room.occupancy }}인</span>
      </div>
    </div>

    <!-- 예약 정보 및 가격 -->
    <div class="details-reservation">
      <div class="details-middle">
        <div class="reservation-cal">캘린더</div>
        <div class="reservation-person">
          <label for="personSelect">예약 인원:</label>
          <select id="personSelect" v-model="selectedPersonCount">
            <option v-for="n in 5" :key="n" :value="n">{{ n }}명</option>
          </select>
        </div>

      </div>
      <div class="details-bottom">
        <div class="reservation-info">
          <h4 class="reservation-text">숙박</h4>
          <p class="check-info">
            체크인: {{ room.hotelCheckIn || "정보없음" }} ~ 체크아웃: {{ room.hotelCheckOut || "정보없음" }}
          </p>
          <h2 class="price">{{ room.price ? `${room.price}원` : "가격 정보 없음" }}</h2>
          <div class="reservation-bottom">
            <div class="room-count">{{ room.availableRooms ? `남은 객실 ${room.availableRooms}개` : "남은 객실 정보 없음" }}</div>
            <router-link to="/rooms">
              <button class="reservation_btn">예약하기</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 후기 섹션 -->
    <h2 class="section-title">후기</h2>
    <div class="review-container">
      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>
        <div class="review-content">
          숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요!
        </div>
      </div>
      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>
        <div class="review-content">
          리뷰
        </div>
      </div>
      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>
        <div class="review-content">
          리뷰
        </div>
      </div>
    </div>
    <button class="review-all-btn">리뷰 더보기</button>

    <!-- 편의시설 정보 -->
    <div class="amenities-container">
      <h2 class="section-title">편의시설</h2>
      <p>목욕시설: <span>{{ room.bathFacility ? '✅' : '❌' }}</span></p>
      <p>욕조: <span>{{ room.bath ? '✅' : '❌' }}</span></p>
      <p>에어컨: <span>{{ room.airCondition ? '✅' : '❌' }}</span></p>
      <p>TV: <span>{{ room.tv ? '✅' : '❌' }}</span></p>
      <p>케이블: <span>{{ room.cable ? '✅' : '❌' }}</span></p>
      <p>인터넷: <span>{{ room.internet ? '✅' : '❌' }}</span></p>
      <p>냉장고: <span>{{ room.refrigerator ? '✅' : '❌' }}</span></p>
      <p>세면도구: <span>{{ room.toiletries ? '✅' : '❌' }}</span></p>
      <p>소파: <span>{{ room.sofa ? '✅' : '❌' }}</span></p>
      <p>테이블: <span>{{ room.tableYn ? '✅' : '❌' }}</span></p>
      <p>드라이기: <span>{{ room.hairdryer ? '✅' : '❌' }}</span></p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HotelRoom",
  data() {
    return {
      room: {},
      hotel: {},
      reviews: [],
      selectedPersonCount: 1
    };
  },
  created() {
    this.fetchRoomDetails();
  },
  methods: {
    async fetchRoomDetails() {
      const roomId = this.$route.params.roomId;
      try {
        const response = await axios.get(`http://localhost:8081/api/rooms/${roomId}`);
        this.room = response.data;
        console.log("객실 데이터:", this.room);
        this.selectedPersonCount = this.room.occupancy || 1;
      } catch (error) {
        console.error("객실 정보를 가져오는 중 오류 발생:", error);
      }
    },
  },
  computed: {
    formattedDescription() {
      return this.room.description ? this.room.description.replace(/<br\s*\/?>/gi, '<br>') : "객실 설명이 없습니다.";
    },
  }
};
</script>

<style scoped>
.details-container {
  width: 60%;
  margin: auto;
}
.img-container {
  width: 100%;
  max-width: 1100px;
  max-height: 500px;
  object-fit: cover;
  border-radius: 15px;
  margin-bottom: 20px;
}
.no-image-text {
  font-size: 16px;
  color: gray;
  text-align: center;
  padding: 50px 0;
  border: 1px solid #ddd;
  border-radius: 15px;
  background-color: #f8f8f8;
}
.hotel-name {
  font-size: 20px;
  font-weight: bold;
}
.room-description {
  font-size: 16px;
  color: #555;
  margin-top: 10px;
  margin-bottom: 20px;
}
.hotel-info2 {
  color: rgb(109, 109, 109);
  margin-top: 20px;
}
.avg-person {
  margin-top: 10px;
  display: flex;
}
.person-icon {
  width: 20px;
}
.avg-person-text {
  margin-left: 10px;
  color: rgb(109, 109, 109);
}
.price {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-top: 15px;
}
.details-reservation {
  margin-top: 20px;
}
.details-middle {
  display: flex;
  justify-content: space-between;
}
.reservation-cal,
.reservation-person {
  width: 100%;
  height: 40px;
  border: 1px solid lightgray;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8px;
  gap: 8px;
}
.details-bottom {
  margin-top: 30px;
  margin-bottom: 20px;
}
.reservation-info {
  padding: 10px;
  height: 270px;
  border: 1px solid lightgray;
  border-radius: 5px;
  position: relative;
}
.room-count {
  color: orange;
  font-weight: bold;
  text-align: center;
  position: absolute;
  bottom: 10px;
  left: 10px;
}
.reservation-bottom {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  position: absolute;
  bottom: 10px;
  right: 10px;
  width: 100%;
}
.reservation_btn {
  width: 150px;
  height: 40px;
  background-color: #00aef0;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  text-align: center;
  border: none;
  font-size: 15px;
}
.review-container {
  display: flex;
  flex-direction: row;
  gap: 15px;
  margin-top: 20px;
}
.review-grid {
  width: 300px;
  height: 150px;
  border: 1px solid lightgray;
  border-radius: 5px;
  padding: 5px;
}
.review-top {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}
.review-date {
  color: rgb(109, 109, 109);
}
.reviewer {
  margin-top: 10px;
  margin-bottom: 10px;
  color: rgb(109, 109, 109);
}
.review-content {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.review-all-btn {
  margin-top: 15px;
  width: 100%;
  height: 40px;
  color: white;
  background-color: #00aef0;
  border-radius: 5px;
  font-weight: bold;
  text-align: center;
  border: none;
}
.amenities-container {
  margin-top: 20px;
}
.section-title {
  font-size: 20px;
  font-weight: bold;
}
hr {
  background: lightgray;
  height: 1px;
  border: 0;
}
</style>
