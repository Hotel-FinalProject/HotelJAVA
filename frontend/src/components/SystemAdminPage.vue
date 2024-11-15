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
              <p>총 사용자 수: 1,200명</p>
              <p>비활성 계정: 45명</p>
            </li>
            <li class="dashboard-item">
              <h3>🏨 호텔 관리자 계정 관리</h3>
              <p>등록된 호텔 관리자: 50명</p>
              <p>승인 대기 관리자: 5명</p>
            </li>
            <li class="dashboard-item">
              <h3>🚨 리뷰 관리</h3>
              <p>신고된 리뷰: 12건</p>
              <p>미검토 리뷰: 3건</p>
            </li>
          </ul>
        </div>
        <div v-if="currentView === 'UserManagement'">
          <h2>사용자 관리</h2>
          <p>사용자 계정을 관리하고 검색할 수 있습니다.</p>
        </div>
        <div v-if="currentView === 'HotelAdminAccounts'">
          <h2>호텔 관리자 계정 관리</h2>
          <p>호텔 관리자 계정을 관리하고 권한을 업데이트할 수 있습니다.</p>
        </div>
        <div v-if="currentView === 'ReviewReports'">
          <h2>리뷰 관리</h2>
          <p>신고된 리뷰를 검토하고 조치를 취할 수 있습니다.</p>
        </div>
      </div>
    </SidebarLayout>
  </template>
  
  <script>
  import SidebarLayout from "@/layout/SidebarLayout.vue";
  
  export default {
    name: "SystemAdminPage",
    components: {
      SidebarLayout,
    },
    data() {
      return {
        currentView: "Dashboard", // 초기 화면 설정
      };
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
  
  .dashboard-item h3 {
    margin-bottom: 10px;
    font-size: 20px;
  }
  
  .dashboard-item p {
    margin: 5px 0;
    color: #555;
  }
  </style>
  