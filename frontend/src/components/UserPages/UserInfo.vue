<template>
  <div class="content">
    <div class="user-info">
      <h2>내 정보</h2>
      <p>이름: {{ userName || "이름을 추가해 주세요." }}</p>
      <p>이메일: {{ email || "이메일을 추가해 주세요." }}</p>
      <p>전화번호: {{ phone || "전화번호를 추가해 주세요." }}</p>
    </div>
  </div>
  <!-- 리뷰 섹션 -->
  <div class="review-section">
    <h2>리뷰</h2>
    <div class="review-card-container">
      <div v-if="reviews.length === 0" class="no-reviews">
        작성한 리뷰가 없습니다.
      </div>
      <div
        v-for="(review, index) in reviews"
        :key="index"
        class="review-card"
      >
        <div class="review-top">
          <h3 class="hotel-name" @click="goToHotel(review.hotelId)">
            {{ review.hotelName }}
          </h3>
          <div class="review-rating">
            <span
              v-for="star in 5"
              :key="star"
              class="star"
              :class="{ filled: star <= review.rating }"
            >
              ★
            </span>
          </div>
        </div>
        <div class="review-content">{{ review.content }}</div>

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

        <!-- 수정/삭제 버튼 -->
        <div class="review-actions">
          <button class="edit-button" @click="openEditModal(review)">
            수정하기
          </button>
          <button class="delete-button" @click="confirmDelete(review.reviewId)">
            삭제하기
          </button>
        </div>
      </div>
    </div>

    <!-- Lightbox -->
    <div v-if="lightboxImage" class="lightbox" @click="closeLightbox">
      <img :src="lightboxImage" alt="Review Image" />
    </div>

    <!-- 리뷰 작성/수정 모달 -->
    <ReviewModal
      v-if="isModalOpen"
      :isEdit="isEditMode"
      :initialData="selectedReviewData"
      @submit="handleReviewSubmit"
      @close="closeModal"
    />
  </div>
</template>

<script>
import ReviewModal from "@/components/UserPages/reviewModal.vue";
import { updateReview, deleteReview } from "@/api/api";

export default {
  props: {
    userName: {
      type: String,
      required: true,
    },
    email: {
      type: String,
      required: true,
    },
    phone: {
      type: String,
      required: true,
    },
    reviews: Array,
    loggedInUserId: Number,
  },
  components: {
    ReviewModal,
  },
  data() {
    return {
      isModalOpen: false, // 모달 표시 여부
      isEditMode: false, // 작성/수정 모드 구분
      selectedReviewData: null, // 선택된 리뷰 데이터
      lightboxImage: null, // 라이트박스 이미지
    };
  },
  methods: {
    openEditModal(review) {
      this.isModalOpen = true; // 모달 열기
      this.isEditMode = true; // 수정 모드로 설정
      this.selectedReviewData = { ...review }; // 선택된 리뷰 데이터 설정
    },
    closeModal() {
      this.isModalOpen = false; // 모달 닫기
      this.isEditMode = false; // 수정 모드 초기화
      this.selectedReviewData = null; // 선택된 리뷰 데이터 초기화
    },
    async handleReviewSubmit(reviewData) {
      try {
        const token = sessionStorage.getItem("token");
        if (this.isEditMode && this.selectedReviewData) {
          await updateReview(
            this.selectedReviewData.reviewId,
            reviewData,
            token
          );
          alert("리뷰가 수정되었습니다.");
        } else {
          alert("새로운 리뷰가 작성되었습니다.");
        }
        this.closeModal();
        this.$emit("update"); // 상위 컴포넌트에 업데이트 이벤트 전달
      } catch (error) {
        console.error("리뷰 처리 중 오류 발생:", error);
        alert("리뷰 처리 중 오류가 발생했습니다.");
      }
    },
    async confirmDelete(reviewId) {
      if (confirm("정말로 이 리뷰를 삭제하시겠습니까?")) {
        try {
          const token = sessionStorage.getItem("token");
          await deleteReview(reviewId, token);
          alert("리뷰가 삭제되었습니다.");
          this.$emit("update"); // 상위 컴포넌트에 업데이트 이벤트 전달
        } catch (error) {
          console.error("리뷰 삭제 중 오류 발생:", error);
          alert("리뷰 삭제 중 오류가 발생했습니다.");
        }
      }
    },
    openLightbox(image) {
      this.lightboxImage = image;
    },
    closeLightbox() {
      this.lightboxImage = null;
    },
    goToHotel(hotelId) {
      this.$router.push(`/hotel-details/${hotelId}`);
    },
  },
};
</script>

<style scoped>
.user-info {
  background: #f7f7f7;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

.review-section {
  background: #ffffff;
  border-radius: 10px;
  padding: 20px;
}

.review-card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;
}

.review-card {
  flex: 1 1 calc(45% - 20px);
  max-width: calc(45% - 20px);
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 25px;
  transition: transform 0.3s;
}

.review-card:hover {
  transform: translateY(-5px);
}

@media (max-width: 1529px) {
  .review-card {
    flex: 1 1 calc(100% - 20px);
    max-width: calc(100% - 20px);
  }
}

.review-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.hotel-name {
  font-size: 20px;
  font-weight: bold;
  color: #007bff;
  cursor: pointer;
  white-space: nowrap;
}

.review-rating {
  display: flex;
}

.star {
  font-size: 22px;
  color: #ddd;
}

.star.filled {
  color: #ffcc00;
}

.review-content {
  margin: 15px 0;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #333;
}

.image-gallery {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.thumbnail {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 5px;
}

.review-actions {
  display: flex;
  gap: 15px;
  margin-top: 15px;
}

button {
  padding: 12px 25px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.delete-button {
  background-color: #ff4d4d;
  color: white;
}

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
}

.lightbox img {
  max-width: 90%;
  max-height: 90%;
}
</style>