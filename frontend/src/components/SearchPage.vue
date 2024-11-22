<template>
    <div class="search-page">
      <!-- 검색 틀 -->
      <div class="search-container">
        <!-- 검색어 입력란 및 돋보기 버튼 -->
        <div class="search-bar">
          <span class="icon">🔍</span>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="지역, 숙소명 키워드로 찾아보세요"
            @keyup.enter="performSearch"
          />
        </div>

        <!-- 연관 검색어 목록 -->
        <ul v-if="autocompleteResults.length > 0" class="autocomplete-list">
          <li
            v-for="result in autocompleteResults"
            :key="result.hotelId"
            @click="selectAutocompleteResult(result)"
            class="autocomplete-item"
          >
          <span class="hotel-name">{{ result.name }}</span>
          <span class="hotel-address">{{ result.address || '주소 정보 없음' }}</span>
          </li>
          <li v-if="noResults" class="no-results">연관된 검색어가 없습니다.</li>
        </ul>
  
        <!-- 날짜 선택 -->
        <div class="date-picker">
                <span class="icon">📅</span>
                <input
                    type="date"
                    v-model="checkInDate"
                    :min="today"
                    placeholder="체크인"
                    @change="validateCheckInDate"
                />
                <span>~</span>
                <input
                  type="date"
                  v-model="checkOutDate"
                  :min="checkInDate || today"
                  placeholder="체크아웃"
                />
            </div>

            <!-- 경고 메시지 -->
            <div v-if="dateWarning" class="warning-message">
                {{ dateWarning }}
            </div>
  
        <!-- 숙박 인원 선택 -->
        <div class="occupancy-selector">
          <span class="icon">👤</span>
          <select v-model="totalGuests">
            <option value="0">인원무관</option>
            <option v-for="n in 20" :key="n" :value="n">{{ n }}명</option>
          </select>
        </div>
      </div>
  
      <!-- 지도 보기 버튼 -->
      <div class="map-button-container">
        <button class="map-button" @click="goToMapView">지도 보기</button>
      </div>
  
      <!-- 검색 결과 목록 -->
      <div class="results">
        <div v-if="hotels.length === 0"></div>
        <div
          v-for="hotel in hotels"
          :key="hotel.hotelId"
          class="hotel-item"
          @click="goToHotelDetail(hotel.hotelId)"
        >
          <img
            :src="hotel.imageUrl || defaultImage"
            alt="Hotel Image"
            class="hotel-image"
          />
          <div class="hotel-info">
            <h3>{{ hotel.name }}</h3>
            <p>{{ hotel.address || "주소 정보 없음" }}</p>
            <p>평점: {{ hotel.rating || "정보 없음" }}</p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    name: "SearchPage",
    data() {
      return {
        searchQuery: this.$route.query.query || "",
        hotels: [],
        checkInDate: "",
        checkOutDate: "",
        totalGuests: 0,
        today: new Date().toISOString().split("T")[0], // 오늘 날짜를 yyyy-mm-dd 형식으로 저장
        defaultImage:
          "https://png.pngtree.com/png-vector/20240613/ourlarge/pngtree-modern-hotel-icon-with-palm-trees-black-isolated-on-white-background-vector-png-image_7010310.png",
        autocompleteResults: [],
        noResults: false,
        dateWarning: ""
      };
    },
    created() {
      // 페이지 로드 시 검색어가 있을 경우 자동으로 검색 수행
        if (this.searchQuery) {
        this.performSearch();
        }
    },
    methods: {
        async performSearch() {
  const params = {
    query: this.searchQuery || undefined,
    checkInDate: this.checkInDate || undefined,
    checkOutDate: this.checkOutDate || undefined,
    guests: this.totalGuests || 1
  };

  try {
    const response = await axios.get("http://localhost:8081/api/hotels/search-by-date-and-guest", { params });
    this.hotels = response.data;

    // URL 업데이트 (검색 조건 유지)
    this.$router.replace({
      query: { ...this.$route.query, query: this.searchQuery }
    });

    // 검색 결과 초기화
    this.autocompleteResults = [];
    this.noResults = false;
  } catch (error) {
    console.error("검색 결과를 가져오는 중 오류 발생:", error);
    this.hotels = [];
  }
}
,
        async fetchAutocompleteResults() {
      if (this.searchQuery.length > 0) {
        const queryWithoutSpaces = this.searchQuery.replace(/\s+/g, '');
        try {
          const response = await axios.get(`http://localhost:8081/api/hotels/search?query=${queryWithoutSpaces}`);
          this.autocompleteResults = response.data;
          this.noResults = this.autocompleteResults.length === 0;
        } catch (error) {
          console.error("자동 완성 결과를 가져오는 중 오류 발생:", error);
          this.autocompleteResults = [];
          this.noResults = true;
        }
      } else {
        this.autocompleteResults = [];
        this.noResults = false;
      }
    },
    validateCheckInDate() {
            if (this.checkInDate < this.today) {
                this.dateWarning = "체크인 날짜는 오늘 이후로 선택해주세요.";
                this.checkInDate = ""; // 과거 날짜를 선택하면 초기화
            } else {
                this.dateWarning = ""; // 유효한 날짜일 경우 경고 문구 제거
            }
        },
        updateCheckoutMinDate() {
    if (this.checkInDate && this.checkInDate < this.today) {
      this.checkInDate = this.today;
    }
  },
      goToHotelDetail(hotelId) {
        this.$router.push(`/hotel-details/${hotelId}`);
      },
      goToMapView() {
        // `hotels` 데이터를 JSON 문자열로 변환하여 쿼리 파라미터로 전달하고 검색어를 함께 전달
        this.$router.push({
          path: '/map-view',
          query: { 
            hotels: JSON.stringify(this.hotels),
            query: this.searchQuery // 검색어를 함께 전달
          }
        });
      },
      selectAutocompleteResult(result) {
        this.searchQuery = result.name;
        this.performSearch();
      },
    },
    
    watch: {
    searchQuery() {
      this.fetchAutocompleteResults();
    }
  },
  };
  </script>
  
  <style scoped>
  .search-page {
    width: 60%;
    margin: auto;
    padding: 20px;
    position: relative; /* 부모 요소에 position: relative 적용 */
  }
  
  .search-container {
    position: sticky;
    top: 0; /* 화면 상단에 고정 */
    z-index: 999;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #f5f5f5;
    padding: 10px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 고정된 요소에 그림자 추가 */
  }
  
  
  .search-bar {
    display: flex;
    align-items: center;
    flex: 1;
    padding: 5px;
    background-color: white;
    border-radius: 5px;
    margin-right: 10px;
  }
  
  .search-bar input {
    border: none;
    outline: none;
    font-size: 16px;
    padding: 5px;
    width: 100%;
  }
  
  .icon {
    margin-right: 5px;
  }

  /* 연관 검색어 목록 스타일 */
  .autocomplete-list {
  list-style: none;
  padding: 0;
  margin: 0;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  position: absolute;
  top: 100%; /* 검색창 바로 아래에 위치하도록 설정 */
  left: 0;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  margin-top: 5px; /* 검색창과 약간의 간격 추가 */
}

.autocomplete-item {
  display: flex; /* Flexbox 사용 */
  justify-content: space-between; /* 좌우 배치 */
  padding: 10px;
  cursor: pointer;
}

.autocomplete-item:hover {
  background-color: #f5f5f5;
}

.hotel-name {
  flex: 1;
  font-weight: bold;
}

.hotel-address {
  color: gray;
  font-size: 0.9em;
}

.no-results {
  padding: 10px;
  color: gray;
  text-align: center;
}
  
  .date-picker,
  .occupancy-selector {
    display: flex;
    align-items: center;
    background-color: white;
    padding: 5px;
    border-radius: 5px;
    margin-right: 10px;
  }
  
  .date-picker input,
  .occupancy-selector select {
    border: none;
    outline: none;
    font-size: 16px;
    margin-left: 5px;
    padding: 5px;
  }
  
  /* 지도 버튼 스타일 */
  .map-button-container {
    position: fixed;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1000; /* 다른 요소 위에 표시 */
  }
  
  .map-button {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
  }
  
  .map-button:hover {
    background-color: #0056b3;
  }
  
  .results {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
  }
  
  .hotel-item {
    border: 1px solid #ddd;
    border-radius: 5px;
    overflow: hidden;
    text-align: center;
    cursor: pointer;
  }
  
  .hotel-image {
    width: 100%;
    height: 150px;
    object-fit: cover;
  }
  
  .hotel-info {
    padding: 10px;
  }

  .warning-message {
    color: red;
    margin-top: 5px;
}
  </style>