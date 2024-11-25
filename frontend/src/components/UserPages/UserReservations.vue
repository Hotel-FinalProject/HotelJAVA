<template>
  <div class="reservation-container">
    <!-- 현재 예약 섹션 -->
    <h2>현재 예약</h2>
    <div class="reservation-card-container">
      <div v-if="!loading && visibleUpcomingReservations.length === 0">
        예약 내역이 없습니다.
      </div>
      <div v-if="loading">예약 목록을 불러오는 중입니다...</div>
      <div
        v-for="(reservation, index) in visibleUpcomingReservations"
        :key="index"
        class="reservation-card"
      >
        <div class="reservation-info">
          <h3 class="hotel-name" @click="goToHotel(reservation.hotelId)">
            {{ reservation.hotelName }}
          </h3>
          <p>객실 이름: {{ reservation.roomName }}</p>
          <p>체크인: {{ reservation.checkIn }}</p>
          <p>체크아웃: {{ reservation.checkOut }}</p>
          <p>투숙 인원: {{ reservation.guestNum }}</p>
          <p class="request">요청 사항: {{ reservation.request }}</p>
          <p>예약 상태: {{ reservation.status }}</p>
          <button
            v-if="canCancel(reservation.checkIn)"
            @click="cancelReservation(reservation)"
            class="cancel-button"
          >
            결제 취소
          </button>
        </div>
      </div>
      <div v-if="upcomingReservations > visibleUpcomingReservations.length">
        <button @click="loadMoreUpcomingReservations">더 보기</button>
      </div>
    </div>

    <h2>예약 기록</h2>
    <div class="reservation-card-container">
      <div v-if="!loading && visiblePastReservations.length === 0">
        지난 예약 내역이 없습니다.
      </div>
      <div
        v-for="(reservation, index) in visiblePastReservations"
        :key="index"
        class="reservation-card"
      >
        <div class="reservation-info">
          <h3 class="hotel-name" @click="goToHotel(reservation.hotelId)">
            {{ reservation.hotelName }}
          </h3>
          <p>객실 이름: {{ reservation.roomName }}</p>
          <p>체크인: {{ reservation.checkIn }}</p>
          <p>체크아웃: {{ reservation.checkOut }}</p>
          <p>투숙 인원: {{ reservation.guestNum }}</p>
          <p class="request">요청 사항: {{ reservation.request }}</p>
          <p>예약 상태: {{ reservation.status }}</p>
          <button
            v-if="
              canWriteReview(reservation.checkOut) &&
              !hasReview(reservation.reservationId)
            "
            @click="openReviewModal(reservation.reservationId)"
          >
            리뷰 작성
          </button>
        </div>
      </div>
      <div v-if="pastReservations > visiblePastReservations.length">
        <button @click="loadMorePastReservations">더 보기</button>
      </div>
    </div>

    <!-- ReviewModal 컴포넌트 -->
    <ReviewModal
      v-if="state.isModalOpen"
      :reservationId="state.selectedReservationId"
      @submit="handleReviewSubmit"
      @close="closeModal"
    />
  </div>
</template>

<script>
import { useReservationStore } from "@/store/mypage_reservations";
import { onMounted, computed, reactive } from "vue";
import { createReview } from "@/api/api";
import ReviewModal from "@/components/UserPages/reviewModal";
import dayjs from "dayjs";
import { cancelReservationPay } from "@/api/api";


export default {
  components: {
    ReviewModal,
  },
  props: {
    reviews: {
      type: Array,
      required: true,
    },
  },
  setup(props, { emit }) {
    const reservationStore = useReservationStore();

    const state = reactive({
      isModalOpen: false,
      selectedReservationId: null,
    });

    // 예약 목록 가져오기
    onMounted(() => {
      reservationStore.fetchReservations();
    });

    const visibleUpcomingReservations = computed(
      () => reservationStore.visibleUpcomingReservations || []
    );
    const visiblePastReservations = computed(
      () => reservationStore.visiblePastReservations || []
    );
    const loading = computed(() => reservationStore.loading);

    const canCancel = (checkIn) => {
      const today = dayjs();
      const checkInDate = dayjs(checkIn);
      return checkInDate.diff(today, "day") >= 2;
    };

    const cancelReservation = async (reservation) => {
      try {
        const token = sessionStorage.getItem("token");
        if(!token) {
          alert("로그인이 필요합니다.");
          return;
        }

        const imp_uid = reservation.imp_uid;
        const roomId = reservation.roomId;

        const response = await cancelReservationPay(imp_uid, roomId, token);

        if (response.status === 200){
          console.log("결제 취소 결과 : ", response.data);
          reservationStore.fetchReservations();
        } else{
          console.log("결제 취소 결과 : ", response.data);
        }
      } catch (error) {
        console.error("결제 취소 오류 : ", error);
        alert("결제 취소 중 오류가 발생했습니다.");
      }
    }

    const canWriteReview = (checkOut) => {
      const today = dayjs();
      const checkOutDate = dayjs(checkOut);
      const daysSinceCheckOut = today.diff(checkOutDate, "day");
      return daysSinceCheckOut >= 0 && daysSinceCheckOut <= 7;
    };

    // 리뷰 작성 버튼 활성화 여부 확인
    const hasReview = (reservationId) => {
      return props.reviews.some(
        (review) => review.reservationId === reservationId
      );
    };

    // 리뷰 작성 모달 열기
    const openReviewModal = (reservationId) => {
      if (!reservationId) {
        alert("예약 정보를 불러오지 못했습니다. 다시 시도해주세요.");
        return;
      }
      state.isModalOpen = true;
      state.selectedReservationId = reservationId;
    };

    // 리뷰 작성 처리
    const handleReviewSubmit = async (reviewData) => {
      try {
        if (!reviewData) {
          alert("리뷰 데이터가 전달되지 않았습니다.");
          console.error("reviewData is undefined:", reviewData);
          return;
        }

        const { content, rating, images } = reviewData;

        if (!content || !rating) {
          alert("리뷰 내용과 별점은 필수입니다.");
          return;
        }

        if (!state.selectedReservationId) {
          alert("예약 정보가 누락되었습니다. 다시 시도해주세요.");
          return;
        }

        const token = sessionStorage.getItem("token");
        if (!token) {
          alert("로그인이 필요합니다.");
          return;
        }

        const formData = new FormData();
        formData.append("content", content);
        formData.append("rating", rating);
        formData.append("reservationId", state.selectedReservationId);

        if (Array.isArray(images) && images.length > 0) {
          images.forEach((image) => {
            if (image.file) {
              formData.append("images", image.file);
            } else {
              console.warn("유효하지 않은 이미지 데이터:", image);
            }
          });
        }

        await createReview(formData, token);
        alert("리뷰가 작성되었습니다.");
        closeModal();
        reservationStore.fetchReservations(); // 예약 목록 새로고침

        // 상위 컴포넌트에 리뷰가 업데이트되었음을 알림
        emit('update-reviews');
      } catch (error) {
        console.error("리뷰 작성 중 오류 발생:", error);
        alert("리뷰 작성 중 오류가 발생했습니다.");
      }
    };

    const closeModal = () => {
      state.isModalOpen = false;
      state.selectedReservationId = null;
    };

    return {
      visibleUpcomingReservations,
      visiblePastReservations,
      loading,
      canCancel,
      cancelReservation,
      canWriteReview,
      hasReview,
      openReviewModal,
      handleReviewSubmit,
      closeModal,
      state,
    };
  },
};
</script>

<style scoped>
.reservation-container {
  background: #f7f7f7;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.reservation-card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;
}

.reservation-card {
  flex: 1 1 calc(45% - 20px);
  max-width: calc(45% - 20px);
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 25px;
  transition: transform 0.3s;
}

.reservation-card:hover {
  transform: translateY(-5px);
}

@media (max-width: 1529px) {
  .reservation-card {
    flex: 1 1 calc(100% - 20px);
    max-width: calc(100% - 20px);
  }
}

.hotel-name {
  font-size: 20px;
  font-weight: bold;
  color: #007bff;
  cursor: pointer;
  white-space: nowrap;
}

/* 요청 사항 스타일 수정 */
.request {
  margin: 15px 0;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4; /* 최대 4줄까지 표시 */
  -webkit-box-orient: vertical;
  line-height: 1.5em;
  max-height: 6em; /* 줄 간격 * 최대 줄 수 */
  white-space: pre-line; /* 줄 바꿈을 유지하면서 텍스트를 출력 */
  color: #333;
}

button {
  background-color: #007bff;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}
</style>