<template>
  <div class="main-container">
    <!-- 검색어 입력란 및 돋보기 버튼 -->
    <div class="search-container">
      <input
        v-model="searchQuery"
        class="search-bar"
        type="text"
        placeholder="호텔 검색"
        @input="fetchAutocompleteResults"
        @keyup.enter="searchHotel"
      />
      <!-- 돋보기 버튼 -->
      <button @click="searchHotel" class="search-button">
        🔍
      </button>
    </div>

    <!-- 자동 완성 목록 -->
    <ul v-if="searchQuery.length > 0" class="autocomplete-list">
      <li
        v-for="result in autocompleteResults"
        :key="result.hotelId"
        @click="goToHotelDetail(result.hotelId)"
        class="autocomplete-item"
      >
        <span class="autocomplete-hotel-name">{{ result.name }}</span> <!-- 호텔 이름 표시 -->
        <span class="hotel-address">{{ result.address || '주소 정보 없음' }}</span> <!-- 주소 표시 -->
      </li>
      <li v-if="noResults" class="no-results">연관된 검색어가 없습니다.</li>
    </ul>

    <!-- 새로고침 버튼 추가 -->
    <button @click="fetchRandomHotels" class="refresh-button">
        🔄
    </button>
    <div class="hotel_list_container">
      <div class="hotel_grid">
        <div v-for="hotel in randomHotels" :key="hotel.hotelId" class="hotel-container">
          <router-link :to="`/hotel-details/${hotel.hotelId}`">
            <img :src="hotel.imageUrl || defaultImage" class="img-container" alt="Hotel Image" />
          </router-link>
          <div class="hotel-name">{{ hotel.name }}</div>
          <div class="hotel-info">
            <span class="rating">⭐4.5</span>
            <span>(리뷰 갯수)</span>
          </div>
        </div>
      </div>

      <h2 class="review-title">리뷰 Top 10</h2>
      <div class="hotel_grid">
        <div class="hotel-container">
          <img
            class="img-container"
            src="https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg"
          />
          <div class="hotel-name">호텔명</div>
          <div class="hotel-info">
            <span class="rating">⭐4.5</span><span>(리뷰갯수)</span>
          </div>
        </div>
      </div>

      <h2 class="review-title">별점 Top 10</h2>
      <div class="hotel_grid">
        <div class="hotel-container">
          <img
            class="img-container"
            src="https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg"
          />
          <div class="hotel-name">호텔명</div>
          <div class="hotel-info">
            <span class="rating">⭐4.5</span><span>(리뷰갯수)</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MainPage',
  data() {
    return {
      searchQuery: '',
      autocompleteResults: [],
      randomHotels: [],
      defaultImage: 'https://png.pngtree.com/png-vector/20240613/ourlarge/pngtree-modern-hotel-icon-with-palm-trees-black-isolated-on-white-background-vector-png-image_7010310.png',
      noResults: false // 연관 검색어가 없는 경우를 표시하기 위한 변수
    };
  },
  created() {
    this.fetchRandomHotels();
  },
  methods: {
    async fetchRandomHotels() {
      try {
        const response = await axios.get('http://localhost:8081/api/hotels/random');
        this.randomHotels = response.data; // 백엔드에서 가져온 랜덤 호텔 목록
      } catch (error) {
        console.error('랜덤 호텔 데이터를 가져오는 중 오류 발생:', error);
      }
    },
    async fetchAutocompleteResults() {
      if (this.searchQuery.length > 0) {
        const queryWithoutSpaces = this.searchQuery.replace(/\s+/g, ''); // 공백 제거
        try {
          const response = await axios.get(`http://localhost:8081/api/hotels/search?query=${queryWithoutSpaces}`);
          this.autocompleteResults = response.data;
          this.noResults = this.autocompleteResults.length === 0;
        } catch (error) {
          console.error('자동 완성 결과를 가져오는 중 오류 발생:', error);
          this.autocompleteResults = [];
          this.noResults = true;
        }
      } else {
        this.autocompleteResults = [];
        this.noResults = false;
      }
    },
    async searchHotel() {
      const exactMatch = this.autocompleteResults.find(result => result.name === this.searchQuery);

      if (exactMatch) {
        // 완전히 일치하는 호텔명이 있는 경우 상세 페이지로 이동
        this.goToHotelDetail(exactMatch.hotelId);
      } else if (this.autocompleteResults.length > 0) {
        // 연관 검색어가 있는 경우 경고 메시지 표시
        alert("연관된 검색어 목록에서 선택해주세요.");
      } else {
        // 연관 검색어가 없는 경우 경고 메시지 표시
        alert("연관된 검색어가 없습니다.");
      }
    },
    goToHotelDetail(hotelId) {
      this.$router.push(`/hotel-details/${hotelId}`);
    }
  }
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
  width: 965px;
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

</style>
