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
    <div class="review-list">
      <div v-if="reviews.length === 0" class="no-reviews">
        작성한 리뷰가 없습니다.
      </div>
      <div class="review" v-for="(review, index) in reviews" :key="index">
        <div class="review-header">
          <!-- 별점 표시 -->
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
          <span class="review-date">{{ review.date }}</span>
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
  },
};
</script>

<style scoped>
/* 기존 스타일 그대로 사용 */
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

.review-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.review {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.review-rating .star {
  font-size: 20px;
  color: #ddd;
}

.review-rating .star.filled {
  color: #ffcc00;
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
  gap: 10px;
  margin-top: 15px;
}

button {
  padding: 10px 20px;
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
</style>
