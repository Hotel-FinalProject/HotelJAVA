<template>
  <div class="mypage-container">
    <!-- 사이드바 컴포넌트 -->
    <SidebarComponent class="sidebar" />

    <!-- 컨텐츠 랜더링 -->
    <div class="router-view-container">
      <router-view v-slot="{ Component }">
        <component
          :is="Component"
          :user-name="userName"
          :email="email"
          :phone="phone"
          :reviews="reviews"
          @update="handleReviewUpdated"
          :logged-in-user-id="loggedInUserId"
        />
      </router-view>
    </div>

    <!-- 찜한 호텔 목록 섹션 -->
    <div class="favorite-hotels">
      <h2>찜한 호텔 목록</h2>
      <div class="hotel-list">
        <div class="hotel-item" v-for="(hotel, index) in favoriteHotels" :key="index" >
          <!-- router-link로 호텔 상세 페이지로 이동 -->
          <router-link :to="`/hotel-details/${hotel.hotelId}`" class="hotel-link" >
            <img :src="hotel.hotelImage" alt="Hotel Image" class="hotel-image" />
            <span class="hotel-name">{{ hotel.hotelName }}</span>
          </router-link>
        </div>
        <div v-if="favoriteHotels.length === 0">찜한 호텔 목록이 없습니다.</div>
      </div>
    </div>
  </div>
</template>

<script>
import SidebarComponent from "@/components/UserPages/UserMyPageSidebar.vue";
import { getReviewsByUser, getFavoriteInfo } from "@/api/api";

export default {
  name: "MyPage",
  components: {
    SidebarComponent,
  },
  data() {
    return {
      userName: "",
      email: "",
      phone: "",
      reviews: [],
      favoriteHotels: [],
      loggedInUserId: null, // 현재 로그인한 사용자 ID
      intervalId: null,
    };
  },
  mounted() {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo) {
      this.userName = userInfo.userName || "이름을 추가해주세요.";
      this.email = userInfo.email || "이메일을 추가해주세요.";
      this.phone = userInfo.phone || "전화번호를 추가해주세요.";
      this.loggedInUserId = userInfo.userId; // 현재 로그인한 사용자 ID 설정

      this.fetchUserReviews();
      this.fetchUserFavorite();
      this.startFetchingReviews();
    } else {
      console.log("로컬 스토리지에 userInfo가 없습니다.");
    }
  },
  beforeUnmount() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  },
  methods: {
    async fetchUserReviews() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await getReviewsByUser(this.loggedInUserId, token);
        this.reviews = response.data;
      } catch (error) {
        console.error("리뷰 데이터를 가져오는 데 실패했습니다:", error);
      }
    },
    startFetchingReviews() {
      this.intervalId = setInterval(this.fetchUserReviews, 30000);
    },
    async fetchUserFavorite() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await getFavoriteInfo(token);
        this.favoriteHotels = response.data;
      } catch (error) {
        console.error(
          "찜한 호텔 목록 데이터를 가져오는 데 실패했습니다:",
          error
        );
      }
    },
    handleReviewUpdated() {
    this.fetchUserReviews();
  },
  },
};
</script>

<style scoped>
.mypage-container {
  display: flex;
  justify-content: space-between; /* 사이드바, 콘텐츠, 찜한 호텔 목록을 공간에 맞게 배치 */
  align-items: flex-start; /* 모든 요소의 상단 정렬 */
  margin: 20px;
}

.sidebar {
  width: 200px;
  flex-shrink: 0; /* 사이드바의 길이가 콘텐츠 길이에 영향을 받지 않도록 */
  background: #f0f0f0;
  padding: 20px;
  border-radius: 10px;
  margin-right: 20px; /* 사이드바와 콘텐츠 사이의 여유 공간 설정 */
}

.router-view-container {
  flex-grow: 1; /* 가운데 콘텐츠 영역의 너비를 최대화 */
  padding: 20px;
  background: #ffffff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  min-height: 500px; /* 최소 높이를 설정하여 콘텐츠가 적어도 일정 크기를 유지하도록 함 */
  margin-right: 20px; /* 가운데 콘텐츠와 찜한 호텔 목록 사이의 여유 공간 설정 */
}

.favorite-hotels {
  width: 250px;
  flex-shrink: 0; /* 찜한 호텔 목록의 길이가 줄어들지 않도록 */
  padding: 20px;
  background: #f7f7f7;
  border-radius: 10px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  align-self: flex-start; /* 이 섹션이 메인 콘텐츠 높이와 관계없이 상단에 고정되도록 설정 */
}

.hotel-item a{
  display: flex;
  align-items: center; /* 이미지와 텍스트를 수직 가운데 정렬 */
  gap: 15px; /* 이미지와 텍스트 사이 간격 */
  padding: 10px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  text-decoration-line: none;
}

.hotel-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 5px;
}

.hotel-name {
  font-size: 14px;
  color: #333333;
}
</style>
