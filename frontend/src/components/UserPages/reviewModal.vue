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
      <div class="image-upload-container" v-if="!isEdit">
        <input
          type="file"
          accept="image/*"
          multiple
          @change="handleImageUpload"
        />
      </div>
      <div class="image-preview">
        <div
          v-for="(image, index) in uploadedImages"
          :key="index"
          class="image-preview-item"
        >
          <img :src="image.url" alt="Preview" />
          <button v-if="!isEdit" @click="removeImage(index)">삭제</button>
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
    isEdit: Boolean,
    initialData: Object,
  },
  data() {
    return {
      reviewContent: "", // 리뷰 내용
      rating: 0, // 별점
      uploadedImages: [], // { file: File, url: string } 형태로 저장
    };
  },
  methods: {
    setRating(star) {
      this.rating = star;
    },
    handleImageUpload(event) {
      if (this.isEdit) return; // 수정 모드에서는 업로드 금지
      const files = Array.from(event.target.files);

      if (!files.length) {
        console.error("이미지 파일이 선택되지 않았습니다.");
        return;
      }

      files.forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          if (e.target && e.target.result) {
            this.uploadedImages.push({ file, url: e.target.result }); // 파일과 URL을 함께 저장
          } else {
            console.error("이미지 로딩 실패:", e);
          }
        };
        reader.readAsDataURL(file); // 파일의 Base64 URL 생성
      });
    },
    removeImage(index) {
      this.uploadedImages.splice(index, 1); // 선택한 이미지 제거
    },
    submitReview() {
      if (!this.reviewContent || this.rating <= 0) {
        alert("리뷰 내용을 입력하고 별점을 선택하세요.");
        return;
      }

      if (!this.uploadedImages || !Array.isArray(this.uploadedImages)) {
        alert("이미지 데이터가 올바르지 않습니다.");
        console.error("uploadedImages is not an array:", this.uploadedImages);
        return;
      }

      // FormData 생성 및 데이터 추가
      const formData = new FormData();
      formData.append("content", this.reviewContent);
      formData.append("rating", this.rating);

      this.uploadedImages.forEach((image, index) => {
        if (image.file) {
          formData.append("images", image.file);
        } else {
          console.warn(`유효하지 않은 이미지 데이터 (index: ${index}):`, image);
        }
      });

      console.log("리뷰 제출 데이터:", {
        content: this.reviewContent,
        rating: this.rating,
        images: this.uploadedImages,
      });

      this.$emit("submit", {
        content: this.reviewContent,
        rating: this.rating,
        images: this.uploadedImages,
      });
    },
  },
  watch: {
    initialData: {
      immediate: true,
      handler(newData) {
        if (newData) {
          this.reviewContent = newData.content || "";
          this.rating = newData.rating || 0;
          this.uploadedImages = newData.imageUrl
            ? newData.imageUrl.map((url) => ({ file: null, url })) // URL만 있을 때 처리
            : [];
        }
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
  transition: color 0.2s;
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
