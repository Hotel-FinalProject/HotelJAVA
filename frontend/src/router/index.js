import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue";
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue';
import OAuthRedirectHandler from '@/components/OAuthRedirectHandler.vue';
import EmailVerificationPage from '@/components/UserPages/EmailVerification.vue';
import PasswordReset from '@/components/UserPages/PasswordReset.vue';
import UserMypage from '@/components/UserPages/UserMypage.vue';
import UserInfo from '@/components/UserPages/UserInfo.vue';
import Reservations from '@/components/UserPages/UserReservations.vue';
import AccountSettings from "@/components/UserPages/AccountSettings.vue";
import HotelAdminPage from '@/components/HotelAdminPage.vue';
import SearchPage from '@/components/SearchPage.vue';
import MapPage from '@/components/MapPage.vue';
import AdminLogin from '@/components/SystemAdminPages/AdminLogin.vue';

import { jwtDecode } from 'jwt-decode';

const routes = [
  {
    path: '/',
    component: MainPage,
  },
  {
    path: '/my_page',
    component: UserMypage,
    children: [
      {
        path: '',
        component: UserInfo,
      },
      {
        path: 'reservations',
        component: Reservations
      },
      {
        path: 'edit-info',
        component: AccountSettings
      }
    ]
  },
  {
    path: '/register',
    component: register,
  },
  {
    path: '/login',
    component: login,
  },
  {
    path: '/hotel-details/:id',
    component: () => import('@/components/HotelDetails.vue')
  },
  {
    path: '/room-details/:roomId',
    component: () => import('@/components/HotelRoom.vue'),
    name: 'HotelRoom',
  },
  {
    path: '/oauth2/success',
    component: OAuthRedirectHandler,
  },
  {
    path: '/find-my-id',
    component: () => import('@/components/UserPages/FindId.vue'),
  },
  {
    path: '/payment',
    component: () => import('@/components/PaymentPage.vue'),
    name: 'paymentPage',
  },
  {
    path: '/verify-email',
    component: EmailVerificationPage
  },
  {
    path: '/admin/:any*',
    component: AdminLogin,
  },
  {
    path: '/admin/hotel',
    component: HotelAdminPage,
    meta: { requiresAuth: true, requiredRole: 'ROLE_HOTELADMIN' }
  },
  {
    path: '/admin/system', // 시스템 관리자 페이지
    component: () => import('@/components/SystemAdminPage.vue'),
    meta: { requiresAuth: true, requiredRole: 'ROLE_ADMIN' }
  },
  {
    path: '/reset-password', // 이메일 인증 페이지 경로
    component: PasswordReset
  },
  {
    path: '/search-results', // 검색 페이지 경로 추가
    component: SearchPage
  },
  {
    path: '/map-view', // 지도 페이지 경로
    component: MapPage,
    props: route => ({ hotels: route.query.hotels ? JSON.parse(route.query.hotels) : [] }),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  }
});

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem("token");

  let userRole = null;
  if (token) {
    try {
      const decodedToken = jwtDecode(token);
      userRole = decodedToken.role;
    } catch (error) {
      console.error('토큰 디코딩 중 오류 발생: ', error);
      return next({ path: '/' });
    }
  }

  if (to.path === '/admin') {
    if (!token) {
      return next();
    } else if (userRole === 'ROLE_ADMIN') {
      // ROLE_ADMIN인 경우 '/admin/system'으로 리다이렉트
      return next({ path: '/admin/system' });
    } else if (userRole === 'ROLE_HOTELADMIN') {
      // ROLE_HOTEL_MANAGER인 경우 '/admin/hotel'으로 리다이렉트
      return next({ path: '/admin/hotel' });
    } else {
      // 권한이 없는 경우 메인 페이지로 리다이렉트
      return next({ path: '/' });
    }
  }

  if ((to.path === '/admin/hotel' || to.path.startsWith('/my_page')) && userRole === 'ROLE_ADMIN') {
    // 어드민 유저가 '/admin/hotel'로 접근할 경우 '/admin/system'으로 리다이렉트
    return next({ path: '/admin/system' });
  }

  if ((to.path === '/admin/system' || to.path.startsWith('/my_page')) && userRole === 'ROLE_HOTELADMIN') {
    return next({ path: '/admin/hotel' });
  }

  if (to.meta.requiresAuth) {
    // 특정 경로에 대해 권한이 필요할 때
    if (!token || to.meta.requiredRole !== userRole) {
      return next({ path: '/' });
    }
  }

  // 그 외 경로로의 접근은 그대로 허용
  next();
});

export default router;