<template>
    <div class="mypage-container">
      <!-- 사이드바 컴포넌트 -->
      <sidebar-component />
  
      <div class="content">
        <!-- 내 정보 섹션 -->
        <div class="user-info">
          <h2>내 정보</h2>
          <p>이름: 사용자 이름</p>
          <p>이메일: user@example.com</p>
          <p>전화번호: 010-1234-5678</p>
        </div>
  
        <!-- 리뷰 섹션 -->
        <div class="review-section">
          <h2>리뷰</h2>
          <div class="review-list">
            <div class="review" v-for="(review, index) in reviews" :key="index">
              <div class="review-top">
                <span class="review-rating">⭐⭐⭐⭐⭐</span>
                <span class="review-date">2024.10.25</span>
              </div>
              <div class="review-content">
                숙소 전반적으로 깔끔했고, 서비스도 매우 만족스러웠습니다.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import SidebarComponent from '@/components/UserPages/UserMyPageSidebar.vue';
  
  export default {
    name: 'MyPage',
    components: {
      SidebarComponent,
    },
    data() {
      return {
        reviews: [
          {
            rating: 5,
            date: '2024.10.25',
            content: '숙소 전반적으로 깔끔했고, 서비스도 매우 만족스러웠습니다.',
          },
          // 다른 리뷰들 추가
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  .mypage-container {
    display: flex;
    margin: 20px;
  }
  
  .content {
    flex-grow: 1;
    padding: 20px;
  }
  
  .user-info {
    background: #f7f7f7;
    border-radius: 10px;
    padding: 20px;
    margin-bottom: 20px;
  }
  
  .review-section {
    background: #f7f7f7;
    border-radius: 10px;
    padding: 20px;
  }
  
  .review-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .review {
    background: #ffffff;
    border-radius: 10px;
    padding: 15px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  }
  
  .review-top {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
  }
  
  .review-rating {
    color: #ffcc00;
  }
  
  .review-date {
    color: #888888;
  }
  </style>
  