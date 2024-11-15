<template>
    <div class="content">
    
        <div class="user-info">
          <h2>내 정보</h2>
          <p>이름: {{ userName || '이름을 추가해 주세요.' }}</p>
          <p>이메일: {{ email || '이메일을 추가해 주세요.' }}</p>
          <p>전화번호: {{ phone || '전화번호를 추가해 주세요.' }}</p>
        </div>
    
        <!-- 리뷰 섹션 -->
        <div class="review-section">
          <h2>리뷰</h2>
          <div class="review-list">
            <div v-if="reviews.length === 0">작성한 리뷰가 없습니다.</div>
            <div class="review" v-for="(review, index) in reviews" :key="index">
              <div class="review-top">
                <span class="review-rating">⭐⭐⭐⭐⭐</span>
                <span class="review-date">{{ review.date }}</span>
              </div>
              <div class="review-content">
                {{ review.content }}
              </div>

          <!-- 이미지 갤러리 -->
          <div class="image-gallery">
            <img
              v-for="(image, imgIndex) in review.imageUrl"
              :src="image"
              :key="imgIndex"
              class="thumbnail"
              @click="openLightbox(image)"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Lightbox -->
    <div v-if="lightboxImage" class="lightbox" @click="closeLightbox">
      <img :src="lightboxImage" alt="Review Image" />
        </div>
      </div>   
    </template>
    
    <script>
    export default {
      props: {
        userName: {
          type: String,
          default: ''
        },
        email: {
          type: String,
          default: ''
        },
        phone: {
          type: String,
          default: ''
        },
        reviews: {
          type: Array,
          default: () => []
        }
  },
  data() {
    return {
      lightboxImage: null
    };
  },
  methods: {
    openLightbox(image) {
      this.lightboxImage = image;
    },
    closeLightbox() {
      this.lightboxImage = null;
      }
    }
};
    </script>

<style scoped>
/* 내 정보 스타일 */
.user-info {
  background: #f7f7f7;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

/* 리뷰 섹션 스타일 */
.review-section {
  background: #f7f7f7;
  border-radius: 10px;
  padding: 20px;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.review {
  background: #ffffff;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.review-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.review-rating {
  color: #ffcc00;
}

.review-date {
  color: #888888;
}

/* 이미지 갤러리 스타일 */
.image-gallery {
  display: flex;
  gap: 5px;
  margin-top: 10px;
}

.thumbnail {
  width: 100px;
  height: 100px;
  object-fit: cover;
  cursor: pointer;
  border-radius: 5px;
  transition: transform 0.2s;
}

.thumbnail:hover {
  transform: scale(1.05);
}

/* Lightbox 스타일 */
.lightbox {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.lightbox img {
  max-width: 80%;
  max-height: 80%;
  border-radius: 10px;
}
</style>
