<!-- html -->
<template>
  <nav class="navbar">
    <div class="navbar_logo">
      <span class="brand_name" @click="to_home()">HotelJava</span>
    </div>
    <div class="navbar_items">
      <ul class="navbar_item" v-if="!isLoggedIn">
        <li>
          <router-link to="/login">
            <span class="login_btn">로그인</span>
          </router-link>
        </li>
        <li>
          <router-link to="/register">
            <span class="signup_btn">회원가입</span>
          </router-link>
        </li>
      </ul>
      <ul class="navbar_item" v-else>
        <li>
          <button @click="logout" class="logout_btn">로그아웃</button>
        </li>
        <li>
          <span
            class="my_page_btn"
            @click="to_my_page"
            @mouseover="hover = true"
            @mouseleave="hover = false"
          >
            {{ hover ? '마이페이지' : userName+"님" }}
          </span>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { useAuthStore } from '@/store/register_login';
import { computed, ref } from 'vue';
import { jwtDecode } from 'jwt-decode';

export default {
  name: "theNavbar",
  setup() {
    const authStore = useAuthStore();

    // Pinia 스토어에서 로그인 상태를 반응형으로 가져오기 위해 computed 사용
    const isLoggedIn = computed(() => authStore.LoggedIn);
    const userName = computed(() => authStore.userName || '사용자'); // 사용자 이름 가져오기
    
    const userRole = computed(() => {
      const token = sessionStorage.getItem("token");
      if (token) {
        try {
          const decodedToken = jwtDecode(token);
          return decodedToken.role;
        } catch (error) {
          console.error('토큰 디코딩 중 오류 발생: ', error);
          return null;
        }
      }
      return null;
    });

    const pageLabel = computed(() => {
      if (userRole.value === 'ROLE_ADMIN' || userRole.value === 'ROLE_HOTEL_MANAGER') {
        return '관리자 페이지';
      }
      return '마이페이지';
    });
    
    // 마우스 오버 상태 관리
    const hover = ref(false);

    // 로그아웃 함수
    const logout = () => {
      authStore.logout();
    };

    return {
      isLoggedIn,
      userName,
      hover,
      logout,
      pageLabel
    }
  },
  methods: {
    to_my_page() {
      this.$router.push("/my_page");
    },
    to_home() {
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #00aef0;
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 20px;
}

.logo {
  height: 40px;
  margin-right: 10px;
}

.brand_name {
  font-size: 3em;
  font-weight: bold;
  color: #333;
  font-family: 'Dancing Script', cursive;
}

.navbar_item {
  align-items: center;
  display: flex; 
  list-style: none; 
  padding: 5;
  margin: 5;
}

.navbar_item button,
.navbar_item span {
  border: 0;
  background-color: transparent;
  font-size: 1.2em;
  margin-left: 20px;
}

.logout_btn {
  color: black;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.2em;
}

.my_page_btn {
  color: black; 
  font-size: 1.2em; 
  margin-left: 20px;
  transition: color 0.3s;
}

.my_page_btn:hover {
  color: #555; /* 마우스 오버 시 약간 더 어두운 색상으로 변화 */
}

.navbar_item li {
  margin-left: 20px;
  font-size: 1.2em;
}

.navbar_item a {
  text-decoration: none;
  color: black;
  margin-left: 20px;
  font-size: 1.2em;
}

.navbar_btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2em;
  margin-left: 20px;
}

@media (max-width: 768px) {
  .brand_name {
    font-size: 2em; /* 로고 크기 축소 */
  }

  .navbar_item li {
    margin-left: 10px;
    font-size: 1em; /* 글자 크기 축소 */
  }

  .navbar_items {
    flex-direction: column;
    align-items: flex-start;
  }

  .navbar_item {
    flex-direction: column;
    align-items: flex-start;
  }

  .navbar {
    padding: 10px;
    flex-direction: column;
    align-items: center;
  }
}

@media (max-width: 480px) {
  .brand_name {
    font-size: 1.8em; /* 더 작은 화면에서 로고 크기 줄임 */
  }

  .navbar_items {
    display: none; /* 작은 화면에서는 기본 메뉴 숨김 */
  }
}
</style>
