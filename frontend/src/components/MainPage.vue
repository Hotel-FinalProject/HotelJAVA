<template>
  <div class="main-container">
    <div class="input-bar">
      <input class="search-bar" type="text" placeholder="호텔 검색" />
      <div class = "search-conatiner">
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
      </div>
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
      hotels: [],
      randomHotels: [],
      defaultImage: 'https://png.pngtree.com/png-vector/20240613/ourlarge/pngtree-modern-hotel-icon-with-palm-trees-black-isolated-on-white-background-vector-png-image_7010310.png',
      range: { start: null, end: null },
      showCalendar: false, // 모달 상태
      selectedPersonCount: 1,
    };
  },
  created() {
    this.fetchHotels();
  },
  methods: {
    async fetchHotels() {
      try {
        const response = await axios.get('http://localhost:8081/api/hotels');
        this.hotels = response.data;
        this.randomHotels = this.shuffleArray(this.hotels).slice(0, 5);
      } catch (error) {
        console.error('호텔 데이터를 가져오는 중 오류 발생:', error);
      }
    },
    shuffleArray(array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    },
    onDateSelect() {
      // 날짜가 선택되면 캘린더를 숨깁니다.
      this.showCalendar = false;
    }
  }
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

.search-bar {
  width: 100%;
  height: 40px;
  margin-bottom: 20px;
  font-size: 15px;
  border: 0;
  border-radius: 15px;
  outline: none;
  padding-left: 10px;
  background-color: rgb(233, 233, 233);
}
.search-conatiner{
  display:flex;

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
