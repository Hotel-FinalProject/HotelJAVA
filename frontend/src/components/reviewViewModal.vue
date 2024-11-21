<template>
    <div v-if="isOpen" class="modal">
      <div class="modal-content">
        <h2>리뷰 내용</h2>
        <p>{{ review.content }}</p>
        <div v-if="review.imageUrl && review.imageUrl.length > 0" class="review-images">
          <img
            v-for="(image, imgIndex) in review.imageUrl"
            :src="image"
            :key="imgIndex"
            class="review-image"
          />
        </div>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      isOpen: {
        type: Boolean,
        required: true,
      },
      review: {
        type: Object,
        required: true,
      },
    },
    methods: {
      closeModal() {
        this.$emit("close");
      },
    },
  };
  </script>
  
  <style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 5px;
    max-width: 500px;
    width: 100%;
  }
  
  .review-images {
    display: flex;
    align-items: center;
    margin-top: 5px;
  }
  
  .review-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 5px;
    margin-right: 10px;
  }
  </style>