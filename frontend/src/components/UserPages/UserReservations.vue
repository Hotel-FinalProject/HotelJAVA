<template>
  <div class="reservation-container">
    <!-- 현재 예약 섹션 -->
    <h2>현재 예약</h2>
    <div class="reservation-list">
      <div v-if="!loading && visibleUpcomingReservations.length === 0">
        예약 내역이 없습니다.
      </div>
      <div v-if="loading">예약 목록을 불러오는 중입니다...</div>
      <div
        v-for="(reservation, index) in visibleUpcomingReservations"
        :key="index"
        class="reservation"
      >
        <div class="reservation-info">
          <p>호텔 이름: {{ reservation.hotelName }}</p>
          <p>객실 이름: {{ reservation.roomName }}</p>
          <p>체크인: {{ reservation.checkIn }}</p>
          <p>체크아웃: {{ reservation.checkOut }}</p>
          <p>투숙 인원: {{ reservation.guestNum }}</p>
          <p>요청 사항: {{ reservation.request }}</p>
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
    <div class="past-reservation-list">
      <div v-if="!loading && visiblePastReservations.length === 0">
        지난 예약 내역이 없습니다.
      </div>
      <div
        v-for="(reservation, index) in visiblePastReservations"
        :key="index"
        class="reservation"
      >
        <div class="reservation-info">
          <p>호텔 이름: {{ reservation.hotelName }}</p>
          <p>객실 이름: {{ reservation.roomName }}</p>
          <p>체크인: {{ reservation.checkIn }}</p>
          <p>체크아웃: {{ reservation.checkOut }}</p>
          <p>투숙 인원: {{ reservation.guestNum }}</p>
          <p>요청 사항: {{ reservation.request }}</p>
          <p>예약 상태: {{ reservation.status }}</p>
          <button
            v-if="canWriteReview(reservation.checkOut)"
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
import ReviewModal from "@/components/UserPages/reviewModal.vue";
import dayjs from "dayjs";

export default {
  components: {
    ReviewModal,
  },
  setup() {
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

    const canWriteReview = (checkOut) => {
      const today = dayjs();
      const checkOutDate = dayjs(checkOut);
      const daysSinceCheckOut = today.diff(checkOutDate, "day");
      return daysSinceCheckOut >= 0 && daysSinceCheckOut <= 7;
    };

    // 리뷰 작성 모달 열기
    const openReviewModal = (reservationId) => {
      console.log("openReviewModal called with reservationId:", reservationId);
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
        console.log("Submitting review with data:", {
          ...reviewData,
          reservationId: state.selectedReservationId,
        });

        if (!state.selectedReservationId) {
          alert("예약 정보가 없습니다. 다시 시도해주세요.");
          return;
        }

        const token = sessionStorage.getItem("token");
        const formData = new FormData();
        formData.append("content", reviewData.content || "");
        formData.append("rating", reviewData.rating || 0);
        formData.append("reservationId", state.selectedReservationId);

        if (reviewData.images && reviewData.images.length > 0) {
          reviewData.images.forEach((image) =>
            formData.append("images", image)
          );
        }

        console.log("FormData prepared for Axios:", formData); // 확인용 로그 추가
        for (let [key, value] of formData.entries()) {
          console.log(`${key}: ${value}`); // FormData 내용 출력
        }

        await createReview(formData, token);
        alert("리뷰가 작성되었습니다.");

        closeModal();
        reservationStore.fetchReservations();
      } catch (error) {
        console.error("리뷰 작성 중 오류 발생:", error);
        alert("리뷰 작성 중 오류가 발생했습니다.");
      }
    };

    // 모달 닫기
    const closeModal = () => {
      state.isModalOpen = false;
      state.selectedReservationId = null;
    };

    return {
      visibleUpcomingReservations,
      visiblePastReservations,
      loading,
      canCancel,
      canWriteReview,
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
  max-width: 600px;
  margin: 0 auto;
  background: #f7f7f7;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.reservation-list,
.past-reservation-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.reservation {
  background: #ffffff;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
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
