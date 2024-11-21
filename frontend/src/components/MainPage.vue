<template>
  <div class="main-container">
    <!-- 검색어 입력란 및 돋보기 버튼 -->
    <div class="search-container">
      <input
        v-model="searchQuery"
        class="search-bar"
        type="text"
        placeholder="호텔 검색"
        @click="searchHotel"
      />
      <!-- 돋보기 버튼 -->
      <button @click="searchHotel" class="search-button">🔍</button>
    </div>

    <!-- 자동 완성 목록 -->
    <ul v-if="searchQuery.length > 0" class="autocomplete-list">
      <li
        v-for="result in autocompleteResults"
        :key="result.hotelId"
        @click="goToHotelDetail(result.hotelId)"
        class="autocomplete-item"
      >
        <span class="autocomplete-hotel-name">{{ result.name }}</span>
        <!-- 호텔 이름 표시 -->
        <span class="hotel-address">{{
          result.address || "주소 정보 없음"
        }}</span>
        <!-- 주소 표시 -->
      </li>
      <li v-if="noResults" class="no-results">연관된 검색어가 없습니다.</li>
    </ul>

    <!-- 새로고침 버튼 추가 -->
    <button @click="fetchRandomHotels" class="refresh-button">🔄</button>
    <div class="hotel_list_container">
      <div class="hotel_grid">
        <div
          v-for="hotel in randomHotels"
          :key="hotel.hotelId"
          class="hotel-container"
        >
          <router-link :to="`/hotel-details/${hotel.hotelId}`">
            <img
              :src="hotel.imageUrl || defaultImage"
              class="img-container"
              alt="Hotel Image"
            />
          </router-link>
          <div class="hotel-name">{{ hotel.name }}</div>
          <div class="hotel-info">
            <span class="rating">⭐{{ hotel.rating || 0 }}</span>
            <span>({{ hotel.reviewCount }})</span>
          </div>
        </div>
      </div>

      <h2 class="review-title">리뷰 Top 10</h2>
      <div class="hotel_grid">
        <div
          v-for="hotel in topByReviewCount"
          :key="hotel.hotelId"
          class="hotel-container"
        >
          <router-link :to="`/hotel-details/${hotel.hotelId}`">
            <img
              :src="hotel.imageUrl || defaultImage"
              class="img-container"
              alt="Hotel Image"
            />
          </router-link>
          <div class="hotel-name">{{ hotel.name }}</div>
          <div class="hotel-info">
            <span class="rating">⭐{{ hotel.rating || 0 }}</span>
            <span>({{ hotel.reviewCount }})</span>
          </div>
        </div>
      </div>

      <h2 class="review-title">별점 Top 10</h2>
      <div class="hotel_grid">
        <div
          v-for="hotel in topByRating"
          :key="hotel.hotelId"
          class="hotel-container"
        >
          <router-link :to="`/hotel-details/${hotel.hotelId}`">
            <img
              :src="hotel.imageUrl || defaultImage"
              class="img-container"
              alt="Hotel Image"
            />
          </router-link>
          <div class="hotel-name">{{ hotel.name }}</div>
          <div class="hotel-info">
            <span class="rating">⭐{{ hotel.rating || 0 }}</span>
            <span>({{ hotel.reviewCount }})</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { getHotelsReviewsTop } from "@/api/hotel";

export default {
  name: "MainPage",
  data() {
    return {
      searchQuery: "",
      autocompleteResults: [],
      randomHotels: [],
      topByReviewCount: [],
      topByRating: [],
      defaultImage:
        "https://png.pngtree.com/png-vector/20240613/ourlarge/pngtree-modern-hotel-icon-with-palm-trees-black-isolated-on-white-background-vector-png-image_7010310.png",
      noResults: false, // 연관 검색어가 없는 경우를 표시하기 위한 변수
    };
  },
  created() {
    this.fetchRandomHotels();
    this.fetchTopHotelList();
  },
  methods: {
    async fetchRandomHotels() {
      try {
        const response = await axios.get(
          "${process.env.VUE_APP_API_URL}/api/hotels/random"
        );
        this.randomHotels = response.data; // 백엔드에서 가져온 랜덤 호텔 목록
      } catch (error) {
        console.error("랜덤 호텔 데이터를 가져오는 중 오류 발생:", error);
      }
    },
    fetchAutocompleteResults() {
      this.autocompleteResults = []; // 입력 시 기존 결과 초기화

      if (this.searchQuery.length > 0) {
        const queryWithoutSpaces = this.searchQuery.replace(/\s+/g, ""); // 공백 제거
        axios
          .get(
            `/api/hotels/search?query=${queryWithoutSpaces}`
          )
          .then((response) => {
            this.autocompleteResults = response.data;
            this.noResults = this.autocompleteResults.length === 0;
          })
          .catch((error) => {
            console.error("자동 완성 결과를 가져오는 중 오류 발생:", error);
            this.autocompleteResults = [];
            this.noResults = true;
          });
      } else {
        this.autocompleteResults = []; // 검색어가 없을 때 결과를 비우기
        this.noResults = false;
      }
    },
    searchHotel() {
      // 검색어가 입력된 상태에서 검색 페이지로 이동하며, 검색어를 쿼리 파라미터로 전달
      this.$router.push({
        path: "/search-results",
        query: { query: this.searchQuery },
      });
    },
    goToSearchPage() {
      this.$router.push({
        path: "/search-page",
        query: { query: this.searchQuery },
      });
    },
    async fetchTopHotelList() {
      try {
        // 데이터를 가져오기
        const response = await getHotelsReviewsTop();

        // 가져온 데이터를 변수에 저장
        let topByReviewCount = response.data.topByReviewCount;
        let topByRating = response.data.topByRating;

        // 리뷰 개수가 많은 순으로 내림차순 정렬 (리뷰 개수 동일시 이름 오름차순으로 정렬)
        topByReviewCount = topByReviewCount.sort((a, b) => {
          if (b.reviewCount !== a.reviewCount) {
            return b.reviewCount - a.reviewCount; // 리뷰 개수 내림차순: 큰 값이 상단에 위치
          }
          return a.name.localeCompare(b.name); // 리뷰 개수가 같으면 이름 오름차순
        });

        // 평점이 높은 순으로 내림차순 정렬 (평점 동일시 이름 오름차순으로 정렬)
        topByRating = topByRating.sort((a, b) => {
          if (b.rating !== a.rating) {
            return b.rating - a.rating; // 평점 내림차순: 큰 값이 상단에 위치
          }
          return a.name.localeCompare(b.name); // 평점이 같으면 이름 오름차순
        });

        // 정렬된 데이터를 Vue 컴포넌트의 데이터에 저장
        this.topByReviewCount = topByReviewCount;
        this.topByRating = topByRating;
      } catch (error) {
        console.error("목록 조회 중 오류 발생: ", error);
      }
    },
  },
};
</script>

<style scoped>
.main-container {
  width: 60%;
  margin: auto;
}

.search-container {
  position: relative; /* 검색바와 돋보기를 같은 컨테이너 안에 배치 */
  display: flex;
  align-items: center;
  width: 100%; /* 검색창이 상위 요소를 가득 채우도록 설정 */
  background-color: rgb(233, 233, 233);
  border-radius: 15px;
}

.search-bar {
  width: 100%;
  height: 40px;
  font-size: 15px;
  border: none;
  outline: none;
  padding-right: 40px; /* 돋보기 버튼 공간 확보 */
  padding-left: 10px;
  background-color: transparent;
  margin-right: 10px;
}

.search-button {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
}

.refresh-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
  font-size: 24px;
  vertical-align: middle;
}

.autocomplete-list {
  list-style: none;
  padding: 0;
  margin-top: 5px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  max-height: 150px;
  overflow-y: auto;
  position: absolute;
  width: 100%;
}

.autocomplete-item {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.autocomplete-hotel-name {
  font-size: 16px;
  color: #333;
  font-weight: bold;
}

.hotel-address {
  font-size: 14px;
  color: #777;
}

.autocomplete-item:hover {
  background-color: #f0f0f0;
}

.no-results {
  padding: 8px;
  color: gray;
  text-align: center;
}

.hotel-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.hotel_grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 30px;
}

.img-container {
  width: 180px;
  height: 120px;
  object-fit: cover;
  border-radius: 10px;
}

.hotel-name {
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  width: 90%;
  max-width: 140px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 5px;
}

.hotel-info {
  text-align: center;
}

.review-title {
  margin-top: 20px;
}

@media (max-width: 1600px) {
  .main-container {
    width: 80%;
  }
  .hotel_grid {
    grid-template-columns: repeat(4, 1fr); /* 4개의 열 */
  }
  .img-container,
  .search-bar {
    width: 200px; /* 이미지와 서치바 너비 동일 */
  }
  .img-container {
    height: 150px;
  }
  .search-bar {
    height: 35px;
  }
}

/* 1200px 이하 화면 */
@media (max-width: 1200px) {
  .main-container {
    width: 80%;
  }
  .hotel_grid {
    grid-template-columns: repeat(3, 1fr); /* 3개의 열 */
  }
  .img-container,
  .search-bar {
    width: 200px; /* 이미지와 서치바 너비 동일 */
  }
  .img-container {
    height: 150px;
  }
  .search-bar {
    height: 35px;
  }
}

/* 800px 이하 화면 */
@media (max-width: 800px) {
  .main-container {
    width: 85%;
  }
  .hotel_grid {
    grid-template-columns: repeat(2, 1fr); /* 2개의 열 */
  }
  .img-container,
  .search-bar {
    width: 200px; /* 이미지와 서치바 너비 동일 */
  }
  .img-container {
    height: 150px;
  }
  .search-bar {
    height: 30px;
  }
}

/* 558px 이하 화면 */
@media (max-width: 558px) {
  .main-container {
    width: 90%;
  }
  .hotel_grid {
    grid-template-columns: repeat(1, 1fr); /* 1개의 열 */
  }
  .img-container,
  .search-bar {
    width: 200px; /* 이미지와 서치바 너비 동일 */
  }
  .img-container {
    height: 150px;
  }
  .search-bar {
    height: 28px;
  }
}

/* 더 작은 화면 (모바일 전용) */
@media (max-width: 375px) {
  .main-container {
    width: 95%;
  }
  .hotel_grid {
    grid-template-columns: 1fr; /* 1개의 열 */
  }
  .img-container,
  .search-bar {
    width: 100%; /* 이미지와 서치바 너비 동일 */
  }
  .img-container {
    height: 80px;
  }
  .search-bar {
    height: 28px;
  }
}
</style>
