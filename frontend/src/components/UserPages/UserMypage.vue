<!-- MyPage.vue -->
<template>
  <div class="mypage-container">
    <!-- 사이드바 컴포넌트 -->
    <SidebarComponent />

    <!-- 컨텐츠 랜더링 -->
    <router-view :user="user" :reviews="reviews" />

    <!-- 찜한 호텔 목록 섹션 -->
    <div class="favorite-hotels">
      <h2>찜한 호텔 목록</h2>
      <div class="hotel-list">
        <div class="hotel-item" v-for="(hotel, index) in favoriteHotels" :key="index">
          {{ hotel.name }}
        </div>
        <div v-if="favoriteHotels.length === 0">
          찜한 호텔 목록이 없습니다.
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SidebarComponent from '@/components/UserPages/UserMyPageSidebar.vue';
import { useAuthStore } from '@/store/register_login'; // authStore 가져오기
import { computed } from 'vue';

export default {
  name: 'MyPage',
  components: {
    SidebarComponent,
  },
  setup() {
    // authStore를 가져오기
    const authStore = useAuthStore();

    // 사용자 정보와 관련된 데이터를 computed로 설정
    const user = computed(() => ({
      name: authStore.userName,
      email: authStore.currentUser,
      phone: authStore.phone,
    }));

    // 찜한 호텔 목록 예시 (일단 빈 리스트)
    const favoriteHotels = computed(() => []);

    // 리뷰 목록 예시 (일단 빈 리스트)
    const reviews = computed(() => []);

    return {
      user,
      reviews,
      favoriteHotels,
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

/* 찜한 호텔 목록 섹션 스타일 */
.favorite-hotels {
  width: 250px; /* 우측에 고정된 너비 */
  margin-left: 20px; /* 좌측 콘텐츠와의 여백 */
  padding: 20px;
  background: #f7f7f7;
  border-radius: 10px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.hotel-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.hotel-item {
  background: #ffffff;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.1);
}
</style>
