<template>
  <div class="reservation-container">
    <h2>내 예약 목록</h2>
    <div class="reservation-list">
      <!-- 예약 목록이 없는 경우 -->
      <div v-if="reservations.length === 0 && !loading">
        예약 내역이 없습니다.
      </div>

      <!-- 예약 목록을 불러오는 중 -->
      <div v-if="loading">예약 목록을 불러오는 중입니다...</div>

      <!-- 예약 목록을 보여줌 -->
      <div
        v-for="(reservation, index) in visibleReservations"
        :key="index"
        class="reservation"
      >
        <div class="reservation-info">
          <p>예약 번호: {{ reservation.id }}</p>
          <p>예약 일자: {{ reservation.date }}</p>
          <p>예약 상태: {{ reservation.status }}</p>
        </div>
      </div>

      <!-- 더 보기 버튼 -->
      <div v-if="reservations.length > visibleReservations.length">
        <button @click="loadMoreReservations">더 보기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useReservationStore } from "@/store/mypage_reservations";
import { onMounted } from "vue";

export default {
  setup() {
    const reservationStore = useReservationStore();

    // 컴포넌트가 마운트되면 예약 정보 불러오기
    onMounted(() => {
      reservationStore.fetchReservations();
    });

    return {
      reservations: reservationStore.reservations,
      visibleReservations: reservationStore.visibleReservations,
      loading: reservationStore.loading,
      loadMoreReservations: reservationStore.loadMoreReservations,
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

.reservation-list {
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
