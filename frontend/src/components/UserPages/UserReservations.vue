<template>
  <div class="reservation-container">
    <!-- 현재 예약 섹션 -->
    <h2>현재 예약</h2>
    <div class="reservation-list">
      <!-- 현재 예약이 없는 경우 -->
      <div v-if="!loading && visibleUpcomingReservations === 0">
        예약 내역이 없습니다.
      </div>

      <!-- 예약 목록을 불러오는 중 -->
      <div v-if="loading">예약 목록을 불러오는 중입니다...</div>

      <!-- 현재 예약 목록을 보여줌 -->
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

          <!-- 결제 취소 버튼 (체크인 이틀 전까지 가능) -->
          <button
            v-if="canCancel(reservation.checkIn)"
            @click="cancelReservation(reservation)"
            class="cancel-button"
          >
            결제 취소
          </button>
        </div>
      </div>

      <!-- 더 보기 버튼 (현재 예약) -->
      <div v-if="upcomingReservations > visibleUpcomingReservations">
        <button @click="loadMoreUpcomingReservations">더 보기</button>
      </div>
    </div>

    <!-- 지난 예약 섹션 -->
    <h2>예약 기록</h2>
    <div class="past-reservation-list">
      <!-- 지난 예약이 없는 경우 -->
      <div v-if="!loading && visiblePastReservations === 0">
        지난 예약 내역이 없습니다.
      </div>

      <!-- 지난 예약 목록을 보여줌 -->
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
        </div>
      </div>

      <!-- 더 보기 버튼 (지난 예약) -->
      <div v-if="pastReservations > visiblePastReservations">
        <button @click="loadMorePastReservations">더 보기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useReservationStore } from "@/store/mypage_reservations";
import { onMounted, computed } from "vue";
import { cancelReservationPay } from "@/api/api";
import dayjs from "dayjs";

export default {
  setup() {
    const reservationStore = useReservationStore();

    // 컴포넌트가 마운트되면 예약 정보 불러오기
    onMounted(() => {
      reservationStore.fetchReservations();
    });

    // 상태값을 computed로 정의해서 반응성을 보장
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
    }

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

    return {
      visibleUpcomingReservations,
      visiblePastReservations,
      loading,
      loadMoreUpcomingReservations: reservationStore.loadMoreUpcomingReservations,
      loadMorePastReservations: reservationStore.loadMorePastReservations,
      canCancel,
      cancelReservation,
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
