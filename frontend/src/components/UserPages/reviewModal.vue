<template>
    <div class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h2 v-if="isEdit">리뷰 수정</h2>
        <h2 v-else>리뷰 작성</h2>
        <textarea
          v-model="reviewContent"
          placeholder="리뷰 내용을 입력해주세요"
          rows="4"
        ></textarea>
        <div class="rating-container">
          <span
            v-for="star in 5"
            :key="star"
            class="star"
            :class="{ selected: star <= rating }"
            @click="setRating(star)"
          >
            ★
          </span>
        </div>
        <div class="modal-actions">
          <button @click="submitReview">{{ isEdit ? "수정" : "작성" }}</button>
          <button @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      isEdit: Boolean,
      reviewId: Number,
    },
    data() {
      return {
        reviewContent: "",
        rating: 0,
      };
    },
    methods: {
      setRating(star) {
        this.rating = star;
      },
      submitReview() {
        this.$emit(
          "submit",
          { content: this.reviewContent, rating: this.rating },
          this.reviewId
        );
      },
      closeModal() {
        this.$emit("close");
      },
    },
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
  }
  textarea {
    width: 100%;
    height: 100px;
    resize: none;
  }
  .rating-container {
    display: flex;
    gap: 5px;
    margin-bottom: 10px;
  }
  .star {
    font-size: 24px;
    cursor: pointer;
    color: #ddd;
  }
  .star.selected {
    color: #ffcc00;
  }
  .modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  </style>
  