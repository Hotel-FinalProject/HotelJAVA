<template>
  <div class="main-container">
    <div class="input-bar">
      <input class="search-bar" type="text" placeholder="호텔 검색" />
    </div>
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
      hotels: [],
      randomHotels: [],
      defaultImage: 'https://png.pngtree.com/png-vector/20240613/ourlarge/pngtree-modern-hotel-icon-with-palm-trees-black-isolated-on-white-background-vector-png-image_7010310.png'
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
    }
  }
};
</script>

<style scoped>
.main-container {
  width: 60%;
  margin: auto;
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
