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
          :class="{ filled: star <= rating }"
          @click="setRating(star)"
        >
          ★
        </span>
      </div>
      <div class="image-upload-container">
        <input
          type="file"
          accept="image/*"
          multiple
          @change="handleImageUpload"
        />
        <div class="image-preview">
          <div
            v-for="(image, index) in uploadedImages"
            :key="index"
            class="image-preview-item"
          >
            <img :src="image" alt="Preview" />
            <button @click="removeImage(index)">삭제</button>
          </div>
        </div>
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
    isEdit: Boolean, // 수정 모드 여부
    initialData: Object, // 수정용 초기 데이터
  },
  data() {
    return {
      reviewContent: "", // 리뷰 내용
      rating: 0, // 별점
      uploadedImages: [], // 업로드된 이미지
    };
  },
  methods: {
    // 초기화 메서드
    initializeModal(data) {
      if (data) {
        this.reviewContent = data.content || "";
        this.rating = data.rating || 0;
        this.uploadedImages = [...(data.imageUrl || [])];
      } else {
        this.reviewContent = "";
        this.rating = 0;
        this.uploadedImages = [];
      }
    },
    setRating(star) {
      this.rating = star;
    },
    handleImageUpload(event) {
      const files = Array.from(event.target.files);
      files.forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.uploadedImages.push(e.target.result);
        };
        reader.readAsDataURL(file);
      });
    },
    removeImage(index) {
      this.uploadedImages.splice(index, 1);
    },
    submitReview() {
      this.$emit("submit", {
        content: this.reviewContent,
        rating: this.rating,
        images: this.uploadedImages,
      });
    },
    closeModal() {
      this.$emit("close");
    },
  },
  watch: {
    initialData: {
      immediate: true,
      handler(newData) {
        this.initializeModal(newData); // 초기 데이터 설정
      },
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
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  max-height: 80vh;
  overflow-y: auto;
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

.image-upload-container {
  margin-top: 10px;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.image-preview-item {
  position: relative;
  width: 80px;
  height: 80px;
}

.image-preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.image-preview-item button {
  position: absolute;
  top: 5px;
  right: 5px;
  background: red;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 12px;
  cursor: pointer;
  padding: 2px 5px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
