import { defineStore } from 'pinia';
import { getReservationInfo } from '@/api/api'; // api.js에서 예약 정보 가져오는 함수 가져오기
import dayjs from 'dayjs'; // 날짜 비교를 위한 라이브러리

export const useReservationStore = defineStore('reservations', {
    state: () => ({
        reservations: [],
        upcomingReservations: [], // 현재 예약
        pastReservations: [], // 지난 예약
        visibleUpcomingReservations: [], // 보여줄 현재 예약
        visiblePastReservations: [], // 보여줄 지난 예약
        loading: false,
        pageUpcoming: 1,
        pagePast: 1,
        itemsPerPageUpcoming: 6, // 현재 예약은 한 번에 6개씩 보여줌
        itemsPerPagePast: 6, // 지난 예약은 한 번에 6개씩 보여줌
    }),

    actions: {
        /** 예약 목록 불러오기 */
        async fetchReservations() {
            try {
                this.loading = true;

                // 토큰 확인
                const token = sessionStorage.getItem('token');
                if (!token) {
                    alert("로그인이 필요합니다.");
                    this.loading = false;
                    return;
                }

                console.log("사용자 토큰:", token); // 토큰 확인을 위한 콘솔 로그 추가

                // Axios 요청을 보내 예약 정보를 가져옴
                const response = await getReservationInfo(token);

                console.log("예약 목록 응답:", response.data); // 응답 데이터 확인

                // 결제 상태가 "결제 취소"인 예약 제거
                this.reservations = response.data.filter(
                    (reservation) => reservation.paymentStatus !== '결제 취소'
                );

                // 예약 목록을 날짜 순으로 정렬 (최근 예약이 먼저)
                this.reservations = this.reservations.sort((a, b) => {
                    const dateA = dayjs(a.checkIn);
                    const dateB = dayjs(b.checkIn);
                    return dateB.diff(dateA);
                });

                // 현재 예약 및 지난 예약 분리 (시간까지 고려하여 구분)
                const now = dayjs();
                this.upcomingReservations = this.reservations.filter(reservation => dayjs(reservation.checkIn).isAfter(now) || dayjs(reservation.checkIn).isSame(now));
                this.pastReservations = this.reservations.filter(reservation => dayjs(reservation.checkIn).isBefore(now));

                // 보여줄 예약 초기화 (페이지 번호 초기화)
                this.pageUpcoming = 1;
                this.pagePast = 1;

                // 초기화된 페이지에 따라 보여줄 예약 설정
                this.visibleUpcomingReservations = this.upcomingReservations.slice(0, this.itemsPerPageUpcoming);
                this.visiblePastReservations = this.pastReservations.slice(0, this.itemsPerPagePast);

                this.loading = false;
            } catch (error) {
                console.error("예약 목록 불러오기 오류:", error);
                this.loading = false;
                alert("예약 목록을 불러오는 중 오류가 발생했습니다.");
            }
        },

        /** 현재 예약 더 보기 */
        loadMoreUpcomingReservations() {
            const startIndex = this.pageUpcoming * this.itemsPerPageUpcoming;
            const endIndex = startIndex + this.itemsPerPageUpcoming;
            const moreReservations = this.upcomingReservations.slice(startIndex, endIndex);
            this.visibleUpcomingReservations = [...this.visibleUpcomingReservations, ...moreReservations];
            this.pageUpcoming += 1;
        },

        /** 지난 예약 더 보기 */
        loadMorePastReservations() {
            const startIndex = this.pagePast * this.itemsPerPagePast;
            const endIndex = startIndex + this.itemsPerPagePast;
            const moreReservations = this.pastReservations.slice(startIndex, endIndex);
            this.visiblePastReservations = [...this.visiblePastReservations, ...moreReservations];
            this.pagePast += 1;
        }
    },
});