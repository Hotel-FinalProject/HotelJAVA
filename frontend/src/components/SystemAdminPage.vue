<template>
  <SidebarLayout title="시스템 관리자">
    <!-- 사이드바 메뉴 -->
    <template #menu>
      <div class="menu-container">
        <a
          href="#"
          :class="{ active: currentView === 'Dashboard' }"
          @click.prevent="currentView = 'Dashboard'"
        >
          <span class="icon">📋</span>대시보드
        </a>
        <a
          href="#"
          :class="{ active: currentView === 'UserManagement' }"
          @click.prevent="currentView = 'UserManagement'"
        >
          <span class="icon">🔍</span> 사용자 관리
        </a>
        <a
          href="#"
          :class="{ active: currentView === 'HotelAdminAccounts' }"
          @click.prevent="currentView = 'HotelAdminAccounts'"
        >
          <span class="icon">🏨</span> 호텔 관리자 계정 관리
        </a>
        <a
          href="#"
          :class="{ active: currentView === 'ReviewReports' }"
          @click.prevent="currentView = 'ReviewReports'"
        >
          <span class="icon">🚨</span> 리뷰 관리
        </a>
      </div>
    </template>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div v-if="currentView === 'Dashboard'">
        <h2>대시보드</h2>
        <p>시스템의 전체 상태를 한눈에 확인하세요.</p>
        <ul class="dashboard-summary">
          <li class="dashboard-item">
            <h3>🔍 사용자 관리</h3>
            <p>총 사용자 수: {{ totalUserCount }}명</p>
            <p>활성 계정: {{ activeUserCount }}명</p>
            <p>비활성 계정: {{ inactiveUserCount }}명</p>
          </li>
          <li class="dashboard-item">
            <h3>🏨 호텔 관리자 계정 관리</h3>
            <p>등록된 호텔 관리자: {{ totalHotelCount }}명</p>
            <p>활성 계정: {{ activeHotelCount }}명</p>
            <p>비활성 계정: {{ inactiveHotelCount }}명</p>
          </li>
          <li class="dashboard-item">
            <h3>🚨 리뷰 관리</h3>
            <p>신고된 리뷰: {{ totalReportCount }}건</p>
            <p>검토 완료 리뷰: {{ completeReportCount }}건</p>
            <p>미검토 리뷰: {{ incompleteReportCount }}건</p>
          </li>
        </ul>
      </div>

      <!--사용자 관리사용자 -->
      <div v-if="currentView === 'UserManagement' && isVerified">
        <!-- 비밀번호 인증이 완료된 경우 -->
        <div class="top-container">
          <div class="title">
            <h2>사용자 관리</h2>
          </div>
          <div class="search-container">
            <input
              class="search-input"
              type="text"
              placeholder="이름, 이메일로 검색해주세요."
              v-model="searchKeyword"
              @input="handleUserSearch"
            />
          </div>
        </div>
        <hr />
        <div class="user-table-container">
          <!-- 테이블 헤더 -->
          <div class="user-table-header">
            <span>계정 상태</span>
            <span>Index</span>
            <span>이름</span>
            <span>이메일</span>
            <span>전화번호</span>
            <span>관리</span>
          </div>
          <!-- 테이블 내용 -->
          <div
            v-for="user in paginatedUserList"
            :key="user.id"
            class="user-table-row"
          >
            <span :class="user.isActive ? 'user-active' : 'user-deactive'">{{
              user.isActive
            }}</span>
            <span>{{ user.userId }}</span>
            <span>{{ user.name }}</span>
            <span>{{ user.email }}</span>
            <span>{{ user.phone }}</span>
            <div class="user-activeBtn-container">
              <button
                v-if="user.isActive"
                @click="handleUserStatusChange(user.userId)"
              >
                정지
              </button>
              <button v-else @click="handleUserStatusChange(user.userId)">
                활성화
              </button>
            </div>
          </div>
        </div>
        <!-- 페이징 처리 -->
        <div class="pagination-container">
          <button
            :disabled="currentPage === 1"
            @click="changePage(currentPage - 1)" class="pagination-button"
          >
            이전
          </button>
          <span>페이지 {{ currentPage }} / {{ totalPages }}</span>
          <button
            :disabled="currentPage === totalPages"
            @click="changePage(currentPage + 1)" class="pagination-button"
          >
            다음
          </button>
        </div>
      </div>

      <!-- 호텔 관리 -->
      <div v-if="currentView === 'HotelAdminAccounts' && isVerified">
        <!-- 비밀번호 인증이 완료된 경우 -->
        <div class="top-container">
          <div class="title">
            <h2>호텔 관리자 계정 관리</h2>
          </div>
          <div class="search-container">
            <input
              class="search-input"
              type="text"
              placeholder="이름, 이메일로 검색해주세요."
              v-model="searchKeyword"
              @input="handleHotelSearch"
            />
            <button class="styled-button" @click="openModal">
              호텔 관리자 계정 생성
            </button>
            <HotelAdminModal
              :isOpen="isModalOpen"
              :adminToken="adminToken"
              @close="closeModal"
            />
          </div>
        </div>
        <hr />
        <div class="user-table-container">
          <!-- 테이블 헤더 -->
          <div class="user-table-header">
            <span>계정 상태</span>
            <span>Index</span>
            <span>호텔명</span>
            <span>이메일</span>
            <span>전화번호</span>
            <span>관리</span>
          </div>
          <!-- 테이블 내용 -->
          <div
            v-for="user in hotelManagerList"
            :key="user.id"
            class="user-table-row"
          >
            <span :class="user.isActive ? 'user-active' : 'user-deactive'">{{
              user.isActive
            }}</span>
            <span>{{ user.userId }}</span>
            <span>{{ user.name }}</span>
            <span>{{ user.email }}</span>
            <span>{{ user.phone }}</span>
            <div class="user-activeBtn-container">
              <button
                v-if="user.isActive"
                @click="handleAccountStatusChange(user.userId)"
              >
                정지
              </button>
              <button v-else @click="handleAccountStatusChange(user.userId)">
                활성화
              </button>
            </div>
          </div>
          <div class="pagination-container">
            <button
            :disabled="currentPage === 1"
            @click="changePage(currentPage - 1)" class="pagination-button"
          >
            이전
          </button>
          <span>페이지 {{ currentPage }} / {{ totalPages }}</span>
          <button
            :disabled="currentPage === totalPages"
            @click="changePage(currentPage + 1)" class="pagination-button"
          >
            다음
          </button>
          </div>
        </div>
      </div>

      <!-- 리뷰 관리: 인증 필요 없음 -->
      <div v-if="currentView === 'ReviewReports'">
        <h2>리뷰 관리</h2>
        <hr />
        <div class="user-table-container">
          <!-- 테이블 헤더 -->
          <div class="review-table-header">
            <span>계정 상태</span>
            <span>Index</span>
            <span>이름</span>
            <span>리뷰 내용</span>
            <span>신고자 이름</span>
            <span>관리</span>
          </div>
          <!-- 테이블 내용 -->
          <div
            v-for="report in reportList"
            :key="report.id"
            class="user-table-row"
          >
            <span
              :class="
                report.status !== '신고 접수됨'
                  ? 'review-active'
                  : 'review-deactive'
              "
              >{{
                report.status === '신고 접수됨' ? '미처리' : '처리완료'
              }}</span
            >
            <span>{{ report.reportId }}</span>
            <span>{{ report.reportedName }}</span>
            <span class="review-content" @click="openReviewModal(report)">{{
              report.content
            }}</span>
            <span>{{ report.reporterName }}</span>
            <div class="user-activeBtn-container">
              <button
                v-if="report.status === '신고 접수됨'"
                @click="handleHideReport(report.reportId)"
              >
                숨김 처리
              </button>
            </div>
          </div>
        </div>
        <div class="pagination-container">
          <button
            :disabled="reviewCurrentPage === 1"
            @click="reviewChangePage(reviewCurrentPage - 1)" class="pagination-button"
          >
            이전
          </button>
          <span>페이지 {{ reviewCurrentPage }} / {{ reviewTotalPages }}</span>
          <button
            :disabled="reviewCurrentPage === reviewTotalPages"
            @click="reviewChangePage(reviewCurrentPage + 1)" class="pagination-button"
          >
            다음
          </button>
        </div>
      </div>
    </div>
    <!-- 비밀번호 인증 모달 -->
    <PasswordVerification
      :isOpen="isPasswordModalOpen"
      :adminToken="adminToken"
      @close="closePasswordModal"
      @verified="handleVerified"
    />
    <!-- 리뷰 모달 -->
    <ReviewModal
      :isOpen="isReviewModalOpen"
      :review="selectedReview"
      @close="closeReviewModal"
    />
  </SidebarLayout>
</template>

<script>
import SidebarLayout from '@/layout/SidebarLayout.vue';
import HotelAdminModal from '@/components/SystemAdminPages/HotelAdminModal.vue';
import PasswordVerification from './SystemAdminPages/PasswordVerification.vue';
import ReviewModal from '@/components/reviewViewModal.vue';
import {
  getUserListByAdmin,
  getHotelManagerListByAdmin,
  getReportListByAdmin,
  getAcountInfo,
  getUserSearch,
  getReportInfo,
  requestReportControl,
  requestActiveStatus,
  getHotelAdminSearch,
} from '@/api/admin';
import { ref, computed } from 'vue';

export default {
  name: 'SystemAdminPage',
  components: {
    SidebarLayout,
    ReviewModal,
    HotelAdminModal,
    PasswordVerification,
  },
  data() {
    return {
      currentView: 'Dashboard', // 초기 화면 설정
      isModalOpen: false,
      isVerified: false,
      isReviewModalOpen: false,
      adminToken: sessionStorage.getItem('token'),
    };
  },
  setup() {
    const userList = ref([]);
    const reportList = ref([]);
    const hotelManagerList = ref([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const hotelCurrentPage = ref(1);
    const hotelTotalPages = ref(0);
    const reviewCurrentPage = ref(1);
    const reviewTotalPages = ref(0);
    const totalPages = ref(0);
    const searchKeyword = ref('');
    const totalUserCount = ref(0);
    const totalHotelCount = ref(0);
    const totalReportCount = ref(0);
    const activeUserCount = ref(0);
    const inactiveUserCount = ref(0);
    const activeHotelCount = ref(0);
    const inactiveHotelCount = ref(0);
    const completeReportCount = ref(0);
    const incompleteReportCount = ref(0);

    const loadData = () => {
      fetchUserList();
      fetchHotelManagerList();
      fetchReportList();
    };

    const changePage = (newPage) => {
      if (newPage >= 1 && newPage <= totalPages.value) {
        currentPage.value = newPage;
        fetchUserList(); // 페이지 변경 후 데이터 가져오기
      }
    };
    const hotelChangePage = (newPage) => {
      if (newPage >= 1 && newPage <= hotelTotalPages.value) {
        hotelCurrentPage.value = newPage;
        fetchUserList(); // 페이지 변경 후 데이터 가져오기
      }
    };
    const reviewChangePage = (newPage) => {
      if (newPage >= 1 && newPage <= reviewTotalPages.value) {
        reviewCurrentPage.value = newPage;
        fetchUserList(); // 페이지 변경 후 데이터 가져오기
      }
    };

    const token = sessionStorage.getItem('token');

    const fetchUserList = async () => {
      try {
        const response = await getUserListByAdmin(
          token,
          currentPage.value,
          pageSize
        );
        userList.value = response.data;
        console.log(
          '사용자 목록 길이 : ',
          response.data.length / pageSize.value
        );
        totalPages.value = Math.ceil(response.data.length / pageSize.value); // 총 페이지 계산
        console.log('totalPages : ', totalPages.value);
      } catch (error) {
        console.error('사용자 목록 불러오기 실패', error);
      }
    };

    const fetchHotelManagerList = async () => {
      try {
        const response = await getHotelManagerListByAdmin(token);

        hotelManagerList.value = response.data;
        hotelTotalPages.value = Math.ceil(
          response.data.length / pageSize.value
        );
      } catch (error) {
        console.error('호텔 관리자 목록 불러오기 실패', error);
      }
    };

    const fetchReportList = async () => {
      try {
        const response = await getReportListByAdmin(
          token,
          currentPage,
          pageSize
        );
        reportList.value = response.data.reports;
        totalPages.value = Math.ceil(response.data.length / pageSize.value);
      } catch (error) {
        console.error('리뷰 목록 불러오기 실패', error);
      }
    };

    const fetchDashboard = async () => {
      const token = sessionStorage.getItem('token');
      const response = await getAcountInfo(token);
      const reportResponse = await getReportInfo(token);

      totalUserCount.value = response.data.userAllCount;
      totalHotelCount.value = response.data.hotelAllCount;
      totalReportCount.value = reportResponse.data.reportCount;
      activeUserCount.value = response.data.userActiveCount;
      inactiveUserCount.value = response.data.userUnActiveCount;
      activeHotelCount.value = response.data.hotelActiveCount;
      inactiveHotelCount.value = response.data.hotelUnActiveCount;
      completeReportCount.value = reportResponse.data.reportInComplete;
      incompleteReportCount.value = reportResponse.data.reportComplete;
    };

    const handleHideReport = async (reportId) => {
      try {
        const response = await requestReportControl(token, reportId);
        console.log(response.data);
        fetchReportList();
        fetchDashboard();
      } catch (error) {
        console.error('리뷰 숨김처리 실패 ', error);
      }
    };

    const handleAccountStatusChange = async (userId) => {
      try {
        const response = await requestActiveStatus(token, userId);
        console.log(response.data);
        // 상태 변경 후 목록을 갱신합니다.
        fetchHotelManagerList();
        fetchDashboard();
      } catch (error) {
        console.error('계정 상태 변경 실패 ', error);
      }
    };

    const handleUserStatusChange = async (userId) => {
      try {
        const response = await requestActiveStatus(token, userId);
        console.log(response.data);
        // 상태 변경 후 목록을 갱신합니다.
        fetchUserList();
        fetchDashboard();
      } catch (error) {
        console.error('계정 상태 변경 실패 ', error);
      }
    };

    const handleUserSearch = async () => {
      try {
        if (searchKeyword.value.trim() === '') {
          await fetchUserList(); // 검색어가 없으면 전체 목록을 다시 불러옴
        } else {
          const response = await getUserSearch(token, searchKeyword.value);
          userList.value = response.data;
        }
      } catch (error) {
        console.error('사용자 검색 실패', error);
      }
    };

    const handleHotelSearch = async () => {
      try {
        if (searchKeyword.value.trim() === '') {
          await fetchHotelManagerList();
        } else {
          const response = await getHotelAdminSearch(
            token,
            searchKeyword.value
          );
          hotelManagerList.value = response.data;
        }
      } catch (error) {
        console.error('사용자 검색 실패', error);
      }
    };

    const paginatedUserList = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      console.log('currentPage:', currentPage.value);
      console.log('totalPages:', totalPages.value);
      return userList.value.slice(start, end); // 현재 페이지 데이터 반환
    });

    return {
      userList,
      hotelManagerList,
      reportList,
      totalUserCount,
      totalHotelCount,
      totalReportCount,
      activeUserCount,
      inactiveUserCount,
      activeHotelCount,
      inactiveHotelCount,
      completeReportCount,
      incompleteReportCount,
      searchKeyword,
      fetchUserList,
      fetchHotelManagerList,
      fetchReportList,
      fetchDashboard,
      handleHideReport,
      handleAccountStatusChange,
      handleUserStatusChange,
      handleUserSearch,
      handleHotelSearch,
      loadData,
      currentPage,
      totalPages,
      changePage,
      paginatedUserList,
      hotelCurrentPage,
      hotelTotalPages,
      reviewCurrentPage,
      reviewTotalPages,
      hotelChangePage,
      reviewChangePage
    };
  },
  mounted() {
    this.fetchUserList();
    this.fetchHotelManagerList();
    this.fetchDashboard();
    this.fetchReportList();
    this.checkSessionValidity();
  },
  watch: {
    searchKeyword(newValue) {
      if (this.currentView.valueOf === 'UserManagement') {
        this.handleUserSearch(newValue);
      } else if (this.currentView.valueOf === 'HotelAdminAccounts') {
        this.handleHotelSearch(newValue);
      }
    },
    currentView(newView) {
      if (
        (newView === 'UserManagement' || newView === 'HotelAdminAccounts') &&
        !this.isVerified
      ) {
        this.openPasswordModal();
      } else if (newView === 'Dashboard' || newView === 'ReviewReports') {
        this.isPasswordModalOpen = false;
      }
    },
  },
  methods: {
    openModal() {
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },
    openPasswordModal() {
      this.isPasswordModalOpen = true;
    },
    closePasswordModal() {
      this.isPasswordModalOpen = false;
    },
    handleVerified() {
      this.isVerified = true; // 비밀번호 인증 성공 시 플래그 변경
      this.isPasswordModalOpen = false; // 모달 닫기

      const currentTime = new Date().getTime();
      sessionStorage.setItem('isVerified', 'true');
      sessionStorage.setItem('verifiedTime', currentTime.toString());

      // 세션 만료 타이머 시작 (예: 15분 후 만료)
      this.startSessionTimeout(15); // 15분
    },
    startSessionTimeout(minutes) {
      const timeoutDuration = minutes * 60 * 1000; // 분을 밀리초로 변환
      setTimeout(() => {
        this.expireSession();
      }, timeoutDuration);
    },
    expireSession() {
      this.isVerified = false;
      sessionStorage.removeItem('isVerified');
      sessionStorage.removeItem('verifiedTime');
      alert('인증이 만료되었습니다. 다시 인증해주세요.');
      this.openPasswordModal(); // 인증이 만료되면 다시 비밀번호 모달 열기
    },
    handleBeforeUnload() {
      sessionStorage.removeItem('isVerified');
      sessionStorage.removeItem('verifiedTime');
    },
    checkSessionValidity() {
      const verifiedTime = sessionStorage.getItem('verifiedTime');
      if (verifiedTime) {
        const currentTime = new Date().getTime();
        const timeElapsed = currentTime - parseInt(verifiedTime, 10);
        const sessionDuration = 15 * 60 * 1000; // 15분

        if (timeElapsed <= sessionDuration) {
          // 세션이 유효한 경우
          this.isVerified = true;
          this.startSessionTimeout(
            (sessionDuration - timeElapsed) / (60 * 1000)
          ); // 남은 시간으로 타이머 시작
        } else {
          // 세션이 만료된 경우
          this.expireSession();
        }
      }
    },
    openReviewModal(review) {
      this.selectedReview = review;
      this.isReviewModalOpen = true;
    },
    closeReviewModal() {
      this.isReviewModalOpen = false;
      this.selectedReview = null;
    },
  },
  beforeRouteLeave() {
    sessionStorage.removeItem('isVerified');
    sessionStorage.removeItem('verifiedTime');
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  },
};
</script>

<style scoped>
/* 사이드바 메뉴 스타일 */
.menu-container {
  display: flex;
  flex-direction: column;
}

.menu-container a {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: #ffffff;
  font-size: 18px;
  text-decoration: none;
  transition: background-color 0.3s ease, color 0.3s ease;
  border-radius: 4px;
  margin-bottom: 5px;
}

.menu-container a:hover {
  background-color: #16518c;
}

.menu-container a.active {
  background-color: #004b8d;
  font-weight: bold;
}

/* 아이콘 스타일 */
.menu-container a .icon {
  margin-right: 10px;
  font-size: 20px;
}

/* 메인 콘텐츠 스타일 */
.main-content {
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-top: 20px;
}

/* 대시보드 요약 스타일 */
.dashboard-summary {
  list-style: none;
  padding: 0;
}

.dashboard-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.top-container {
  display: flex;
  justify-content: space-between;
}

.search-container {
  display: flex;
  padding: 20px;
  gap: 20px;
}

.search-input {
  width: 300px;
  border-radius: 5px;
  border: 1px solid lightgray;
}

.review-content {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 300px; /* 원하는 최대 너비로 설정하세요 */
}

.user-table-container {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
  overflow: hidden;
}

.user-table-header,
.user-table-row {
  display: grid;
  grid-template-columns: 0.5fr 0.5fr 1fr 2fr 1.5fr 1fr;
  gap: 10px;
  padding: 10px 20px;
  font-size: 14px;
  align-items: center;
}
.review-table-header {
  display: grid;
  grid-template-columns: 0.5fr 0.5fr 1fr 2fr 1.5fr 1fr;
  gap: 10px;
  padding: 10px 20px;
  font-size: 14px;
  align-items: center;
}

.user-table-header,
.review-table-header {
  background-color: #ddd;
  color: #fff;
  font-weight: bold;
}

.user-table-row {
  background-color: #ffffff;
}

.user-active,
.review-active {
  width: 60px;
  background-color: rgb(219, 238, 159);
  border-radius: 4px;
  font-weight: bold;
  padding: 5px 10px;
  text-align: center;
}
.user-deactive,
.review-deactive {
  width: 60px;
  background-color: lightgray;
  border-radius: 4px;
  font-weight: bold;
  padding: 5px 10px;
  text-align: center;
}

.user-activeBtn-container {
  display: flex;
  gap: 5px;
}

.user-activeBtn-container button {
  padding: 5px 10px;
  font-size: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.user-activeBtn-container button:hover {
  background-color: #f0f0f0;
}

.dashboard-item h3 {
  margin-bottom: 10px;
  font-size: 20px;
}

.dashboard-item p {
  margin: 5px 0;
  color: #555;
}

.styled-button {
  background-color: #004b8d;
  color: #ffffff;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

 .pagination-container {
    display: flex;
    justify-content: center; /* 중앙 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
    margin: 20px 0;
  }

  .pagination-button {
    background-color: #00aef0;
    color: white;
    border: none;
    padding: 10px 20px;
    margin: 0 10px;
    cursor: pointer;
    font-size: 16px;
    border-radius: 5px;
  }

  .pagination-button:disabled {
    background-color: #d1d1d1;
    cursor: not-allowed;
  }

  .pagination-text {
    font-size: 16px;
    color: #333;
  }
</style>
