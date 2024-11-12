<template>
  <div class="main-container">
    <div class="input-bar">
      <div class = "search-container">
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
      <div class="reservation-cal">
        <div v-if="showCalendar" class="calendar-modal">
          <div class="modal-content">
            <VDatePicker v-model.range="range" />
            <button @click="onDateSelect">확인</button>
          </div>
        </div>

        <div @click="showCalendar = !showCalendar"> 
          <div> 
            {{ range.start ? `${range.start.getFullYear()}.${(range.start.getMonth() + 1).toString().padStart(2, '0')}.${range.start.getDate().toString().padStart(2, '0')} (${range.start.toLocaleDateString('ko-KR', { weekday: 'short' })})`
            : `${new Date().getFullYear()}.${(new Date().getMonth() + 1).toString().padStart(2, '0')}.${new Date().getDate().toString().padStart(2, '0')} (${new Date().toLocaleDateString('ko-KR', { weekday: 'short' })})`
            }} -
            {{
              range.end
                ? `${range.end.getFullYear()}.${(range.end.getMonth() + 1).toString().padStart(2, '0')}.${range.end.getDate().toString().padStart(2, '0')} (${range.end.toLocaleDateString('ko-KR', { weekday: 'short' })})`
                : `${new Date().getFullYear()}.${(new Date().getMonth() + 1).toString().padStart(2, '0')}.${new Date().getDate().toString().padStart(2, '0')} (${new Date().toLocaleDateString('ko-KR', { weekday: 'short' })})`
            }}
          </div>
        </div>
      </div>

      <div class="reservation-person">
        <label for="personSelect">예약 인원:</label>
        <select id="personSelect" v-model="selectedPersonCount">
          <option v-for="n in 5" :key="n" :value="n">{{ n }}명</option>
        </select>
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
    </div>
    <!-- 호텔 리스트 -->
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
    </div>
    <h2 class="review-title"> 리뷰 Top 10 </h2>
        <div class ="hotel_grid">
            <div class = "hotel-container">
                <img class = "img-container"  src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
                <div class="hotel-name">호텔명</div>
                <div class="hotel-info">
                    <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
                </div>
            </div>  
        </div>

        <h2 class="review-title"> 별점 Top 10 </h2>
        <div class ="hotel_grid">
            <div class = "hotel-container">
                <img class = "img-container" src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
                <div class="hotel-name">호텔명</div>
                <div class="hotel-info">
                    <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
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
      noResults: false, // 연관 검색어가 없는 경우를 표시하기 위한 변수
      selectedPersonCount: 1,
      showCalendar: false,
      range: { start: null, end: null },

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
    },
    onDateSelect() {
      // 날짜가 선택되면 캘린더를 숨깁니다.
      this.showCalendar = false;
    },
  },
};
</script>

<style scoped>
.main-container {
  width: 60%;
  margin: auto;
}
.input-bar {
  margin-bottom: 10px;
}


.search-container{
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
  width: 1100px;
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

.calendar-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 배경 반투명 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
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











