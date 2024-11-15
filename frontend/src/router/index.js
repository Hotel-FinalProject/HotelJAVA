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
    component: () => import ('@/components/UserPages/FindId.vue'),
  },
  {
    path: '/payment',
    component: () => import ('@/components/PaymentPage.vue'),
    name: 'paymentPage',
  },
  {
    path: '/verify-email',
    component: EmailVerificationPage
  },
  {
    path: '/admin/hotel',
    component: HotelAdminPage
  },
  {
    path: '/admin/system', // 시스템 관리자 페이지
    component: () => import('@/components/SystemAdminPage.vue'),
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

export default router;