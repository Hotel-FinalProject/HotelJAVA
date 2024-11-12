import { defineStore } from 'pinia';
import { getReservationInfo } from '@/api/api'; // api.js에서 예약 정보 가져오는 함수 가져오기

export const useReservationStore = defineStore('reservations', {
  state: () => ({
    reservations: [],
    visibleReservations: [],
    loading: false,
    page: 1,
    itemsPerPage: 5,
  }),
  
  actions: {
    /** 예약 목록 불러오기 */
    async fetchReservations() {
      try {
        this.loading = true;
        const token = sessionStorage.getItem('token'); // 세션에서 토큰 가져오기
        if (!token) {
          alert("로그인이 필요합니다.");
          this.loading = false;
          return;
        }

        const response = await getReservationInfo(token);
        this.reservations = response.data;
        this.visibleReservations = this.reservations.slice(0, this.itemsPerPage);
        this.loading = false;
      } catch (error) {
        console.error("예약 목록 불러오기 오류:", error);
        this.loading = false;
        alert("예약 목록을 불러오는 중 오류가 발생했습니다.");
      }
    },

    /** 더 보기 - 다음 페이지 예약 불러오기 */
    loadMoreReservations() {
      const startIndex = this.page * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      const moreReservations = this.reservations.slice(startIndex, endIndex);
      this.visibleReservations = [...this.visibleReservations, ...moreReservations];
      this.page += 1;
    },
  },
});
