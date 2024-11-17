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
        
        <!--사용자 관리사용자 -->
        <div v-if="currentView === 'UserManagement'">
          <h2>사용자 관리</h2>
          <hr>
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
            <div class="user-table-row">
              <span class="user-active">사용중</span>
              <span>1</span>
              <span>홍길동</span>
              <span>user1@example.com</span>
              <span>010-1234-5678</span>
              <div class="user-activeBtn-container">
                <button>활성화</button>
                <button>정지</button>
              </div>
            </div>
            <div class="user-table-row">
              <span class="user-deactive">비활성</span>
              <span>2</span>
              <span>김영희</span>
              <span>user2@example.com</span>
              <span>010-8765-4321</span>
              <div class="user-activeBtn-container">
                <button>활성화</button>
                <button>정지</button>
              </div>
            </div>
          </div>
        </div>

         <!-- 호텔 관리 -->
        <div v-if="currentView === 'HotelAdminAccounts'">
          <h2>호텔 관리자 계정 관리</h2>
          <hr>
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
            <div class="user-table-row">
              <span class="user-active">사용중</span>
              <span>1</span>
              <span>홍길동</span>
              <span>user1@example.com</span>
              <span>010-1234-5678</span>
              <div class="user-activeBtn-container">
                <button>활성화</button>
                <button>정지</button>
              </div>
            </div>
            <div class="user-table-row">
              <span class="user-deactive">비활성</span>
              <span>2</span>
              <span>김영희</span>
              <span>user2@example.com</span>
              <span>010-8765-4321</span>
              <div class="user-activeBtn-container">
                <button>활성화</button>
                <button>정지</button>
              </div>
            </div>
          </div>
        </div>

        <!-- 리뷰 관리 -->
        <div v-if="currentView === 'ReviewReports'">
          <h2>리뷰 관리</h2>
          <hr>
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
            <div class="user-table-row">
              <span class="review-active">처리완료</span>
              <span>1</span>
              <span>test1</span>
              <span>바보</span>
              <span>ㅇㅇㅇ</span>
              <div class="user-activeBtn-container">
                <button>숨김 처리</button>
              </div>
            </div>
            <div class="user-table-row">
              <span class="review-deactive">미처리</span>
              <span>2</span>
              <span>test2</span>
              <span>바보</span>
              <span>ㅇㅇㅇ</span>
              <div class="user-activeBtn-container">
                <button>숨김 처리</button>
              </div>
            </div>
          </div>
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

  .user-table-container {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
  overflow: hidden;
}

.user-table-header, .user-table-row {
  display: grid;
  grid-template-columns: 0.5fr 0.5fr 1fr 2fr 1.5fr 1fr;
  gap: 10px;
  padding: 10px 20px;
  font-size: 14px;
  align-items: center;
}
 .review-table-header{
   display: grid;
  grid-template-columns: 0.5fr 0.5fr 1fr 2fr 1.5fr 1fr;
  gap: 10px;
  padding: 10px 20px;
  font-size: 14px;
  align-items: center;
 }

.user-table-header, .review-table-header {
  background-color: #ddd;
  color: #fff;
  font-weight: bold;
}

.user-table-row {
  background-color: #ffffff;
}


.user-active,.review-active {
  width:60px;
  background-color: rgb(219, 238, 159);
  border-radius: 4px;
  font-weight: bold;
  padding: 5px 10px;
  text-align: center;
}
.user-deactive,.review-deactive{
  width:60px;
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
  </style>
  