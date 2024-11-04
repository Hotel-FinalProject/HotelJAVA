<template>
  <div v-if="hotel" class="details-container">
    <img :src="hotel.imageUrl || defaultImage" class="img-container" alt="Hotel Image" />
    <div class="hotel-info-card">
      <h2 class="hotel-name">{{ hotel.name }}</h2>
      <div class="hotel-info">
        <span class="rating">⭐ {{ hotel.rating || "4.5" }}</span>
        <span>({{ hotel.reviewCount || 0 }} 리뷰)</span>
      </div>
      <div class="hotel-info-details">
        <p>전화번호 : {{ hotel.hotelnum || "N/A" }}</p>
        <p>주소 : {{ hotel.address || "N/A" }}</p>
        <p>체크인 : {{ hotel.checkIn || "N/A" }}</p>
        <p>체크아웃 : {{ hotel.checkOut || "N/A" }}</p>
      </div>
    </div>

    <!-- 리뷰 섹션 -->
    <div class="review-conatiner">
      <div class="review-grid">
        <div class="review-top">
          <div class="review-racting">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="review-content">
          숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요! 동향이었는지 아침마다 햇빛이 엄청 들어왔지만 ㅋㅋㅋㅋ 이중 커튼과 에어컨으로 충분히 커버 가능합니다.
          무엇보다도 조식이 깔끔하면서도 맛있었어요 콩나물국과 죽이 제일 기억이 많이 남습니다 ㅎㅎㅎ 오는정김밥이 숙소 바로 옆이라 예약하기 쉬웠다는 메리트도 있었습니다.ㅋㅋㅋ 직원분들도 저희가 체크인을 늦게 하게 됐는데
          바로 연락주셔서 주차 안내해주시고, 내내 친절하셔서 감사했어요.(전기차는 주차타워를 아예 못 쓰는 것 같아요!)
        </div>
      </div>
      <div class="review-grid">
        <div class="review-top">
          <div class="review-racting">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="review-content">
          숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요! 동향이었는지 아침마다 햇빛이 엄청 들어왔지만 ㅋㅋㅋㅋ 이중 커튼과 에어컨으로 충분히 커버 가능합니다.
          무엇보다도 조식이 깔끔하면서도 맛있었어요 콩나물국과 죽이 제일 기억이 많이 남습니다 ㅎㅎㅎ 오는정김밥이 숙소 바로 옆이라 예약하기 쉬웠다는 메리트도 있었습니다.ㅋㅋㅋ 직원분들도 저희가 체크인을 늦게 하게 됐는데
          바로 연락주셔서 주차 안내해주시고, 내내 친절하셔서 감사했어요.(전기차는 주차타워를 아예 못 쓰는 것 같아요!)
        </div>
      </div>
      <div class="review-grid">
        <div class="review-top">
          <div class="review-racting">⭐⭐⭐⭐⭐</div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="review-content">
          숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요! 동향이었는지 아침마다 햇빛이 엄청 들어왔지만 ㅋㅋㅋㅋ 이중 커튼과 에어컨으로 충분히 커버 가능합니다.
          무엇보다도 조식이 깔끔하면서도 맛있었어요 콩나물국과 죽이 제일 기억이 많이 남습니다 ㅎㅎㅎ 오는정김밥이 숙소 바로 옆이라 예약하기 쉬웠다는 메리트도 있었습니다.ㅋㅋㅋ 직원분들도 저희가 체크인을 늦게 하게 됐는데
          바로 연락주셔서 주차 안내해주시고, 내내 친절하셔서 감사했어요.(전기차는 주차타워를 아예 못 쓰는 것 같아요!)
        </div>
      </div>
    </div>

    <div class="room-list">
      <h3>객실 정보</h3>
      <div v-for="room in hotel.rooms" :key="room.roomId" class="room-card">
        <div class="room-image-container">
          <img :src="(room.images && room.images[0]?.imageUrl) || defaultRoomImage" class="room-img" alt="Room Image" />
        </div>
        <div class="room-info">
          <h4 class="room-name">{{ room.name }}</h4>
          <div class="avg-person">
            <img class="person-icon" src="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png" />
            <span class="avg-person-text">기준인원 {{ room.occupancy }}인</span>
          </div>
          <div class="reservation-info">
            <h5 class="reservation-text">숙박</h5>
            <div class="check-info">
              체크인 {{ hotel.checkIn }} ~ 체크아웃 {{ hotel.checkOut }}
            </div>
            <h2 class="price">{{ room.price }}원</h2>
            <div class="reservation-bottom">
              <div class="room-count">남은 객실 {{ room.availableRooms }}개</div>
              <router-link :to="`/room-details/${room.roomId}`">
                <button class="reservation_btn">예약 및 상세보기</button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <p>호텔 정보를 불러오는 중입니다...</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HotelDetails",
  data() {
    return {
      hotel: null,
      defaultImage: "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg",
      defaultRoomImage: "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg",
    };
  },
  created() {
    this.fetchHotelDetails();
  },
  methods: {
    async fetchHotelDetails() {
      const hotelId = this.$route.params.id;
      try {
        const response = await axios.get(`http://localhost:8081/api/hotels/${hotelId}`);
        this.hotel = response.data;
        console.log(this.hotel);
      } catch (error) {
        console.error("호텔 상세 정보를 가져오는 중 오류 발생:", error);
      }
    },
  },
};
</script>

<style scoped>
.details-container {
  width: 65%;
  margin: auto;
}
.img-container {
  width: 100%;
  height: auto;
  max-height: 500px;
  object-fit: contain;
  border-radius: 15px;
  overflow: hidden;
  margin-bottom: 20px;
}
.hotel-info-card {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}
.hotel-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}
.hotel-info {
  font-size: 18px;
  color: #555;
}
.hotel-info-details p {
  margin: 5px 0;
  font-size: 16px;
  color: #666;
}
.review-conatiner {
  display: flex;
}
.review-grid {
  width: 400px;
  height: 150px;
  border: 1px solid lightgray;
  border-radius: 5px;
  margin-top: 15px;
  margin-right: 20px;
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
.review-content {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.room-list {
  margin-top: 30px;
}
.room-card {
  display: flex;
  align-items: flex-start;
  border: 1px solid #ddd;
  border-radius: 15px;
  padding: 15px;
  margin-top: 20px;
}
.room-image-container {
  width: 40%;
  margin-right: 20px;
}
.room-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 15px;
}
.room-info {
  width: 60%;
}
.room-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}
.avg-person {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
.person-icon {
  width: 20px;
}
.avg-person-text {
  margin-left: 5px;
  color: rgb(109, 109, 109);
}
.reservation-info {
  margin-top: 10px;
  padding: 10px;
  border-top: 1px solid lightgray;
}
.reservation-text,
.check-info {
  color: rgb(109, 109, 109);
}
.price {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 10px 0;
}
.room-count {
  color: orange;
  font-weight: bold;
  margin-top: 10px;
}
.reservation-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  cursor: pointer;
}
</style>
