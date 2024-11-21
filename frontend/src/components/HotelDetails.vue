<template>
  <div v-if="hotel" class="details-container">
    <!-- 호텔 이미지 -->
    <div class="img-container">
      <template v-if="hotel.imageUrl">
        <img :src="hotel.imageUrl" alt="Hotel Image" />
      </template>
      <template v-else>
        <div class="no-image-container">
          <p class="no-image-text">업체측에서 제공된 이미지가 없습니다</p>
        </div>
      </template>
    </div>

    <!-- 호텔 정보 -->
    <div class="hotel-info-card">
      <div class="hotel-top">
        <div>
          <div class="hotel-name">{{ hotel.name }}</div>
        </div>
        <div class="favorite-container" v-if="isLoggedIn">
          <div 
            class="heart-button" 
            :class="{'favorited': isFavorited, 'unfavorited': !isFavorited}" 
            @click="toggleFavorite">
            <i class="fas fa-heart" v-if="isFavorited"></i>
            <i class="far fa-heart" v-else></i>
          </div>
        </div>
      </div>
      <div class="hotel-info">
        <span class="rating">⭐ {{ hotel.rating || "0" }}</span>
        <span>({{ hotel.reviewCount || 0 }} 리뷰)</span>
      </div>
      <div class="hotel-info-details">
        <p>
          <span class="phone-icon">📞</span>
          전화번호 : {{ hotel.hotelnum || "업체측에서 제공된 정보가 없습니다." }}
        </p>
        <p>
          <span class="location-icon">📍</span> 
          {{ hotel.address || "업체측에서 제공된 정보가 없습니다." }}
          <button class="copy-button" @click="copyAddressToClipboard">주소복사</button>
        </p>
        <div id="map" style="width:500px;height:400px;"></div>
      </div>
    </div>

    <!-- 리뷰 섹션 -->
    <div class="review-container">
      <h3>리뷰</h3>

      <!-- 리뷰가 없을 때 -->
      <div v-if="hotelReviews && hotelReviews.length === 0">
        <p>이 호텔에 대한 리뷰가 없습니다.</p>
      </div>

      <!-- 리뷰가 있을 때 -->
      <div v-else>
        <transition-group name="fade" tag="div" class="review-list">
          <div
            v-for="(review, index) in visibleReviews"
            :key="index"
            class="review-grid"
          >
            <!-- 리뷰 상단 -->
            <div class="review-top">
              <div class="review-rating">
                <span
                  v-for="star in 5"
                  :key="star"
                  class="star"
                  :class="{ filled: star <= review.rating }"
                >
                  ⭐
                </span>
              </div>
              <div class="review-actions">
                <div class="review-date">
                  {{ reviewFormatDate(review.updateDate || review.writeDate) }}
                </div>
                <button
                  v-if="review.userId === loggedInUserId"
                  class="edit-button"
                  @click="openEditModal(review)"
                >
                  수정하기
                </button>
                <button
                  v-else
                  class="report-button"
                  @click="reportReviews(review.reviewId, review.userId)"
                >
                  신고하기
                </button>
              </div>
            </div>

            <!-- 작성자 및 객실 정보 -->
            <div class="reviewer">{{ review.userName }}</div>

            <!-- 리뷰 내용 -->
            <div class="review-content">{{ review.content }}</div>

            <!-- 이미지 갤러리 -->
            <div
              class="review-images"
              v-if="review.imageUrl && review.imageUrl.length > 0"
            >
              <img
                v-for="(image, imgIndex) in review.imageUrl"
                :src="image"
                :key="imgIndex"
                class="review-image"
                @click="openLightbox(image)"
              />
            </div>
          </div>
        </transition-group>

        <!-- 더 보기 버튼 -->
        <div
          v-if="visibleReviewCount < hotelReviews.length"
          class="load-more-container"
        >
          <button @click="expandReviews" class="load-more-btn">
            ➕ 더 보기
          </button>
        </div>
      </div>
    </div>

    <div class="room-list">
      <h3>객실 정보</h3>
      <div v-if="hotel.rooms && hotel.rooms.length > 0">
        <div v-for="room in hotel.rooms" :key="room.roomId" class="room-card">
          <div class="room-image-container">
            <template v-if="room.roomImageUrl">
              <img :src="room.roomImageUrl" class="room-img" alt="Room Image" />
            </template>
            <template v-else>
              <div class="no-room-image">
                <p class="no-room-image-text">업체측에서 제공된 이미지가 없습니다</p>
              </div>
            </template>
          </div>

          <div class="room-info">
            <h4 class="room-name">{{ room.roomType }}</h4>
            <div class="avg-person">
              <img class="person-icon" src="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png" />
              <span class="avg-person-text">기준인원 {{ room.roomOccupancy }}인</span>
            </div>
            <div class="reservation-info">
              <h5 class="reservation-text">숙박</h5>
              <div class="check-info">
                체크인 <span v-html="formattedCheckIn"></span> ~ 체크아웃 <span v-html="formattedCheckOut"></span>
              </div>
              <h2 class="price">{{ room.roomPrice ? `${room.roomPrice.toLocaleString()}원` : "가격 정보 없음" }}</h2>
              <div class="reservation-bottom">
                <div class="room-count">남은 객실 {{ room.roomCount }}개</div>
                  <button @click="move(room)" class="reservation_btn">예약 및 상세보기</button>

              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="room-card">
          <div class="no-room-info-container">
            <p class="no-room-info-text">업체측에서 제공된 객실 정보가 없습니다.</p>
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
/* global kakao */
import axios from "axios";
import { getReviewsByHotel, reportReview } from "@/api/api";
import { useAuthStore } from "@/store/register_login";

export default {
  name: "HotelDetails",
  data() {
    return {
      hotel: null,
      hotelReviews: [],
      visibleReviews: [],
      visibleReviewCount: 3,
      isFavorited: false,
      isLoggedIn: false,
    };
  },
  mounted() {
    this.fetchFavoriteStatus();
  },
  async created() {
    const authStore = useAuthStore();
    authStore.checkLoginStatus();
    this.isLoggedIn = authStore.LoggedIn;
    this.token = authStore.accessToken;

    await this.fetchHotelDetails();
    if (this.hotel && this.hotel.mapX && this.hotel.mapY) {
      this.loadKakaoMap();
    }
  },
  beforeUnmount() {
    // Kakao Map 스크립트를 제거하여 충돌 방지
    const kakaoScript = document.querySelector("script[src*='//dapi.kakao.com/v2/maps/sdk.js']");
    if (kakaoScript) {
      kakaoScript.remove();
      delete window.kakao; // 전역 kakao 객체 삭제
    }
  },
  watch: {
    hotel(newHotel) {
      if (newHotel && newHotel.mapX && newHotel.mapY) {
        this.loadKakaoMap();
      }
    }
  },

  computed: {
  formattedCheckIn() {
    return this.hotel.checkIn ? this.hotel.checkIn.replace(/<br\s*\/?>/gi, '<br>') : "정보없음";
  },
  formattedCheckOut() {
    return this.hotel.checkOut ? this.hotel.checkOut.replace(/<br\s*\/?>/gi, '<br>') : "정보없음";
  }
},

  methods: {
    async fetchHotelDetails() {
      const hotelId = this.$route.params.id;
      try {
        const response = await axios.get(`/api/hotels/${hotelId}`);
        this.hotel = response.data; // HotelDetailDTO 형태로 데이터 수신
        console.log(this.hotel);

        await this.fetchHotelReviews(hotelId);
      } catch (error) {
        console.error("호텔 상세 정보를 가져오는 중 오류 발생:", error);
      }
    },
    move(room) {
      this.$router.push({
        params: { roomId: room.roomId },
        name: 'HotelRoom',
        state: {
          hotelName: this.hotel.name,
          roomName: room.roomType,
          roomPrice: room.roomPrice,
          checkIn: this.hotel.checkIn,
          checkOut: this.hotel.checkOut,
          roomId: room.roomId,
        },
      });
    },
    copyAddressToClipboard() {
      if (this.hotel && this.hotel.address) {
        navigator.clipboard.writeText(this.hotel.address)
          .then(() => {
            alert("주소가 복사되었습니다.");
          })
          .catch(err => {
            console.error("주소 복사 중 오류가 발생했습니다.", err);
          });
      }
    },
    loadKakaoMap() {
      //const apiKey = process.env.VUE_APP_KAKAO_API_KEY;
      if (typeof kakao === "undefined") {
        const script = document.createElement("script");
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=f300564fb3593ce3670ebbb6ccfb7151&autoload=false`;
        script.onload = this.initMap;  // 스크립트 로드 후 initMap 호출
        document.head.appendChild(script);
        console.log(process.env);
      } else {
        this.initMap(); // kakao 객체가 이미 있으면 바로 지도 초기화
      }
    },
    initMap() {
      kakao.maps.load(() => {
        const container = document.getElementById("map");
        const options = {
          center: new kakao.maps.LatLng(this.hotel.mapY || "좌표❌", this.hotel.mapX || "좌표❌"), // 지도의 중심 좌표
          level: 3, // 지도의 확대 레벨
        };

        // 여기서 map을 지역 변수로 정의
        const map = new kakao.maps.Map(container, options); // 지도 생성

        // 마커를 생성하고 지도에 표시
        const markerPosition = new kakao.maps.LatLng(this.hotel.mapY || "좌표❌", this.hotel.mapX || "좌표❌");
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(map);
      });
    },
    async fetchFavoriteStatus() {
      const token = this.token;
      const hotelId = this.$route.params.id;

      if (!this.isLoggedIn) {
        return;
      }

     try {
        const response = await axios.get(`/api/auth/favorites/status/${hotelId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.isFavorited = response.data; // 서버에서 받은 true/false 값을 isFavorited에 반영
      } catch (error) {
        console.error("찜 상태 불러오기 실패", error);
      }
    },
    async toggleFavorite() {
      const token = this.token;
       const hotelId = this.$route.params.id;
       if (!this.isLoggedIn) {
        alert("로그인 후 즐겨찾기를 추가할 수 있습니다.");
        return;
      }
      const url = this.isFavorited
        ? `/api/auth/favorites/cancel/${hotelId}`
        : `/api/auth/favorites/${hotelId}`;

      try {
        await axios.post(url, {}, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        this.isFavorited = !this.isFavorited; // 상태 변경 후 isFavorited 반영
      } catch (error) {
        console.error("찜 상태 변경 실패", error);
      }
    },
    async fetchHotelReviews(hotelId) {
      try {
        const response = await getReviewsByHotel(hotelId); // API 호출
        this.hotelReviews = response.data; // 리뷰 데이터를 저장

        // 초기 visibleReviews 설정
        this.visibleReviews = this.hotelReviews.slice(
          0,
          this.visibleReviewCount
        );
      } catch (error) {
        console.error("호텔 리뷰 조회 중 오류 발생:", error);
      }
    },
    reviewFormatDate(dateString) {
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      return new Date(dateString).toLocaleDateString("ko-KR", options);
    },
    expandReviews() {
      const newCount = this.visibleReviewCount + 3;
      this.visibleReviewCount = newCount;
      this.visibleReviews = this.hotelReviews.slice(0, newCount);
    },
    async reportReviews(reviewId, reviewUser) {
      const authStore = useAuthStore();
      const token = sessionStorage.getItem("token");

      // 로그인 확인
      if (!this.isLoggedIn) {
        alert("로그인이 필요합니다");
        this.$router.push("/login");
        return;
      }

      // loggedInUserId와 reviewId 확인
      if (!reviewId) {
        alert("신고 정보를 확인할 수 없습니다.");
        console.error("Missing userId or reviewId");
        return;
      }

      console.log("리뷰 id : ", reviewId);
      console.log("리뷰 유저 : ", reviewUser);

      // 리퀘스트 바디 생성
      const reportData = {
        reporterId: authStore.userId, // 현재 로그인된 사용자 ID
        reportedId: reviewUser, // 신고 대상 사용자 ID
        reviewId: reviewId, // 신고 대상 리뷰 ID
      };

      try {
        // Axios를 사용한 신고 요청
        const response = await reportReview(reportData, token);

        if (response.data) {
          alert("신고되었습니다.");
          console.log(response.data);

        }
      } catch (error) {
        alert("신고 중 오류가 발생했습니다.");
        console.error("Axios request error:", error.response || error.message);
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
  height: 300px; /* 이미지 틀의 고정 높이 */
  display: flex;
  justify-content: center; /* 이미지 중앙 정렬 */
  align-items: center;
  border-radius: 15px;
  overflow: hidden;
  margin-bottom: 20px;
  background-color: #f8f8f8;
}

.img-container img {
  width: auto;
  height: 100%;
  object-fit: contain;
}

.no-image-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f8f8f8;
}

.no-image-text {
  font-size: 16px;
  color: gray;
  text-align: center;
}

.hotel-info-card {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}
.hotel-top {
  display: flex;
  justify-content: space-between;
}
.fa-heart {
  font-size: 30px;
}
.favorite-container {
  display: flex;
  align-items: center;
}
.favorited {
  color: #FF0000;
}
.unfavorited {
  color: gray;
}
.location-icon {
  margin-right: 5px;
  font-size: 18px; /* 이모지 크기 조정 */
  vertical-align: middle;
}

.copy-button {
  margin-left: 10px;
  padding: 5px 10px;
  font-size: 14px;
  background-color: #00aef0;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.copy-button:hover {
  background-color: #008dc0;
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
.review-list {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
}

.review-grid {
  flex-wrap: nowrap;
  overflow-x: auto;
  flex: 0 1 30%;
  width: 400px;
  height: 150px;
  border: 1px solid lightgray;
  border-radius: 5px;
  margin-top: 15px;
  margin-right: 10px;
  padding: 5px;
}

.review-rating {
  font-size: 16px;
  font-weight: bold;
  color: #ffcc00;
}

.review-date {
  font-size: 14px;
  color: #999;
}

.reviewer {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
}

.review-images {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.review-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 5px;
}

.review-top {
  display: flex;
  justify-content: space-between; /* 좌우 정렬 */
  align-items: center; /* 수직 정렬 */
  margin-bottom: 10px;
}

.review-actions {
  display: flex;
  gap: 10px; /* 버튼 간격 설정 */
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

.load-more-container {
  text-align: center;
  margin-top: 10px;
}

.load-more-btn {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.load-more-btn:hover {
  background-color: #0056b3;
}

.room-list {
  margin-top: 30px;
}

.room-card {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #ddd;
  border-radius: 15px;
  padding: 15px;
  margin-top: 20px;
  min-height: 280px;
  text-align: center;
}

.no-room-info-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.no-room-info-text {
  font-size: 18px;
  color: gray;
}

.room-image-container {
  width: 40%;
  margin-right: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 15px;
  overflow: hidden;
  background-color: #f8f8f8;
}

.room-img {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.no-room-image {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 280px;
  background-color: #f8f8f8;
}

.no-room-image-text {
  font-size: 16px;
  color: gray;
  text-align: center;
}

.room-info {
  width: 60%;
}
.room-name {
  font-size: 20px;
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
  font-size: 20px;
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
#map {
  width: auto !important;
  height: 400px !important;
  margin-top: 10px;
  margin-left: 30px;
}

.edit-button {
  background-color: #007bff;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}

.edit-button:hover {
  background-color: #0056b3;
}

.report-button {
  background-color: #ff4d4d;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}

.report-button:hover {
  background-color: #d32f2f;
}
</style>